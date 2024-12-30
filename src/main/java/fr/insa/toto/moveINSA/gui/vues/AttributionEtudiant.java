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
 * Classe permettant à un étudiant de voir les résultats de ses candidatures.
 */


@PageTitle("Résultats des candidatures (ÉTUDIANT)")
@Route(value = "attributions/etudiant", layout = MainLayout.class)
public class AttributionEtudiant extends VerticalLayout {

    private TextField ineField; // Champ pour entrer l'INE
    private Button validerButton; // Bouton pour valider l'INE
    private Grid<Candidature> grid; // Tableau pour afficher les candidatures

    public AttributionEtudiant() {
        // Titre de la page
        this.add(new H3("Résultat de vos candidatures"));

        // Champ pour entrer l'INE
        ineField = new TextField("Entrez votre INE");

        // Bouton pour valider
        validerButton = new Button("Valider", event -> afficherCandidatures());

        // Ajout des composants
        this.add(ineField, validerButton);

        // Configuration du tableau
        grid = new Grid<>(Candidature.class, false);
        grid.addColumn(Candidature::getOrdre).setHeader("Ordre");
        grid.addColumn(Candidature::getIdOffreMobilité).setHeader("Partenaire");
        grid.addColumn(Candidature::getStatut).setHeader("Statut");

        // Ajout du tableau à la vue
        this.add(grid);
    }

    /**
     * Affiche les candidatures de l'étudiant après validation de son INE.
     */
    private void afficherCandidatures() {
        String ine = ineField.getValue().trim();

        // Vérification que l'INE est renseigné
        if (ine.isEmpty()) {
            Notification.show("Veuillez entrer un INE valide.");
            return;
        }

        // Chargement des candidatures depuis la base de données
        try (Connection con = ConnectionPool.getConnection()) {
            List<Candidature> candidatures = Candidature.trouverCandidaturesParEtudiant(con, ine);

            // Si aucune candidature n'est trouvée
            if (candidatures.isEmpty()) {
                Notification.show("Aucune candidature trouvée pour cet INE.");
                grid.setItems(); // Vider le tableau
                return;
            }

            // Affichage des candidatures dans le tableau
            grid.setItems(candidatures);
        } catch (SQLException ex) {
            Notification.show("Erreur lors du chargement des candidatures : " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}