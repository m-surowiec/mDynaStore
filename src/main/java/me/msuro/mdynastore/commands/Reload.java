package me.msuro.mdynastore.commands;

import me.msuro.mdynastore.ConfigUtil;
import me.msuro.mdynastore.Constants;
import me.msuro.mdynastore.colors.ColorAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Reload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("mdynastore.cmd.reload")) {
                sender.sendMessage(ConfigUtil.getMessage(Constants.NOPERMISSION));
                return true;
            }
            sender.sendMessage(ColorAPI.process(ConfigUtil.getPrefix() + " &aReloading plugin..."));
            ConfigUtil.init(sender);
            sender.sendMessage(ColorAPI.process(ConfigUtil.getPrefix() + " &aPlugin reloaded!"));
            return true;
        }
        return false;
    }
}