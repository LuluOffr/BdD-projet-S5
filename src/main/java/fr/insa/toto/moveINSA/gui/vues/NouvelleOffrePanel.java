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
import com.vaadin.flow.component.textfield.TextField;
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
    private TextField tfSemestre;
    private TextField tfDispositif;
    private TextField tfSpecialite;
    private Button bSave;

    public NouvelleOffrePanel() {
        this.cbPartenaire = new ChoixPartenaireCombo();
        this.passwordField = new PasswordField("Mot de passe de l'établissement");
        this.ifPlaces = new IntegerField("Nombre de places");
        this.tfSemestre = new TextField("Semestre");
        this.tfDispositif = new TextField("Dispositif");
        this.tfSpecialite = new TextField("Spécialité");
        this.ifPlaces.setEnabled(false); // désactivé par défaut
        this.tfSemestre.setEnabled(false);
        this.tfDispositif.setEnabled(false);
        this.tfSpecialite.setEnabled(false);
        this.bSave = new Button("Sauvegarder");

        // verif mdp
        this.cbPartenaire.addValueChangeListener(event -> {
            Partenaire selected = this.cbPartenaire.getValue();
            if (selected != null) {
                this.passwordField.setEnabled(true);
                this.ifPlaces.setEnabled(false);
                this.tfSemestre.setEnabled(false);
                this.tfDispositif.setEnabled(false);
                this.tfSpecialite.setEnabled(false);
            } else {
                this.passwordField.setEnabled(false);
                this.ifPlaces.setEnabled(false);
                this.tfSemestre.setEnabled(false);
                this.tfDispositif.setEnabled(false);
                this.tfSpecialite.setEnabled(false);
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
                    this.tfSemestre.setEnabled(true);
                    this.tfDispositif.setEnabled(true);
                    this.tfSpecialite.setEnabled(true);
                } else {
                    Notification.show("Mot de passe incorrect !");
                    this.ifPlaces.setEnabled(false);
                    this.tfSemestre.setEnabled(false);
                    this.tfDispositif.setEnabled(false);
                    this.tfSpecialite.setEnabled(false);
                }
            }
        });

        // sauv la nouvelle offre
        this.bSave.addClickListener((t) -> {
            Partenaire selected = this.cbPartenaire.getValue();
            if (selected == null) {
                Notification.show("Vous devez sélectionner un partenaire");
            } else {
                Integer places = this.ifPlaces.getValue();
                String semestre = this.tfSemestre.getValue();
                String dispositif = this.tfDispositif.getValue();
                String specialite = this.tfSpecialite.getValue();
                if (places == null || places <= 0) {
                    Notification.show("Vous devez préciser un nombre de places valide");
                } else if (semestre == null || semestre.isEmpty()) {
                    Notification.show("Vous devez préciser un semestre");
                } else if (dispositif == null || dispositif.isEmpty()) {
                    Notification.show("Vous devez préciser un dispositif");
                } else if (specialite == null || specialite.isEmpty()) {
                    Notification.show("Vous devez préciser une spécialité");
                } else {
                    int partId = selected.getId();
                    OffreMobilite nouvelle = new OffreMobilite(-1, places, partId, semestre, dispositif, specialite);
                    try (Connection con = ConnectionPool.getConnection()) {
                        nouvelle.saveInDB(con);
                        Notification.show("Nouvelle offre enregistrée");
                    } catch (SQLException ex) {
                        Notification.show("Problème : " + ex.getLocalizedMessage());
                    }
                }
            }
        });

        this.add(this.cbPartenaire, this.passwordField, this.ifPlaces, this.tfSemestre, this.tfDispositif, this.tfSpecialite, this.bSave);
    }
}
