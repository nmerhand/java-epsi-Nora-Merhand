package org.sebsy.compta.app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.sebsy.compta.domain.Fournisseur;

public class AppJpa31 {

    public static void main(String[] args) {
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("compta-pu");
            EntityManager em = emf.createEntityManager()) {
            Fournisseur fournisseur = em.find(Fournisseur.class, 1);
            System.out.println(fournisseur);
        }
    }
}
