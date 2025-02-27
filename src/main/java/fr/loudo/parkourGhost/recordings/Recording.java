package fr.loudo.parkourGhost.recordings;

import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.data.PlayerData;
import fr.loudo.parkourGhost.data.PlayersDataManager;
import io.github.a5h73y.parkour.Parkour;
import io.github.a5h73y.parkour.type.player.session.ParkourSession;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Recording {

    private String courseName;
    private Player player;
    private List<MovementData> movements;
    private boolean isRecording;
    private BukkitTask task;

    public Recording(String courseName, Player player) {
        this.courseName = courseName;
        this.player = player;
        this.movements = new ArrayList<>();
    }

    public boolean start() {
        if(isRecording) return false;

        isRecording = true;

        movements.clear();
        task = Bukkit.getScheduler().runTaskTimer(ParkourGhost.getPlugin(), () -> {
            MovementData movementData = MovementData.getMovementDataFromPlayer(player);
            movements.add(movementData);
        }, 0L, 1L);

        return true;
    }

    public boolean stop(boolean force) {
        if(!isRecording) return false;

        isRecording = false;
        task.cancel();
        task = null;

        if(!force) {
            try {
                ParkourSession pSession = Parkour.getInstance().getParkourSessionManager().getParkourSession(player);
                if(Parkour.getInstance().getDatabaseManager().isBestCourseTime(pSession.getCourseName(), pSession.getTimeFinished())) {
                    save();
                    player.sendMessage("You beat your last pb, record saved.");
                } else {
                    player.sendMessage("No new PB, record not saved.");
                }
            } catch (Exception e) {
                player.sendMessage("An unexpected error occured while saving your position data!");
                throw new RuntimeException(e);
            }
        }

        return true;
    }

    public void save() throws IOException {
        PlayersDataManager.writeRecordingData(movements, player, courseName);
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<MovementData> getMovements() {
        return movements;
    }

    public void setMovements(List<MovementData> movements) {
        this.movements = movements;
    }

    public BukkitTask getTask() {
        return task;
    }

    public void setTask(BukkitTask task) {
        this.task = task;
    }
}
