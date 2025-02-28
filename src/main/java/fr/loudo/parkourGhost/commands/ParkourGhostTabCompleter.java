package fr.loudo.parkourGhost.commands;

import fr.loudo.parkourGhost.manager.PlayersDataManager;
import fr.loudo.parkourGhost.recordings.RecordingData;
import io.github.a5h73y.parkour.Parkour;
import io.github.a5h73y.parkour.commands.ParkourCommands;
import io.github.a5h73y.parkour.database.DatabaseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import pro.husk.Database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            HashMap<String, RecordingData> recordingData = PlayersDataManager.getRecordingData(p).getRecordedRuns();
            return new ArrayList<>(recordingData.keySet());
        } catch (IOException e) {
            throw new RuntimeException("Couldn't get recorded courses of " + p.getName() + ": " + e);
        }

    }
}
