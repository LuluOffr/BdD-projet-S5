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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.MainLayout;
import fr.insa.toto.moveINSA.model.Candidature;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author lucas
 * pour voir les resulats de ses candidaturs
 */


@PageTitle("Résultats des candidatures (ÉTUDIANT)")
@Route(value = "attributions/etudiant", layout = MainLayout.class)
public class AttributionEtudiant extends VerticalLayout {

    private TextField ineField; 
    private Button validerButton; 
    private Grid<Candidature> grid; 
    public AttributionEtudiant() {
        this.add(new H3("Résultat de vos candidatures"));

        ineField = new TextField("Entrez votre INE");

        validerButton = new Button("Valider", event -> afficherCandidatures());

        this.add(ineField, validerButton);

        grid = new Grid<>(Candidature.class, false);
        grid.addColumn(Candidature::getOrdre).setHeader("Ordre");
        grid.addColumn(Candidature::getIdOffreMobilité).setHeader("Partenaire");
        grid.addColumn(Candidature::getStatut).setHeader("Statut");

        this.add(grid);
    }

//affiche les candidatures de l'étudiant apres validation de l'ine
    
    private void afficherCandidatures() {
        String ine = ineField.getValue().trim();

        // verif ine
        if (ine.isEmpty()) {
            Notification.show("Veuillez entrer un INE valide.");
            return;
        }

        try (Connection con = ConnectionPool.getConnection()) {
            List<Candidature> candidatures = Candidature.trouverCandidaturesParEtudiant(con, ine);

            // si pas de cand trouvée
            if (candidatures.isEmpty()) {
                Notification.show("Aucune candidature trouvée pour cet INE.");
                grid.setItems(); 
                return;
            }

            grid.setItems(candidatures);
        } catch (SQLException ex) {
            Notification.show("Erreur lors du chargement des candidatures : " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}