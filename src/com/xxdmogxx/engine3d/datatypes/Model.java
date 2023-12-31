package com.xxdmogxx.engine3d.datatypes;

import java.util.ArrayList;

public class Model {
    public Loc loc;
    public Rot rot;
    public Vector scale;
    public int[] color;
    public ArrayList<Loc[]> mesh;
    public Model(Loc loc, Rot rot, Vector scale, int[] color, ArrayList<Loc[]> mesh) {
        this.loc = loc;
        this.rot = rot;
        this.scale = scale;
        this.color = color;
        this.mesh = mesh;
    }

    public void rotateAll(float amount) {
        rot.add(amount);
    }
}

