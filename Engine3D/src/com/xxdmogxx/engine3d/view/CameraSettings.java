package com.xxdmogxx.engine3d.view;

public class CameraSettings {
    // Target frames per second
    public int FPS;
    // Target updates per second
    public int UPS;
    // Draws a black line along the edges of the models
    public boolean wireframe;
    // Fills in and shades the faces of the models
    public boolean filled;
    // Distance to the nearest plane that's rendered
    public float near;
    // Distance to the furthest plane that's rendered
    public float far;
    // The angle between the edges of the view plane
    public int fov;
}
