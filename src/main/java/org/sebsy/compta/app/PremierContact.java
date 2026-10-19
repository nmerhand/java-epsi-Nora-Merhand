package org.sebsy.compta.app;

import org.sebsy.compta.infra.Env;

import java.sql.*;

public class PremierContact {

    public static void main(String[] args) {
        try(Connection cnx = DriverManager.getConnection(
                Env.required("DB_URL"),
                Env.required("DB_USER"),
                Env.required("DB_PWD"));
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery("SELECT count(*) FROM fournisseur")) {
            System.out.println(cnx.getClass().getName());
            rs.next();
            System.out.println("Nombre de fournisseurs" + rs.getInt(1));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
