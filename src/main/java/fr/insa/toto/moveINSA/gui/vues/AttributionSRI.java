/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.toto.moveINSA.gui.vues;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.MainLayout;
import fr.insa.toto.moveINSA.model.Candidature;
import fr.insa.toto.moveINSA.model.Etudiant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author lucas
 */


@PageTitle("Attribution des candidatures (SRI)")
@Route(value = "attributions/sri", layout = MainLayout.class)
public class AttributionSRI extends VerticalLayout {

    private static final String PASSWORD = "SRI2024";
    private boolean isAuthenticated = false;
    private Etudiant etudiant;
    private VerticalLayout contentLayout;

    public AttributionSRI() {
        this.add(new H3("Espace d'attribution des candidatures (SRI)"));

        PasswordField passwordField = new PasswordField("Mot de passe");
        Button validatePasswordButton = new Button("Valider", event -> {
            if (PASSWORD.equals(passwordField.getValue())) {
                isAuthenticated = true;
                Notification.show("Accès autorisé !");
                afficherRechercheEtudiant();
            } else {
                Notification.show("Mot de passe incorrect !", 3000, Notification.Position.MIDDLE);
            }
        });

        HorizontalLayout passwordLayout = new HorizontalLayout(passwordField, validatePasswordButton);
        this.add(passwordLayout);

        contentLayout = new VerticalLayout();
        this.add(contentLayout);
    }

    private void afficherRechercheEtudiant() {
        if (!isAuthenticated) return;

        contentLayout.removeAll();

        TextField ineField = new TextField("Entrez l'INE de l'étudiant");
        Button rechercherButton = new Button("Rechercher", event -> {
            String ine = ineField.getValue();
            if (ine.isEmpty()) {
                Notification.show("Veuillez entrer un INE valide.");
                return;
            }

            try (Connection con = ConnectionPool.getConnection()) {
                Optional<Etudiant> etu = Etudiant.trouveEtudiant(con, ine);
                if (etu.isPresent()) {
                    etudiant = etu.get();
                    Notification.show("Étudiant trouvé !");
                    afficherProfilEtudiant(con);
                } else {
                    Notification.show("Aucun étudiant trouvé avec cet INE.");
                }
            } catch (SQLException ex) {
                Notification.show("Erreur lors de la recherche : " + ex.getLocalizedMessage());
                ex.printStackTrace();
            }
        });

        HorizontalLayout rechercheLayout = new HorizontalLayout(ineField, rechercherButton);
        contentLayout.add(new Paragraph("Recherchez un étudiant :"), rechercheLayout);
    }

    private void afficherProfilEtudiant(Connection con) {
    contentLayout.removeAll();

    contentLayout.add(new H3("Profil de l'étudiant :"));
    contentLayout.add(new Paragraph("Nom : " + etudiant.getNom()));
    contentLayout.add(new Paragraph("Prénom : " + etudiant.getPrenom()));
    contentLayout.add(new Paragraph("Classe : " + etudiant.getClasse()));

    try {
        List<Candidature> candidatures = Candidature.trouverCandidaturesParEtudiant(con, etudiant.getIne());
        if (candidatures.isEmpty()) {
            contentLayout.add(new Paragraph("Aucune candidature trouvée pour cet étudiant."));
            return;
        }

        contentLayout.add(new H3("Candidatures de l'étudiant :"));

        Grid<Candidature> grid = new Grid<>(Candidature.class, false);

        grid.addColumn(Candidature::getIdOffreMobilité).setHeader("Établissement demandé");
        grid.addColumn(Candidature::getOrdre).setHeader("Ordre");
        grid.addColumn(Candidature::getDate).setHeader("Date");
        grid.addColumn(Candidature::getStatut).setHeader("Statut");

        // Ajouter une colonne pour afficher les places disponibles
        grid.addColumn(candidature -> {
            try (Connection localCon = ConnectionPool.getConnection()) {
            return getPlacesDisponibles(localCon, candidature.getIdOffreMobilité(), candidature.getIdOffreMobilité());
        } catch (SQLException e) {
            e.printStackTrace();
        return "Erreur";
        }
        }).setHeader("Places Disponibles");

        grid.addComponentColumn(candidature -> {
            Button accepterButton = new Button("Accepter");
            Button refuserButton = new Button("Refuser");

            // gestion accepter
            accepterButton.addClickListener(event -> {
                try (Connection localCon = ConnectionPool.getConnection()) {
                    traiterCandidature(localCon, candidature, "ACCEPTE");
                    accepterButton.setEnabled(false); 
                    refuserButton.setEnabled(false);  //desas bouton apres clic
                } catch (SQLException ex) {
                    Notification.show("Erreur lors du traitement de la candidature : " + ex.getLocalizedMessage());
                    ex.printStackTrace();
                }
            });

            // gestion refuser
            refuserButton.addClickListener(event -> {
                try (Connection localCon = ConnectionPool.getConnection()) {
                    traiterCandidature(localCon, candidature, "REFUSE");
                    accepterButton.setEnabled(false); // Désactiver le bouton après clic
                    refuserButton.setEnabled(false);  // Désactiver également le bouton Accepter
                } catch (SQLException ex) {
                    Notification.show("Erreur lors du traitement de la candidature : " + ex.getLocalizedMessage());
                    ex.printStackTrace();
                }
            });

            // Désactiver les boutons si le statut est déjà défini
            if (!"ATTENTE".equalsIgnoreCase(candidature.getStatut())) {
                accepterButton.setEnabled(false);
                refuserButton.setEnabled(false);
            }

            return new HorizontalLayout(accepterButton, refuserButton);
        }).setHeader("Actions");

        grid.setItems(candidatures);
        contentLayout.add(grid);
    } catch (SQLException ex) {
        Notification.show("Erreur lors du chargement des candidatures : " + ex.getLocalizedMessage());
        ex.printStackTrace();
    }
}

    private void traiterCandidature(Connection con, Candidature candidature, String statut) {
        try {
            // Mettre à jour le statut de la candidature
            Candidature.mettreAJourStatut(con, candidature.getIne(), candidature.getOrdre(), statut);

            if ("ACCEPTE".equalsIgnoreCase(statut)) {
                // Récupérer l'ID du partenaire associé à l'offre de mobilité
                String queryPartenaire = """
                    SELECT partenaire.id AS idPartenaire
                    FROM partenaire
                    JOIN offremobilite ON partenaire.id = offremobilite.proposepar
                    WHERE partenaire.refPartenaire = ?
                """;

                int partenaireId = -1;
                try (PreparedStatement pst = con.prepareStatement(queryPartenaire)) {
                    pst.setString(1, candidature.getIdOffreMobilité()); // Ici, `refPartenaire` est utilisé
                    try (ResultSet rs = pst.executeQuery()) {
                        if (rs.next()) {
                            partenaireId = rs.getInt("idPartenaire");
                        } else {
                            Notification.show("Erreur : Partenaire introuvable pour cette candidature.");
                            return;
                        }
                    }
                }

                // Réduire le nombre de places pour ce partenaire
                String queryUpdatePlaces = "UPDATE offremobilite SET nbrplaces = nbrplaces - 1 WHERE proposepar = ? AND nbrplaces > 0";
                try (PreparedStatement pst = con.prepareStatement(queryUpdatePlaces)) {
                    pst.setInt(1, partenaireId);
                    int rowsUpdated = pst.executeUpdate();

                    if (rowsUpdated > 0) {
                        Notification.show("Une place a été retirée pour le partenaire.");
                    } else {
                        Notification.show("Aucune place disponible pour ce partenaire.");
                    }
                }
            } else if ("REFUSE".equalsIgnoreCase(statut)) {
                Notification.show("Aucune place n'a été modifiée pour ce partenaire.");
            }

            afficherProfilEtudiant(con);

        } catch (SQLException ex) {
            Notification.show("Erreur lors du traitement de la candidature : " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    private String getPlacesDisponibles(Connection con, String refPartenaire, String idOffreMobilite) {
    String query = """
        SELECT offremobilite.nbrplaces AS placesDisponibles
        FROM offremobilite
        JOIN partenaire ON offremobilite.proposepar = partenaire.id
        WHERE partenaire.refPartenaire = ? AND offremobilite.id = ?
    """;

    try (PreparedStatement pst = con.prepareStatement(query)) {
        pst.setString(1, refPartenaire); // Récupère les places disponibles pour le partenaire
        pst.setString(2, idOffreMobilite); // Filtre par l'offre spécifique
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                int places = rs.getInt("placesDisponibles");
                return places + " places";
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return "erreur SQL";
    }
    return "Indisponible";
}
}