package engine3d.datatypes;

public class Vector {
    protected float x;
    protected float y;
    protected float z;

    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vector() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    public void setX(float value) {x = value;}
    public void setY(float value) {y = value;}
    public void setZ(float value) {z = value;}

    public float getX() {return this.x;}
    public float getY() {return this.y;}
    public float getZ() {return this.z;}

    public Vector copy() {
        return new Vector(x, y, z);
    }

    public void normalize() {
        double m = Math.sqrt(x*x + y*y + z*z);
        x /= m;
        y /= m;
        z /= m;
    }
}

