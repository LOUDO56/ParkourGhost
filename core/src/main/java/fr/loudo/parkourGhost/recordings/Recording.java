package fr.loudo.parkourGhost.recordings;

import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.manager.PlayersDataManager;
import fr.loudo.parkourGhost.recordings.actions.ActionPlayer;
import fr.loudo.parkourGhost.recordings.actions.ActionType;
import fr.loudo.parkourGhost.recordings.actions.MovementData;
import fr.loudo.parkourGhost.recordings.actions.PlayerPoseChange;
import io.github.a5h73y.parkour.Parkour;
import io.github.a5h73y.parkour.event.ParkourTimeResultEvent;
import io.github.a5h73y.parkour.type.player.TimeResult;
import io.github.a5h73y.parkour.type.player.session.ParkourSession;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class Recording {

    private String courseName;
    private Player player;
    private RecordingData recordingData;
    private boolean isRecording;
    private int tick;
    private BukkitTask recordTask;

    public Recording(Player player, String courseName) {
        this.courseName = courseName;
        this.player = player;
        this.recordingData = new RecordingData();
    }

    public boolean start() {
        if(isRecording) return false;

        isRecording = true;

        recordingData.getMovementData().clear();

        AtomicReference<Pose> lastPos = new AtomicReference<>(player.getPose());
        tick = 0;

        recordTask = new BukkitRunnable() {
            @Override
            public void run() {
                MovementData movementData = MovementData.getMovementDataFromPlayer(player);
                recordingData.getMovementData().add(movementData);

                if(player.getPose() != lastPos.get()) {
                    recordingData.getActionsPlayer().put(tick, new PlayerPoseChange(player.getPose()));
                }
                lastPos.set(player.getPose());
                tick++;
            }
        }.runTaskTimer(ParkourGhost.getPlugin(), 0L, 1L);

        return true;
    }

    public void addAction(ActionType actionType) {
        recordingData.getActionsPlayer().put(tick, new ActionPlayer(actionType));
    }

    public boolean stop(ParkourTimeResultEvent event, boolean force) {
        if(!isRecording) return false;

        recordTask.cancel();
        recordTask = null;

        isRecording = false;

        if(!force) {
            try {
                if(event.isPlayerRecord() || event.isGlobalRecord()) {
                    save();
                    player.sendMessage(ParkourGhost.getPlugin().getConfig().getString("messages.new_pb").replace("%course_name%", event.getCourseName()));
                }
            } catch (Exception e) {
                player.sendMessage(ParkourGhost.getPlugin().getConfig().getString("messages.unexpected_error_save_record"));
                throw new RuntimeException(e);
            }
        }

        return true;
    }

    public void save() throws IOException {
        PlayersDataManager.writeRecordingData(recordingData, player, courseName);
    }
}
