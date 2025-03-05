package fr.loudo.parkourGhost.events;

import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.utils.CheckVersion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event) {
        if(event.getPlayer().hasPermission("parkourghost.admin")) {
            if(ParkourGhost.getPlugin().getConfig().getBoolean("check-version")) {
                CheckVersion.notifyPlayer(event.getPlayer());
            }
        }
    }

}