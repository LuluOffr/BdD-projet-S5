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
package fr.insa.toto.moveINSA.moveINSA.gui;

import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import fr.insa.toto.moveINSA.gui.jeu.BoiteACoucou;
import fr.insa.toto.moveINSA.gui.jeu.TrouveEntier;
import fr.insa.toto.moveINSA.gui.testDataGrid.TestDataGrid;
import fr.insa.toto.moveINSA.gui.testDataGrid.TestGridDirect;
import fr.insa.toto.moveINSA.gui.testDataGrid.TestResultSetGrid;
import fr.insa.toto.moveINSA.gui.vues.NouveauPartenairePanel;
import fr.insa.toto.moveINSA.gui.vues.NouvelleOffrePanel;
import fr.insa.toto.moveINSA.gui.vues.OffresPanel;
import fr.insa.toto.moveINSA.gui.vues.PartenairesPanel;
import fr.insa.toto.moveINSA.gui.vues.RAZBdDPanel;
import fr.insa.toto.moveINSA.gui.vues.TestDriverPanel;
import fr.insa.toto.moveINSA.gui.vues.EtudiantCreationPanel;
import fr.insa.toto.moveINSA.gui.vues.EtudiantsListePanel;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

/**
 *
 * @author francois
 */
public class MenuGauche extends SideNav {

    public MenuGauche() {
        // Menu principal
        SideNavItem main = new SideNavItem("main", VuePrincipale.class);

        // Menu partenaires
        SideNavItem partenaires = new SideNavItem("partenaires");
        partenaires.addItem(new SideNavItem("liste", PartenairesPanel.class));
        partenaires.addItem(new SideNavItem("nouveau", NouveauPartenairePanel.class));

        // Menu offres
        SideNavItem offres = new SideNavItem("offres");
        offres.addItem(new SideNavItem("liste", OffresPanel.class));
        offres.addItem(new SideNavItem("nouvelle", NouvelleOffrePanel.class));
        
        // Onglet Classe
        SideNavItem classes = new SideNavItem("Classes");

        // Ajouter des sous-onglets pour chaque classe
        String[] classNames = {
            "GC2", "GC3", "GC4", "GC5",
            "TP2", "TP3", "TP4", "TP5",
            "GE2", "GE3", "GE4", "GE5",
            "GTEE2", "GTEE3", "GTEE4", "GTEE5",
            "GM2", "GM3", "GM4", "GM5",
            "PL2", "PL3", "PL4", "PL5",
            "MIQ2", "MIQ3", "MIQ4", "MIQ5"
        };

        for (String className : classNames) {
            // Ajouter un sous-menu pour chaque classe
            SideNavItem classItem = new SideNavItem(className);

            // Sous-menu : Liste des étudiants de cette classe
           /* SideNavItem listEtudiantsItem = new SideNavItem(
                "Liste des étudiants",
                () -> getUI().ifPresent(ui -> ui.navigate(EtudiantsListePanel.class, "classe=" + className))
            );

            // Sous-menu : Ajouter un étudiant à cette classe
            SideNavItem addEtudiantItem = new SideNavItem(
                "Ajouter un étudiant",
                () -> getUI().ifPresent(ui -> ui.navigate(EtudiantCreationPanel.class, "classe=" + className))
            );

            classItem.addItem(listEtudiantsItem);
            classItem.addItem(addEtudiantItem);*/

            // Ajouter ce sous-menu à l'onglet "Classes"
            classes.addItem(classItem);
        }

        // Ajouter l'onglet Classes au menu principal
        //sideNav.addItem(classes);

        // Ajouter le menu dans l'interface principale
       // add(sideNav);
    



        // Menu étudiants
        SideNavItem etudiants = new SideNavItem("étudiants");
        etudiants.addItem(new SideNavItem("liste", EtudiantsListePanel.class)); // Liste des étudiants
        etudiants.addItem(new SideNavItem("nouveau", EtudiantCreationPanel.class)); // Création d'un étudiant

        // Menu debug
        SideNavItem debug = new SideNavItem("debug");
        debug.addItem(new SideNavItem("test driver", TestDriverPanel.class));
        debug.addItem(new SideNavItem("raz BdD", RAZBdDPanel.class));
        debug.addItem(new SideNavItem("test ResultSetGrid", TestResultSetGrid.class));
        debug.addItem(new SideNavItem("test DataGrid", TestDataGrid.class));
        debug.addItem(new SideNavItem("test Grid direct", TestGridDirect.class));

        // Menu jeux
        SideNavItem jeux = new SideNavItem("jeux");
        jeux.addItem(new SideNavItem("boite à coucou", BoiteACoucou.class));
        jeux.addItem(new SideNavItem("trouve", TrouveEntier.class));

        // Ajout de tous les menus
        this.addItem(main, partenaires, offres, etudiants, jeux, debug);
    }
}