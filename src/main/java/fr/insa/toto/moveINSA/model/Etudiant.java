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
 *
 * @author lucas
 */
public class Etudiant {
    
     private int id;
     private String ine;
     private String nom;
     private String prenom;
    
     
     public Etudiant (String nom, String prenom){
         
        this.nom = nom;
        this.prenom = prenom;
    }

    @Override
    public String toString() {
    return "Etudiant{" + "nom=" + nom + " ; prenom=" + prenom + '}';
}

public int saveInDB(Connection con) throws SQLException {
        if (this.id != -1) {
            throw new EntiteDejaSauvegardee();
        }
        try (PreparedStatement insert = con.prepareStatement(
                "INSERT INTO Etudiant (ine, nom, prenom) VALUES (?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, this.ine);
            insert.setString(2, this.prenom);
            insert.setString(3, this.nom);
            insert.executeUpdate();
            try (ResultSet rid = insert.getGeneratedKeys()) {
                if (rid.next()) {
                    this.id = rid.getInt(1);
                }
                return this.id;
            }
        }
    }
         
         
    
}
     