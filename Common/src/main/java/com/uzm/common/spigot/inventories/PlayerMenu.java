package com.uzm.common.spigot.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

public abstract class PlayerMenu extends Menu implements Listener {

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

    public abstract void click(InventoryClickEvent click);

    public void cancel() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
        if (evt.getPlayer().equals(this.player)) {
            this.cancel();
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt) {
        if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
            this.cancel();
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().equals(this.getInventory())) {
            event.setCancelled(true);
            if (event.getClickedInventory() != null && event.getClickedInventory().equals(this.getInventory())) {
                player.updateInventory();
                click(event);
            }
        }
    }

}
