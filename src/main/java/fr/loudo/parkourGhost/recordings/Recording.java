package fr.loudo.parkourGhost.recordings;

import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.manager.PlayersDataManager;
import fr.loudo.parkourGhost.recordings.actions.ActionPlayer;
import fr.loudo.parkourGhost.recordings.actions.ActionType;
import fr.loudo.parkourGhost.recordings.actions.PlayerPoseChange;
import fr.loudo.parkourGhost.recordings.actions.MovementData;
import io.github.a5h73y.parkour.Parkour;
import io.github.a5h73y.parkour.type.player.session.ParkourSession;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
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

    public Recording(String courseName, Player player) {
        this.courseName = courseName;
        this.player = player;
        this.recordingData = new RecordingData();
    }

    public boolean start() {
        if(isRecording) return false;

        isRecording = true;

        recordingData.getMovementData().clear();
        ServerPlayer serverPlayer = ((CraftPlayer)player).getHandle();

        AtomicReference<Pose> lastPos = new AtomicReference<>(serverPlayer.getPose());
        tick = 0;

        recordTask = new BukkitRunnable() {
            @Override
            public void run() {
                MovementData movementData = MovementData.getMovementDataFromPlayer(player);
                recordingData.getMovementData().add(movementData);

                if(serverPlayer.getPose() != lastPos.get()) {
                    recordingData.getActionsPlayer().put(tick, new PlayerPoseChange(serverPlayer.getPose()));
                }
                lastPos.set(serverPlayer.getPose());
                tick++;
            }
        }.runTaskTimer(ParkourGhost.getPlugin(), 0L, 1L);

        return true;
    }

    public void addAction(ActionType actionType) {
        recordingData.getActionsPlayer().put(tick, new ActionPlayer(actionType));
    }

    public boolean stop(boolean force) {
        if(!isRecording) return false;

        recordTask.cancel();
        recordTask = null;

        isRecording = false;

        if(!force) {
            try {
                ParkourSession pSession = Parkour.getInstance().getParkourSessionManager().getParkourSession(player);
                if(Parkour.getInstance().getDatabaseManager().isBestCourseTime(pSession.getCourseName(), pSession.getTimeFinished())) {
                    save();
                    player.sendMessage(ChatColor.GREEN + "New Personal Best. Challenge your ghost with /paghost " + pSession.getCourseName());
                }
            } catch (Exception e) {
                player.sendMessage("An unexpected error occurred while saving your position data!");
                throw new RuntimeException(e);
            }
        }

        return true;
    }

    public void save() throws IOException {
        PlayersDataManager.writeRecordingData(recordingData, player, courseName);
    }
}
