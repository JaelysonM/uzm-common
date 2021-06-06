package com.uzm.common.database.solutions;


import com.uzm.common.database.cache.CacheHandler;
import com.uzm.common.database.data.DataContainer;
import com.uzm.common.database.data.DataTable;
import com.uzm.common.database.exceptions.DataLoadExpection;
import com.uzm.common.plugin.Common;
import com.uzm.common.plugin.abstracts.UzmPlugin;
import com.uzm.common.plugin.logger.CustomLogger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author JotaMPê (UzmStudio)
 */

public abstract class DatabaseSolution extends CacheHandler {


    public static final CustomLogger LOGGER = ((CustomLogger) Common.getInstance().getLogger()).getModule("DATABASES");

    private @Getter(AccessLevel.MODULE)
    @Setter(AccessLevel.MODULE)
    Connection connection;


    private @Getter(AccessLevel.MODULE)
    @Setter(AccessLevel.MODULE)
    ExecutorService executorService;

    @Getter(AccessLevel.PUBLIC)
    private UzmPlugin plugin;


    public DatabaseSolution(UzmPlugin plugin) {
        this.plugin = plugin;
        this.openConnection();
        this.executorService = Executors.newCachedThreadPool();
    }


    public static <T extends DatabaseSolution> void register(String databaseName, Class<T> type, String mysqlHost, String mysqlPort, String mysqlDbname, String mysqlUsername, String mysqlPassword, boolean mariadb) {
        register(databaseName, type, mysqlHost, mysqlPort, mysqlDbname, mysqlUsername, mysqlPassword, mariadb, null);
    }


    public static <T extends DatabaseSolution> void register(String databaseName, Class<T> type, String mysqlHost, String mysqlPort, String mysqlDbname, String mysqlUsername, String mysqlPassword, boolean mariadb,
                                                             String mongoURL) {
        if (type == MySQLDatabase.class || type == HirakiDatabase.class) {
            CacheHandler.getCache(databaseName, type, mysqlHost, mysqlPort, mysqlDbname, mysqlUsername, mysqlPassword, mariadb);
        }else {
            // TODO Do more...
        }
    }


    @Override
    public void gc() {
        this.plugin = null;
        this.connection = null;
        this.executorService = null;
    }

    @Override
    public void save(boolean async) {

    }

    @Override
    public void load() {

    }

    public abstract void openConnection();

    public abstract void close();

    public abstract Map<String, Map<String, DataContainer>> load(String key) throws DataLoadExpection;

    public abstract <T extends DataTable> Map<String, Map<String, DataContainer>> load(String key, Class<T>... tables) throws DataLoadExpection;

    public abstract void save(String name, Map<String, Map<String, DataContainer>> tableMap);

    public abstract void saveSync(String name, Map<String, Map<String, DataContainer>> tableMap);

    public abstract String exists(String name);


}
