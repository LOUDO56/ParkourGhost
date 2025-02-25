package fr.loudo.parkourGhost.recording;

import io.github.a5h73y.parkour.Parkour;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Recording {

    private Parkour parkour;
    private ServerPlayer player;
    private List<MovementData> movements;
    private boolean isRecording;

    public Recording(Parkour parkour, ServerPlayer player) {
        this.parkour = parkour;
        this.player = player;
    }

    public boolean start() {
        if(isRecording) return false;

        isRecording = true;

        return true;
    }

    public boolean stop() {
        if(!isRecording) return false;

        isRecording = false;

        return true;
    }

    private void record() {

        Vec3 pPosition = player.position();
        float xRot = player.getXRot();
        float yRot = player.getYRot();
        float yHeadRot = player.getYHeadRot();
        float yBodyRot = player.yBodyRot;

        MovementData movementData = new MovementData(
                pPosition.x(),
                pPosition.y(),
                pPosition.z(),
                xRot,
                yRot,
                yHeadRot,
                yBodyRot
        );

        movements.add(movementData);

    }

}
