package com.xxdmogxx.engine3d.datatypes;

public class Loc extends Vector {

    public Loc(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Loc() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    public Loc copy() {
        return new Loc(x, y, z);
    }

    public float dotProduct(Vector v) {
        return x * v.getX() + y * v.getY() + z * v.getZ();
    }

    public void add(float n) {
        x += n;
        y += n;
        z += n;
    }

    public void add(Vector v) {
        x += v.getX();
        y += v.getY();
        z += v.getZ();
    }

    public void subtract(float n) {
        x -= n;
        y -= n;
        z -= n;
    }

    public void subtract(Vector v) {
        x -= v.getX();
        y -= v.getY();
        z -= v.getZ();
    }

    public void multiply(float n) {
        x *= n;
        y *= n;
        z *= n;
    }

    public void multiply(Vector v) {
        x *= v.getX();
        y *= v.getY();
        z *= v.getZ();
    }

    public void multiply(double[][] m) {
        float ox = x;
        float oy = y;
        float oz = z;
        x = (float) (ox * m[0][0] + oy * m[1][0] + oz * m[2][0] + m[3][0]);
        y = (float) (ox * m[0][1] + oy * m[1][1] + oz * m[2][1] + m[3][1]);
        z = (float) (ox * m[0][2] + oy * m[1][2] + oz * m[2][2] + m[3][2]);
        float w = (float) (x * m[0][3] + y * m[1][3] + z * m[2][3] + m[3][3]);
        if (w != 0) {
            x /= w;
            y /= w;
            z /= w;
        }
    }

    public Loc crossProduct(Loc line) {
        float tx = y * line.getZ() - z * line.getY();
        float ty = z * line.getX() - x * line.getZ();
        float tz = x * line.getY() - y * line.getX();
        return new Loc(tx, ty, tz);
    }
}
