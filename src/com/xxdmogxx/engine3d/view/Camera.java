package com.xxdmogxx.engine3d.view;

import com.xxdmogxx.engine3d.Main;
import com.xxdmogxx.engine3d.datatypes.Model;
import com.xxdmogxx.engine3d.datatypes.Vector;
import com.xxdmogxx.engine3d.render.Window;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.inspector.TagInspector;

import java.io.InputStream;

public class Camera {
    private CameraSettings settings;
    // Ratio between the height and the width of the screen
    private float aspectRatio;
    private float fovRad;
    // The position of the camera in space
    private Vector pos;
    // The matrix responsible for adding perspective to the models
    // While I suggest reading up on or watching a video about how these are derived,
    // you can basically just think of this as a black box
    private double[][] projectionMatrix;

    public Camera() {
        loadSettings();
        pos = new Vector(0.0f, 0.0f, 0.0f);
        calcAspectRatio();
        calcFovRad();
        calcProjectionMatrix();
    }

    private void loadSettings() {
        var loaderoptions = new LoaderOptions();
        TagInspector taginspector =
                tag -> tag.getClassName().equals(CameraSettings.class.getName());
        loaderoptions.setTagInspector(taginspector);
        Yaml yaml = new Yaml(new Constructor(CameraSettings.class, loaderoptions));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("com/xxdmogxx/engine3d/view/CameraSettings.yaml");
        settings = yaml.load(inputStream);
    }

    // Moves the model closer or further to the camera
    public void zoom(float amount) {
        for (Model m : Main.objectList) {
            m.loc.add(new Vector(0.0f, 0.0f, amount));
        }
        Main.updateNeeded = true;
    }

    // Translates the model around in relation to the view plane
    public void pan(float xdiff, float ydiff) {
        for (Model m : Main.objectList) {
            m.loc.add(new Vector(xdiff, ydiff, 0.0f));
        }
        Main.updateNeeded = true;
    }

    // Rotates the model to allow turning side-to-side and looking above and below (tilt is prevented)
    public void rotate(float xdiff, float ydiff) {
        for (Model m : Main.objectList) {
            m.rot.add(xdiff, ydiff, 0);
        }
        Main.updateNeeded = true;
    }

    private void calcAspectRatio() {
        aspectRatio = (float) Window.height / (float)Window.width;
    }

    private void calcFovRad() {
        fovRad = (float)(1 / Math.tan(settings.fov * 0.5 / 180 * Math.PI));
    }

    private void calcProjectionMatrix() {
        projectionMatrix = new double[][] {
                {aspectRatio * fovRad, 0, 0, 0},
                {0, fovRad, 0, 0},
                {0, 0, settings.far / (settings.far - settings.near),
                        (-settings.far * settings.near) / (settings.far - settings.near)},
                {0, 0, -1.0, 0}};
    }

    public void setFPS(int FPS) { settings.FPS = FPS; }
    public void setUPS(int UPS) { settings.UPS = UPS; }
    public void setWireframe(boolean wireframeStatus) { settings.wireframe = wireframeStatus; }
    public void setFilled(boolean filledStatus) { settings.filled = filledStatus; }
    public void setNear(float near) {
        settings.near = near;
        calcProjectionMatrix();
    }
    public void setFar(float far) {
        settings.far = far;
        calcProjectionMatrix();
    }
    public void setFov(int fov) {
        settings.fov = fov;
        calcFovRad();
        calcProjectionMatrix();
    }

    public int getFPS() { return settings.FPS; }
    public int getUPS() { return settings.UPS; }
    public boolean getWireframe() { return settings.wireframe; }
    public boolean getFilled() { return settings.filled; }
    public float getNear() { return settings.near; }
    public float getFar() { return settings.far; }
    public int getFov() { return settings.fov; }
    public Vector getPos() { return pos; }
    public double[][] getProjectionMatrix() { return projectionMatrix; }
}
