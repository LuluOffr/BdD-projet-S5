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

//classe qui permet au SRI de créer le profil de l'étudiant qui peut faire sa mobilité

@PageTitle("Créer un Étudiant")
@Route(value = "etudiants/nouveau/:classeSelectionnee", layout = MainLayout.class)
public class EtudiantCreationPanel extends VerticalLayout implements BeforeEnterObserver{

    private static String classeSelectionnee;
    private static final String PASSWORD = "SRI2024"; 
    private boolean isAuthenticated = false; 
    private VerticalLayout contentLayout; 

    public EtudiantCreationPanel() {
        this.add(new H3("Création d'un nouveau profil étudiant"));


        PasswordField passwordField = new PasswordField("Mot de passe");
        Button verifyButton = new Button("Vérifier", event -> {
            if (PASSWORD.equals(passwordField.getValue())) {
                isAuthenticated = true;
                Notification.show("Accès autorisé !");
                montreEtudiant(classeSelectionnee); //affiche formulaire étudiants
            } else {
                Notification.show("Mot de passe incorrect !", 3000, Notification.Position.MIDDLE);
            }
        });

        HorizontalLayout passwordLayout = new HorizontalLayout(passwordField, verifyButton);
        this.add(new Paragraph("Veuillez entrer le mot de passe :"));
        this.add(passwordLayout);

        contentLayout = new VerticalLayout();
        this.add(contentLayout); //
    }
 @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // extrait le paramètre 'className' de l'URL
        this.classeSelectionnee = event.getRouteParameters().get("classeSelectionnee").orElse("Aucune classe");
        // affiche étudiants pour la classe sélectionnée
        montreEtudiant(classeSelectionnee);
    }
    private void montreEtudiant(String classeSelectionnee) {
    if (isAuthenticated) {
        TextField ineField = new TextField("INE");
        TextField nomField = new TextField("Nom");
        TextField prenomField = new TextField("Prénom");
//        TextField classeField = new TextField("Classe");
        TextField scoreField = new TextField("Score");
            
        Button saveButton = new Button("Sauvegarder", event -> {
            String ine = ineField.getValue();
            String nom = nomField.getValue();
            String prenom = prenomField.getValue();
            String classe = classeSelectionnee;
            String score = scoreField.getValue();
            
                Etudiant nouvelEtudiant = new Etudiant(ine, nom, prenom, classe, score);

                try (Connection con = ConnectionPool.getConnection()) {
                    nouvelEtudiant.saveInDB(con); // sauv etudiant bdd
                    
                    String tableName = classe; 
                ResultSet rs = con.getMetaData().getTables(null, null, tableName, null);

                if (!rs.next()) {
                    // si table n'existe pas
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

                // insére l'étudiant dans la table correspondante
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
                } catch (SQLException e) {
            Notification.show("Erreur lors de l'enregistrement dans la base : " + e.getMessage());
            e.printStackTrace();
        }


        });

        VerticalLayout formLayout = new VerticalLayout(ineField, nomField, prenomField,  scoreField, saveButton);
        formLayout.setSpacing(true);
        formLayout.setPadding(true);
        formLayout.setWidthFull();
    



            contentLayout.removeAll(); // Nettoyer le conteneur avant d'ajouter les champs
            contentLayout.add(
                    new Paragraph("Remplissez les informations pour créer un étudiant :"),
                    ineField, nomField, prenomField,  scoreField, saveButton
            );
        }
    }
}
