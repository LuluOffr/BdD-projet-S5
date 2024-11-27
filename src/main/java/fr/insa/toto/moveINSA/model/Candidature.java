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
 * relationnelle `candidature` dans une base de données.
 * 
 * @author votre_nom
 */
public class Candidature implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String ine;
    private String idOffreMobilite;
    private java.sql.Date date;
    private int ordre;

    /**
     * Création d'une nouvelle Candidature en mémoire, non existante dans la Base
     * de données.
     *
     * @param ine
     * @param idOffreMobilite
     * @param date
     * @param ordre
     */
    public Candidature(String ine, String idOffreMobilite, java.sql.Date date, int ordre) {
        this(-1, ine, idOffreMobilite, date, ordre);
    }

    /**
     * Création d'une Candidature retrouvée dans la base de données.
     *
     * @param id
     * @param ine
     * @param idOffreMobilite
     * @param date
     * @param ordre
     */
    public Candidature(int id, String ine, String idOffreMobilite, java.sql.Date date, int ordre) {
        this.id = id;
        this.ine = ine;
        this.idOffreMobilite = idOffreMobilite;
        this.date = date;
        this.ordre = ordre;
    }

    @Override
    public String toString() {
        return "Candidature{" +
                "id=" + id +
                ", ine='" + ine + '\'' +
                ", idOffreMobilite='" + idOffreMobilite + '\'' +
                ", date=" + date +
                ", ordre=" + ordre +
                '}';
    }

    public int saveInDB(Connection con) throws SQLException {
        if (this.id != -1) {
            throw new EntiteDejaSauvegardee();
        }
        try (PreparedStatement insert = con.prepareStatement(
                "INSERT INTO candidature (ine, idOffreMobilite, date, ordre) VALUES (?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, this.ine);
            insert.setString(2, this.idOffreMobilite);
            insert.setDate(3, this.date);
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

    public static List<Candidature> toutesLesCandidatures(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "SELECT id, ine, idOffreMobilite, date, ordre FROM candidature")) {
            ResultSet rs = pst.executeQuery();
            List<Candidature> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new Candidature(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4),
                        rs.getInt(5)
                ));
            }
            return res;
        }
    }

    public static Optional<Candidature> trouveCandidature(Connection con, String ine) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "SELECT id, ine, idOffreMobilite, date, ordre FROM candidature WHERE ine = ?")) {
            pst.setString(1, ine);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return Optional.of(new Candidature(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4),
                        rs.getInt(5)
                ));
            } else {
                return Optional.empty();
            }
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

    public String getIdOffreMobilite() {
        return idOffreMobilite;
    }

    public void setIdOffreMobilite(String idOffreMobilite) {
        this.idOffreMobilite = idOffreMobilite;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }
}
