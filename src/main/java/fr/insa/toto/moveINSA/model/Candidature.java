
//marche
package fr.insa.toto.moveINSA.model;

import fr.insa.beuvron.utils.ConsoleFdB;
import fr.insa.beuvron.utils.list.ListUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe "miroir" de la table candidature.
 *
 * Cette classe permet d'interfacer facilement un programme Java avec une table
 * relationnelle candidature dans une base de données.
 * 
 * @author lucas
 */
public class Candidature implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String ine;
    private String idOffreMobilité;
    private java.sql.Date Date;
    private int ordre;

    /**
     * Création d'une nouvelle Candidature en mémoire, non existante dans la Base
     * de données.
     *
     * @param ine
     * @param idOffreMobilité
     * @param Date
     * @param ordre
     */
    public Candidature(String ine, String idOffreMobilité, java.sql.Date Date, int ordre) {
        this(-1, ine, idOffreMobilité, Date, ordre);
    }

/**
 * Création d'une Candidature retrouvée dans la base de données.
 *
 * @param id Identifiant unique de la candidature
 * @param ine INE de l'étudiant
 * @param idOffreMobilite Référence de l'offre de mobilité
 * @param date Date de la candidature
 * @param ordre Ordre du choix de l'étudiant
 */
public Candidature(int id, String ine, String idOffreMobilite, java.sql.Date date, int ordre) {
    this.id = id;
    this.ine = ine;
    this.idOffreMobilité = idOffreMobilite; // Correction
    this.Date = date; // Correction
    this.ordre = ordre;
}

    @Override
    public String toString() {
        return "Candidature{" +
                "id=" + id +
                ", ine='" + ine + '\'' +
                ", idOffreMobilité='" + idOffreMobilité + '\'' +
                ", Date=" + Date +
                ", ordre=" + ordre +
                '}';
    }

public int saveInDB(Connection con) throws SQLException {
    try (PreparedStatement insert = con.prepareStatement(
            "INSERT INTO candidature (ine, idOffreMobilité, Date, ordre) VALUES (?, ?, ?, ?)",
            PreparedStatement.RETURN_GENERATED_KEYS)) {
        insert.setString(1, this.ine);
        insert.setString(2, this.idOffreMobilité); 
        insert.setDate(3, this.Date);
        insert.setInt(4, this.ordre);
        insert.executeUpdate();
        try (ResultSet rid = insert.getGeneratedKeys()) {
            if (rid.next()) {
                this.id = rid.getInt(1);
            }
            return this.id;
        }
    }
}
/*
public static List<Candidature> toutesLesCandidatures(Connection con) throws SQLException {
    try (PreparedStatement pst = con.prepareStatement(
            "SELECT ine, idOffreMobilité, Date, ordre FROM candidature")) {
        ResultSet rs = pst.executeQuery();
        List<Candidature> res = new ArrayList<>();
        while (rs.next()) {
            res.add(new Candidature(
                    -1, // Pas d'id
                    rs.getString(1),
                    rs.getString(2),
                    rs.getDate(3),
                    rs.getInt(4)
            ));
        }
        return res;
    }
}
*/

public static Optional<Candidature> trouveCandidature(Connection con, String ine) throws SQLException {
    try (PreparedStatement pst = con.prepareStatement(
            "SELECT ine, idOffreMobilité, Date, ordre FROM candidature WHERE ine = ?")) {
        pst.setString(1, ine);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return Optional.of(new Candidature(
                    -1, // Pas d'id
                    rs.getString(1),
                    rs.getString(2),
                    rs.getDate(3),
                    rs.getInt(4)
            ));
        } else {
            return Optional.empty();
        }
    }
}

/*
public static List<Candidature> trouverCandidaturesParEtudiant(Connection con, String ine) throws SQLException {
    String query = "SELECT ine, idOffreMobilité, Date, ordre FROM candidature WHERE ine = ?";
    try (PreparedStatement pst = con.prepareStatement(query)) {
        pst.setString(1, ine);
        ResultSet rs = pst.executeQuery();
        List<Candidature> candidatures = new ArrayList<>();
        while (rs.next()) {
            candidatures.add(new Candidature(
                rs.getString("ine"),
                rs.getString("idOffreMobilité"),
                rs.getDate("Date"),
                rs.getInt("ordre")
            ));
        }
        return candidatures;
    }
}
*/

public static List<Candidature> toutesLesCandidatures(Connection con) throws SQLException {
    String query = "SELECT ine, idOffreMobilité, Date, ordre FROM candidature";
    try (PreparedStatement pst = con.prepareStatement(query)) {
        ResultSet rs = pst.executeQuery();
        List<Candidature> candidatures = new ArrayList<>();
        while (rs.next()) {
            candidatures.add(new Candidature(
                rs.getString("ine"),
                rs.getString("idOffreMobilité"),
                rs.getDate("Date"),
                rs.getInt("ordre")
            ));
        }
        return candidatures;
    }
}

    public static int creeConsole(Connection con) throws SQLException {
        String ine = ConsoleFdB.entreeString("INE : ");
        String idOffreMobilite = ConsoleFdB.entreeString("ID Offre Mobilité : ");
        java.sql.Date date = java.sql.Date.valueOf(ConsoleFdB.entreeString("Date (YYYY-MM-DD) : "));
        int ordre = ConsoleFdB.entreeInt("Ordre : ");
        Candidature nouvelle = new Candidature(ine, idOffreMobilite, date, ordre);
        return nouvelle.saveInDB(con);
    }

    public static Candidature selectInConsole(Connection con) throws SQLException {
        return ListUtils.selectOne("Choisissez une candidature :",
                toutesLesCandidatures(con), (elem) -> elem.getIne());
    }

    public int getId() {
        return id;
    }

    public String getIne() {
        return ine;
    }

    public void setIne(String ine) {
        this.ine = ine;
    }

    public String getIdOffreMobilité() {
        return idOffreMobilité;
    }

    public void setIdOffreMobilité(String idOffreMobilité) {
        this.idOffreMobilité = idOffreMobilité;
    }

    public java.sql.Date getDate() {
        return Date;
    }

    public void setDate(java.sql.Date date) {
        this.Date = Date;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }
}
