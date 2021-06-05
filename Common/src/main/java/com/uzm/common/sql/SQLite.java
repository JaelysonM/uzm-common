package com.uzm.common.sql;

import com.uzm.common.plugin.abstracts.UzmPlugin;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * @author JotaMPê (UzmStudio)
 */

public class SQLite extends Database {
    private File file;

    public SQLite(UzmPlugin plugin, String name, File file) {
        super(name, plugin);
        this.file = file;
        open();
    }

    public void open() {
        if (!isConnected()) {
            getExecutorService().execute(() -> {
                try {
                    Class.forName("org.sqlite.JDBC");
                    setConnection(DriverManager.getConnection("jdbc:sqlite:" + file));
                    createTables(getTablesRules());
                } catch (SQLException | ClassNotFoundException e) {
                    getPlugin().getServer().getConsoleSender()
                            .sendMessage("\n\n§b[UzmCommons | SQLite] §7Banco de dados não conectado, plugin desabilitado.\n\n");

                    getPlugin().getLogger().log(Level.SEVERE, "[UzmCommons | SQLite] Error information:", e);

                    Bukkit.getServer().getPluginManager().disablePlugin(getPlugin());
                }
            });
        }
    }
}

