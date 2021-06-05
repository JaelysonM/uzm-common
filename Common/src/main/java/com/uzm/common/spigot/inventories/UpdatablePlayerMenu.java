package com.uzm.common.spigot.inventories;

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
 * @author JotaMPê (UzmStudio)
 */

public abstract class UpdatablePlayerMenu extends UpdatableMenu implements Listener {


    protected Player player;

    public Player getPlayer() {
        return this.player;
    }

    public UpdatablePlayerMenu(Player player, String name) {
        this(player, name, 3);
    }

    public UpdatablePlayerMenu(Player player, String name, int rows) {
        super(name, rows);
        this.player = player;
    }

    @Override
    public void register(JavaPlugin plugin, long updateEveryTicks) {
        super.register(plugin, updateEveryTicks);
        getPlayer().openInventory(getInventory());
    }

    public abstract void click(InventoryClickEvent event);

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().equals(getInventory())) {
            event.setCancelled(true);
            if (event.getClickedInventory() != null && event.getClickedInventory().equals(getInventory())) {
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
        if (evt.getPlayer().equals(getPlayer()) && evt.getInventory().equals(getInventory())) {
            this.cancel();
        }
    }
}