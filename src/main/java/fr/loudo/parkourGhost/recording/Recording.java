package fr.loudo.parkourGhost.recording;

import fr.loudo.parkourGhost.ParkourGhost;
import io.github.a5h73y.parkour.Parkour;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class Recording {

    private Parkour parkour;
    private ServerPlayer player;
    private List<MovementData> movements;
    private boolean isRecording;
    private BukkitTask task;

    public Recording(Parkour parkour, ServerPlayer player) {
        this.parkour = parkour;
        this.player = player;

    }

    public boolean start() {
        if(isRecording) return false;

        isRecording = true;

        task = Bukkit.getScheduler().runTaskTimer(ParkourGhost.getPlugin(), () -> {
            MovementData movementData = MovementData.getMovementDataFromPlayer(player);
            movements.add(movementData);
        }, 0L, 20L);

        return true;
    }

    public boolean stop() {
        if(!isRecording) return false;

        isRecording = false;
        task.cancel();
        task = null;

        return true;
    }


}
