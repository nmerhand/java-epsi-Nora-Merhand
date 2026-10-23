package org.sebsy.compta.app;

import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.sebsy.compta.domain.Fournisseur;
import org.sebsy.compta.infra.Jpa;

public class EssaiPortees {

    public static void main(String[] args) {
        SessionFactory sf = Jpa.factory();
        Statistics stats = sf.getStatistics();

        try {
            stats.clear();
            try (EntityManager em = sf.createEntityManager()) {
                em.getTransaction().begin();
                Fournisseur a = em.find(Fournisseur.class, 1L);
                Fournisseur b = em.find(Fournisseur.class, 1L);
                em.getTransaction().commit();
                System.out.println("10. a == b : " + (a == b));
            }

            stats.clear();
            Fournisseur c;
            Fournisseur d;
            try (EntityManager em1 = sf.createEntityManager()) {
                em1.getTransaction().begin();
                c = em1.find(Fournisseur.class, 1L);
                em1.getTransaction().commit();
            }
            try (EntityManager em2 = sf.createEntityManager()) {
                em2.getTransaction().begin();
                d = em2.find(Fournisseur.class, 1L);
                em2.getTransaction().commit();
            }
            System.out.println("11a. c == d : " + (c == d));
            System.out.println("11a. c.equals(d) : " + c.equals(d));
            System.out.println("11a. requêtes : " + stats.getPrepareStatementCount());

            stats.clear();
            try (EntityManager em = sf.createEntityManager()) {
                em.getTransaction().begin();
                Fournisseur e = em.find(Fournisseur.class, 1L);
                em.getTransaction().commit();

                em.getTransaction().begin();
                Fournisseur f = em.find(Fournisseur.class, 1L);
                em.getTransaction().commit();

                System.out.println("11b. e == f : " + (e == f));
                System.out.println("11b. requêtes : " + stats.getPrepareStatementCount());
            }
        } finally {
            Jpa.close();
        }
    }
}