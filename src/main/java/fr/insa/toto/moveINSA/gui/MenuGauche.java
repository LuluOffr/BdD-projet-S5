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
 * Classe MenuGauche pour la gestion du menu latéral avec tous les départements et filières.
 */
public class MenuGauche extends SideNav {

    public MenuGauche() {
        
/*
// style pour menu gauche
        this.getStyle()
            .set("width", "250px")
            .set("background-color", "#f8f9fa")
            .set("box-shadow", "2px 0 8px rgba(0, 0, 0, 0.1)")
            .set("border-radius", "8px")
            .set("padding", "10px");
*/

        // Menu principal
        SideNavItem main = new SideNavItem("Menu", VuePrincipale.class);

        // Menu partenaires
        SideNavItem partenaires = new SideNavItem("Partenaires");
        partenaires.addItem(new SideNavItem("Liste", PartenairesPanel.class));
        partenaires.addItem(new SideNavItem("Nouveau (PARTENAIRE)", NouveauPartenairePanel.class));

        // Menu offres
        SideNavItem offres = new SideNavItem("Offres");
        offres.addItem(new SideNavItem("Liste", OffresPanel.class));
        offres.addItem(new SideNavItem("Nouvelle (PARTENAIRE)", NouvelleOffrePanel.class));

        // Menu départements
        SideNavItem departements = new SideNavItem("Départements");

        // Sous-menus pour chaque département
        SideNavItem archi = new SideNavItem("Département Architecture");
        SideNavItem gcivil = new SideNavItem("Génie civil et topographie");
        SideNavItem gelec = new SideNavItem("Génie électrique et énergétique");
        SideNavItem gmec = new SideNavItem("Mécanique");

        // Classes pour Génie civil et topographie
        classeDep(gcivil, new String[]{"GC2", "GC3", "GC4", "GC5", "G2", "G3", "G4", "G5"});

        // Classes pour Génie électrique et énergétique
        classeDep(gelec, new String[]{"GE2", "GE3", "GE4", "GE5", "GTEE2", "GTEE3", "GTEE4", "GTEE5"});

        // Classes pour Mécanique
        classeDep(gmec, new String[]{
            "GM2", "GM3", "GM4", "GM5",
            "PL2", "PL3", "PL4", "PL5",
            "MIQ2", "MIQ3", "MIQ4", "MIQ5"
        });

        departements.addItem(archi, gcivil, gelec, gmec);

        // candidatures
        SideNavItem candidature = new SideNavItem("Candidatures");
        candidature.addItem(new SideNavItem("Candidature (ÉTUDIANT)", CandidaturePanel.class));
        candidature.addItem(new SideNavItem("Liste Candidatures (SRI)", CandidatureListePanel.class));

        //attributions
        SideNavItem attribution = new SideNavItem("Attributions");
        attribution.addItem(new SideNavItem("Attribution (SRI)", AttributionSRI.class));
        attribution.addItem(new SideNavItem("Attribution (ÉTUDIANT)", AttributionEtudiant.class));

        // debug
        SideNavItem debug = new SideNavItem("Debug");
        debug.addItem(new SideNavItem("Test Driver", TestDriverPanel.class));
        debug.addItem(new SideNavItem("RAZ BdD", RAZBdDPanel.class));
        debug.addItem(new SideNavItem("Test ResultSetGrid", TestResultSetGrid.class));
        debug.addItem(new SideNavItem("Test DataGrid", TestDataGrid.class));
        debug.addItem(new SideNavItem("Test Grid direct", TestGridDirect.class));

        //final
        this.addItem(main, departements, partenaires, offres, candidature, attribution, debug);
    }
    
    
    private void classeDep(SideNavItem departmentMenu, String[] classNames) {
        for (String className : classNames) {
            SideNavItem classItem = new SideNavItem(className);
            SideNavItem etudiants = new SideNavItem("Étudiants");
            etudiants.addItem(new SideNavItem("Liste (SRI)", "etudiants/liste/" + className));
            etudiants.addItem(new SideNavItem("Nouveau (SRI)", "etudiants/nouveau/" + className));
            classItem.addItem(etudiants);

            departmentMenu.addItem(classItem);
        }
    }
}

