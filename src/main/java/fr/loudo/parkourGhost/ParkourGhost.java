package fr.loudo.parkourGhost;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import fr.loudo.parkourGhost.commands.DebugCommand;
import fr.loudo.parkourGhost.commands.ParkourGhostCommand;
import fr.loudo.parkourGhost.commands.ParkourGhostTabCompleter;
import fr.loudo.parkourGhost.events.ParkourEvents;
import fr.loudo.parkourGhost.manager.PlayersDataManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ParkourGhost extends JavaPlugin {

    private static ParkourGhost plugin;
    private static String version;
    private static ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        plugin = this;

        Plugin parkour = getServer().getPluginManager().getPlugin("Parkour");
        Plugin protocolLib = getServer().getPluginManager().getPlugin("ProtocolLib");
        if (parkour == null && !parkour.isEnabled()) {
            Bukkit.getLogger().severe("[ParkourGhost] Parkour plugin isn't installed, ParkourGhost is an add-on from this plugin and need to be installed! Install it here: https://www.spigotmc.org/resources/parkour.23685/");
            this.setEnabled(false);
            return;
        }

        if (protocolLib == null && !protocolLib.isEnabled()) {
            Bukkit.getLogger().severe("[ParkourGhost] ProtocolLib isn't installed, ProtocolLib is needed to spawn ghost! Install it here: https://github.com/dmulloy2/ProtocolLib/releases");
            this.setEnabled(false);
            return;
        }

        getCommand("parkourghost").setExecutor(new ParkourGhostCommand());
        getCommand("parkourghost").setTabCompleter(new ParkourGhostTabCompleter());

        getServer().getPluginManager().registerEvents(new ParkourEvents(), this);

        PlayersDataManager.folderInit();
        saveDefaultConfig();

        version = Bukkit.getBukkitVersion().split("-")[0];

        protocolManager = ProtocolLibrary.getProtocolManager();



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

    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }
}
