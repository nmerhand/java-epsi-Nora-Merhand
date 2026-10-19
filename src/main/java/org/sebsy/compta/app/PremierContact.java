package org.sebsy.compta.app;

import org.sebsy.compta.infra.Env;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PremierContact {

    static void main() {
        try(Connection cnx = DriverManager.getConnection(
                Env.required("DB_URL"),
                Env.required("DB_USER"),
                Env.required("DB_PWD")))

        Statement st = cnx.createStatement()
        ResultSet rs = st.executeQuery("SELECT count(*) FROM fournisseur")) {

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
