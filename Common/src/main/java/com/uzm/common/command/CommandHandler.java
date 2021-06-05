package com.uzm.common.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class CommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        handle(commandSender, s, strings);
        return false;
    }

    public abstract boolean handle(CommandSender sender, String command, String[] args);

    public abstract List<String> getCommands();

    public abstract void help(CommandSender sender, String label);

}
