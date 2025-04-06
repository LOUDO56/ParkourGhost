package fr.loudo.parkourGhost.commands;

import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.manager.ParkourGhostManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ParkourGhostCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player p) {
            if(Arrays.stream(args).toList().isEmpty()) {
                sendHelp(p);
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "help":
                    sendHelp(p);
                    break;
                case "reload":
                    if(p.hasPermission("parkourghost.admin")) {
                        ParkourGhost.getPlugin().reloadConfig();
                        p.sendMessage(ChatColor.GREEN + "Successfully reloaded ParkourGhost config!");
                    } else {
                        p.sendMessage(ParkourGhost.getPlugin().getConfig().getString("messages.no_ghost"));
                    }
                    break;

                case "play":
                    if(args.length == 1) {
                        p.sendMessage(ParkourGhost.getPlugin().getConfig().getString("messages.not_valid_name"));
                        return true;
                    }
                    if(!ParkourGhostManager.joinPlayerParkourAndStartPlayback(p, args[1].toLowerCase())) {
                        p.sendMessage(ParkourGhost.getPlugin().getConfig().getString("messages.no_ghost"));
                    }
                    break;

                default:
                    sendHelp(p);
                    break;
            }
        }

        return true;
    }

    private void sendHelp(Player p) {
        p.sendMessage(ParkourGhost.getPlugin().getConfig().getString("help_pg_cmd.1"));
        if(p.hasPermission("parkourghost.admin")) {
            p.sendMessage(ParkourGhost.getPlugin().getConfig().getString("help_pg_cmd.2"));
        }
    }
}
