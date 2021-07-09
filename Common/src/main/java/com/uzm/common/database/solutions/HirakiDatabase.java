package com.uzm.common.database.solutions;


import com.uzm.common.database.data.DataContainer;
import com.uzm.common.database.data.DataTable;
import com.uzm.common.database.exceptions.DataLoadExpection;
import com.uzm.common.java.util.StringUtils;
import com.uzm.common.plugin.abstracts.UzmPlugin;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

public class HirakiDatabase extends DatabaseSolution {

    private String host, port, database, username, password;
    private boolean mariadb;
    private HikariDataSource dataSource;

    public HirakiDatabase(UzmPlugin plugin, String host, String port, String database, String username, String password, boolean mariadb) {
        this(plugin, host, port, database, username, password, mariadb, false);
    }

    public HirakiDatabase(UzmPlugin plugin, String host, String port, String database, String username, String password, boolean mariadb, boolean tables) {
        super(plugin, tables);
        this.host = host;
        this.mariadb = mariadb;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;

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
            if (tables.length > 0 && Arrays.stream(tables).noneMatch(c -> c.isAssignableFrom(table.getClass()))) {
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
            if (!tableMap.containsKey(table.getInfo().name())) return;
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

    @Override
    public void openConnection() {
        HikariConfig config = new HikariConfig();
        config.setPoolName("uzm-pool");
        config.setMaximumPoolSize(32);
        config.setConnectionTimeout(30000L);
        config.setDriverClassName(this.mariadb ? "org.mariadb.jdbc.Driver" : "com.mysql.jdbc.Driver");
        config.setJdbcUrl((this.mariadb ? "jdbc:mariadb://" : "jdbc:mysql://") + this.host + ":" + this.port + "/" + this.database);
        config.setUsername(this.username);
        config.setPassword(this.password);
        config.addDataSourceProperty("autoReconnect", "true");
        this.dataSource = new HikariDataSource(config);
        LOGGER.info("MySQL(HirakiEngine) Server connected with driver: §a" + (mariadb ? "MariaDB" : "MySQL"));
    }


    @Override
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    @Override
    public void close() {
        if (isConnected()) {
            this.dataSource.close();
        }
    }

    public boolean isConnected() {
        return !this.dataSource.isClosed();
    }


    public void update(String sql, Object... vars) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < vars.length; i++) {
                ps.setObject(i + 1, vars[i]);
            }
            ps.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, "Unable to execute an SQL: ", ex);
        } finally {
            try {
                if (connection != null && !connection.isClosed())
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null && !ps.isClosed())
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(String sql, Object... vars) {
        this.getExecutorService().execute(() -> {
            update(sql, vars);
        });
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

    public int updateWithInsertId(String sql, Object... vars) {
        int id = -1;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
                if (connection != null && !connection.isClosed())
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null && !ps.isClosed())
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (rs != null && !rs.isClosed())
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return id;
    }


    public CachedRowSet query(String query, Object... vars) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CachedRowSet rowSet = null;
        try {
            connection = getConnection();
            ps = connection.prepareStatement(query);
            for (int i = 0; i < vars.length; i++) {
                ps.setObject(i + 1, vars[i]);
            }
            rs = ps.executeQuery();
            rowSet = RowSetProvider.newFactory().createCachedRowSet();
            rowSet.populate(rs);

            if (rowSet.next()) {
                return rowSet;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, "Unable to execute an Requisition: ", ex);
        } finally {
            try {
                if (connection != null && !connection.isClosed())
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null && !ps.isClosed())
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (rs != null && !rs.isClosed())
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
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
