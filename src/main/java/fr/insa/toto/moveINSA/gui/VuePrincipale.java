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

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.List;

@PageTitle("MoveINSA")
@Route(value = "", layout = MainLayout.class)
public class VuePrincipale extends VerticalLayout {

    public VuePrincipale() {
        this.add(new H3("Bienvenue sur moveINSA"));
        List<Paragraph> attention = List.of(
                new Paragraph("Attention : ceci est la beta du site "),
                new Paragraph("Vous pouvez vous connecter en tant que qu'établissement Partenaire, membre du SRI, "
                        + "ou alors en tant qu'étudiant"),
                new Paragraph("Version créée par Salim, Djibril et Lucas")

        );
        
        attention.get(0).getStyle().set("color", "red");
        attention.forEach((p) -> this.add(p));

    }
}
