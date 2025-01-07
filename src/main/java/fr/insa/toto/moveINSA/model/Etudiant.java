//ca marche

package fr.insa.toto.moveINSA.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import fr.insa.beuvron.utils.ConsoleFdB;
import fr.insa.beuvron.utils.list.ListUtils;

public class Etudiant implements Serializable {

    private static final long serialVersionUID = 1L;

    // Attributs

    private String ine;
    private String nom;
    private String prenom;
    private String classe;
    private String score;

    // Constructeurs

    public Etudiant(String ine, String nom, String prenom, String classe, String score) {
 
        this.ine = ine;
        this.nom = nom;
        this.prenom = prenom;
        this.classe = classe;
        this.score = score;
    }

    // Méthodes CRUD
public String saveInDB(Connection con) throws SQLException {
    // Requête d'insertion
    String query = "INSERT INTO etudiant (ine, nom, prénom, classe, score) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement insert = con.prepareStatement(query)) {
        // Remplir les paramètres de la requête
        insert.setString(1, this.ine);    // INE
        insert.setString(2, this.nom);   // Nom
        insert.setString(3, this.prenom); // Prénom
        insert.setString(4, this.classe); // Classe
        insert.setString(5, this.score);    // Score

        // Exécuter la requête d'insertion
        int rowsAffected = insert.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Étudiant ajouté avec succès : " + this.ine);
            return this.ine;
        } else {
            throw new SQLException("Impossible d'ajouter l'étudiant. Aucune ligne affectée.");
        }
    } catch (SQLException e) {
        // Gérer le cas où l'INE n'est pas unique
        if (e.getSQLState().equals("23505")) { // Code SQL pour violation de contrainte d'unicité (peut varier selon la BDD)
            throw new SQLException("Un étudiant avec cet INE existe déjà : " + this.ine, e);
        } else {
            throw e; // Propager d'autres erreurs SQL
        }
    }
}



public static List<Etudiant> tousLesEtudiants(Connection con) throws SQLException {
    String query = "SELECT ine, nom, Prénom AS prenom, Classe AS classe, Score AS score FROM etudiant";
    try (PreparedStatement pst = con.prepareStatement(query)) {
        ResultSet rs = pst.executeQuery();
        List<Etudiant> etudiants = new ArrayList<>();
        while (rs.next()) {
            etudiants.add(new Etudiant(
                    rs.getString("ine"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("classe"),
                    rs.getString("score")
            ));
        }
        return etudiants;
    }
}
public static List<Etudiant> tousLesEtudiantsParClasse(Connection con, String classe) throws SQLException {
    List<Etudiant> etudiants = new ArrayList<>();
    String query = "SELECT * FROM " + classe;
    try (PreparedStatement stmt = con.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            etudiants.add(new Etudiant(
                rs.getString("INE"),
                rs.getString("Nom"),
                rs.getString("Prenom"),
                rs.getString("Classe"),
                rs.getString("Score")
            ));
        }
    }
    return etudiants;
}
//trouve l'etudiant avec l'ine
public static Optional<Etudiant> trouveEtudiant(Connection con, String ine) throws SQLException {
    String query = "SELECT ine, nom, Prénom AS prenom, Classe AS classe, Score AS score FROM etudiant WHERE ine = ?";
    try (PreparedStatement pst = con.prepareStatement(query)) {
        pst.setString(1, ine);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return Optional.of(new Etudiant(
                        rs.getString("ine"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("classe"),
                        rs.getString("score")
                ));
            }
        }
    }
    return Optional.empty();
}


public static String creeConsole(Connection con) throws SQLException {
    String ine = ConsoleFdB.entreeString("INE : ");
    String nom = ConsoleFdB.entreeString("Nom : ");
    String prenom = ConsoleFdB.entreeString("Prénom : ");
    String classe = ConsoleFdB.entreeString("Classe : ");
    String score = ConsoleFdB.entreeString("Score : ");
    Etudiant nouveau = new Etudiant(ine, nom, prenom, classe, score);
    return nouveau.saveInDB(con);
}

    public static Etudiant selectInConsole(Connection con) throws SQLException {
        return ListUtils.selectOne("Choisissez un étudiant :", 
                tousLesEtudiants(con), (elem) -> elem.getNom() + " " + elem.getPrenom());
    }

    // Méthodes Getters et Setters
   

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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Etudiant{" +
                ", ine='" + ine + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", classe='" + classe + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}

