package com.uzm.common.plugin;

import com.uzm.common.controllers.LagController;
import com.uzm.common.database.cache.CacheHandler;
import com.uzm.common.database.data.DataTable;
import com.uzm.common.libraries.holograms.HologramLibrary;
import com.uzm.common.libraries.npclib.NPCLibrary;
import com.uzm.common.nms.NMS;
import com.uzm.common.plugin.abstracts.UzmLoader;
import com.uzm.common.plugin.abstracts.UzmPlugin;
import com.uzm.common.plugin.logger.CustomLogger;
import com.uzm.common.plugin.updater.Updater;
import com.uzm.common.spigot.enums.MinecraftVersion;
import com.uzm.common.utils.Metrics;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.logging.Level;

@Getter(AccessLevel.PUBLIC)
public class CommonLoader extends UzmLoader {

    private LagController lagController;
    private Updater updater;

    protected int pluginId = 11979;

    public CommonLoader(UzmPlugin uzmPlugin, String commandsPath, String listenersPath, String protocolsPath) {
        super(uzmPlugin, commandsPath, listenersPath, protocolsPath);
        this.metrics();
    }

    @Override
    public void managers() {
        this.lagController = new LagController();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.getUzmPlugin(), this.lagController, 0, 1);
        Bukkit.getScheduler().runTask(this.getUzmPlugin(), () -> {
            this.updater = new Updater(this.getUzmPlugin());
            this.updater.run();
        });


    }

    public void metrics() {
        @SuppressWarnings("unused")
        Metrics metrics = new Metrics(this.getUzmPlugin(), pluginId);
        metrics.addCustomChart(new Metrics.SingleLineChart("number_of_repositories", () -> DataTable.listTables().size()));
        metrics.addCustomChart(new Metrics.SingleLineChart("number_of_cachehandlers", () -> CacheHandler.getCACHE().size()));
        metrics.addCustomChart(new Metrics.SingleLineChart("number_of_plugins_using", () -> Common.getUsingCommon().size()));

    }

    @Override
    public void libraries() {
        ((CustomLogger) getUzmPlugin().getLogger()).getModule("NMS").info("§aSpigotAPI version §f§n" + MinecraftVersion.getCurrentVersion().getVersion() + " §aidentified!");
        if (NMS.setupNMS()) {
            ((CustomLogger) getUzmPlugin().getLogger()).getModule("NMS").info("§aNMS module version §f§n" + MinecraftVersion.getCurrentVersion().getVersion() + " §abridged!");
            NPCLibrary.setupNPCs(getUzmPlugin());
            HologramLibrary.setupHolograms(getUzmPlugin());
            ((CustomLogger) getUzmPlugin().getLogger()).getModule("Libraries").info("§fHologram and NPC §alibrary loaded.");

        } else {
            ((CustomLogger) getUzmPlugin().getLogger()).getModule("NMS").log(Level.WARNING, "SpigotAPI version §f§n" + MinecraftVersion.getCurrentVersion().getVersion() + "§eis unsupported so far (" + getUzmPlugin().getDescription().getVersion() + ")");

        }


    }

    @Override
    public void configurations() {

    }
}
