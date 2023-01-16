package com.berezanskiy.XO.db;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;

public class DBServerInstance {
    private static Database ebeanServer = null;

    public DBServerInstance() {

    }

    public static Database getInstance() {
        if (ebeanServer == null) {
            ebeanServer = getEbeanServer();
        }

        return ebeanServer;
    }

    private static Database getEbeanServer() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();

        dataSourceConfig.setUsername("postgres");
        dataSourceConfig.setPassword("postgres");
        dataSourceConfig.setUrl("jdbc:postgresql://localhost:5436/hexlet");

        DatabaseConfig cfg = new DatabaseConfig();

        cfg.setDataSourceConfig(dataSourceConfig);
        Database db = DatabaseFactory.create(cfg);

        System.out.println(db);

        return db;
    }
}
