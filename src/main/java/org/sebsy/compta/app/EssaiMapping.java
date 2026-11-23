package org.sebsy.compta.app;

import org.hibernate.SessionFactory;
import org.sebsy.compta.domain.Article;
import org.sebsy.compta.infra.Jpa;

import java.math.BigDecimal;

public class EssaiMapping {

    public static void main(String[] args) throws Exception {

        try (SessionFactory sf = Jpa.factory()) {
            Article article1 = sf.callInTransaction(em ->
                em.find(Article.class, 1L)
            );
            System.out.println(article1);
            System.out.println(article1.getPrixTtc());

            sf.runInTransaction(em -> {
                Article a2 = new Article("A05", "Perceuse P2", new BigDecimal(89.99));
                em.persist(a2);
                em.find(Article.class, a2.getId());
            });
        }
    }
}
