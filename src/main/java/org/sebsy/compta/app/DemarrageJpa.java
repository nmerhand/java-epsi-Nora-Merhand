package org.sebsy.compta.app;

import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.sebsy.compta.domain.ArticleCourt;
import org.sebsy.compta.domain.Fournisseur;
import org.sebsy.compta.infra.Jpa;

public class DemarrageJpa {

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = Jpa.factory()) {
            Fournisseur fournisseur4 = sessionFactory.callInTransaction(
                    em -> em.find(Fournisseur.class, 4L)
            );
            System.out.println(fournisseur4);
            System.out.println(fournisseur4.getAdresse().estComplete());

            ArticleCourt articleCourt = sessionFactory.callInTransaction(
                    em -> em.find(ArticleCourt.class, 1L)
            );
            System.out.println(articleCourt);
        }
    }
}
