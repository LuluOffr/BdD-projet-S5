package fr.insa.toto.moveINSA.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Etudiant implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String ine;
    private String nom;
    private String prenom;
    private String classe;
    private String score; // Modifié de double à String

    // Constructeurs
    public Etudiant(String ine, String nom, String prenom, String classe, String score) {
        this(-1, ine, nom, prenom, classe, score);
    }

    public Etudiant(int id, String ine, String nom, String prenom, String classe, String score) {
        this.id = id;
        this.ine = ine;
        this.nom = nom;
        this.prenom = prenom;
        this.classe = classe;
        this.score = score;
    }

    // Méthodes CRUD

    public int saveInDB(Connection con) throws SQLException {
        if (this.id != -1) {
            throw new IllegalStateException("Étudiant déjà sauvegardé.");
        }
        String query = "INSERT INTO etudiant (ine, nom, prenom, classe, score) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement insert = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, this.ine);
            insert.setString(2, this.nom);
            insert.setString(3, this.prenom);
            insert.setString(4, this.classe);
            insert.setString(5, this.score); // Modifié pour utiliser setString
            insert.executeUpdate();

            try (ResultSet rs = insert.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
            return this.id;
        }
    }

    public static List<Etudiant> tousLesEtudiants(Connection con) throws SQLException {
        String query = "SELECT id, ine, nom, prenom, classe, score FROM etudiant";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            List<Etudiant> etudiants = new ArrayList<>();
            while (rs.next()) {
                etudiants.add(new Etudiant(
                        rs.getInt("id"),
                        rs.getString("ine"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("classe"),
                        rs.getString("score") // Modifié pour récupérer une chaîne
                ));
            }
            return etudiants;
        }
    }

    public static Optional<Etudiant> trouveEtudiant(Connection con, String ine) throws SQLException {
        String query = "SELECT id, ine, nom, prenom, classe, score FROM etudiant WHERE ine = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, ine);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Etudiant(
                            rs.getInt("id"),
                            rs.getString("ine"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("classe"),
                            rs.getString("score") // Modifié pour récupérer une chaîne
                    ));
                }
            }
        }
        return Optional.empty();
    }

    // Méthodes Getters et Setters
    public int getId() {
        return id;
    }

    public String getIne() {
        return ine;
    }

    public void setIne(String ine) {
        this.ine = ine;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getScore() { // Modifié pour retourner une chaîne
        return score;
    }

    public void setScore(String score) { // Modifié pour accepter une chaîne
        this.score = score;
    }

    @Override
    public String toString() {
        return "Etudiant{" +
                "id=" + id +
                ", ine='" + ine + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", classe='" + classe + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}

     