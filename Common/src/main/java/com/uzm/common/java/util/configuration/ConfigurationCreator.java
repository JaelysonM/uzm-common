package com.uzm.common.java.util.configuration;

import com.google.common.collect.Maps;
import com.uzm.common.java.util.file.FileUtils;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.6
 */

@Getter
public class ConfigurationCreator {

    private File file;
    private String fileName;
    private YamlConfiguration config;

    private JavaPlugin plugin;


    public ConfigurationCreator(JavaPlugin plugin, String path, String fileName) {
        this.fileName = fileName;
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), path + fileName + ".yml");

        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            InputStream in = getPlugin().getResource(fileName + ".yml");
            if (in != null) {
                FileUtils.copyFile(in, this.file);
            } else {
                try {
                    this.file.createNewFile();
                } catch (IOException ex) {
                    plugin.getLogger().log(Level.SEVERE, "Um erro inesperado ocorreu ao criar o arquivo \"" + file.getName() + "\": ", ex);
                }
            }

        }
        try {
            this.config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(this.file), StandardCharsets.UTF_8));
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Um erro inesperado ocorreu ao criar a config \"" + this.file.getName() + "\": ", ex);
        }
    }

    public boolean createSection(String path) {
        this.config.createSection(path);
        return true;
    }

    public boolean set(String path, Object obj) {
        this.config.set(path, obj);
        return true;
    }

    public boolean contains(String path) {
        return this.config.contains(path);
    }

    public Object get(String path) {
        return this.config.get(path);
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public int getInt(String path, int def) {
        return this.config.getInt(path, def);
    }

    public double getDouble(String path) {
        return this.config.getDouble(path);
    }

    public double getDouble(String path, double def) {
        return this.config.getDouble(path, def);
    }

    public long getLong(String path) {
        return this.config.getLong(path);
    }

    public long getLong(String path, long def) {
        return this.config.getLong(path, def);
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public String getStringColorCodes(String path) {
        return ChatColor.translateAlternateColorCodes('&', this.config.getString(path));
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }

    public boolean getBoolean(String path, boolean def) {
        return this.config.getBoolean(path, def);
    }

    public List<String> getStringList(String path) {
        return this.config.getStringList(path);
    }

    public List<String> getStringListColorCodes(String path) {
        return this.config.getStringList(path).stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
    }

    public List<Integer> getIntegerList(String path) {
        return this.config.getIntegerList(path);
    }

    public Set<String> getKeys(boolean flag) {
        return this.config.getKeys(flag);
    }

    public ConfigurationSection getSection(String path) {
        return this.config.getConfigurationSection(path);
    }

    public void reload() {
        try {
            this.config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Um erro inesperado ocorreu ao recarregar a config \"" + file.getName() + "\": ", ex);
        }
    }

    public boolean save() {
        try {
            this.config.save(this.file);
            return true;
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Um erro inesperado ocorreu ao salvar a config \"" + file.getName() + "\": ", ex);
            return false;
        }
    }


    public static ConfigurationCreator getConfig(JavaPlugin plugin, String path, String name) {
        if (!cache.containsKey(path + "/" + name)) {
            cache.put(path + name.toLowerCase() + " in " + plugin.getName(), new ConfigurationCreator(plugin, path, name));
        }

        return cache.get(path + name.toLowerCase() + " in " + plugin.getName());
    }

    public static ConfigurationCreator getConfig(JavaPlugin plugin, String name) {
        return getConfig(plugin, "", name);
    }

    public static HashMap<String, ConfigurationCreator> cache = Maps.newHashMap();


}
