package fr.loudo.parkourGhost.recording;

public class MovementData {

    private double x;
    private double y;
    private double z;

    private float xRot;
    private float yRot;
    private float yHeadRot;
    private float yBodyRot;

    public MovementData(double x, double y, double z, float xRot, float yRot, float yHeadRot, float yBodyRot) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xRot = xRot;
        this.yRot = yRot;
        this.yHeadRot = yHeadRot;
        this.yBodyRot = yBodyRot;
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

    public float getyHeadRot() {
        return yHeadRot;
    }

    public void setyHeadRot(float yHeadRot) {
        this.yHeadRot = yHeadRot;
    }

    public float getyBodyRot() {
        return yBodyRot;
    }

    public void setyBodyRot(float yBodyRot) {
        this.yBodyRot = yBodyRot;
    }
}
