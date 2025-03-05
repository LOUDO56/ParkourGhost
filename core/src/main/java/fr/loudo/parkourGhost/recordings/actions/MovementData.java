package fr.loudo.parkourGhost.recordings.actions;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MovementData {

    private double x, y, z;
    private float xRot, yRot;
    private byte headYRot;

    public MovementData(double x, double y, double z, float xRot, float yRot, byte headYRot) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xRot = xRot;
        this.yRot = yRot;
        this.headYRot = headYRot;
    }

    public static MovementData getMovementDataFromPlayer(Player player) {
        Location pPosition = player.getLocation();
        return new MovementData(
                pPosition.getX(),
                pPosition.getY(),
                pPosition.getZ(),
                pPosition.getPitch(),
                pPosition.getYaw(),
                (byte) (pPosition.getYaw() * 256.0F / 360.0F)
        );
    }



    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getxRot() {
        return xRot;
    }

    public float getyRot() {
        return yRot;
    }

    public byte getHeadYRot() {
        return headYRot;
    }
}
