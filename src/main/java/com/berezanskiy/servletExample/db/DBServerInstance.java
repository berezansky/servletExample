package com.berezanskiy.servletExample.db;

import com.berezanskiy.servletExample.utils.LoggerFactory;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DBServerInstance {
    private static Database ebeanServer = null;
    private static Logger LOGGER = LoggerFactory.getLogger(DBServerInstance.class);

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

        LOGGER.log(Level.INFO, "Database has successful create");

        return db;
    }
}
