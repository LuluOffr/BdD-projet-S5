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
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Classe pour gérer les candidatures des étudiants par le SRI.
 */
@PageTitle("Attribution des candidatures (SRI)")
@Route(value = "attributions/sri", layout = MainLayout.class)
public class AttributionSRI extends VerticalLayout {

    private static final String PASSWORD = "SRI2024"; // Mot de passe requis
    private boolean isAuthenticated = false; // Indique si l'utilisateur a été authentifié
    private Etudiant etudiant; // Étudiant sélectionné
    private VerticalLayout contentLayout; // Conteneur pour les informations

    public AttributionSRI() {
        this.add(new H3("Espace d'attribution des candidatures (SRI)"));

        // Champ pour entrer le mot de passe
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

        // Conteneur pour afficher les informations une fois authentifié
        contentLayout = new VerticalLayout();
        this.add(contentLayout);
    }

    private void afficherRechercheEtudiant() {
        if (!isAuthenticated) return;

        contentLayout.removeAll();

        // Formulaire de recherche pour trouver un étudiant par son INE
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

        // Affichage des informations de l'étudiant
        contentLayout.add(new H3("Profil de l'étudiant :"));
        contentLayout.add(new Paragraph("Nom : " + etudiant.getNom()));
        contentLayout.add(new Paragraph("Prénom : " + etudiant.getPrenom()));
        contentLayout.add(new Paragraph("Classe : " + etudiant.getClasse()));

        try {
            // Récupération des candidatures de l'étudiant
            List<Candidature> candidatures = Candidature.trouverCandidaturesParEtudiant(con, etudiant.getIne());
            if (candidatures.isEmpty()) {
                contentLayout.add(new Paragraph("Aucune candidature trouvée pour cet étudiant."));
                return;
            }

            contentLayout.add(new H3("Candidatures de l'étudiant :"));

            // Création d'une grille pour afficher les candidatures
            Grid<Candidature> grid = new Grid<>(Candidature.class, false);

            grid.addColumn(Candidature::getIdOffreMobilité).setHeader("Établissement demandé");
            grid.addColumn(Candidature::getOrdre).setHeader("Ordre");
            grid.addColumn(Candidature::getDate).setHeader("Date");
            grid.addColumn(Candidature::getStatut).setHeader("Statut");

            // Ajouter des boutons "Accepter" et "Refuser" pour chaque candidature
            grid.addComponentColumn(candidature -> {
                Button accepterButton = new Button("Accepter", event -> traiterCandidature(con, candidature, "ACCEPTE"));
                Button refuserButton = new Button("Refuser", event -> traiterCandidature(con, candidature, "REFUSE"));
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
            // Mise à jour du statut dans la base de données
            Candidature.mettreAJourStatut(con, candidature.getIne(), candidature.getOrdre(), statut);
            Notification.show("La candidature pour l'établissement " + candidature.getIdOffreMobilité() + " a été " + statut.toLowerCase() + ".");
            afficherProfilEtudiant(con); // Rafraîchir la liste après mise à jour
        } catch (SQLException ex) {
            Notification.show("Erreur lors du traitement de la candidature : " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}