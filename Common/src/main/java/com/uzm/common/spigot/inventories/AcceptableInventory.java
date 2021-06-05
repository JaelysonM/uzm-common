package com.uzm.common.spigot.inventories;

import com.uzm.common.plugin.Common;
import com.uzm.common.spigot.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Method;

/**
 * @author JotaMPê (UzmStudio)
 */

public class AcceptableInventory extends Menu implements Listener {

    private String[] lore;
    private Player player;
    private ItemStack core;
    private Method handshake;
    private Object[] params;

    public AcceptableInventory(Player p, ItemStack core, String[] lore) {
        super("§7Confirmação", 4);
        this.player = p;
        this.core = core;
        this.lore = lore;

        Bukkit.getServer().getPluginManager().registerEvents(this, Common.getInstance());
    }

    public void build(Method m, Object... params) {

        setItem(11, new ItemBuilder(Material.WOOL).durability(14).name("§cRecusar")
                .lore("§7Deseja cancelar essa ação?").build());

        setItem(13, new ItemBuilder(core.clone()).lore(lore).build());

        setItem(15, new ItemBuilder(Material.WOOL).durability(5).durability(5).name("§aConfirmar")
                .lore("§7Deseja aceitar fazer essa ação?").build());

        setItem(31, new ItemBuilder(Material.SEA_LANTERN).amount(30).name("§3Tempo").build());

        this.handshake = m;
        this.params = params;
        new BukkitRunnable() {
            float x = 30.0F;

            @Override
            public void run() {
                if (!getInventory().getViewers().contains(player)) {
                    cancel();
                } else {
                    if (x <= 0) {
                        try {
                            m.invoke("", params);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        cancel();
                        HandlerList.unregisterAll(AcceptableInventory.this);
                        return;
                    }
                    x -= 0.5F;
                    setItem(31, new ItemBuilder(getItem(31).clone())
                            .amount(Math.round(x) == 0 ? 1 : Math.round(x)).build());
                    if (x >= 1 && x <= 5 && x == Math.round(x)) {
                        if (getInventory().getViewers().contains(player)) {
                            player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
                        }
                    }
                }

            }
        }.runTaskTimer(Common.getInstance(), 1L, 10L);
        open(player);
    }

    @EventHandler
    void click(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();
        if ((item != null) && (item.hasItemMeta())) {
            if (e.getInventory().equals(getInventory())) {
                e.setCancelled(true);
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aConfirmar")) {
                    p.closeInventory();
                    try {
                        this.handshake.invoke("", params);
                    } catch (Exception err) {
                        p.sendMessage("§cNão foi possível confirmar, tente novamente mais tarde.");
                    }

                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cRecusar")) {
                    p.sendMessage("§cVocê recusou essa confirmação!");
                    p.closeInventory();

                }

            }
        }
    }

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

    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
