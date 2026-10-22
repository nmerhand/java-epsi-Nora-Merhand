package org.sebsy.compta.app;

import org.sebsy.compta.dao.BonDAO;
import org.sebsy.compta.dao.FournisseurDAO;
import org.sebsy.compta.dao.jdbc.BonDaoJdbc;
import org.sebsy.compta.dao.jdbc.FournisseurDaoJdbc;
import org.sebsy.compta.domain.Fournisseur;

import static org.sebsy.compta.domain.Adresse.ville;

public class EssaiFournisseurs {

    public static void main(String[] args) throws Exception {

        // ////////////////// FOURNISSEUR //////////////////////////
        FournisseurDAO dao = new FournisseurDaoJdbc();
        System.out.println(dao.findAll());
        System.out.println(dao.findById(2L));
        System.out.println(dao.findById(999L));
        System.out.println(dao.findByNom("RDS"));
        System.out.println(dao.findByVille("Nantes"));
        System.out.println(dao.findByVille("Rennes"));
        Fournisseur fournisseur = new Fournisseur("Peinture Express", ville("Nantes"));
        Fournisseur newF = dao.create(fournisseur);
        System.out.println(newF);
        System.out.println(dao.rename(newF.getId(), "Peinture Express 2"));
        System.out.println(dao.rename(999L, "Peinture Express 3"));
        System.out.println(dao.delete(newF.getId()));

        // ////////////////// BON //////////////////////////
        BonDAO daoBon = new BonDaoJdbc();
        System.out.println(daoBon.FindDelaiByIdBon(1));
        System.out.println(daoBon.FindDelaiByIdBon(7));
    }
}
