package me.msuro.mdynastore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MDS implements CommandExecutor {

    private final CommandExecutor reloadCommand;
    private final CommandExecutor shopCommand;

    public MDS(CommandExecutor reloadCommand, CommandExecutor shopCommand) {
        this.reloadCommand = reloadCommand;
        this.shopCommand = shopCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /mds <subcommand>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                return reloadCommand.onCommand(sender, command, label, args);
            case "shop":
                return shopCommand.onCommand(sender, command, label, args);
            default:
                sender.sendMessage("Unknown subcommand!");
                return true;
        }
    }
}