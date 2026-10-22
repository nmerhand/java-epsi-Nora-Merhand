package org.sebsy.compta.infra;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public final class Db {
    private static final HikariDataSource DATA_SOURCE ;

    // Bloc d'initialisation static => Chargement en mémoire lors de la compilation
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Env.required("DB_URL"));
        config.setUsername(Env.required("DB_USER"));
        config.setPassword(Env.required("DB_PWD"));
        config.setPoolName("compta-pool");
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(3_000);
        config.setLeakDetectionThreshold(20_000);

        DATA_SOURCE = new HikariDataSource(config);
    }

    private Db() {

    }

    public static DataSource dataSource() {
        return DATA_SOURCE;
    }

    public static void close() {
        DATA_SOURCE.close();
    }
}
