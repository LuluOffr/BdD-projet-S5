
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
    private String statut; // Champ pour le statut (ACCEPTE, REFUSE, EN ATTENTE)

    /**
     * Création d'une nouvelle Candidature en mémoire.
     *
     * @param ine INE de l'étudiant
     * @param idOffreMobilité Référence de l'offre de mobilité
     * @param Date Date de création de la candidature
     * @param ordre Ordre du choix
     * @param statut Statut de la candidature
     */
    public Candidature(String ine, String idOffreMobilité, java.sql.Date Date, int ordre, String statut) {
        this(-1, ine, idOffreMobilité, Date, ordre, statut);
    }

    /**
     * Création d'une Candidature retrouvée dans la base de données.
     *
     * @param id Identifiant unique de la candidature
     * @param ine INE de l'étudiant
     * @param idOffreMobilité Référence de l'offre de mobilité
     * @param Date Date de création de la candidature
     * @param ordre Ordre du choix
     * @param statut Statut de la candidature
     */
    public Candidature(int id, String ine, String idOffreMobilité, java.sql.Date Date, int ordre, String statut) {
        this.id = id;
        this.ine = ine;
        this.idOffreMobilité = idOffreMobilité;
        this.Date = Date;
        this.ordre = ordre;
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Candidature{" +
                "id=" + id +
                ", ine='" + ine + '\'' +
                ", idOffreMobilité='" + idOffreMobilité + '\'' +
                ", Date=" + Date +
                ", ordre=" + ordre +
                ", statut='" + statut + '\'' +
                '}';
    }

    /**
     * Enregistre une candidature dans la base de données.
     */
    public int saveInDB(Connection con) throws SQLException {
        try (PreparedStatement insert = con.prepareStatement(
                "INSERT INTO candidature (ine, idOffreMobilité, Date, ordre, statut) VALUES (?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, this.ine);
            insert.setString(2, this.idOffreMobilité);
            insert.setDate(3, this.Date);
            insert.setInt(4, this.ordre);
            insert.setString(5, this.statut);
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
    String query = "SELECT ine, idOffreMobilité, Date, ordre, statut FROM candidature";
    try (PreparedStatement pst = con.prepareStatement(query)) {
        ResultSet rs = pst.executeQuery();
        List<Candidature> candidatures = new ArrayList<>();
        while (rs.next()) {
            candidatures.add(new Candidature(
                rs.getString("ine"),
                rs.getString("idOffreMobilité"),
                rs.getDate("Date"),
                rs.getInt("ordre"),
                rs.getString("statut")
            ));
        }
        return candidatures;
    }
}

    /**
     * Trouve une candidature par INE.
     */
    public static Optional<Candidature> trouveCandidature(Connection con, String ine) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "SELECT ine, idOffreMobilité, Date, ordre, statut FROM candidature WHERE ine = ?")) {
            pst.setString(1, ine);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return Optional.of(new Candidature(
                        -1,
                        rs.getString("ine"),
                        rs.getString("idOffreMobilité"),
                        rs.getDate("Date"),
                        rs.getInt("ordre"),
                        rs.getString("statut")
                ));
            } else {
                return Optional.empty();
            }
        }
    }

    /**
     * Récupère toutes les candidatures pour un étudiant.
     */
    public static List<Candidature> trouverCandidaturesParEtudiant(Connection con, String ine) throws SQLException {
        String query = "SELECT ine, idOffreMobilité, Date, ordre, statut FROM candidature WHERE ine = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, ine);
            ResultSet rs = pst.executeQuery();
            List<Candidature> candidatures = new ArrayList<>();
            while (rs.next()) {
                candidatures.add(new Candidature(
                        rs.getString("ine"),
                        rs.getString("idOffreMobilité"),
                        rs.getDate("Date"),
                        rs.getInt("ordre"),
                        rs.getString("statut")
                ));
            }
            return candidatures;
        }
    }

    /**
     * Vérifie si un étudiant a atteint le maximum de candidatures.
     */
    public static boolean Candidaturesmax(Connection con, String ine) throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM candidature WHERE ine = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, ine);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int totalCandidatures = rs.getInt("total");
                    return totalCandidatures >= 5; // Retourne true si l'étudiant a 5 candidatures ou plus
                }
            }
        }
        return false;
    }

    /**
     * Met à jour le statut d'une candidature.
     */
    public static void mettreAJourStatut(Connection con, String ine, int ordre, String statut) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "UPDATE candidature SET statut = ? WHERE ine = ? AND ordre = ?")) {
            pst.setString(1, statut);
            pst.setString(2, ine);
            pst.setInt(3, ordre);
            pst.executeUpdate();
        }
    }

    /**
     * Méthode console pour créer une candidature.
     */
    public static int creeConsole(Connection con) throws SQLException {
        String ine = ConsoleFdB.entreeString("INE : ");
        String idOffreMobilite = ConsoleFdB.entreeString("ID Offre Mobilité : ");
        java.sql.Date date = java.sql.Date.valueOf(ConsoleFdB.entreeString("Date (YYYY-MM-DD) : "));
        int ordre = ConsoleFdB.entreeInt("Ordre : ");
        String statut = ConsoleFdB.entreeString("Statut : ");
        Candidature nouvelle = new Candidature(ine, idOffreMobilite, date, ordre, statut);
        return nouvelle.saveInDB(con);
    }

    /**
     * Méthode console pour sélectionner une candidature.
     */
    public static Candidature selectInConsole(Connection con) throws SQLException {
        return ListUtils.selectOne("Choisissez une candidature :",
                toutesLesCandidatures(con), (elem) -> elem.getIne());
    }

    // Getters et setters
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
        this.Date = date;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}