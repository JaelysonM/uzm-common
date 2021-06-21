package com.uzm.common.plugin;

import com.uzm.common.java.util.file.FileUtils;
import com.uzm.common.plugin.abstracts.UzmLoader;
import com.uzm.common.plugin.abstracts.UzmPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;

public class Common extends UzmPlugin {
    @Getter(AccessLevel.PUBLIC)
    private static UzmPlugin instance;

    @Override
    public void load() {

    }

    @Override
    public void enable() {
        instance = this;

        UzmLoader uzmLoader = new CommonLoader(this, "com.uzm.common.commands", "com.uzm.common.listeners", "com.uzm.common.protocol");
        this.setLoader(uzmLoader);
    }


    @Override
    public void disable() {
        File update = new File("plugins/" + getDescription().getName() + "/update", getDescription().getName() + ".jar");
        if (update.exists()) {
            try {
                FileUtils.deleteFile(new File("plugins/" + getDescription().getName() + ".jar"));
                FileUtils.copyFile(new FileInputStream(update), new File("plugins/" + getDescription().getName() + ".jar"));
                FileUtils.deleteFile(update.getParentFile());
                Bukkit.getConsoleSender().sendMessage("§b[UzmCommons | Updater] §7Update applied!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
