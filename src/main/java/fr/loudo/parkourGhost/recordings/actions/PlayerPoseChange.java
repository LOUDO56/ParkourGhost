package fr.loudo.parkourGhost.recordings.actions;

import net.minecraft.world.entity.Pose;

public class PlayerPoseChange extends ActionPlayer {

    private Pose pose;

    public PlayerPoseChange(Pose pose) {
        super(ActionType.POSE);
        this.pose = pose;
    }

    public Pose getPose() {
        return pose;
    }

    @Override
    public String toString() {
        return "ChangePose{" +
                "pose=" + pose +
                '}';
    }
}
