package org.sebsy.compta.app;

import org.sebsy.compta.dao.BonDAO;
import org.sebsy.compta.dao.FournisseurDAO;
import org.sebsy.compta.dao.jdbc.BonDaoJdbc;
import org.sebsy.compta.dao.jdbc.FournisseurDaoJdbc;
import org.sebsy.compta.domain.Fournisseur;

import static org.sebsy.compta.domain.Adresse.ville;

public class Brouillon {


    public static void main(String[] args) throws Exception {

        // FournisseurDAO dao = new FournisseurDaoJdbc();
        // Fournisseur fournisseur = new Fournisseur("Peinture Express 2", ville("Nantes"));
        // System.out.println(dao.create(fournisseur));

        BonDAO daoBon = new BonDaoJdbc();
        System.out.println(daoBon.FindDelaiByIdBon(7));
    }
}
