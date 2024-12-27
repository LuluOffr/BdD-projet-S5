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
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.MainLayout;
import fr.insa.toto.moveINSA.model.Candidature;
import fr.insa.toto.moveINSA.model.Etudiant;
import fr.insa.toto.moveINSA.model.OffreMobilite;
import fr.insa.toto.moveINSA.model.Partenaire;
import com.vaadin.flow.component.textfield.TextField;


import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author lucas
 */

@PageTitle("Candidature Étudiant")
@Route(value = "candidatures/nouveau", layout = MainLayout.class)
public class CandidaturePanel extends VerticalLayout {

    private Etudiant etudiant;
    private List<ComboBox<OffreMobilite>> choixPartenaires;
    private Button validerButton;

    public CandidaturePanel() {
    this.add(new H3("Création d'une candidature"));

    // Champs pour entrer l'INE
    TextField ineField = new TextField("Entrez votre INE");
    Button validerINE = new Button("Valider", event -> {
        String ine = ineField.getValue();

        // Vérifier si un INE est bien saisi
        if (ine == null || ine.trim().isEmpty()) {
            Notification.show("Veuillez entrer un INE valide.");
            return;
        }

        // Connexion pour chercher l'étudiant
        try (Connection con = ConnectionPool.getConnection()) {
            Optional<Etudiant> etu = Etudiant.trouveEtudiant(con, ine);

            if (etu.isPresent()) {
                etudiant = etu.get();
                Notification.show("Bienvenue " + etudiant.getPrenom() + " " + etudiant.getNom());
                afficherFormulaire(); // Appelle une méthode pour afficher le formulaire des choix
            } else {
                Notification.show("Aucun étudiant trouvé avec cet INE.");
            }
        } catch (SQLException e) {
            Notification.show("Erreur lors de la recherche : " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    });

    // Ajouter les champs et le bouton au panneau
    this.add(ineField, validerINE);
}


     private void afficherFormulaire() {
    try (Connection con = ConnectionPool.getConnection()) {
        // Charger les offres disponibles
        List<OffreMobilite> offres = OffreMobilite.toutesLesOffres(con);

        if (offres.isEmpty()) {
            Notification.show("Aucune offre de mobilité disponible !");
            return;
        }

        choixPartenaires = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            ComboBox<OffreMobilite> comboBox = new ComboBox<>("Établissement choisi n°" + i);
            comboBox.setItems(offres);

            // Générer les libellés des offres
            comboBox.setItemLabelGenerator(offre -> {
                try (Connection localCon = ConnectionPool.getConnection()) {
                    Partenaire partenaire = offre.getPartenaire(localCon); // Récupérer le partenaire associé
                    return partenaire.getRefPartenaire() + " (" + offre.getNbrPlaces() + " places restantes)";
                } catch (SQLException ex) {
                    Notification.show("Erreur lors de la récupération du partenaire : " + ex.getLocalizedMessage());
                    return "Erreur : données non disponibles";
                }
            });

            // Vérifier les doublons lors des sélections
            comboBox.addValueChangeListener(event -> checkDuplicates());
            choixPartenaires.add(comboBox);
            this.add(comboBox);
        }

        // Bouton pour valider les choix
        validerButton = new Button("Valider mes choix", event -> {
            try (Connection localCon = ConnectionPool.getConnection()) {
                enregistrerCandidatures(localCon);
            } catch (SQLException ex) {
                Notification.show("Erreur lors de l'enregistrement des candidatures : " + ex.getLocalizedMessage());
                ex.printStackTrace();
            }
        });
        this.add(validerButton);

    } catch (SQLException e) {
        Notification.show("Erreur lors du chargement des offres : " + e.getLocalizedMessage());
        e.printStackTrace();
    }
}

    private void checkDuplicates() {
        List<OffreMobilite> selections = new ArrayList<>();
        for (ComboBox<OffreMobilite> comboBox : choixPartenaires) {
            OffreMobilite selection = comboBox.getValue();
            if (selection != null && selections.contains(selection)) {
                Notification.show("Vous ne pouvez pas choisir deux fois le même établissement !");
                comboBox.setValue(null);
            } else if (selection != null) {
                selections.add(selection);
            }
        }
    }

    private void enregistrerCandidatures(Connection con) {
        try {
            for (int i = 0; i < choixPartenaires.size(); i++) {
                OffreMobilite selection = choixPartenaires.get(i).getValue();
                if (selection != null) {
                    Candidature candidature = new Candidature(
                            etudiant.getIne(),
                            String.valueOf(selection.getId()),
                            Date.valueOf(LocalDate.now()),
                            i + 1 // Ordre
                    );
                    candidature.saveInDB(con);
                }
            }
            Notification.show("Candidatures enregistrées avec succès !");
        } catch (SQLException e) {
            Notification.show("Erreur lors de l'enregistrement : " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}