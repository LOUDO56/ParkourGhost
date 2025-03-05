package fr.loudo.parkourGhost.events;

import fr.loudo.parkourGhost.manager.ParkourGhostManager;
import fr.loudo.parkourGhost.recordings.Recording;
import fr.loudo.parkourGhost.recordings.actions.ActionType;
import io.github.a5h73y.parkour.Parkour;
import io.github.a5h73y.parkour.event.ParkourFinishEvent;
import io.github.a5h73y.parkour.event.ParkourJoinEvent;
import io.github.a5h73y.parkour.event.ParkourLeaveEvent;
import io.github.a5h73y.parkour.event.ParkourRestartEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ParkourEvents implements Listener {

    @EventHandler
    public void onPlayerParkourJoin(ParkourJoinEvent event) {
        ParkourGhostManager.joinPlayerParkour(event.getPlayer(), event.getCourseName());

    }

    @EventHandler
    public void onPlayerParkourFinish(ParkourFinishEvent event) {
        ParkourGhostManager.stopRecordOrPlayback(event.getPlayer(), false);
    }

    @EventHandler
    public void onPlayerParkourLeave(ParkourLeaveEvent event) {
        ParkourGhostManager.stopRecordOrPlayback(event.getPlayer(), true);
    }

    @EventHandler
    public void onPlayerParkourRestart(ParkourRestartEvent event) {
        ParkourGhostManager.restartPlayerParkour(event.getPlayer(), event.getCourseName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ParkourGhostManager.stopRecordOrPlayback(event.getPlayer(), true);
    }

    @EventHandler
    public void onPlayerSwing(PlayerInteractEvent event) {
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Recording recording = ParkourGhostManager.getCurrentPlayerRecording(event.getPlayer());
            if(recording != null) {
                recording.addAction(ActionType.SWING);
            }

        }
    }

    @EventHandler
    public void onPlayerHurt(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player p) {
            Recording recording = ParkourGhostManager.getCurrentPlayerRecording(p);
            if(recording != null) {
                recording.addAction(ActionType.HURT);
            }
        }
    }


}
