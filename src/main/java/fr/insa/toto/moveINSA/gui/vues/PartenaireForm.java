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

    private TextField tfrefPartenaire = new TextField("refPartenaire");
    private PasswordField initialPasswordField = new PasswordField("Mot de passe initial");
    private Label generatedPasswordLabel = new Label();

    private Button validateButton = new Button("Valider le mot de passe initial");

    public PartenaireForm(Partenaire model, boolean modifiable) {
        this.model = model;
        this.setEnabled(modifiable);

        // Désactiver le champ refPartenaire tant que le mot de passe initial n'est pas validé
        this.tfrefPartenaire.setEnabled(false);

        // Ajout du champ de mot de passe initial et bouton de validation
        this.add(this.initialPasswordField, this.validateButton);

        // Action pour valider le mot de passe initial
        this.validateButton.addClickListener(event -> {
            String enteredPassword = this.initialPasswordField.getValue();
            if ("partenaire2024".equals(enteredPassword)) {
                Notification.show("Mot de passe validé !");
                this.tfrefPartenaire.setEnabled(true); // Activer le champ refPartenaire
            } else {
                Notification.show("Mot de passe incorrect !", 3000, Notification.Position.MIDDLE);
            }
        });

        // Ajout du champ refPartenaire et de l'étiquette pour afficher le mot de passe généré
        this.tfrefPartenaire.addValueChangeListener(event -> {
            String enteredRef = this.tfrefPartenaire.getValue();
            if (!enteredRef.isEmpty()) {
                String generatedPassword = enteredRef + "2024";
                this.generatedPasswordLabel.setText("Mot de passe généré : " + generatedPassword);
            } else {
                this.generatedPasswordLabel.setText("");
            }
        });

        this.add(this.tfrefPartenaire, this.generatedPasswordLabel);

        // Mettre à jour la vue et ajouter les champs
        this.updateView();
    }

    public void updateModel() {
        this.model.setRefPartenaire(this.tfrefPartenaire.getValue());
    }

    public void updateView() {
        this.tfrefPartenaire.setValue(this.model.getRefPartenaire());
    }
}