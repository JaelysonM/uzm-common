package com.uzm.common.spigot.inventories;

import com.uzm.common.plugin.Common;
import com.uzm.common.spigot.items.ItemBuilder;
import lombok.AccessLevel;
import lombok.Getter;
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
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author JotaMPê (UzmStudio)
 */


@Getter(AccessLevel.PUBLIC)
public class AcceptableInventory extends PlayerMenu implements Listener {

    protected static final ItemStack ACCEPT_ITEM;
    protected static final ItemStack DECLINE_ITEM;

    static {
        ACCEPT_ITEM = new ItemBuilder(Material.WOOL).durability(5).durability(5).name("§aConfirmar")
                .lore("§7Deseja aceitar fazer essa ação?").build();
        DECLINE_ITEM = new ItemBuilder(Material.WOOL).durability(14).name("§cRecusar")
                .lore("§7Deseja cancelar essa ação?").build();

    }

    private ItemStack iconCore;
    private int maxAwayTime = 30;

    private BukkitTask scheduleTask;

    private Consumer<Player> accept, decline;

    public AcceptableInventory(Player player, ItemStack iconCore) {
        super(player, "§7Confirmação", 4);

        this.iconCore = iconCore;

        Bukkit.getServer().getPluginManager().registerEvents(this, Common.getInstance());

        this.setupInventory();
    }

    public AcceptableInventory setMaxAwayTime(int maxAwayTime) {
        this.maxAwayTime = maxAwayTime;
        return this;
    }

    public AcceptableInventory run(Consumer<Player> accept) {
        this.run(accept, (player -> {
            player.sendMessage("§cVocê recusou essa confirmação!");
            player.closeInventory();
        }));
        return this;
    }

    protected void setupInventory() {
        this.setItem(11, DECLINE_ITEM);
        this.setItem(13, this.iconCore);
        this.setItem(15, ACCEPT_ITEM);

        this.setItem(31, new ItemBuilder(Material.SEA_LANTERN).amount(this.maxAwayTime).name("§3Tempo").build());

    }

    public AcceptableInventory run(Consumer<Player> accept, Consumer<Player> decline) {
        this.accept = accept;
        this.decline = decline;

        AtomicInteger offTime = new AtomicInteger(0);
        this.scheduleTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Common.getInstance(), () -> {
            setItem(31, new ItemBuilder(getItem(31).clone())
                    .amount(this.maxAwayTime - offTime.get()).build());
            if (offTime.incrementAndGet() >= this.maxAwayTime) {
                accept.accept(this.player);
                if (this.scheduleTask != null)
                    this.scheduleTask.cancel();
                HandlerList.unregisterAll(AcceptableInventory.this);
            } else {
                if (offTime.get() >= 1 && offTime.get() <= 5) {
                    if (this.getInventory().getViewers().contains(this.player))
                        this.player.playSound(this.player.getLocation(), Sound.CLICK, 1, 1);
                }
            }
        }, 0, 20);
        this.open(this.player);
        return this;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().equals(this.getInventory())) {
            event.setCancelled(true);
            if (event.getClickedInventory() != null && event.getClickedInventory().equals(this.getInventory())) {
                player.updateInventory();

                ItemStack item = event.getCurrentItem();
                if (item != null && item.getType() != Material.AIR) {
                    if (item == ACCEPT_ITEM) {
                        this.accept.accept(this.player);
                    } else if (item == DECLINE_ITEM) {
                        this.decline.accept(this.player);
                    }
                }
            }
        }
    }


    public void cancel() {
        if (this.scheduleTask != null)
            this.scheduleTask.cancel();
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
