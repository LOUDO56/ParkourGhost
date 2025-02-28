package fr.loudo.parkourGhost.commands;

import fr.loudo.parkourGhost.manager.ParkourGhostManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ParkourGhostCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player p) {
            String courseName = args[0];
            if(args[0].isEmpty()) {
                p.sendMessage(ChatColor.RED + "Please, put the course name.");
                return true;
            }

            if(courseName.equalsIgnoreCase("help")) {
                p.sendMessage(ChatColor.GREEN + "/parkourghost [course_name] - Challenge your best time ghost on a parkour.");
                if(p.hasPermission("parkourghost.admin")) {
                    p.sendMessage(ChatColor.GREEN + "/parkourghost reload - Reload Parkour Ghost configuration.");
                }
                return true;
            }

            if(!ParkourGhostManager.joinPlayerParkourAndStartPlayback(p, courseName.toLowerCase())) {
                p.sendMessage(ChatColor.RED + "No record found for this parkour.");
            }

        }

        return true;
    }
}
