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

/**
 *
 * @author lucas
 */


package fr.insa.toto.moveINSA.gui.vues;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.MainLayout;
import fr.insa.toto.moveINSA.model.Etudiant;
import com.vaadin.flow.router.BeforeEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Vue pour créer un nouvel étudiant avec vérification par mot de passe.
 */
@PageTitle("Créer un Étudiant")
@Route(value = "etudiants/nouveau/:classeSelectionnee", layout = MainLayout.class)
public class EtudiantCreationPanel extends VerticalLayout implements BeforeEnterObserver{

    private static String classeSelectionnee;
    private static final String PASSWORD = "SRI2024"; // Mot de passe requis
    private boolean isAuthenticated = false; // Vérifie si l'utilisateur a entré le bon mot de passe
    private VerticalLayout contentLayout; // Contient les champs et le bouton de sauvegarde

    public EtudiantCreationPanel() {
        this.add(new H3("Création d'un nouveau profil étudiant"));

        // Section pour la vérification par mot de passe
        PasswordField passwordField = new PasswordField("Mot de passe");
        Button verifyButton = new Button("Vérifier", event -> {
            if (PASSWORD.equals(passwordField.getValue())) {
                isAuthenticated = true;
                Notification.show("Accès autorisé !");
                showCreationForm(classeSelectionnee); // Afficher le formulaire de création d'étudiant
            } else {
                Notification.show("Mot de passe incorrect !", 3000, Notification.Position.MIDDLE);
            }
        });

        HorizontalLayout passwordLayout = new HorizontalLayout(passwordField, verifyButton);
        this.add(new Paragraph("Veuillez entrer le mot de passe :"));
        this.add(passwordLayout);

        // Conteneur pour les champs du formulaire
        contentLayout = new VerticalLayout();
        this.add(contentLayout); // Ajout du conteneur (vide initialement)
    }
 @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Extraire le paramètre 'className' de l'URL
        this.classeSelectionnee = event.getRouteParameters().get("classeSelectionnee").orElse("Aucune classe");
        // Afficher les étudiants pour la classe sélectionnée
        showCreationForm(classeSelectionnee);
    }
    private void showCreationForm(String classeSelectionnee) {
    if (isAuthenticated) {
        // Champs de formulaire
        TextField ineField = new TextField("INE");
        TextField nomField = new TextField("Nom");
        TextField prenomField = new TextField("Prénom");
//        TextField classeField = new TextField("Classe");
        TextField scoreField = new TextField("Score");

        // Bouton de sauvegarde
        Button saveButton = new Button("Sauvegarder", event -> {
            String ine = ineField.getValue();
            String nom = nomField.getValue();
            String prenom = prenomField.getValue();
            String classe = classeSelectionnee;
            String score = scoreField.getValue();

            try (Connection con = ConnectionPool.getConnection()) {
                // Vérification que la table existe pour la classe spécifiée
                String tableName = classe; // Exemple : "GC2", "TP3"
                ResultSet rs = con.getMetaData().getTables(null, null, tableName, null);

                if (!rs.next()) {
                    // Si la table n'existe pas, la créer
                    String createTableSQL = "CREATE TABLE " + tableName + " (" +
                            "INE VARCHAR(50) PRIMARY KEY, " +
                            "Nom VARCHAR(100), " +
                            "Prenom VARCHAR(100), " +
                            "Classe VARCHAR(50), " +
                            "Score VARCHAR(50)" +
                            ")";
                    con.createStatement().executeUpdate(createTableSQL);
                    System.out.println("Table créée : " + tableName);
                }

                // Insérer l'étudiant dans la table correspondante
                String insertSQL = "INSERT INTO " + tableName + " (INE, Nom, Prenom, Classe, Score) VALUES (?, ?, ?, ?, ?)";
                try (var pstmt = con.prepareStatement(insertSQL)) {
                    pstmt.setString(1, ine);
                    pstmt.setString(2, nom);
                    pstmt.setString(3, prenom);
                    pstmt.setString(4, classe);
                    pstmt.setString(5, score);
                    pstmt.executeUpdate();
                }

                Notification.show("Étudiant sauvegardé avec succès dans la classe : " + classe);
                ineField.clear();
                nomField.clear();
                prenomField.clear();
//                classeField.clear();
                scoreField.clear();
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la sauvegarde : " + ex.getLocalizedMessage());
                Notification.show("Erreur lors de la sauvegarde : " + ex.getLocalizedMessage());
            }
        });

        // Ajouter le formulaire au contenu principal
        VerticalLayout formLayout = new VerticalLayout(ineField, nomField, prenomField,  scoreField, saveButton);
        formLayout.setSpacing(true);
        formLayout.setPadding(true);
        formLayout.setWidthFull();
        //getUI().ifPresent(ui -> ui.setContent(formLayout));
    



            // Ajouter les champs et le bouton au conteneur
            contentLayout.removeAll(); // Nettoyer le conteneur avant d'ajouter les champs
            contentLayout.add(
                    new Paragraph("Remplissez les informations pour créer un étudiant :"),
                    ineField, nomField, prenomField,  scoreField, saveButton
            );
        }
    }
}