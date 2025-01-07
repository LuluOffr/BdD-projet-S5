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
 * Classe MenuGauche pour la gestion du menu latéral avec styles intégrés.
 */
public class MenuGauche extends SideNav {

    public MenuGauche() {
        // Ajout de styles globaux au conteneur principal
        this.getStyle()
            .set("width", "250px")
            .set("background-color", "#f8f9fa")
            .set("box-shadow", "2px 0 8px rgba(0, 0, 0, 0.1)")
            .set("border-radius", "8px")
            .set("padding", "10px");

        // Menu principal
        SideNavItem main = new SideNavItem("Menu", VuePrincipale.class);
        styleMenuItem(main, "#d32f2f", "18px", "bold", null);

        // Menu partenaires
        SideNavItem partenaires = new SideNavItem("Partenaires");
        styleMenuSection(partenaires, "#3f51b5");
        partenaires.addItem(new SideNavItem("Liste", PartenairesPanel.class));
        partenaires.addItem(new SideNavItem("Nouveau (PARTENAIRE)", NouveauPartenairePanel.class));

        // Menu offres
        SideNavItem offres = new SideNavItem("Offres");
        styleMenuSection(offres, "#3f51b5");
        offres.addItem(new SideNavItem("Liste", OffresPanel.class));
        offres.addItem(new SideNavItem("Nouvelle (PARTENAIRE)", NouvelleOffrePanel.class));

        // Menu départements
        SideNavItem departements = new SideNavItem("Départements");
        styleMenuSection(departements, "#3f51b5");

        // Sous-menus pour chaque département
        SideNavItem archi = new SideNavItem("Département Architecture");
        SideNavItem gcivil = new SideNavItem("Génie civil et topographie");
        SideNavItem gelec = new SideNavItem("Génie électrique et énergétique");
        SideNavItem gmec = new SideNavItem("Mécanique");

        // Ajout des sous-menus pour les départements
        addClassesToDepartment(gcivil, new String[]{"GC2", "GC3", "GC4", "GC5"});
        addClassesToDepartment(gelec, new String[]{"GE2", "GE3", "GE4", "GE5"});
        addClassesToDepartment(gmec, new String[]{"GM2", "GM3", "GM4", "GM5"});

        departements.addItem(archi, gcivil, gelec, gmec);

        // Menu candidatures
        SideNavItem candidature = new SideNavItem("Candidatures");
        styleMenuSection(candidature, "#3f51b5");
        candidature.addItem(new SideNavItem("Candidature (ÉTUDIANT)", CandidaturePanel.class));
        candidature.addItem(new SideNavItem("Liste Candidatures (SRI)", CandidatureListePanel.class));

        // Menu attributions
        SideNavItem attribution = new SideNavItem("Attributions");
        styleMenuSection(attribution, "#3f51b5");
        attribution.addItem(new SideNavItem("Attribution (SRI)", AttributionSRI.class));
        attribution.addItem(new SideNavItem("Attribution (ÉTUDIANT)", AttributionEtudiant.class));

        // Menu debug
        SideNavItem debug = new SideNavItem("Debug");
        styleMenuSection(debug, "#ff5722");
        debug.addItem(new SideNavItem("Test Driver", TestDriverPanel.class));
        debug.addItem(new SideNavItem("RAZ BdD", RAZBdDPanel.class));

        // Ajout final
        this.addItem(main, departements, partenaires, offres, candidature, attribution, debug);
    }

    /**
     * Ajoute des classes à un département avec un sous-menu pour les étudiants.
     *
     * @param departmentMenu Menu du département.
     * @param classNames     Noms des classes à ajouter.
     */
    private void addClassesToDepartment(SideNavItem departmentMenu, String[] classNames) {
        for (String className : classNames) {
            SideNavItem classItem = new SideNavItem(className);
            SideNavItem etudiants = new SideNavItem("Étudiants");
            etudiants.addItem(new SideNavItem("Liste (SRI)", "etudiants/liste/" + className));
            etudiants.addItem(new SideNavItem("Nouveau (SRI)", "etudiants/nouveau/" + className));
            classItem.addItem(etudiants);

            // Ajout de styles aux sous-menus
            styleSubMenu(classItem);
            departmentMenu.addItem(classItem);
        }
    }

    /**
     * Applique des styles à une section de menu.
     *
     * @param item  L'élément de menu.
     * @param color La couleur du texte.
     */
    private void styleMenuSection(SideNavItem item, String color) {
        item.getStyle()
            .set("margin-top", "10px")
            .set("font-size", "16px")
            .set("color", color);
    }

    /**
     * Applique des styles à un élément de menu.
     *
     * @param item       L'élément de menu.
     * @param color      La couleur du texte.
     * @param fontSize   La taille de la police.
     * @param fontWeight Le poids de la police.
     * @param bgColor    La couleur de fond (null si aucune).
     */
    private void styleMenuItem(SideNavItem item, String color, String fontSize, String fontWeight, String bgColor) {
        item.getStyle()
            .set("color", color)
            .set("font-size", fontSize)
            .set("font-weight", fontWeight);
        if (bgColor != null) {
            item.getStyle().set("background-color", bgColor);
        }
    }

    /**
     * Applique des styles à un sous-menu.
     *
     * @param item L'élément de sous-menu.
     */
    private void styleSubMenu(SideNavItem item) {
        item.getStyle()
            .set("padding-left", "20px")
            .set("transition", "all 0.3s ease-in-out");
    }
}

