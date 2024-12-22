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

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.MainLayout;
import fr.insa.toto.moveINSA.model.Etudiant;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Panneau pour afficher la liste des étudiants
 *
 * @author lucas
 */
@PageTitle("Liste des étudiants")
@Route(value = "etudiants/liste", layout = MainLayout.class)
public class EtudiantsListePanel extends VerticalLayout {

    public EtudiantsListePanel() {
        try (Connection con = ConnectionPool.getConnection()) {
            this.add(new H3("Liste de tous les étudiants"));

            // Récupérer la liste des étudiants depuis la base de données
            List<Etudiant> etudiants = Etudiant.tousLesEtudiants(con);

            // Créer un grid pour afficher les étudiants
            Grid<Etudiant> grid = new Grid<>(Etudiant.class, false);

            // Ajouter des colonnes spécifiques
            grid.addColumn(Etudiant::getId).setHeader("ID");
            grid.addColumn(Etudiant::getIne).setHeader("INE");
            grid.addColumn(Etudiant::getNom).setHeader("Nom");
            grid.addColumn(Etudiant::getPrenom).setHeader("Prénom");
            grid.addColumn(Etudiant::getClasse).setHeader("Classe");
            grid.addColumn(Etudiant::getScore).setHeader("Score");

            // Ajouter les données au grid
            grid.setItems(etudiants);

            // Ajouter le grid au panneau
            this.add(grid);
        } catch (SQLException ex) {
            this.add(new H3("Erreur : Impossible de charger les étudiants."));
            ex.printStackTrace();
        }
    }
}