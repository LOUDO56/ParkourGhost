package fr.loudo.parkourGhost.commands;

import fr.loudo.parkourGhost.data.player.PlayerData;
import fr.loudo.parkourGhost.manager.PlayersDataManager;
import fr.loudo.parkourGhost.recordings.RecordingData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ParkourGhostTabCompleter implements TabCompleter {

    private static final List<String> ARG1_OPTIONS = Arrays.asList("help", "play");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> coursesNameCompleted = getRecordedCourses((Player) sender);
        if (args.length == 1) {
            completions.addAll(ARG1_OPTIONS);
            if (sender.hasPermission("parkourghost.admin")) {
                completions.add("reload");
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("play")) {
                completions.addAll(coursesNameCompleted);
            }
        }

        return completions;
    }

    private List<String> getRecordedCourses(Player p) {
        try {
            List<String> result = new ArrayList<>();
            PlayerData playerData = PlayersDataManager.getRecordingData(p);
            if(playerData != null) {
                HashMap<String, RecordingData> recordingData = playerData.getRecordedRuns();
                result.addAll(recordingData.keySet());
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Couldn't get recorded courses of " + p.getName() + ": " + e);
        }

    }
}
