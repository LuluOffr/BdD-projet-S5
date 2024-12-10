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

import fr.insa.beuvron.utils.ConsoleFdB;
import fr.insa.beuvron.utils.exceptions.ExceptionsUtils;
import fr.insa.beuvron.utils.list.ListUtils;
import fr.insa.beuvron.utils.database.ResultSetUtils;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.h2.jdbc.meta.DatabaseMetaServer;
import java.util.Optional;

/**
 * Opération générales sur la base de donnée de gestion des tournois.
 * <p>
 * Les opérations plus spécifiques aux diverses tables sont réparties dans les
 * classes correspondantes.
 * </p>
 *
 * @author francois
 */
public class GestionBdD {

    /**
     * création complète du schéma de la BdD.
     *
     * @param con
     * @throws SQLException
     */
    public static void creeSchema(Connection con)
            throws SQLException {
        con.setAutoCommit(false);
        try (Statement st = con.createStatement()) {
            // creation des tables
            st.executeUpdate(
                    "create table partenaire ( \n"
                    + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                    + " refPartenaire varchar(50) not null unique\n"
                    + ")");
            st.executeUpdate(
                    "create table offremobilite ( \n"
                    + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                    + " nbrplaces int not null,\n"
                    + " proposepar int not null\n"
                    + ")");
            // création des liens
            st.executeUpdate(
                    """
                    alter table offremobilite
                        add constraint fk_offremobilite_proposepar
                        foreign key (proposepar) references partenaire(id)
                        on delete restrict on update restrict
                    """);
            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

    /**
     * suppression complete de toute la BdD.
     *
     * @param con
     * @throws SQLException
     */
    public static void deleteSchema(Connection con) throws SQLException {
        try (Statement st = con.createStatement()) {
            // je supprime d'abord les liens
            try {
                st.executeUpdate(
                        "alter table offremobilite drop constraint fk_offremobilite_proposepar");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            // je peux maintenant supprimer les tables
            try {
                st.executeUpdate("drop table offremobilite");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
            try {
                st.executeUpdate("drop table partenaire");
            } catch (SQLException ex) {
            }
        }
    }

    /**
     * crée un jeu de test dans la BdD.
     *
     * @param con
     * @throws SQLException
     */
    public static void initBdDTest(Connection con) throws SQLException {
        List<Partenaire> partenaires = List.of(
             new Partenaire("MIT", "USA", "Massachusetts Institute of Technology", "Cambridge"),
            new Partenaire("Oxford", "UK", "University of Oxford", "Oxford")
        );
        for (var p : partenaires) {
            p.saveInDB(con);
        }
        List<OffreMobilite> offres = List.of(
                new OffreMobilite(1, partenaires.get(0).getId()),
                new OffreMobilite(2, partenaires.get(0).getId()),
                new OffreMobilite(5, partenaires.get(1).getId())
        );
        for (var o : offres) {
            o.saveInDB(con);
        }

    }

    public static void razBDD(Connection con) throws SQLException {
        deleteSchema(con);
        creeSchema(con);
        initBdDTest(con);
    }

    public static void menuPartenaire(Connection con) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu partenaires");
            System.out.println("==================");
            System.out.println((i++) + ") liste de tous les partenaires");
            System.out.println((i++) + ") créer un nouveau partenaire");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    List<Partenaire> users = Partenaire.tousLesPartaires(con);
                    System.out.println(users.size() + " utilisateurs : ");
                    System.out.println(ListUtils.enumerateList(users, (elem) -> elem.toString()));
                } else if (rep == j++) {
                    Partenaire.creeConsole(con);
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
        }
    }

    public static void menuOffre(Connection con) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu offres mobilité");
            System.out.println("==================");
            System.out.println((i++) + ") liste de toutes les offres");
            System.out.println((i++) + ") créer une nouvelle offre");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    List<OffreMobilite> offres = OffreMobilite.toutesLesOffres(con);
                    System.out.println(offres.size() + " offres : ");
                    System.out.println(ListUtils.enumerateList(offres, (elem) -> elem.toString()));
                } else if (rep == j++) {
                    OffreMobilite.creeConsole(con);
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
        }
    }

    public static void menuBdD(Connection con) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu gestion base de données");
            System.out.println("============================");
            System.out.println((i++) + ") RAZ BdD = delete + create + init");
            System.out.println((i++) + ") donner un ordre SQL update quelconque");
            System.out.println((i++) + ") donner un ordre SQL query quelconque");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    razBDD(con);
                } else if (rep == j++) {
                    String ordre = ConsoleFdB.entreeString("ordre SQL : ");
                    try (PreparedStatement pst = con.prepareStatement(ordre)) {
                        pst.executeUpdate();
                    }
                } else if (rep == j++) {
                    String ordre = ConsoleFdB.entreeString("requete SQL : ");
                    try (PreparedStatement pst = con.prepareStatement(ordre)) {
                        try (ResultSet rst = pst.executeQuery()) {
                            System.out.println(ResultSetUtils.formatResultSetAsTxt(rst));
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.beuvron", 3));
            }
        }
    }

public static void menuEtudiant(Connection con) {
    int rep = -1;
    while (rep != 0) {
        int i = 1;
        System.out.println("Menu Étudiant");
        System.out.println("==================");
        System.out.println((i++) + ") Liste de tous les étudiants");
        System.out.println((i++) + ") Créer un nouveau profil");
        System.out.println((i++) + ") Rechercher un étudiant par INE");
        System.out.println("0) Retour");
        rep = ConsoleFdB.entreeEntier("Votre choix : ");
        try {
            int j = 1;
            if (rep == j++) {
                // Liste de tous les étudiants
                List<Etudiant> etudiants = Etudiant.tousLesEtudiants(con);
                if (etudiants.isEmpty()) {
                    System.out.println("Aucun étudiant trouvé.");
                } else {
                    System.out.println(etudiants.size() + " étudiants : ");
                    System.out.println(ListUtils.enumerateList(etudiants, (elem) -> elem.toString()));
                }
            } else if (rep == j++) {
                // Créer un nouvel étudiant
                int id = Etudiant.creeConsole(con);
                System.out.println("Étudiant créé avec l'ID : " + id);
            } else if (rep == j++) {
                // Rechercher un étudiant par INE
                String ine = ConsoleFdB.entreeString("INE de l'étudiant : ");
                Optional<Etudiant> etu = Etudiant.trouveEtudiant(con, ine);
                if (etu.isPresent()) {
                    System.out.println("Étudiant trouvé : " + etu.get());
                } else {
                    System.out.println("Aucun étudiant trouvé avec cet INE.");
                }
            }
        } catch (Exception ex) {
            System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
        }
    }
}

public static void menuCandidature(Connection con) {
    int rep = -1;
    while (rep != 0) {
        int i = 1;
        System.out.println("Menu Candidatures");
        System.out.println("==================");
        System.out.println((i++) + ") Liste de toutes les candidatures");
        System.out.println((i++) + ") Créer une nouvelle candidature");
        System.out.println((i++) + ") Rechercher une candidature par INE");
        System.out.println("0) Retour");
        rep = ConsoleFdB.entreeEntier("Votre choix : ");
        try {
            int j = 1;
            if (rep == j++) {
                // Liste de toutes les candidatures
                List<Candidature> candidatures = Candidature.toutesLesCandidatures(con);
                if (candidatures.isEmpty()) {
                    System.out.println("Aucune candidature trouvée.");
                } else {
                    System.out.println(candidatures.size() + " candidatures : ");
                    System.out.println(ListUtils.enumerateList(candidatures, (elem) -> elem.toString()));
                }
            } else if (rep == j++) {
                // Créer une nouvelle candidature
                int id = Candidature.creeConsole(con);
                System.out.println("Candidature créée avec l'ID : " + id);
            } else if (rep == j++) {
                // Rechercher une candidature par INE
                String ine = ConsoleFdB.entreeString("INE de la candidature : ");
                Optional<Candidature> cand = Candidature.trouveCandidature(con, ine);
                if (cand.isPresent()) {
                    System.out.println("Candidature trouvée : " + cand.get());
                } else {
                    System.out.println("Aucune candidature trouvée pour cet INE.");
                }
            }
        } catch (Exception ex) {
            System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
        }
    }
}
          
    
    public static void menuPrincipal() {
        int rep = -1;
        Connection con = null;
        try {
            con = ConnectionSimpleSGBD.defaultCon();
            System.out.println("Connection OK");
        } catch (SQLException ex) {
            System.out.println("Problème de connection : " + ex.getLocalizedMessage());
            throw new Error(ex);
        }
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu principal");
            System.out.println("==================");
            System.out.println((i++) + ") test driver mysql");
            System.out.println((i++) + ") menu gestion BdD");
            System.out.println((i++) + ") menu partenaires");
            System.out.println((i++) + ") menu offres");
            System.out.println((i++) + ") menu Etudiant");
            System.out.println((i++) + ") menu Candidature");
            System.out.println("0) Fin");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    try {
                        Class<Driver> mysqlDriver = (Class<Driver>) Class.forName("com.mysql.cj.jdbc.Driver");
                    } catch (ClassNotFoundException ex) {
                        System.out.println("com.mysql.cj.jdbc.Driver not found");
                    }
                    DatabaseMetaData meta = con.getMetaData();
                    System.out.println("jdbc driver version : " + meta.getDriverName() + " ; " + meta.getDriverVersion());
                } else if (rep == j++) {
                    menuBdD(con);
                } else if (rep == j++) {
                    menuPartenaire(con);
                } else if (rep == j++) {
                    menuOffre(con);
                } else if (rep == j++) {
                    menuEtudiant(con);
                } else if (rep == j++) {
                    menuCandidature(con);
                }
                
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
        }
    }

    public static void main(String[] args) {
        menuPrincipal();
    }
}
