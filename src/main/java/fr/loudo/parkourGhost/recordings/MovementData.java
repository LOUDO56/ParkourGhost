package fr.loudo.parkourGhost.recordings;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MovementData {

    private double x, y, z;
    private float xRot, yRot;
    private byte yHeadRot;

    public MovementData(double x, double y, double z, float xRot, float yRot, byte yHeadRot) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xRot = xRot;
        this.yRot = yRot;
        this.yHeadRot = yHeadRot;
    }

    public static MovementData getMovementDataFromPlayer(Player player) {
        Location pPosition = player.getLocation();
        float xRot = pPosition.getPitch();
        float yRot = pPosition.getYaw();

        return new MovementData(
                pPosition.getX(),
                pPosition.getY(),
                pPosition.getZ(),
                xRot,
                yRot,
                (byte) (yRot * 256.0F / 360.0F)
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

    public byte getyHeadRot() {
        return yHeadRot;
    }

    public void setyHeadRot(byte yHeadRot) {
        this.yHeadRot = yHeadRot;
    }
}
