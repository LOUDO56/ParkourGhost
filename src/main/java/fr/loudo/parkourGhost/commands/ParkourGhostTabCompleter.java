package fr.loudo.parkourGhost.commands;

import io.github.a5h73y.parkour.Parkour;
import io.github.a5h73y.parkour.commands.ParkourCommands;
import io.github.a5h73y.parkour.database.DatabaseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import pro.husk.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParkourGhostTabCompleter implements TabCompleter {

    private static final List<String> ARG1_OPTIONS = Arrays.asList("help", "play", "reload");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> coursesNameCompleted = getCompletedCourse((Player) sender);
        if (args.length == 1) {
            if (sender.hasPermission("parkourghost.admin")) {
                completions.addAll(ARG1_OPTIONS);
            } else {
                completions.add("play");
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("play")) {
                completions.addAll(coursesNameCompleted);
            }
        }

        return completions;
    }

    private List<String> getCompletedCourse(Player p) {
        DatabaseManager databaseManager = Parkour.getInstance().getDatabaseManager();
        Database database = Parkour.getInstance().getDatabaseManager().getDatabase();
        List<String> result = new ArrayList<>();

        try (PreparedStatement statement = database.getConnection().prepareStatement("SELECT name FROM course INNER JOIN time ON course.courseId = time.courseId WHERE playerId = ?")) {
            statement.setString(1, databaseManager.getPlayerId(p));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getString("name"));
            }

            resultSet.getStatement().close();
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't get player completed courses: " + e);
        }

        return result;
    }
}
