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
package fr.insa.toto.moveINSA.model;
import java.sql.Date;

/**
 *
 * @author seife
 */
/**
 * Classe enrichie pour inclure les informations du partenaire associées à une candidature.
 */
public class CandidatureAvecPartenaire extends Candidature {

    private int idPartenaire;
    private String nomPartenaire;
    private String refPartenaire;
    private String pays;
    private String ville;

    public CandidatureAvecPartenaire(int id, String ine, String idOffreMobilite, Date date, int ordre,
                                     int idPartenaire, String nomPartenaire, String refPartenaire, String pays, String ville) {
        super(id, ine, idOffreMobilite, date, ordre);
        this.idPartenaire = idPartenaire;
        this.nomPartenaire = nomPartenaire;
        this.refPartenaire = refPartenaire;
        this.pays = pays;
        this.ville = ville;
    }

    public int getIdPartenaire() {
        return idPartenaire;
    }

    public void setIdPartenaire(int idPartenaire) {
        this.idPartenaire = idPartenaire;
    }

    public String getNomPartenaire() {
        return nomPartenaire;
    }

    public void setNomPartenaire(String nomPartenaire) {
        this.nomPartenaire = nomPartenaire;
    }

    public String getRefPartenaire() {
        return refPartenaire;
    }

    public void setRefPartenaire(String refPartenaire) {
        this.refPartenaire = refPartenaire;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    @Override
    public String toString() {
        return super.toString() +
               ", idPartenaire=" + idPartenaire +
               ", nomPartenaire='" + nomPartenaire + '\'' +
               ", refPartenaire='" + refPartenaire + '\'' +
               ", pays='" + pays + '\'' +
               ", ville='" + ville + '\'';
    }
}

