package com.uzm.common.spigot.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

public abstract class ProgressivePlayerMenu extends ProgressiveMenu implements Listener {

    protected Player player;

    public ProgressivePlayerMenu(Player player, String name) {
        this(player, name, 3);
    }

    public ProgressivePlayerMenu(Player player, String name, int rows) {
        super(name, rows);
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void open() {
        player.openInventory(this.menus.get(0).getInventory());
    }

    public void register(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openPrevious() {
        if (this.currentPage == 1) {
            return;
        }

        this.currentPage--;
        player.openInventory(this.menus.get(this.currentPage - 1).getInventory());
    }

    public void openNext() {
        if (this.currentPage + 1 > this.menus.size()) {
            return;
        }

        this.currentPage++;
        player.openInventory(this.menus.get(this.currentPage - 1).getInventory());
    }


    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().equals(this.getCurrentInventory())) {
            event.setCancelled(true);
            if (event.getClickedInventory() != null && event.getClickedInventory().equals(this.getCurrentInventory())) {
                player.updateInventory();

                ItemStack item = event.getCurrentItem();
                if (item != null && item.getType() != Material.AIR) {
                    this.click(event);

                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
        if (evt.getPlayer().equals(getPlayer())) {
            this.cancel();
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt) {
        if (evt.getPlayer().equals(getPlayer()) && evt.getInventory().equals(getCurrentInventory())) {
            this.cancel();
        }
    }


}
