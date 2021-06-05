package com.uzm.common.spigot.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author JotaMPê (UzmStudio)
 */

public class PlayerMenu extends Menu implements Listener {

    protected Player player;

    public PlayerMenu(Player player, String title) {
        this(player, title, 6);
    }

    public PlayerMenu(Player player, String title, int rows) {
        super(title, rows);
        this.player = player;
    }

    public void register(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void open() {
        this.player.openInventory(getInventory());
    }

    public Player getPlayer() {
        return player;
    }
}
