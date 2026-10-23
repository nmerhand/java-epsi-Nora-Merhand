package org.sebsy.compta.infra;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernatePersistenceConfiguration;
import org.hibernate.tool.schema.Action;
import org.sebsy.compta.domain.Fournisseur;

public class Jpa {
    private static SessionFactory factory ;

    private Jpa() {

    }

    public static synchronized SessionFactory factory() {
        if (factory == null || factory.isClosed()) {
            factory = build();
        }
        return factory;
    }

    public static synchronized void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
        factory = null;
    }

    private static SessionFactory build() {
        return new HibernatePersistenceConfiguration("compta")
            .managedClasses(Fournisseur.class)
            .jdbcUrl(Env.required("DB_URL"))
            .jdbcUsername(Env.required("DB_USER"))
            .jdbcPassword(Env.required("DB_PASSWORD"))
            .jdbcPoolSize(10)
            .schemaToolingAction(Action.VALIDATE)
            .property("hibernate.generate_statistics", true)
            .createEntityManagerFactory();
    }
}
