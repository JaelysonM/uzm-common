package com.uzm.common.sql;


import com.uzm.common.plugin.abstracts.UzmPlugin;
import org.bukkit.Bukkit;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

/**
 * @author JotaMPê (UzmStudio)
 */
public class MariaDB extends Database {

    private String host, port, database, username, password;
    private Properties info;

    public MariaDB(UzmPlugin plugin, String name, String host, String port, String database, String username, String password) {
        super(name, plugin);
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;

        this.info = new Properties();
        this.info.put("autoReconnect", true);
        this.info.put("user", this.username);
        this.info.put("password", this.password);
        this.info.put("useUnicode", "true");
        this.info.put("characterEncoding", "utf8");
        open();
    }

    public void open() {
        if (!isConnected()) {
            getExecutorService().execute(() -> {
                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    setConnection(DriverManager.getConnection("jdbc:mariadb://" + this.host + ":" + this.port + "/" + this.database, this.info));
                    createTables(getTablesRules());
                } catch (SQLException | ClassNotFoundException e) {
                    Bukkit.getServer().getConsoleSender()
                            .sendMessage("\n\n§b[UzmCommons | MariaDB] §7Banco de dados não conectado, plugin desabilitado.\n\n");

                    getPlugin().getLogger().log(Level.SEVERE, "[UzmCommons | MariaDB] Error information:", e);

                    Bukkit.getServer().getPluginManager().disablePlugin(getPlugin());
                }
            });
        }
    }

}
