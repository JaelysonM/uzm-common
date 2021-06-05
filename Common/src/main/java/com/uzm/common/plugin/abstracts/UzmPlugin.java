package com.uzm.common.plugin.abstracts;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public abstract class UzmPlugin extends JavaPlugin {

    protected long bootTime;
    private UzmLoader loader;
    private String filePath;


    public abstract void load();

    public abstract void enable();

    public abstract void disable();

    @Override
    public void onLoad() {
        this.load();
    }

    @Override
    public void onEnable() {

        this.bootTime = System.currentTimeMillis();
        this.filePath = getFile().getPath();

        getServer().getConsoleSender().sendMessage("§b[" + this.getDescription().getName() + "] §7Plugin §bessencialmente §7carregado com sucesso.");
        getServer().getConsoleSender().sendMessage("§eVersão: §f" + this.getDescription().getVersion() + " e criado por §f" + String.join(",", this.getDescription().getAuthors()));
        try {
            this.enable();
            this.bootTime = System.currentTimeMillis() - this.bootTime;
            getServer().getConsoleSender()
                    .sendMessage("§b[" + this.getDescription().getName() + "] §7Plugin §adefinitivamente §7carregado com sucesso (§f" + (this.bootTime + " milisegundos§7)"));

        } catch (Exception err) {
            getServer().getConsoleSender()
                    .sendMessage("§b[" + this.getDescription().getName() + "] §cErro ao carregar o plugin, veja os arquivos de configuração e veja se ele volta.");
            getServer().getConsoleSender()
                    .sendMessage("§b[" + this.getDescription().getName() + "] §cEnvie esse erro para o administrador:");
            getServer().getConsoleSender()
                    .sendMessage(err.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }


    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("§b[" + this.getDescription().getName() + "] §7Plugin §6desligado§7, juntamente todos os eventos e comandos também.");
        this.disable();
    }

    public void setLoader(UzmLoader loader) {
        this.loader = loader;
    }
}
