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

    private PasswordField initialPasswordField = new PasswordField("Mot de passe initial");
    private Label generatedPasswordLabel = new Label();

    private Button validateButton = new Button("Valider le mot de passe initial");

    public PartenaireForm(Partenaire model, boolean modifiable) {
        this.model = model;
        this.setEnabled(modifiable);

        // Désactiver les champs jusqu'à validation du mot de passe initial
        disableAllFields();

        // Ajout du champ de mot de passe initial et bouton de validation
        this.add(this.initialPasswordField, this.validateButton);

        // Action pour valider le mot de passe initial
        this.validateButton.addClickListener(event -> {
            String enteredPassword = this.initialPasswordField.getValue();
            if ("partenaire2024".equals(enteredPassword)) {
                Notification.show("Mot de passe validé !");
                enableAllFields();
            } else {
                Notification.show("Mot de passe incorrect !", 3000, Notification.Position.MIDDLE);
            }
        });

        // Ajout des champs
        this.tfRefPartenaire.addValueChangeListener(event -> updateGeneratedPassword());
        this.add(this.tfRefPartenaire, this.tfPays, this.tfNom, this.tfVille, this.generatedPasswordLabel);

        // Mettre à jour la vue
        this.updateView();
    }

    private void disableAllFields() {
        this.tfRefPartenaire.setEnabled(false);
        this.tfPays.setEnabled(false);
        this.tfNom.setEnabled(false);
        this.tfVille.setEnabled(false);
    }

    private void enableAllFields() {
        this.tfRefPartenaire.setEnabled(true);
        this.tfPays.setEnabled(true);
        this.tfNom.setEnabled(true);
        this.tfVille.setEnabled(true);
    }

    private void updateGeneratedPassword() {
        String enteredRef = this.tfRefPartenaire.getValue();
        if (!enteredRef.isEmpty()) {
            String generatedPassword = enteredRef + "2024";
            this.generatedPasswordLabel.setText("Mot de passe généré : " + generatedPassword);
        } else {
            this.generatedPasswordLabel.setText("");
        }
    }

    public void updateModel() {
        this.model.setRefPartenaire(this.tfRefPartenaire.getValue());
        this.model.setPays(this.tfPays.getValue());
        this.model.setNom(this.tfNom.getValue());
        this.model.setVille(this.tfVille.getValue());
    }

    public void updateView() {
        this.tfRefPartenaire.setValue(this.model.getRefPartenaire());
        this.tfPays.setValue(this.model.getPays());
        this.tfNom.setValue(this.model.getNom());
        this.tfVille.setValue(this.model.getVille());
    }
}