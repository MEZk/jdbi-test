package io.github.mezk;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCp {

    public static DataSource dataSource() {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        cfg.setUsername("postgres");
        cfg.setUsername("postgres");
        return new HikariDataSource(cfg);
    }

}
