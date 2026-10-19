package org.sebsy.compta.app;

import org.sebsy.compta.infra.Env;

import java.sql.*;

public class PremierContact {

    public static void main(String[] args) {
        try(Connection cnx = DriverManager.getConnection(
                Env.required("DB_URL"),
                Env.required("DB_USER"),
                Env.required("DB_PWD"))) {
        Statement st = cnx.createStatement();
        ResultSet rs1 = st.executeQuery("SELECT count(*) FROM fournisseur");

        System.out.println(cnx.getClass().getName());
        rs1.next();
        System.out.println("Nombre de fournisseurs : " + rs1.getInt(1));

        ResultSet rs2 = st.executeQuery("SELECT * FROM fournisseur ORDER BY fournisseur.nom");

        while (rs2.next()) {
            System.out.println("Fournisseur n°" + rs2.getInt("id") + " : " + rs2.getString("nom") + " à " + rs2.getString("ville"));
        }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}