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

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.StreamResource;

/**
 * 
 */
public class EnteteInitiale extends HorizontalLayout {

    public EnteteInitiale() {
        //banderole
        this.getStyle().set("background-color", "#d50000"); 
        this.getStyle().set("color", "white"); 
        this.getStyle().set("padding", "10px");
        this.getStyle().set("width", "100%");
        this.setAlignItems(FlexComponent.Alignment.CENTER);

        //texte gauche
        Span titre = new Span("MoveINSA");
        titre.getStyle().set("font-size", "24px");
        titre.getStyle().set("font-weight", "bold");
        titre.getStyle().set("margin-left", "20px");

        // Image insa stras
        StreamResource logoResource = new StreamResource("insa-logo.jpg",
                () -> getClass().getResourceAsStream("/images/insa-logo.jpg")); 
        Image logo = new Image(logoResource, "INSA Strasbourg");
        logo.setWidth("100px");
        logo.setHeight("auto");
        logo.getStyle().set("margin-right", "20px");

        this.add(titre);
        this.add(logo);

        this.expand(titre);
    }
}
    
    
    
    
    
    
    
    
//ce qu'il y avait de base
    
    /*private TextField tfNom;
    private Button bLogin;
    private Button bLogout;

    public EnteteInitiale() {
        this.setWidthFull();
        this.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        this.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        this.tfNom = new TextField("ref partenaire");
        this.bLogin = new Button("login");
        this.bLogin.addClickListener((t) -> {
            try (Connection con = ConnectionPool.getConnection()) {
                String ref = this.tfNom.getValue();
                Optional<Partenaire> p = Partenaire.trouvePartaire(con, ref);
                if (p.isEmpty()) {
                    Notification.show(ref + " n'est pas un partenaire");
                } else {
                    SessionInfo.doLogin(p.get());
                }
            } catch (SQLException ex) {
                Notification.show("Problem : " + ex.getLocalizedMessage());
            } finally {
                this.refresh();
            }
        });
        this.bLogout = new Button("logout");
        this.bLogout.addClickListener((t) -> {
            SessionInfo.doLogout();
            this.refresh();
        });
        this.refresh();
    }

    private void refresh() {
        this.removeAll();
        if (SessionInfo.connected()) {
            this.add(new H3("bonjour " + SessionInfo.getLoggedPartRef()));
            this.add(this.bLogout);
        } else {
            this.add(this.tfNom, this.bLogin);
        }
    }
*/
