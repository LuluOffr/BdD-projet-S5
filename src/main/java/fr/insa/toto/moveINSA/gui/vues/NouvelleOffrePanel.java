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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.MainLayout;
import fr.insa.toto.moveINSA.model.OffreMobilite;
import fr.insa.toto.moveINSA.model.Partenaire;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author lucas
 */
@Route(value = "offres/nouveau", layout = MainLayout.class)
public class NouvelleOffrePanel extends VerticalLayout {

    private ChoixPartenaireCombo cbPartenaire;
    private PasswordField passwordField;
    private IntegerField ifPlaces;
    private Button bSave;

    public NouvelleOffrePanel() {
        this.cbPartenaire = new ChoixPartenaireCombo();
        this.passwordField = new PasswordField("Mot de passe de l'établissement");
        this.ifPlaces = new IntegerField("Nombre de places");
        this.ifPlaces.setEnabled(false); // desac par défaut
        this.bSave = new Button("Sauvegarder");

        // vérifi mdp avant d'afficher la suite
        this.cbPartenaire.addValueChangeListener(event -> {
            Partenaire selected = this.cbPartenaire.getValue();
            if (selected != null) {
                this.passwordField.setEnabled(true);
                this.ifPlaces.setEnabled(false);
            } else {
                this.passwordField.setEnabled(false);
                this.ifPlaces.setEnabled(false);
            }
        });

        this.passwordField.addValueChangeListener(event -> {
            Partenaire selected = this.cbPartenaire.getValue();
            if (selected != null) {
                String enteredPassword = this.passwordField.getValue();
                String correctPassword = selected.getRefPartenaire() + "2024";
                if (enteredPassword.equals(correctPassword)) {
                    Notification.show("Mot de passe correct !");
                    this.ifPlaces.setEnabled(true);
                } else {
                    Notification.show("Mot de passe incorrect !");
                    this.ifPlaces.setEnabled(false);
                }
            }
        });

        //sauvegarder la nouvelle offre
        this.bSave.addClickListener((t) -> {
            Partenaire selected = this.cbPartenaire.getValue();
            if (selected == null) {
                Notification.show("Vous devez sélectionner un partenaire");
            } else {
                Integer places = this.ifPlaces.getValue();
                if (places == null || places <= 0) {
                    Notification.show("Vous devez préciser un nombre de places valide");
                } else {
                    int partId = selected.getId();
                    OffreMobilite nouvelle = new OffreMobilite(places, partId);
                    try (Connection con = ConnectionPool.getConnection()) {
                        nouvelle.saveInDB(con);
                        Notification.show("Nouvelle offre enregistrée");
                    } catch (SQLException ex) {
                        Notification.show("Problème" + ex.getLocalizedMessage());
                    }
                }
            }
        });

        this.add(this.cbPartenaire, this.passwordField, this.ifPlaces, this.bSave);
    }
}
