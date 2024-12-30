//marche
package fr.insa.toto.moveINSA.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

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
    private java.sql.Date date;
    private int ordre;

    public Candidature(String ine, String idOffreMobilité, java.sql.Date date, int ordre) {
        this(-1, ine, idOffreMobilité, date, ordre);
    }

    public Candidature(int id, String ine, String idOffreMobilité, java.sql.Date date, int ordre) {
        this.id = id;
        this.ine = ine;
        this.idOffreMobilité = idOffreMobilité;
        this.date = date;
        this.ordre = ordre;
    }

    @Override
    public String toString() {
        return "Candidature{" +
               "id=" + id +
               ", ine='" + ine + '\'' +
               ", idOffreMobilité='" + idOffreMobilité + '\'' +
               ", date=" + date +
               ", ordre=" + ordre +
               '}';
    }

    public int saveInDB(Connection con) throws SQLException {
        if (this.id != -1) {
            throw new SQLException("Candidature déjà enregistrée.");
        }

        if (!verifierOffreMobiliteExiste(con, this.idOffreMobilité)) {
            throw new SQLException("L'offre de mobilité spécifiée n'existe pas.");
        }

        try (PreparedStatement insert = con.prepareStatement(
                "INSERT INTO candidature (ine, idOffreMobilité, Date, ordre) VALUES (?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, this.ine);
            insert.setString(2, this.idOffreMobilité);
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

    private boolean verifierOffreMobiliteExiste(Connection con, String idOffreMobilite) throws SQLException {
        String query = "SELECT COUNT(*) FROM offre_de_mobilité WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, idOffreMobilite);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public static List<Candidature> toutesLesCandidatures(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "SELECT id, ine, idOffreMobilité, Date, ordre FROM candidature")) {
            ResultSet rs = pst.executeQuery();
            List<Candidature> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new Candidature(
                        rs.getInt("id"),
                        rs.getString("ine"),
                        rs.getString("idOffreMobilité"),
                        rs.getDate("Date"),
                        rs.getInt("ordre")
                ));
            }
            return res;
        }
    }

    public static Optional<Candidature> trouveCandidature(Connection con, String ine) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "SELECT id, ine, idOffreMobilité, Date, ordre FROM candidature WHERE ine = ?")) {
            pst.setString(1, ine);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return Optional.of(new Candidature(
                        rs.getInt("id"),
                        rs.getString("ine"),
                        rs.getString("idOffreMobilité"),
                        rs.getDate("Date"),
                        rs.getInt("ordre")
                ));
            } else {
                return Optional.empty();
            }
        }
    }

    /**
     * Crée une nouvelle candidature en saisissant les données via la console.
     */
    public static int creeConsole(Connection con) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("INE : ");
        String ine = scanner.nextLine();

        System.out.print("ID Offre Mobilité : ");
        String idOffreMobilite = scanner.nextLine();

        System.out.print("Date (YYYY-MM-DD) : ");
        java.sql.Date date = java.sql.Date.valueOf(scanner.nextLine());

        System.out.print("Ordre : ");
        int ordre = scanner.nextInt();

        Candidature nouvelle = new Candidature(ine, idOffreMobilite, date, ordre);

        return nouvelle.saveInDB(con);
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
