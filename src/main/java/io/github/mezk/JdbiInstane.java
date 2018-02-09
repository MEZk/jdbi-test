package io.github.mezk;

import org.jdbi.v3.core.Jdbi;

public class JdbiInstane {

    public static Jdbi jdbi() {
        return Jdbi.create(HikariCp.dataSource());
    }

}
