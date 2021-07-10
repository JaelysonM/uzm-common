package com.uzm.common.plugin.abstracts;


import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketListener;
import com.uzm.common.java.util.ReflectionUtils;
import com.uzm.common.plugin.Common;
import com.uzm.common.plugin.logger.CustomLogger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.6
 */

@Getter
public abstract class UzmLoader {

    private UzmPlugin uzmPlugin;
    private PluginManager pluginManager;
    private boolean papiHooked = false;

    protected String listenersPath;
    protected String commandsPath;
    protected String protocolsPath;

    public UzmLoader(UzmPlugin uzmPlugin) {
        this(uzmPlugin, uzmPlugin.getDescription().getMain().replace(".plugin", "".replace(".core", "")) + ".commands", uzmPlugin.getDescription().getMain().replace(".plugin", "".replace(".core", "")) + ".protocol", uzmPlugin.getDescription().getMain().replace(".plugin", "".replace(".core", "")) + ".listeners");
    }


    public UzmLoader(UzmPlugin uzmPlugin, String commandsPath, String listenersPath, String protocolsPath) {
        this.uzmPlugin = uzmPlugin;
        this.pluginManager = Bukkit.getServer().getPluginManager();
        this.commandsPath = commandsPath;
        this.protocolsPath = protocolsPath;
        this.listenersPath = listenersPath;

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            ((CustomLogger) getUzmPlugin().getLogger()).getModule("Hooks").info("§aPlaceholderAPI identified, init hooks and handlers!");
            this.papiHooked = true;
        }

        this.commands();
        this.listeners();
        this.protocols();
        this.configurations();
        this.libraries();
        this.managers();

        Common.getUsingCommon().add(uzmPlugin);

    }

    public abstract void managers();

    public abstract void libraries();

    public abstract void configurations();

    protected void listeners() {
        CompletableFuture.runAsync(() -> {
            AtomicInteger counter = new AtomicInteger(0);
            List<Class<?>> classList = ReflectionUtils.getClasses(this.listenersPath, this.getUzmPlugin());
            classList.parallelStream().forEach(l -> {
                try {
                    this.pluginManager.registerEvents((Listener) l.newInstance(), this.uzmPlugin);
                    counter.incrementAndGet();
                } catch (InstantiationException | IllegalAccessException e) {
                    System.err.println("[UzmLoader | Listeners] Probally An error occurred while trying to register some listeners");
                    e.printStackTrace();
                }
            });
            Bukkit.getConsoleSender().sendMessage("§b[" + this.uzmPlugin.getDescription().getName() + " | UzmLoader] §7We're registered §f(" + counter + "/" + classList.size() + ") §7listeners classes.");

        });
    }

    protected void commands() {
        CompletableFuture.runAsync(() -> {
            AtomicInteger counter = new AtomicInteger(0);
            List<Class<?>> classList = ReflectionUtils.getClasses(this.commandsPath, this.getUzmPlugin());
            classList.parallelStream().forEach(c -> {
                try {
                    c.newInstance();
                    counter.incrementAndGet();

                } catch (Exception e) {
                    System.err.println("[UzmLoader | Commands] Probally An error occurred while trying to register some commands");
                    e.printStackTrace();
                }
            });
            Bukkit.getConsoleSender().sendMessage("§b[" + this.uzmPlugin.getDescription().getName() + " | UzmLoader] §7We're registered §f(" + counter + "/" + classList.size() + ") §7commands classes.");
        });
    }

    protected void protocols() {
        CompletableFuture.runAsync(() -> {
            AtomicInteger counter = new AtomicInteger(0);
            List<Class<?>> classList = ReflectionUtils.getClasses(this.protocolsPath, this.getUzmPlugin());
            classList.parallelStream().forEach(c -> {
                try {
                    ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener) c.newInstance());
                    counter.incrementAndGet();
                } catch (InstantiationException | IllegalAccessException e) {
                    System.err.println("[UzmLoader | Commands] Probally An error occurred while trying to register some protocols");
                    e.printStackTrace();
                }
            });
            Bukkit.getConsoleSender().sendMessage("§b[" + this.uzmPlugin.getDescription().getName() + " | UzmLoader] §7We're registered §f(" + counter + "/" + classList.size() + ") §7protocol classes.");

        });
    }


}
