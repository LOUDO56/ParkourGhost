package fr.loudo.parkourGhost;

import fr.loudo.parkourGhost.commands.ParkourGhostCommand;
import fr.loudo.parkourGhost.commands.ParkourGhostTabCompleter;
import fr.loudo.parkourGhost.events.JoinEvent;
import fr.loudo.parkourGhost.events.ParkourEvents;
import fr.loudo.parkourGhost.manager.PlayersDataManager;
import fr.loudo.parkourGhost.utils.CheckVersion;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.UnsafeValues;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ParkourGhost extends JavaPlugin {

    private static ParkourGhost plugin;
    private static String version;

    @Override
    public void onEnable() {
        plugin = this;

        Plugin parkour = getServer().getPluginManager().getPlugin("Parkour");
        if (parkour == null && !parkour.isEnabled()) {
            Bukkit.getLogger().severe("[ParkourGhost] Parkour plugin isn't installed, ParkourGhost is an add-on from this plugin and need to be installed! Install it here: https://www.spigotmc.org/resources/parkour.23685/");
            this.setEnabled(false);
            return;
        }

        getCommand("parkourghost").setExecutor(new ParkourGhostCommand());
        getCommand("parkourghost").setTabCompleter(new ParkourGhostTabCompleter());

        getServer().getPluginManager().registerEvents(new ParkourEvents(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);

        PlayersDataManager.folderInit();
        saveDefaultConfig();

        version = Bukkit.getBukkitVersion();
        String versionDot = version.split("-")[0];

        List<String> versionsSupported = new ArrayList<>(Arrays.asList("1.18.2", "1.19.4", "1.20.6", "1.21.4"));

        if(!versionsSupported.contains(versionDot)) {
            Bukkit.getLogger().severe(
                    "[ParkourGhost] Your minecraft version isn't supported. ParkourGhost is disabled."
                    + " If you think this is an error, check that your server is at the latest version, e.g. 1.18 -> 1.18.2"
            );
            this.setEnabled(false);
        }
        version = versionDot.replace(".", "_");
        if(version.split("_").length > 2) {
            version = version.split("_")[0] + "_" + version.split("_")[1];
        }

        if(getConfig().getBoolean("check-version")) {
            try {
                CheckVersion.verify();
            } catch (IOException | ParseException e) {
                getLogger().info("Couldn't check for a new update, passing.");
            }
        }

        //Bstats
        int pluginId = 24993; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ParkourGhost getPlugin() {
        return plugin;
    }

    public static String getVersion() {
        return version;
    }
}
