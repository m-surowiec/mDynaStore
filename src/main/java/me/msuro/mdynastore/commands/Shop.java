package me.msuro.mdynastore.commands;

import me.msuro.mdynastore.ConfigUtil;
import me.msuro.mdynastore.Constants;
import me.msuro.mdynastore.colors.ColorAPI;
import me.msuro.mdynastore.guis.CategorySelection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Shop implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("shop")) {
            if (!sender.hasPermission("mdynastore.cmd.shop")) {
                sender.sendMessage(ConfigUtil.getMessage(Constants.NOPERMISSION));
                return true;
            }
            Inventory inv = new CategorySelection().getInventory();
            ((Player) sender).openInventory(inv);
            return true;
        }
        return false;
    }
}

