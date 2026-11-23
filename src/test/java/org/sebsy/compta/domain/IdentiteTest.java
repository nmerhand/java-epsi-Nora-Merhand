package org.sebsy.compta.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceConfiguration;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sebsy.compta.infra.Env;

import org.hibernate.Hibernate;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class IdentiteTest {

    @Entity
    @Table(name = "fournisseur", schema = "compta")
    public static class FournisseurParId {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "nom")
        private String nom;

        @Column(name = "ville")
        private String ville;

        public FournisseurParId() {
        }

        public FournisseurParId(String nom, String ville) {
            this.nom = nom;
            this.ville = ville;
        }

        public Long getId() {
            return id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FournisseurParId that = (FournisseurParId) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    private static EntityManagerFactory emf;

    @BeforeAll
    static void demarrer() {
        emf = new PersistenceConfiguration("identite-test")
                .managedClass(Fournisseur.class)
                .managedClass(Article.class)
                .managedClass(FournisseurParId.class)
                .property("jakarta.persistence.jdbc.url", Env.required("DB_URL"))
                .property("jakarta.persistence.jdbc.user", Env.required("DB_USER"))
                .property("jakarta.persistence.jdbc.password", Env.required("DB_PWD"))
                .property("hibernate.hbm2ddl.auto", "validate")
                .createEntityManagerFactory();
    }

    @AfterAll
    static void arreter() {
        if (emf != null) {
            emf.close();
        }
    }

    private static void dansTransactionAnnulee(java.util.function.Consumer<EntityManager> corps) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            try {
                corps.accept(em);
            } finally {
                em.getTransaction().rollback();
            }
        }
    }

    @Test
    void a_identiteSurId_lePersistCasseLeHashSet() {
        dansTransactionAnnulee(em -> {
            FournisseurParId f = new FournisseurParId("Acme", "Paris");
            Set<FournisseurParId> set = new HashSet<>();
            set.add(f);

            em.persist(f);

            assertEquals(1, set.size());
            assertFalse(set.contains(f));
        });
    }

    @Test
    void b_identiteSurNom_leHashSetResteCoherent() {
        dansTransactionAnnulee(em -> {
            Fournisseur f = new Fournisseur();
            f.setNom("Acme");
            f.setAdresse(new Adresse("", "","Paris"));
            Set<Fournisseur> set = new HashSet<>();
            set.add(f);

            em.persist(f);

            assertEquals(1, set.size());
            assertTrue(set.contains(f));
        });
    }

    @Test
    void c_getReferenceRenvoieUnProxy_egalALArticleCharge() {
        try (EntityManager em1 = emf.createEntityManager();
             EntityManager em2 = emf.createEntityManager()) {
            em1.getTransaction().begin();
            em2.getTransaction().begin();
            try {
                Article charge = em1.find(Article.class, 1L);
                Article ref = em2.getReference(Article.class, 1L);

                assertNotSame(Article.class, ref.getClass());
                assertTrue(Article.class.isAssignableFrom(ref.getClass()));
                assertFalse(Hibernate.isInitialized(ref));

                assertEquals(charge, ref);
            } finally {
                em1.getTransaction().rollback();
                em2.getTransaction().rollback();
            }
        }
    }
}