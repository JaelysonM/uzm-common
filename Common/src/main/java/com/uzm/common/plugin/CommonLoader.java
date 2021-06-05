package com.uzm.common.plugin;

import com.uzm.common.controllers.LagController;
import com.uzm.common.libraries.holograms.HologramLibrary;
import com.uzm.common.libraries.npclib.NPCLibrary;
import com.uzm.common.nms.NMS;
import com.uzm.common.plugin.abstracts.UzmLoader;
import com.uzm.common.plugin.abstracts.UzmPlugin;
import com.uzm.common.plugin.updater.Updater;
import com.uzm.common.spigot.enums.MinecraftVersion;
import org.bukkit.Bukkit;

public class CommonLoader extends UzmLoader {

    private LagController lagController;

    public CommonLoader(UzmPlugin uzmPlugin, String commandsPath, String listenersPath, String protocolsPath) {
        super(uzmPlugin, commandsPath, listenersPath, protocolsPath);
    }

    @Override
    public void managers() {
        this.lagController = new LagController();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.getUzmPlugin(), this.lagController, 0, 1);
        Bukkit.getScheduler().runTask(this.getUzmPlugin(), () -> new Updater(this.getUzmPlugin()).run());
    }

    @Override
    public void libraries() {
        Bukkit.getServer().getConsoleSender().sendMessage("§b[UzmCommons] §aVersion §f" + Bukkit.getBukkitVersion() + " §aidentified.");
        if (Bukkit.getBukkitVersion().contains("1.8")) {
            if (NMS.setupNMS()) {
                Bukkit.getServer().getConsoleSender().sendMessage("§b[UzmCommons] §aLoading NMSImpls (1.8 version).");
                NPCLibrary.setupNPCs(getUzmPlugin());
                HologramLibrary.setupHolograms(getUzmPlugin());
            }
        }


    }

    @Override
    public void configurations() {

    }
}
