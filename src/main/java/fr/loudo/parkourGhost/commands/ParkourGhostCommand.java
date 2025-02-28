package fr.loudo.parkourGhost.commands;

import fr.loudo.parkourGhost.data.ParkourData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftAbstractArrow;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
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

            if(ParkourData.joinPlayerParkourAndStartPlayback(p, courseName.toLowerCase())) {
                p.sendMessage(ChatColor.GREEN + "Joined");
            } else {
                p.sendMessage(ChatColor.RED + "No record found for this parkour.");
            }

        }

        return true;
    }
}
