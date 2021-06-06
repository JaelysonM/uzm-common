package com.uzm.common.database.solutions;


import com.google.common.collect.Lists;
import com.uzm.common.database.data.DataContainer;
import com.uzm.common.database.data.DataTable;
import com.uzm.common.database.exceptions.DataLoadExpection;
import com.uzm.common.plugin.Common;
import com.uzm.common.plugin.abstracts.UzmPlugin;
import org.bukkit.Bukkit;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author JotaMPê (UzmStudio)
 */
public class MySQLDatabase extends DatabaseSolution {

    private String host, port, database, username, password;
    private boolean mariadb;

    public MySQLDatabase(UzmPlugin plugin, String host, String port, String database, String username, String password, boolean mariadb) {
        this(plugin, host, port, database, username, password, mariadb, false);
    }

    public MySQLDatabase(UzmPlugin plugin, String host, String port, String database, String username, String password, boolean mariadb, boolean tables) {
        super(plugin);
        this.host = host;
        this.mariadb = mariadb;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;

        if (!tables) {
            DataTable.listTables().parallelStream().filter(t -> t.getDatabaseSolution() == this).forEach(table -> {
                this.update(table.getInfo().create());
                table.init();
            });
        }

    }


    public void openConnection() {
        try {
            boolean reconnected = true;
            if (this.getConnection() != null) {
                reconnected = false;
            }
            Class.forName(this.mariadb ? "org.mariadb.jdbc.Driver" : "com.mysql.jdbc.Driver");
            this.setConnection(DriverManager.getConnection((this.mariadb ?
                    "jdbc:mariadb://" :
                    "jdbc:mysql://") + host + ":" + port + "/" + this.database + "?verifyServerCertificate=false&useSSL=false&useUnicode=yes&characterEncoding=UTF-8", username, password));
            if (reconnected) {

                LOGGER.info("Trying to reconnect to MySQL Server.");
                return;
            }

            LOGGER.info("MySQL Server connected with driver: §a" + (mariadb ? "MariaDB" : "MySQL"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database not connected, plugin disabled.", ex);
            System.exit(0);
        }
    }

    @Override
    public Map<String, Map<String, DataContainer>> load(String key) throws DataLoadExpection {
        return load(key);
    }

    @Override
    public <T extends DataTable> Map<String, Map<String, DataContainer>> load(String key, Class<T>... tables) throws DataLoadExpection {
        Map<String, Map<String, DataContainer>> tableMap = new HashMap<>();
        for (DataTable table : DataTable.listTables().stream().filter(t -> t.getDatabaseSolution() == this).collect(Collectors.toList())) {
            if (tables.length > 0 && Lists.newArrayList(tables).contains(table.getClass())) {
                return null;
            }
            Map<String, DataContainer> containerMap = new LinkedHashMap<>();
            tableMap.put(table.getInfo().name(), containerMap);

            try (CachedRowSet rs = this.query(table.getInfo().select(), key.toLowerCase())) {
                if (rs != null) {
                    for (int collumn = 2; collumn <= rs.getMetaData().getColumnCount(); collumn++) {
                        containerMap.put(rs.getMetaData().getColumnName(collumn), new DataContainer(rs.getObject(collumn)));
                    }
                    continue;
                }
            } catch (SQLException ex) {
                throw new DataLoadExpection(ex.getMessage());
            }

            containerMap = table.getDefaultValues();
            tableMap.put(table.getInfo().name(), containerMap);
            List<Object> list = new ArrayList<>();
            list.add(key);
            list.addAll(containerMap.values().stream().map(DataContainer::get).collect(Collectors.toList()));
            this.execute(table.getInfo().insert(), list.toArray());
            list.clear();
        }

        return tableMap;
    }

    @Override
    public void save(String name, Map<String, Map<String, DataContainer>> tableMap) {
        this.save0(name, tableMap, true);
    }

    @Override
    public void saveSync(String name, Map<String, Map<String, DataContainer>> tableMap) {
        this.save0(name, tableMap, false);
    }

    @Override
    public String exists(String name) {
        return null;
    }

    private void save0(String name, Map<String, Map<String, DataContainer>> tableMap, boolean async) {
        for (DataTable table : DataTable.listTables().stream().filter(t -> t.getDatabaseSolution() == this).collect(Collectors.toList())) {
            Map<String, DataContainer> rows = tableMap.get(table.getInfo().name());
            if (rows.values().stream().noneMatch(DataContainer::isUpdated)) {
                continue;
            }

            List<Object> values = rows.values().stream().filter(DataContainer::isUpdated).map(DataContainer::get).collect(Collectors.toList());
            StringBuilder query = new StringBuilder("UPDATE `" + table.getInfo().name() + "` SET ");
            for (Map.Entry<String, DataContainer> collumn : rows.entrySet()) {
                if (collumn.getValue().isUpdated()) {
                    collumn.getValue().setUpdated(false);
                    query.append("`").append(collumn.getKey()).append("` = ?, ");
                }
            }
            query.deleteCharAt(query.length() - 1);
            query.deleteCharAt(query.length() - 1);
            query.append(" WHERE LOWER(`").append(table.getInfo().key()).append("`) = ?");
            values.add(name.toLowerCase());
            if (async) {
                this.execute(query.toString(), values.toArray());
            } else {
                this.update(query.toString(), values.toArray());
            }
            values.clear();
        }
    }

    @Override
    public void close() {
        this.getExecutorService().shutdownNow().forEach(Runnable::run);
        this.closeConnection();
    }


    public void closeConnection() {
        if (isConnected()) {
            try {
                this.getConnection().close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Unable to close a connection with MySQL:", e);
            }
        }
    }

    public boolean isConnected() {
        try {
            return !(this.getConnection() == null || this.getConnection().isClosed() || !this.getConnection().isValid(5));
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unable to verify connection with MySQL: ", ex);
            return false;
        }
    }

    public void update(String sql, Object... vars) {
        try (PreparedStatement ps = prepareStatement(sql, vars)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, "Unable to execute an SQL: ", ex);
        }
    }

    public void execute(String sql, Object... vars) {
        this.getExecutorService().execute(() -> {
            update(sql, vars);
        });
    }

    public int updateWithInsertId(String sql, Object... vars) {
        int id = -1;
        ResultSet rs = null;
        try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < vars.length; i++) {
                ps.setObject(i + 1, vars[i]);
            }
            ps.execute();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, "Unable to execute an SQL: ", ex);
        } finally {
            try {
                if (rs != null && !rs.isClosed())
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return id;
    }

    public PreparedStatement prepareStatement(String query, Object... vars) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(query);
            for (int i = 0; i < vars.length; i++) {
                ps.setObject(i + 1, vars[i]);
            }
            return ps;
        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, "Unable to prepare an SQL: ", ex);
        }

        return null;
    }

    public CachedRowSet query(String query, Object... vars) {
        CachedRowSet rowSet = null;
        try {
            Future<CachedRowSet> future = this.getExecutorService().submit(() -> {
                CachedRowSet crs = null;
                try (PreparedStatement ps = prepareStatement(query, vars); ResultSet rs = ps.executeQuery()) {

                    CachedRowSet rs2 = RowSetProvider.newFactory().createCachedRowSet();
                    rs2.populate(rs);

                    if (rs2.next()) {
                        crs = rs2;
                    }
                } catch (SQLException ex) {
                    LOGGER.log(Level.WARNING, "Unable to execute an Requisition: ", ex);
                }

                return crs;
            });

            if (future.get() != null) {
                rowSet = future.get();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Unable to execute an FutureTask: ", ex);
        }

        return rowSet;
    }

    @Override
    public void gc() {
        this.host = null;
        this.port = null;
        this.database = null;
        this.username = null;
        this.password = null;
        super.gc();
    }

}
