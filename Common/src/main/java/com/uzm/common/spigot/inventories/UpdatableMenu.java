package com.uzm.common.spigot.inventories;

import org.bukkit.Bukkit;
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

public abstract class UpdatableMenu extends Menu implements Listener {


    private BukkitTask task;

    public UpdatableMenu(String name) {
        this(name, 3);
    }

    public UpdatableMenu(String name, int rows) {
        super(name, rows);
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

    public void cancel() {
        this.task.cancel();
        this.task = null;
    }

    public abstract void update();
}