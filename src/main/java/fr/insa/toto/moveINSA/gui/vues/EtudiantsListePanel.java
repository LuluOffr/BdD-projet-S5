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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.MainLayout;
import fr.insa.toto.moveINSA.model.Etudiant;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.BeforeEvent;



/**
 *
 * @author lucas
 */

@PageTitle("Liste des étudiants")
@Route(value = "etudiants/liste/:classeSelectionnee", layout = MainLayout.class)
public class EtudiantsListePanel extends VerticalLayout implements BeforeEnterObserver {

    private String classeSelectionnee;
    private static final String PASSWORD = "SRI2024"; // Mot de passe requis pour afficher la liste
    private boolean isAuthenticated = false; // Vérifie si l'utilisateur a entré le bon mot de passe
    private Grid<Etudiant> grid; // Grid pour afficher les étudiants
    private VerticalLayout contentLayout; // Contenu principal
    
    
    public EtudiantsListePanel() {
        this.add(new H3("Liste des étudiants"));

        // Ajout du champ pour entrer le mot de passe
        PasswordField passwordField = new PasswordField("Mot de passe");
        Button verifyButton = new Button("Vérifier", event -> {
            if (PASSWORD.equals(passwordField.getValue())) {
                isAuthenticated = true;
                Notification.show("Accès autorisé !");
                showStudentList(classeSelectionnee); // Afficher la liste des étudiants
            } else {
                Notification.show("Mot de passe incorrect !", 3000, Notification.Position.MIDDLE);
            }
        });

        HorizontalLayout passwordLayout = new HorizontalLayout(passwordField, verifyButton);
        this.add(passwordLayout);

        // Conteneur pour la liste des étudiants (sera rempli après l'authentification)
        contentLayout = new VerticalLayout();
        this.add(contentLayout);
        
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Extraire le paramètre 'className' de l'URL
        this.classeSelectionnee = event.getRouteParameters().get("classeSelectionnee").orElse("Aucune classe");
        // Afficher les étudiants pour la classe sélectionnée
        showStudentList(classeSelectionnee);
    }
    
    private void showStudentList(String classeSelectionnee) {
    if (isAuthenticated) {
        try (Connection con = ConnectionPool.getConnection()) {
            // Vérification des tables disponibles
            ResultSet rs = con.getMetaData().getTables(null, null, "%", null);
            System.out.println("Tables disponibles dans la base :");
            while (rs.next()) {
                System.out.println(rs.getString("TABLE_NAME"));
            }

            // Vérifier si la table de la classe existe
            String tableName = classeSelectionnee;
            rs = con.getMetaData().getTables(null, null, tableName, null);

            if (!rs.next()) {
                Notification.show("Aucune donnée disponible pour la classe : " + classeSelectionnee);
                return;
            }

            // Récupérer les étudiants pour la classe sélectionnée
            List<Etudiant> etudiants = Etudiant.tousLesEtudiantsParClasse(con, classeSelectionnee);

            // Créer un grid pour afficher les étudiants
            grid = new Grid<>(Etudiant.class, false);

            // Ajouter des colonnes spécifiques
            grid.addColumn(Etudiant::getIne).setHeader("INE");
            grid.addColumn(Etudiant::getNom).setHeader("Nom");
            grid.addColumn(Etudiant::getPrenom).setHeader("Prénom");
            grid.addColumn(Etudiant::getClasse).setHeader("Classe");
            grid.addColumn(Etudiant::getScore).setHeader("Score");

            // Ajouter les données au grid
            grid.setItems(etudiants);

            // Ajouter le grid au panneau principal
            contentLayout.removeAll(); // Nettoyer le contenu précédent
            contentLayout.add(new Paragraph("Liste des étudiants pour la classe : " + classeSelectionnee));
            contentLayout.add(grid);

        } catch (SQLException ex) {
            Notification.show("Erreur : Impossible de charger les étudiants. Détails : " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}

}