package com.xxdmogxx.engine3d.datatypes;

public class Rot extends Vector {

    public Rot(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        wrap();
    }

    Rot() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    public Rot copy() {
        return new Rot(x, y, z);
    }

    public void setX(float value) {
        x = value;
        wrapX();
    }

    public void setY(float value) {
        y = value;
        wrapY();
    }

    public void setZ(float value) {
        z = value;
        wrapZ();
    }

    private void wrap() {
        wrapX();
        wrapY();
        wrapZ();
    }

    private void wrapX() {
        if (x >= 360) {
            x = x % 360;
        } else if (x < 0) {
            x = 360 + x % -360;
        }
    }

    private void wrapY() {
        if (y >= 360) {
            y = y % 360;
        } else if (y < 0) {
            y = 360 + y % -360;
        }
    }

    private void wrapZ() {
        if (z >= 360) {
            z = z % 360;
        } else if (z < 0) {
            z = 360 + z % -360;
        }
    }

    public double toRadians(float angle) {
        return angle * (Math.PI / 180);
    }

    public void add(float n) {
        x += n;
        y += n;
        z += n;
        wrap();
    }

    public void add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        wrap();
    }

    public void subtract(float n) {
        x -= n;
        y -= n;
        z -= n;
        wrap();
    }

    public void subtract(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        wrap();
    }

    public double[][] matRotX() {
        double rad = toRadians(x);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new double[][]{
                {1, 0, 0, 0},
                {0, cos, -sin, 0},
                {0, sin, cos, 0},
                {0, 0, 0, 1}};
    }

    public double[][] matRotY() {
        double rad = toRadians(y);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new double[][]{
                {cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}};
    }

    public double[][] matRotZ() {
        double rad = toRadians(z);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new double[][]{
                {cos, -sin, 0, 0},
                {sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};
    }
}
