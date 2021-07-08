package com.uzm.common.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

import java.util.List;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

public abstract class CommandHandler extends Command {


    public CommandHandler(String name) {
        super(name);
        this.setAliases(getAliases());
        try {
            SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
            simpleCommandMap.register(this.getName(), "uzm-common", this);
        } catch (ReflectiveOperationException ignore) {

        }
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        handle(commandSender, s, strings);
        return false;
    }


    public abstract boolean handle(CommandSender sender, String command, String[] args);

    public abstract List<String> getAliases();

    public abstract void help(CommandSender sender, String label);

}
