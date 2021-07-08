package com.uzm.common.database.solutions;


import com.google.common.collect.Lists;
import com.uzm.common.database.data.DataContainer;
import com.uzm.common.database.data.DataTable;
import com.uzm.common.database.exceptions.DataLoadExpection;
import com.uzm.common.java.util.StringUtils;
import com.uzm.common.plugin.abstracts.UzmPlugin;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

public class SQLiteDatabase extends DatabaseSolution {

    private Connection connection;
    private File file;
    private boolean mariadb;

    public SQLiteDatabase(UzmPlugin plugin, File file) {
        this(plugin, file, false);
    }

    public SQLiteDatabase(UzmPlugin plugin, File file, boolean tables) {
        super(plugin, tables);
        this.file = file;
        if (!tables) {
            DataTable.listTables().parallelStream().filter(t -> t.getDatabaseSolution() == this).forEach(table -> {
                this.update(table.getInfo().create());
                table.init();
            });
        }

    }

    @Override
    public void createTables() {
        if (!this.isTables()) {
            DataTable.listTables().parallelStream().filter(t -> t.getDatabaseSolution() == this).forEach(table -> {
                this.update(table.getInfo().create());
                table.init();
            });
        }
    }


    @Override
    public List<String[]> getLeaderBoard(DataTable table, String... columns) {
        List<String[]> result = new ArrayList<>();
        StringBuilder add = new StringBuilder(), select = new StringBuilder();
        for (String column : columns) {
            add.append("`").append(column).append("` + ");
            select.append("`").append(column).append("`, ");
        }

        try (CachedRowSet rs = query("SELECT " + select + "`name` FROM `" + table.getInfo().name() + "` ORDER BY " + add + " 0 DESC LIMIT 10")) {
            if (rs != null) {
                rs.beforeFirst();
                while (rs.next()) {
                    long count = 0;
                    for (String column : columns) {
                        count += rs.getLong(column);
                    }
                    result.add(new String[]{rs.getString(table.getInfo().key()), StringUtils.formatNumber(count)});
                }
            }
        } catch (SQLException ignore) {
        }

        return result;
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
                    collumn.getValue().save();
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

    public void openConnection() {
        try {
            boolean reconnected = this.getConnection() == null;
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + file);
            if (reconnected) {
                LOGGER.info("Trying to reconnect to SQLite Server.");
                return;
            }

            LOGGER.info("SQLite Server connected with file output: " + file.getName());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database not connected, plugin disabled.", ex);
            this.getPlugin().disable();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (!isConnected()) {
            this.openConnection();
        }

        return this.connection;
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
        this.file = null;
        super.gc();
    }

}
