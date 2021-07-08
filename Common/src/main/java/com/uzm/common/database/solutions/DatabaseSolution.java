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

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

@Getter(AccessLevel.MODULE)
@Setter(AccessLevel.MODULE)
public abstract class DatabaseSolution extends CacheHandler {


    public static final CustomLogger LOGGER = ((CustomLogger) Common.getInstance().getLogger()).getModule("DATABASES");

    private boolean tables;


    private ExecutorService executorService;

    @Getter(AccessLevel.PUBLIC)
    private UzmPlugin plugin;


    public DatabaseSolution(UzmPlugin plugin, boolean tables) {
        this.plugin = plugin;
        this.executorService = Executors.newCachedThreadPool();
        this.tables = tables;
    }


    public static <T extends DatabaseSolution> T register(String databaseName, Class<T> type, String mysqlHost, String mysqlPort, String mysqlDbname, String mysqlUsername, String mysqlPassword, boolean mariadb) {
        return register(databaseName, type, mysqlHost, mysqlPort, mysqlDbname, mysqlUsername, mysqlPassword, mariadb, null);
    }


    public static <T extends DatabaseSolution> T register(String databaseName, Class<T> type, String mysqlHost, String mysqlPort, String mysqlDbname, String mysqlUsername, String mysqlPassword, boolean mariadb,
                                                          String mongoURL) {
        if (type.isAssignableFrom(MySQLDatabase.class) || type.isAssignableFrom(HirakiDatabase.class)) {
            return CacheHandler.getCache(databaseName, type, Common.getInstance(), mysqlHost, mysqlPort, mysqlDbname, mysqlUsername, mysqlPassword, mariadb).join();
        } else if (type.isAssignableFrom(SQLiteDatabase.class)) {
            return CacheHandler.getCache(databaseName, type, Common.getInstance(), new File("data.db")).join();
        } else {
            return null;
        }
    }

    public static <T extends DatabaseSolution> T register(String databaseName, Class<T> type) {
        return register(databaseName, type, null, null, null, null, null, false, null);
    }


    @Override
    public void gc() {
        this.plugin = null;
        this.executorService = null;
    }

    @Override
    public void save(boolean async) {

    }

    @Override
    public void load() {

    }

    public abstract void createTables();

    public abstract Connection getConnection() throws SQLException;

    public abstract void openConnection();

    public abstract void close();

    public abstract Map<String, Map<String, DataContainer>> load(String key) throws DataLoadExpection;

    public abstract <T extends DataTable> Map<String, Map<String, DataContainer>> load(String key, Class<T>... tables) throws DataLoadExpection;

    public abstract List<String[]> getLeaderBoard(DataTable table, String... columns);

    public abstract void save(String name, Map<String, Map<String, DataContainer>> tableMap);

    public abstract void saveSync(String name, Map<String, Map<String, DataContainer>> tableMap);


    public abstract String exists(String name);


}
