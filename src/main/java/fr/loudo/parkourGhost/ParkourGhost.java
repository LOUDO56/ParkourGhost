package fr.loudo.parkourGhost;

import fr.loudo.parkourGhost.commands.ParkourGhostCommand;
import fr.loudo.parkourGhost.commands.ParkourGhostTabCompleter;
import fr.loudo.parkourGhost.events.ParkourEvents;
import fr.loudo.parkourGhost.manager.PlayersDataManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ParkourGhost extends JavaPlugin {

    private static ParkourGhost plugin;

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

        PlayersDataManager.folderInit();
        saveDefaultConfig();



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ParkourGhost getPlugin() {
        return plugin;
    }
}
