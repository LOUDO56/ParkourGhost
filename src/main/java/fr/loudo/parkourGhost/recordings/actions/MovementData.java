package fr.loudo.parkourGhost.recordings.actions;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MovementData {

    private double x, y, z;
    private float xRot, yRot;

    public MovementData(double x, double y, double z, float xRot, float yRot) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xRot = xRot;
        this.yRot = yRot;
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
                yRot
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

    public double getZ() {
        return z;
    }

    public float getxRot() {
        return xRot;
    }

    public float getyRot() {
        return yRot;
    }

}
