package fr.loudo.parkourGhost.events;

import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.data.ParkourData;
import fr.loudo.parkourGhost.recordings.Recording;
import fr.loudo.parkourGhost.recordings.actions.ActionPlayer;
import fr.loudo.parkourGhost.recordings.actions.ActionType;
import fr.loudo.parkourGhost.utils.GhostPlayer;
import io.github.a5h73y.parkour.Parkour;
import io.github.a5h73y.parkour.event.ParkourFinishEvent;
import io.github.a5h73y.parkour.event.ParkourJoinEvent;
import io.github.a5h73y.parkour.event.ParkourLeaveEvent;
import io.github.a5h73y.parkour.event.ParkourRestartEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ParkourEvents implements Listener {

    @EventHandler
    public void onPlayerParkourJoin(ParkourJoinEvent event) {
        ParkourData.joinPlayerParkour(event.getPlayer(), event.getCourseName());

    }

    @EventHandler
    public void onPlayerParkourFinish(ParkourFinishEvent event) {
        ParkourData.stopRecordOrPlayback(event.getPlayer(), false);
    }

    @EventHandler
    public void onPlayerParkourLeave(ParkourLeaveEvent event) {
        ParkourData.stopRecordOrPlayback(event.getPlayer(), true);
    }

    @EventHandler
    public void onPlayerParkourRestart(ParkourRestartEvent event) {
        ParkourData.restartPlayerParkour(event.getPlayer(), event.getCourseName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(Parkour.getInstance().getParkourSessionManager().isPlaying(event.getPlayer())) {
            ParkourData.stopRecordOrPlayback(event.getPlayer(), true);
        }
    }

    @EventHandler
    public void onPlayerSwing(PlayerInteractEvent event) {
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if(Parkour.getInstance().getParkourSessionManager().isPlaying(event.getPlayer())) {
                Recording recording = ParkourData.getCurrentPlayerRecording(event.getPlayer());
                if(recording != null) {
                    recording.addAction(ActionType.SWING);
                }
            }
        }
    }


}
