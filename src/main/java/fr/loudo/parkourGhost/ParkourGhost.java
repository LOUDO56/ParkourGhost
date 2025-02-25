package fr.loudo.parkourGhost;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.plugin.java.JavaPlugin;

public final class ParkourGhost extends JavaPlugin {

    private static ParkourGhost plugin;

    @Override
    public void onEnable() {
       plugin = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ParkourGhost getPlugin() {
        return plugin;
    }
}
