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

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.util.List;

@PageTitle("MoveINSA")
@Route(value = "", layout = MainLayout.class)
public class VuePrincipale extends VerticalLayout {

    public VuePrincipale() {
        H3 titre = new H3("Bienvenue sur MoveINSA");
        titre.getStyle()
                .set("font-size", "32px")
                .set("color", "#d50000")
                .set("margin-bottom", "20px");


        List<Paragraph> attention = List.of(
                new Paragraph("Attention : ceci est la beta du site."),
                new Paragraph("Vous pouvez vous connecter en tant que qu'établissement Partenaire, membre du SRI, "
                        + "ou alors en tant qu'étudiant.")
        );

        attention.get(0).getStyle().set("color", "red").set("font-size", "18px").set("font-weight", "bold");
        attention.forEach(p -> p.getStyle().set("margin-bottom", "10px"));

        this.add(titre);
        attention.forEach(this::add);

        // banderole
        HorizontalLayout bannerLayout=new HorizontalLayout();
        bannerLayout.setWidthFull();
        bannerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        bannerLayout.getStyle()
                .set("background-color","#f5f5f5") 
                .set("padding","20px")
                .set("border-radius","10px")
                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.2)");

        bannerLayout.add(image("insa1.jpg"));
        bannerLayout.add(image("insa2.jpg"));
        bannerLayout.add(image("insa3.jpg"));

        this.add(bannerLayout);

        
        List<Paragraph> ver = List.of(
                new Paragraph("Version créée par Salim, Djibril et Lucas"));

        ver.forEach(p -> p.getStyle().set("margin-bottom", "10px"));
        
        ver.forEach(this::add);
        
        
        Div footer = new Div();
        footer.setText("MoveINSA - 2025");
        footer.getStyle()
                .set("text-align", "center")
                .set("margin-top", "30px")
                .set("color", "#757575")
                .set("font-size", "14px");
        this.add(footer);
    }

    private Image image(String fileName) {
        StreamResource resource = new StreamResource(fileName,
                () -> getClass().getResourceAsStream("/images/" + fileName)); 
        Image image = new Image(resource, "");
        image.getStyle()
                .set("height", "200px") 
                .set("margin", "0 10px");
        return image;
    }
}