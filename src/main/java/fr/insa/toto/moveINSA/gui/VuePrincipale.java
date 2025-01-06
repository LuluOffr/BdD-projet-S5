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

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
        // Uniformiser la police et les couleurs principales
        this.getStyle().set("font-family", "'Roboto', sans-serif")
                .set("background", "linear-gradient(to bottom, #ffffff, #f5f5f5)")
                .set("color", "#333");

        H3 titre = new H3("Bienvenue sur MoveINSA");
        titre.getStyle()
                .set("font-size", "32px")
                .set("color", "#d50000")
                .set("margin-bottom", "20px");

        List<Paragraph> attention = List.of(
                new Paragraph("Attention : ceci est la beta du site."),
                new Paragraph("Vous pouvez vous connecter en tant qu'établissement Partenaire, membre du SRI, "
                        + "ou alors en tant qu'étudiant."),
                new Paragraph("Date limite de dépôt des candidatures : 15 janvier 2025")
        );

        attention.get(0).getStyle().set("color", "red").set("font-size", "18px").set("font-weight", "bold");
        attention.forEach(p -> p.getStyle().set("margin-bottom", "10px"));

        this.add(titre);
        attention.forEach(this::add);

        // Ajout de la phrase avec le lien
        Paragraph infoParagraph = new Paragraph();
        infoParagraph.setText("Vous trouverez ");

        Anchor infoLink = new Anchor("https://www.insa-strasbourg.fr/wp-content/uploads/Dates-semestres-outgoing-langues.pdf", "ICI");
        infoLink.setTarget("_blank");
        infoLink.getStyle().set("color", "#d50000").set("font-weight", "bold");

        infoParagraph.add(infoLink);
        infoParagraph.add(" des informations sur le calendrier universitaire des partenaires où des étudiants y ont effectué une mobilité ainsi que sur la langue d’enseignement dans les universités d’accueil.");

        infoParagraph.getStyle().set("margin-top", "20px").set("font-size", "16px").set("color", "#333");

        this.add(infoParagraph);

        // Ajout d'un "carrousel" manuel avec des boutons pour faire défiler les images
HorizontalLayout imageCarousel = new HorizontalLayout();
imageCarousel.setWidthFull();
imageCarousel.setJustifyContentMode(JustifyContentMode.CENTER);
imageCarousel.getStyle()
        .set("background", "linear-gradient(45deg, #f5f5f5, #ffffff)")
        .set("padding", "20px")
        .set("border-radius", "10px")
        .set("box-shadow", "0 4px 12px rgba(0,0,0,0.2)")
        .set("margin-bottom", "30px");

// Images pour le "carrousel"
Image image1 = image("insa1.jpg");
Image image2 = image("insa2.jpg");
Image image3 = image("insa3.jpg");

// Conteneur pour l'image active
Div imageContainer = new Div(image1);
imageContainer.setWidth("400px");
imageContainer.getStyle().set("text-align", "center").set("overflow", "hidden");

// Boutons pour naviguer entre les images
Icon prevButton = VaadinIcon.ANGLE_LEFT.create();
prevButton.getStyle().set("cursor", "pointer").set("color", "#d50000").set("font-size", "24px");

Icon nextButton = VaadinIcon.ANGLE_RIGHT.create();
nextButton.getStyle().set("cursor", "pointer").set("color", "#d50000").set("font-size", "24px");

// Logique pour changer les images
Image[] images = {image1, image2, image3};
int[] currentIndex = {0};
prevButton.addClickListener(e -> {
    currentIndex[0] = (currentIndex[0] - 1 + images.length) % images.length;
    imageContainer.removeAll();
    imageContainer.add(images[currentIndex[0]]);
});
nextButton.addClickListener(e -> {
    currentIndex[0] = (currentIndex[0] + 1) % images.length;
    imageContainer.removeAll();
    imageContainer.add(images[currentIndex[0]]);
});

// Ajouter les éléments au "carrousel"
imageCarousel.add(prevButton, imageContainer, nextButton);
this.add(imageCarousel);


        // Ajout des statistiques avec style et icônes
        HorizontalLayout statsLayout = new HorizontalLayout();
        statsLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        statsLayout.getStyle()
                .set("background", "linear-gradient(45deg, #f5f5f5, #e0e0e0)")
                .set("padding", "20px")
                .set("border-radius", "10px")
                .set("margin-top", "20px")
                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.1)")
                .set("align-self", "center");

        statsLayout.add(createStatWithIcon("Pays", "39", VaadinIcon.GLOBE));
        statsLayout.add(createStatWithIcon("Accords", "230", VaadinIcon.HANDSHAKE));
        statsLayout.add(createStatWithIcon("Programmes", "50", VaadinIcon.BOOK));
        statsLayout.add(createStatWithIcon("Partenaires internationaux", ">120", VaadinIcon.GROUP));

        this.add(statsLayout);

        List<Paragraph> ver = List.of(
                new Paragraph("Version créée par Salim, Djibril et Lucas"));

        ver.forEach(p -> p.getStyle().set("margin-bottom", "10px"));

        ver.forEach(this::add);

        Div footer = new Div();
        footer.setText("MoveINSA - 2025");

        Anchor contactLink = new Anchor("mailto:relations.internationales@insa-strasbourg.fr", "Nous contacter");
        contactLink.getStyle().set("margin-left", "10px").set("color", "#d50000").set("font-weight", "bold");

        footer.add(contactLink);
        footer.getStyle()
                .set("text-align", "center")
                .set("margin-top", "30px")
                .set("color", "#757575")
                .set("font-size", "14px");
        this.add(footer);
    }

    private Div createStatWithIcon(String title, String value, VaadinIcon iconType) {
        Div stat = new Div();
        stat.getStyle()
                .set("text-align", "center")
                .set("padding", "20px")
                .set("border-radius", "10px")
                .set("background-color", "#ffffff")
                .set("transition", "transform 0.2s, box-shadow 0.2s");

        stat.getElement().addEventListener("mouseenter", e -> {
            stat.getStyle().set("transform", "scale(1.05)").set("box-shadow", "0 6px 12px rgba(0,0,0,0.15)");
        });
        stat.getElement().addEventListener("mouseleave", e -> {
            stat.getStyle().set("transform", "scale(1)").set("box-shadow", "none");
        });

        Icon icon = iconType.create();
        icon.getStyle().set("color", "#d50000").set("font-size", "24px").set("margin-bottom", "5px");

        Span statValue = new Span(value);
        statValue.getStyle()
                .set("font-size", "24px")
                .set("font-weight", "bold")
                .set("color", "#d50000");

        Span statTitle = new Span(title);
        statTitle.getStyle()
                .set("font-size", "16px")
                .set("color", "#333");

        stat.add(icon, statValue, new Div(statTitle));
        return stat;
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

