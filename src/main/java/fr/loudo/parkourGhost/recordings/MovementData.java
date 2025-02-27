package fr.loudo.parkourGhost.recordings;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class MovementData {

    private double x, y, z;
    private float xRot, yRot, yHeadRot;
    private Pose pose;

    public MovementData(double x, double y, double z, float xRot, float yRot, Pose pose) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xRot = xRot;
        this.yRot = yRot;
        this.pose = pose;
    }

    public static MovementData getMovementDataFromPlayer(Player player) {
        Location pPosition = player.getLocation();
        float xRot = pPosition.getPitch();
        float yRot = pPosition.getYaw();
        Pose pPose = ((CraftPlayer)player).getHandle().getPose();

        return new MovementData(
                pPosition.getX(),
                pPosition.getY(),
                pPosition.getZ(),
                xRot,
                yRot,
                pPose
        );
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getxRot() {
        return xRot;
    }

    public void setxRot(float xRot) {
        this.xRot = xRot;
    }

    public float getyRot() {
        return yRot;
    }

    public void setyRot(float yRot) {
        this.yRot = yRot;
    }

    public void setyHeadRot(byte yHeadRot) {
        this.yHeadRot = yHeadRot;
    }

    public Pose getPose() {
        return pose;
    }
}
