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
                        p.sendMessage(ChatColor.RED + "No record found for this parkour.");
                    }
                    break;

                case "play":
                    if(args[1].isEmpty()) {
                        p.sendMessage(ChatColor.RED + "Please, put a valid parkour name.");
                        return true;
                    }
                    if(!ParkourGhostManager.joinPlayerParkourAndStartPlayback(p, args[1].toLowerCase())) {
                        p.sendMessage(ChatColor.RED + "No record found for this parkour.");
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
        p.sendMessage(ChatColor.GREEN + "/parkourghost play [course_name] - Challenge your best time ghost on a parkour.");
        if(p.hasPermission("parkourghost.admin")) {
            p.sendMessage(ChatColor.GREEN + "/parkourghost reload - Reload Parkour Ghost configuration.");
        }
    }
}
