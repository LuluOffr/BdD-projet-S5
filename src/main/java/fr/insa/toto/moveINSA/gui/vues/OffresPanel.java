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
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.beuvron.vaadin.utils.dataGrid.ColumnDescription;
import fr.insa.beuvron.vaadin.utils.dataGrid.GridDescription;
import fr.insa.beuvron.vaadin.utils.dataGrid.ResultSetGrid;
import fr.insa.toto.moveINSA.gui.MainLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 *
 * @author francois
 */
@PageTitle("MoveINSA")
@Route(value = "offres/liste", layout = MainLayout.class)
public class OffresPanel extends VerticalLayout {

    /**
     * Un petit composant qui affiche un entier sous forme d'un ensemble d'icones.
     */
    public static class IntAsIcon extends HorizontalLayout {

        public IntAsIcon(int nbr) {
            for (int i = 0; i < nbr; i++) {
                this.add(new Icon(VaadinIcon.EXIT));
            }
        }
    }

    private ResultSetGrid gOffres;
    private Button bPostule;

    public OffresPanel() {
        try (Connection con = ConnectionPool.getConnection()) {
            this.add(new H2("Liste des offres proposées"));
            PreparedStatement offresAvecPart = con.prepareStatement(
                    "select offremobilite.id as idOffre,partenaire.refPartenaire,offremobilite.nbrplaces,offremobilite.semestre,offremobilite.nomdispositif,partenaire.id as idPartenaire \n"
                    + "  from offremobilite \n"
                    + "    join partenaire on offremobilite.proposepar = partenaire.id");
            
            this.gOffres = new ResultSetGrid(offresAvecPart, new GridDescription(List.of(
                    new ColumnDescription().colData(1).headerString("Partenaire"),
                    new ColumnDescription().colData(2).headerString("Nombre de places"),
                    new ColumnDescription().colDataCompo(2, (t) -> new IntAsIcon((Integer) t)).headerString("Places"),
                    new ColumnDescription().colData(3).visible(false) // même chose pour l'id du partenaire
            )));

            this.add(new H3("Liste des toutes les offres que proposent nos partenaires "));
            this.add(new Paragraph("Pour plus de détails, veuillez directement consulter le site web de l'université partenaire"));
            this.add(this.gOffres);

            PreparedStatement offresParPartenaire = con.prepareStatement(
            "select partenaire.id,partenaire.refPartenaire,sum(offremobilite.nbrplaces) as placesPartenaire, \n"
                    + "       group_concat(distinct offremobilite.semestre separator ', ') as semestres, \n"
                    + "       group_concat(distinct offremobilite.nomdispositif separator ', ') as dispositifs, \n"
                    + "       group_concat(distinct offremobilite.specialite separator ', ') as specialites, \n"
                    + "       (select sum(nbrplaces) from offremobilite) as totplaces \n"
                    + "  from offremobilite \n"
                    + "    join partenaire on offremobilite.proposepar = partenaire.id \n"
                    + "  group by partenaire.id");

            this.add(new H3("Offres regroupées de nos partenaires"));
            ResultSetGrid parPart = new ResultSetGrid(offresParPartenaire, new GridDescription(List.of(
                    new ColumnDescription().colData(0).visible(false), // on veut pouvoir accéder à l'id du partenaire mais non l'afficher
                    new ColumnDescription().colData(1).headerString("Partenaire"),
                    new ColumnDescription().colData(2).headerString("Places totales"),
                    new ColumnDescription().colData(3).headerString("Semestres"),
                    new ColumnDescription().colData(4).headerString("Dispositifs"),
                    new ColumnDescription().colData(5).headerString("Spécialités"),
                    new ColumnDescription().colCalculatedObject((t) -> {
                        int nbrPart = Integer.parseInt("" + t.get(2));
                        int nbrTot = Integer.parseInt("" + t.get(6));
                        double percent = ((double) nbrPart) / nbrTot * 100;
                        return String.format("%.0f%%", percent);
                    }).headerString("En pourcentage")
            )));
            this.add(parPart);
        } catch (SQLException ex) {
            System.out.println("Probleme : " + ex.getLocalizedMessage());
            Notification.show("Probleme : " + ex.getLocalizedMessage());
        }
    }

}

