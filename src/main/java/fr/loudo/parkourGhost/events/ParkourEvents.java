package fr.loudo.parkourGhost.events;

import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.data.ParkourData;
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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ParkourEvents implements Listener {

    @EventHandler
    public void onPlayerParkourJoin(ParkourJoinEvent event) {
        ParkourData.joinPlayerParkour(event.getPlayer(), event.getCourseName());

    }

    @EventHandler
    public void onPlayerParkourFinish(ParkourFinishEvent event) {
        ParkourData.leavePlayerParkour(event.getPlayer(), false);
    }

    @EventHandler
    public void onPlayerParkourLeave(ParkourLeaveEvent event) {
        ParkourData.leavePlayerParkour(event.getPlayer(), true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

}
