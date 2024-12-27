//ca marche
package fr.insa.toto.moveINSA.moveINSA.model;

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

   public int saveInDB(Connection con) throws SQLException {
    if (this.ine == null || this.ine.isEmpty()) {
        throw new IllegalStateException("L'INE de l'étudiant est invalide ou non défini.");
    }

    String queryCheck = "SELECT COUNT(*) FROM etudiants WHERE ine = ?";
    try (PreparedStatement checkStmt = con.prepareStatement(queryCheck)) {
        checkStmt.setString(1, this.ine);
        try (ResultSet rs = checkStmt.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) {
                throw new IllegalStateException("Un étudiant avec cet INE est déjà sauvegardé dans la base de données.");
            }
        }
    }

    String insertQuery = "INSERT INTO etudiants (ine, nom, prenom, classe, score) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement insertStmt = con.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
        insertStmt.setString(1, this.ine);
        insertStmt.setString(2, this.nom);
        insertStmt.setString(3, this.prenom);
        insertStmt.setString(4, this.classe);
        insertStmt.setString(5, this.score);

        int affectedRows = insertStmt.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Échec de la sauvegarde de l'étudiant, aucune ligne ajoutée.");
        }

        try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Retourne l'ID généré si nécessaire.
            } else {
                throw new SQLException("Échec de la sauvegarde de l'étudiant, aucun ID généré.");
            }
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

public static int creeConsole(Connection con) throws SQLException {
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
        return "Etudiant{ ine='" + ine + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", classe='" + classe + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}

