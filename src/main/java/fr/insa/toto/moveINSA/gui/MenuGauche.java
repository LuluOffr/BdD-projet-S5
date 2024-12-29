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
package fr.insa.toto.moveINSA.gui;

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
import fr.insa.toto.moveINSA.gui.vues.CandidaturePanel;
import fr.insa.toto.moveINSA.gui.vues.AttributionSRI;
import fr.insa.toto.moveINSA.gui.vues.AttributionEtudiant;
import fr.insa.toto.moveINSA.gui.vues.CandidatureListePanel;

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
        partenaires.addItem(new SideNavItem("nouveau (PARTENAIRE)", NouveauPartenairePanel.class));
        
        // Menu offres
        SideNavItem offres = new SideNavItem("offres");
        offres.addItem(new SideNavItem("liste", OffresPanel.class));
        offres.addItem(new SideNavItem("nouvelle (PARTENAIRE)", NouvelleOffrePanel.class));

        // Menu Département
        
        SideNavItem departements = new SideNavItem("Départements");

        // Ajouter des sous-onglets pour chaque departement
        SideNavItem archi = new SideNavItem("Département Architecture");
        SideNavItem gcivil = new SideNavItem("Génie civil et topographie");
        // Onglet Classe
        SideNavItem classes = new SideNavItem("Classes");

        // Ajouter des sous-onglets pour chaque classe
        String[] gcclassNames = {
            "GC2", "GC3", "GC4", "GC5",
            "G2", "G3", "G4", "G5"
        };

        for (String className : gcclassNames) {
            // Ajouter un sous-menu pour chaque classe
            SideNavItem classItem = new SideNavItem(className);

            // Menu étudiants
        SideNavItem etudiants = new SideNavItem("étudiants");
        etudiants.addItem(new SideNavItem("liste (SRI)", EtudiantsListePanel.class)); 
        etudiants.addItem(new SideNavItem("nouveau (SRI)", EtudiantCreationPanel.class));
        classItem.addItem(etudiants);
            // Ajouter ce sous-menu à l'onglet "Classes"
            classes.addItem(classItem);
            gcivil.addItem(classItem);
        }
        SideNavItem gelec = new SideNavItem("Génie électrique et énergetique");
        // Onglet Classe
        gelec.addItem(classes);

        // Ajouter des sous-onglets pour chaque classe
        String[] geclassNames = {
            "GE2", "GE3", "GE4", "GE5",
            "GTEE2", "GTEE3", "GTEE4", "GTEE5"
        };

        for (String className : geclassNames) {
            // Ajouter un sous-menu pour chaque classe
            SideNavItem classItem = new SideNavItem(className);

            // Menu étudiants
        SideNavItem etudiants = new SideNavItem("étudiants");
        etudiants.addItem(new SideNavItem("liste (SRI)", EtudiantsListePanel.class)); 
        etudiants.addItem(new SideNavItem("nouveau (SRI)", EtudiantCreationPanel.class));
        classItem.addItem(etudiants);
            // Ajouter ce sous-menu à l'onglet "Classes"
            classes.addItem(classItem);
            gelec.addItem(classItem);
        }   
        SideNavItem gmec = new SideNavItem("Mécanique");
        // Onglet Classe
        gmec.addItem(classes);

        // Ajouter des sous-onglets pour chaque classe
        String[] gmclassNames = {
            "GM2", "GM3", "GM4", "GM5",
            "PL2", "PL3", "PL4", "PL5",
            "MIQ2", "MIQ3", "MIQ4", "MIQ5"
        };

        for (String className : gmclassNames) {
            // Ajouter un sous-menu pour chaque classe
            SideNavItem classItem = new SideNavItem(className);

            // Menu étudiants
        SideNavItem etudiants = new SideNavItem("étudiants");
        etudiants.addItem(new SideNavItem("liste (SRI)", EtudiantsListePanel.class)); 
        etudiants.addItem(new SideNavItem("nouveau (SRI)", EtudiantCreationPanel.class));
        classItem.addItem(etudiants);
            // Ajouter ce sous-menu à l'onglet "Classes"
            classes.addItem(classItem);
            gmec.addItem(classItem);
        }   
        
        departements.addItem(archi,gcivil,gelec,gmec);

        
        
         
        
        //Menu candidature
        SideNavItem candidature = new SideNavItem("candidature");
        candidature.addItem(new SideNavItem("Candidature (ETUDIANT)", CandidaturePanel.class));
        candidature.addItem(new SideNavItem("Liste Candidature (SRI)", CandidatureListePanel.class));
        
        
        // Menu attribution
        SideNavItem attribution = new SideNavItem("attribution");
        attribution.addItem(new SideNavItem("attribution (SRI)", AttributionSRI.class)); 
        attribution.addItem(new SideNavItem("attribution (ETUDIANT)", AttributionEtudiant.class)); 

        
        //Menu candidature
        //SideNavItem Candidature = new SideNavItem("Candidature (ETUDIANT)");

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
        this.addItem(main, departements,partenaires, offres,  candidature , attribution, jeux, debug);
    }
}
