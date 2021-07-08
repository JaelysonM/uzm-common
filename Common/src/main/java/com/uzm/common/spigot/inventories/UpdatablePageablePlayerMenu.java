package com.uzm.common.spigot.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

public abstract class UpdatablePageablePlayerMenu extends PageablePlayerMenu implements Listener {

    protected BukkitTask task;

    public UpdatablePageablePlayerMenu(Player player, String name) {
        this(player, name, 3);
    }

    public UpdatablePageablePlayerMenu(Player player, String name, int rows) {
        super(player, name, rows);
    }

    public void register(JavaPlugin plugin, long updateEveryTicks) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        }.runTaskTimer(plugin, 0, updateEveryTicks);
    }

    public abstract void update();
}
