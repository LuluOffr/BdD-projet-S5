/*
 * Copyright (C) 2023 francois
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.toto.moveINSA.gui.vues;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import fr.insa.toto.moveINSA.model.Partenaire;

/**
 *
 * @author lucas
 */
public class PartenaireForm extends FormLayout {

    private Partenaire model;

    private TextField tfRefPartenaire = new TextField("Nom Partenaire");
    private TextField tfPays = new TextField("Pays");
    private TextField tfNom = new TextField("Nom de l'établissement");
    private TextField tfVille = new TextField("Ville");

    private PasswordField mdp = new PasswordField("Mot de passe");
    private Label generatedPasswordLabel = new Label();

    private Button valid = new Button("Valider le mot de passe");

    public PartenaireForm(Partenaire model, boolean modifiable) {
        this.model = model;
        this.setEnabled(modifiable);

        // désactive tout jusqu'a mdp
        desactiv();

        this.add(this.mdp, this.valid);

        this.valid.addClickListener(event -> {
            String enteredPassword = this.mdp.getValue();
            if ("partenaire2024".equals(enteredPassword)) {
                Notification.show("Mot de passe validé !");
                activ();
            } else {
                Notification.show("Mot de passe incorrect !", 3000, Notification.Position.MIDDLE);
            }
        });

        // ajout si bon mdp 
        this.tfRefPartenaire.addValueChangeListener(event -> mdpgenere());
        this.add(this.tfRefPartenaire, this.tfPays, this.tfNom, this.tfVille, this.generatedPasswordLabel);

        //met à jour la vue
        this.majvue();
    }

    private void desactiv() {
        this.tfRefPartenaire.setEnabled(false);
        this.tfPays.setEnabled(false);
        this.tfNom.setEnabled(false);
        this.tfVille.setEnabled(false);
    }

    private void activ() {
        this.tfRefPartenaire.setEnabled(true);
        this.tfPays.setEnabled(true);
        this.tfNom.setEnabled(true);
        this.tfVille.setEnabled(true);
    }

    private void mdpgenere() {
        String enteredRef = this.tfRefPartenaire.getValue();
        if (!enteredRef.isEmpty()) {
            String generatedPassword = enteredRef + "2024";
            this.generatedPasswordLabel.setText("Mot de passe généré : " + generatedPassword);
        } else {
            this.generatedPasswordLabel.setText("");
        }
    }

    public void maj() {
        this.model.setRefPartenaire(this.tfRefPartenaire.getValue());
        this.model.setPays(this.tfPays.getValue());
        this.model.setNom(this.tfNom.getValue());
        this.model.setVille(this.tfVille.getValue());
    }

    public void majvue() {
        this.tfRefPartenaire.setValue(this.model.getRefPartenaire());
        this.tfPays.setValue(this.model.getPays());
        this.tfNom.setValue(this.model.getNom());
        this.tfVille.setValue(this.model.getVille());
    }
}