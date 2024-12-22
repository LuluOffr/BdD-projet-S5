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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.MainLayout;
import fr.insa.toto.moveINSA.model.Etudiant;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Vue pour créer un nouvel étudiant.
 */
@PageTitle("Créer un Étudiant")
@Route(value = "etudiants/nouveau", layout = MainLayout.class)
public class EtudiantCreationPanel extends VerticalLayout {

    public EtudiantCreationPanel() {
        this.add(new H3("Création d'un nouveau profil étudiant"));

        // Champs de formulaire
        TextField ineField = new TextField("INE");
        TextField nomField = new TextField("Nom");
        TextField prenomField = new TextField("Prénom");
        TextField classeField = new TextField("Classe");
        TextField scoreField = new TextField("Score");

        // Bouton de sauvegarde
        Button saveButton = new Button("Sauvegarder", event -> {
            String ine = ineField.getValue();
            String nom = nomField.getValue();
            String prenom = prenomField.getValue();
            String classe = classeField.getValue();
            String score = scoreField.getValue();

            try (Connection con = ConnectionPool.getConnection()) {
                // Vérification des tables disponibles
                ResultSet rs = con.getMetaData().getTables(null, null, "%", null);
                System.out.println("Tables disponibles dans la base :");
                while (rs.next()) {
                    System.out.println(rs.getString("TABLE_NAME"));
                }

                // Création d'un nouvel étudiant et enregistrement en base
                Etudiant etudiant = new Etudiant(ine, nom, prenom, classe, score);
                etudiant.saveInDB(con);

                Notification.show("Étudiant sauvegardé avec succès !");
                ineField.clear();
                nomField.clear();
                prenomField.clear();
                classeField.clear();
                scoreField.clear();
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la sauvegarde : " + ex.getLocalizedMessage());
                Notification.show("Erreur lors de la sauvegarde : " + ex.getLocalizedMessage());
            }
        });

        this.add(ineField, nomField, prenomField, classeField, scoreField, saveButton);
    }
}