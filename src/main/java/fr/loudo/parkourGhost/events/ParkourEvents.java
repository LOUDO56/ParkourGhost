package fr.loudo.parkourGhost.events;

import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.recordings.Recording;
import fr.loudo.parkourGhost.utils.GhostPlayer;
import io.github.a5h73y.parkour.event.ParkourFinishEvent;
import io.github.a5h73y.parkour.event.ParkourJoinEvent;
import io.github.a5h73y.parkour.event.ParkourLeaveEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ParkourEvents implements Listener {

    @EventHandler
    public void onPlayerParkourJoin(ParkourJoinEvent event) {
        Player p = event.getPlayer();

    }

    @EventHandler
    public void onPlayerParkourFinish(ParkourFinishEvent event) {

    }

    @EventHandler
    public void onPlayerParkourFinish(ParkourLeaveEvent event) {

    }

}
