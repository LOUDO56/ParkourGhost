package fr.loudo.parkourGhost;

import fr.loudo.parkourGhost.commands.CreateGhostPlayer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.plugin.java.JavaPlugin;

public final class ParkourGhost extends JavaPlugin {

    private static ParkourGhost plugin;

    @Override
    public void onEnable() {
       plugin = this;
       getCommand("createghostplayer").setExecutor(new CreateGhostPlayer());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ParkourGhost getPlugin() {
        return plugin;
    }
}
