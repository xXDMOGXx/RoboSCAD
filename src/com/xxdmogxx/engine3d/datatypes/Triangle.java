package com.xxdmogxx.engine3d.datatypes;

public class Triangle {
    public Loc[] points;
    public String hex;
    public float midpoint;
    public Triangle(Loc[] points, String hex) {
        this.points = points;
        this.hex = hex;
        midpoint = (points[0].getZ() + points[1].getZ() + points[2].getZ()) / 3.0f;
    }
}
