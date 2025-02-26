package fr.loudo.parkourGhost;

import fr.loudo.parkourGhost.commands.ParkourGhostCommand;
import fr.loudo.parkourGhost.data.PlayersDataManager;
import fr.loudo.parkourGhost.events.ParkourEvents;
import io.github.a5h73y.parkour.Parkour;
import io.github.a5h73y.parkour.type.ParkourManager;
import io.github.a5h73y.parkour.type.course.CourseManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ParkourGhost extends JavaPlugin {

    private static ParkourGhost plugin;

    @Override
    public void onEnable() {
        plugin = this;

        Plugin parkour = getServer().getPluginManager().getPlugin("Parkour");
        if (parkour != null && parkour.isEnabled()) {
            System.out.println("Found Parkour v" + parkour.getDescription().getVersion());
        } else {
            System.out.println("Parkour not installed");
        }

        getCommand("parkourghost").setExecutor(new ParkourGhostCommand());

        getServer().getPluginManager().registerEvents(new ParkourEvents(), this);

        PlayersDataManager.folderInit();



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ParkourGhost getPlugin() {
        return plugin;
    }
}
