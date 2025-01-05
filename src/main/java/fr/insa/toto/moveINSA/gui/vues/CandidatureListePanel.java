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
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.MainLayout;
import fr.insa.toto.moveINSA.model.Candidature;
import fr.insa.toto.moveINSA.model.Etudiant;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author lucas
 */

@PageTitle("Liste des candidatures")
@Route(value = "candidatures/liste", layout = MainLayout.class)
public class CandidatureListePanel extends VerticalLayout {

    private static final String PASSWORD = "SRI2024"; 
    private boolean isAuthenticated = false; 
    private Grid<CandidatureAvecEtudiant> grid;
    private VerticalLayout contentLayout;

    public CandidatureListePanel() {
        this.add(new H3("Liste des candidatures"));

        PasswordField passwordField = new PasswordField("Mot de passe");
        Button verifyButton = new Button("Vérifier", event -> {
            if (PASSWORD.equals(passwordField.getValue())) {
                isAuthenticated = true;
                Notification.show("Accès autorisé !");
                ListeCand();
            } else {
                Notification.show("Mot de passe incorrect !", 3000, Notification.Position.MIDDLE);
            }
        });

        HorizontalLayout passwordLayout = new HorizontalLayout(passwordField, verifyButton);
        this.add(passwordLayout);

        contentLayout = new VerticalLayout();
        this.add(contentLayout);
    }

    //pour afficher candidatures avec infos de l'étudiant
    private void ListeCand() {
        if (isAuthenticated) {
            try (Connection con = ConnectionPool.getConnection()) {
                List<Candidature> candidatures = Candidature.toutesLesCandidatures(con);

                // Enrichir les candidatures avec les informations des étudiants
                List<CandidatureAvecEtudiant> candidaturesAvecEtudiant = new ArrayList<>();
                for (Candidature candidature : candidatures) {
                    Optional<Etudiant> etudiantOpt = Etudiant.trouveEtudiant(con, candidature.getIne());
                    if (etudiantOpt.isPresent()) {
                        Etudiant etudiant = etudiantOpt.get();
                        candidaturesAvecEtudiant.add(new CandidatureAvecEtudiant(
                                candidature,
                                etudiant.getNom(),
                                etudiant.getPrenom(),
                                etudiant.getClasse()
                        ));
                    }
                }

                grid = new Grid<>(CandidatureAvecEtudiant.class, false);

                grid.addColumn(CandidatureAvecEtudiant::getIne).setHeader("INE").setSortable(true);
                grid.addColumn(CandidatureAvecEtudiant::getNom).setHeader("Nom").setSortable(true);
                grid.addColumn(CandidatureAvecEtudiant::getPrenom).setHeader("Prénom").setSortable(true);
                grid.addColumn(CandidatureAvecEtudiant::getClasse).setHeader("Classe").setSortable(true);
                grid.addColumn(CandidatureAvecEtudiant::getIdOffreMobilite).setHeader("ID Offre Mobilité").setSortable(true);
                grid.addColumn(CandidatureAvecEtudiant::getDate).setHeader("Date").setSortable(true);
                grid.addColumn(CandidatureAvecEtudiant::getOrdre).setHeader("Ordre").setSortable(true);
                grid.addColumn(CandidatureAvecEtudiant::getStatut).setHeader("Statut").setSortable(true);

                grid.setItems(candidaturesAvecEtudiant);

                // enlever ancien contenu et ajouter le tableau
                contentLayout.removeAll();
                contentLayout.add(new Paragraph("Liste des candidatures :"));
                contentLayout.add(grid);

            } catch (SQLException ex) {
                Notification.show("Erreur : Impossible de charger les candidatures. Détails : " + ex.getLocalizedMessage());
                ex.printStackTrace();
            }
        }
    }

    //classe interne pour avoir des infos de l'étudiant en plus dans la tableau
    public static class CandidatureAvecEtudiant {
        private final Candidature candidature;
        private final String nom;
        private final String prenom;
        private final String classe;

        public CandidatureAvecEtudiant(Candidature candidature, String nom, String prenom, String classe) {
            this.candidature = candidature;
            this.nom = nom;
            this.prenom = prenom;
            this.classe = classe;
        }

        public String getIne() {
            return candidature.getIne();
        }

        public String getIdOffreMobilite() {
            return candidature.getIdOffreMobilité();
        }

        public java.sql.Date getDate() {
            return candidature.getDate();
        }

        public int getOrdre() {
            return candidature.getOrdre();
        }

        public String getStatut() {
            return candidature.getStatut();
        }

        public String getNom() {
            return nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public String getClasse() {
            return classe;
        }
    }
}