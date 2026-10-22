package org.sebsy.compta.app;

import org.sebsy.compta.dao.FournisseurDAO;
import org.sebsy.compta.dao.jdbc.FournisseurDaoJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class PremierContact {

    private static final Logger LOGGER = LoggerFactory.getLogger(PremierContact.class);

    public static void main(String[] args) throws Exception {

        FournisseurDAO dao = new FournisseurDaoJdbc();
        System.out.println(dao.findAll());
        System.out.println(dao.findById(2L));
        System.out.println(dao.findById(999L));
        System.out.println(dao.findByNom("Dubois & Fils"));
        System.out.println(dao.findByNom("RDS"));
        System.out.println(dao.findByVille("Nantes"));
        System.out.println(dao.findByVille("Rennes"));
    }
}