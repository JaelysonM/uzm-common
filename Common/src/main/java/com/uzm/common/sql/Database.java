package com.uzm.common.sql;


import com.uzm.common.plugin.abstracts.UzmPlugin;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author JotaMPê (UzmStudio)
 */

public abstract class Database {

    private static final LinkedHashMap<String, Database> SQLS = new LinkedHashMap<>();

    private Connection connection;


    private ExecutorService executorService;

    private String name;

    private Properties info;

    private UzmPlugin plugin;

    private String[] tablesRules;


    public Database(String name, UzmPlugin plugin) {
        this.name = name;
        this.plugin = plugin;

        this.executorService = Executors.newCachedThreadPool();
        open();
    }

    public void setTablesRules(String... tablesRules) {
        this.tablesRules = tablesRules;
    }

    public abstract void open();

    public void close() {
        if (isConnected()) {
            try {
                getConnection().close();
            } catch (SQLException e) {
                System.err.println("[UzmCommons | Database] SQL error, probably the connection is closed > " + e.getMessage());
            }
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            System.err.println("[UzmCommons | Database] SQL error, probably the connection is closed > " + e.getMessage());
        }
        return false;
    }


    public void execute(final String query, final Object... vars) {
        if (isConnected()) {
            getExecutorService().execute(() -> {
                try {
                    PreparedStatement ps = prepareStatement(query, vars);
                    if (ps != null) {
                        ps.execute();
                        ps.close();
                    }
                } catch (SQLException e) {
                    System.err.println("[UzmCommons | Database] SQL error, probably the connection is closed > " + e.getMessage());
                }
            });
        } else {
            open();
            execute(query, vars);
        }
    }

    private PreparedStatement prepareStatement(String query, Object... vars) {
        try {
            if (isConnected()) {
                PreparedStatement ps = getConnection().prepareStatement(query);
                int i = 0;
                if (query.contains("?") && vars.length != 0) {
                    for (Object obj : vars) {
                        i++;
                        ps.setObject(i, obj);
                    }
                }
                return ps;
            } else {
                open();
                return prepareStatement(query, vars);
            }
        } catch (SQLException e) {
            System.err.println("[UzmCommons | Database] SQL error, probably the connection is closed > " + e.getMessage());
        }

        return null;
    }

    public CachedRowSet query(final String query, final Object... vars) {
        CachedRowSet rowSet = null;
        if (isConnected()) {
            try {

                Future<CachedRowSet> future = getExecutorService().submit(() -> {
                    try {
                        PreparedStatement ps = prepareStatement(query, vars);
                        if (ps != null) {
                            ResultSet rs = ps.executeQuery();
                            CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
                            crs.populate(rs);
                            rs.close();
                            ps.close();

                            if (crs.next())
                                return crs;
                        }
                    } catch (SQLException e) {
                        System.err.println("[UzmCommons | Database] SQL error, probably the connection is closed > " + e.getMessage());
                    }

                    return null;
                });

                if (future.get() != null)
                    rowSet = future.get();

            } catch (Exception e) {
                System.err.println("[UzmCommons | Database] SQL error, probably the connection is closed > " + e.getMessage());
            }
        } else {
            open();
            query(query, vars);
        }

        return rowSet;
    }

    public void createTables(String... tables) {
        for (String t : tables) {
            execute(t);
        }
    }


    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Connection getConnection() {
        return connection;
    }

    public String[] getTablesRules() {
        return tablesRules;
    }

    public String getName() {
        return name;
    }

    public void destroy() {
        this.executorService = null;

        this.tablesRules = null;
        this.name = null;

        this.info.clear();
        this.info = null;
    }


    public static LinkedHashMap<String, Database> getSqls() {
        return SQLS;
    }


    public static Database create(String name, Database database) {
        return SQLS.computeIfAbsent(name, map -> database);
    }

    public static void remove(String name) {
        if (SQLS.containsKey(name)) {
            get(name).destroy();
            SQLS.remove(name);
        }
    }

    public UzmPlugin getPlugin() {
        return this.plugin;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public static Database get(String name) {
        return SQLS.getOrDefault(name, null);
    }

}
