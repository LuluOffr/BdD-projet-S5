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

        // Menu étudiants
        SideNavItem etudiants = new SideNavItem("étudiants");
        etudiants.addItem(new SideNavItem("liste (SRI)", EtudiantsListePanel.class)); 
        etudiants.addItem(new SideNavItem("nouveau (SRI)", EtudiantCreationPanel.class)); 
        
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
        this.addItem(main, partenaires, offres, etudiants, candidature , attribution, jeux, debug);
    }
}