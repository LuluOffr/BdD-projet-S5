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
//marche
package fr.insa.toto.moveINSA.model;

import java.sql.SQLException;

/**
 *
 * @author francois
 */
public class EntiteDejaSauvegardee extends SQLException {

    public EntiteDejaSauvegardee() {
        super("L'entité à déjà été sauvegardée (id != -1");
    }

}
