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

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import fr.insa.toto.moveINSA.model.Partenaire;

/**
 *
 * @author francois
 */
public class PartenaireForm extends FormLayout {
    
    private Partenaire model;

    private TextField tfrefPartenaire = new TextField("refPartenaire");
    
    public PartenaireForm(Partenaire model, boolean modifiable) {
        this.model = model;
        this.setEnabled(modifiable);
        this.updateView();
        this.add(this.tfrefPartenaire);
    }
    
    public void updateModel() {
        this.model.setRefPartenaire(this.tfrefPartenaire.getValue()); 
    }
    
    public void updateView() {
        this.tfrefPartenaire.setValue(this.model.getRefPartenaire());
    }
}
