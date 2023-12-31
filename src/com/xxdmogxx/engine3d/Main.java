package com.xxdmogxx.engine3d;

import com.xxdmogxx.engine3d.config.Controls;
import com.xxdmogxx.engine3d.datatypes.Loc;
import com.xxdmogxx.engine3d.datatypes.Model;
import com.xxdmogxx.engine3d.datatypes.Rot;
import com.xxdmogxx.engine3d.datatypes.Triangle;
import com.xxdmogxx.engine3d.datatypes.Vector;
import com.xxdmogxx.engine3d.file.Obj;
import com.xxdmogxx.engine3d.render.Window;
import com.xxdmogxx.engine3d.view.Camera;

import java.util.*;
import javax.swing.SwingUtilities;
import java.util.ArrayList;

// TODO: Differentiate between mouse, touchpad, and touchscreen on the fly to apply correct control
// TODO: Add buttons on canvas to control existing functionality
// TODO: Add clipping and depth buffer
// TODO: Add functionality for exporting models via OpenSCAD command line
// TODO: Either allow native reading of STL files or implement STL to OBJ conversion
// TODO: Improve file structure and project object layout

public class Main implements Runnable {
    public static Camera camera;
    public static Controls controls;

    // Variable declarations
    public static final ArrayList<Model> objectList = new ArrayList<>();
    public static ArrayList<Triangle> trianglesToDraw;
    private static ArrayList<Triangle> updatingTriangles;
    private volatile Status status;
    private final double timeU, timeF;
    private long initialTime, timer;
    private int frames, ticks;
    public static double deltaTime;
    public static boolean updateNeeded, updateReady, renderNeeded, renderReady;

    public Main() {
        status = Status.STOPPED;
        camera = new Camera();
        controls = new Controls();
        trianglesToDraw = new ArrayList<>();
        updatingTriangles = new ArrayList<>();
        initialTime = System.nanoTime();
        timeU = 1000000000 / (float)camera.getUPS();
        timeF = 1000000000 / (float)camera.getFPS();
        frames = 0;
        ticks = 0;
        timer = System.currentTimeMillis();
        updateNeeded = true;
        renderNeeded = true;
        updateReady = false;
        renderReady = false;

        // Default values
        Loc dLoc = new Loc(0.0f, 0.0f, 0.0f);
        Rot dRot = new Rot(0.0f, 0.0f, 0.0f);
        Loc dScale = new Loc(1.0f, 1.0f, 1.0f);
        int[] dColor = new int[]{220, 220, 220};

        // Creates a model object using a .obj loaded from a file
        Model chassis = new Model(new Loc(0.0f, 0.0f, 500.0f),
                dRot, dScale, dColor, Obj.loadObjFile("src/com/xxdmogxx/engine3d/resources/chassis.obj"));

        //Model assembly = new Model(new Loc(0.0f, 0.0f, 500.0f),
        //        dRot, dScale, dColor, Obj.loadObjFile("src/com/xxdmogxx/engine3d/resources/assembly.obj"));

        //Model axis = new Model(new Loc(0.0f, 0.0f, 50.0f),
        //        dRot, dScale, dColor, loadObjFile("src/com/xxdmogxx/engine3d/resources/axis.obj"));

        //Model teapot = new Model(new Loc(0.0f, 0.0f, 100.0f),
        //        dRot, dScale, dColor, loadObjFile("src/com/xxdmogxx/engine3d/resources/teapot.obj"));

        // The objects that are created in the world and drawn
        objectList.add(chassis);
    }

    @Override
    public void run() {
        status = Status.RUNNING;
        // Shows the GUI Window
        SwingUtilities.invokeLater(Window::createWindow);

        // The thread that controls updates
        Thread update = new Thread(() -> {
            while (status == Status.RUNNING) {
                if (updateNeeded && updateReady) {
                    update();
                    updateReady = false;
                    updateNeeded = false;
                    renderNeeded = true;
                }
            }
        });
        // The thread that controls renderings
        Thread render = new Thread(() -> {
            while (status == Status.RUNNING) {
                if (renderNeeded && renderReady) {
                    Window.render();
                    renderNeeded = false;
                }
            }
        });

        // Starts both threads
        update.start();
        render.start();

        // Main thread handles the coordination of the update and render threads
        runLoop();
    }

    private void runLoop() {
        double deltaU = 0, deltaF = 0;
        while (status == Status.RUNNING) {
            long currentTime = System.nanoTime();
            if (updateNeeded) { deltaU += (currentTime - initialTime) / timeU; }
            if (renderNeeded) { deltaF += (currentTime - initialTime) / timeF; }
            initialTime = currentTime;

            // Whenever there are updates to calculate, and the update thread isn't busy, this begins an update
            if (deltaU >= 1 && !updateReady) {
                deltaTime = deltaU;
                updateReady = true;
                ticks++;
                deltaU--;
            }

            // Whenever there are renders to draw, and the render thread isn't busy, this begins a render
            if (deltaF >= 1 && !renderReady) {
                renderReady = true;
                frames++;
                deltaF--;
            }

            // Displays to the console the number of updates and renders in the last second
            if (System.currentTimeMillis() - timer > 1000) {
                System.out.printf("UPS: %s, FPS: %s%n", ticks, frames);
                frames = 0;
                ticks = 0;
                timer += 1000;
            }
        }
    }

    private void update() {
        // Calculates the triangle for every model
        for (Model currentObject : objectList) {
            calculateTriangles(currentObject);
        }
        // Sorts the triangles by furthest first (to draw closer triangles on top of further triangles
        updatingTriangles.sort(Comparator.comparingDouble(o -> o.midpoint));
        Collections.reverse(updatingTriangles);
        // Adds all the sorted triangles into a list to be rendered
        Main.trianglesToDraw.clear();
        trianglesToDraw.addAll(updatingTriangles);
        updatingTriangles.clear();
    }

    public void shutdown() {
        status = Status.STOPPED;
        Window.closeWindow();
    }

    static String rgbToHex(int[] rgb) {
        // Takes in an array holding rgb values and does fancy formatting to turn it into a color hex
        return String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
    }

    static void calculateTriangles(Model model) {
        // Doesn't draw the mesh if the mesh doesn't exist
        if (model.mesh != null) {
            // All the calculations below will be on individual triangles
            for (Loc[] otriangle : model.mesh) {
                // Creates copies of the points to allow calculations without modifying the originals
                Loc[] triangle = {otriangle[0].copy(), otriangle[1].copy(), otriangle[2].copy()};

                // Applies rotation, translation, and scale transformations to every point in the triangle
                for (Loc vertex : triangle) {
                    // Uses rotation matrices for rotating the points
                    vertex.multiply(model.rot.matRotZ());
                    vertex.multiply(model.rot.matRotY());
                    //vertex.multiply(new Vector(1, 1, -1));
                    vertex.multiply(model.rot.matRotX());
                    // Scales the point by the model's scale
                    vertex.multiply(model.scale);
                    // Translates the point to its location in space
                    vertex.add(model.loc);
                }

                Loc line1 = triangle[1].copy();
                line1.subtract(triangle[0]);

                Loc line2 = triangle[2].copy();
                line2.subtract(triangle[0]);
                // The cross product does some fancy math black magic to find a line that is
                // perpendicular to two other lines, which effectively calculates which direction
                // is outwards for the triangle (as long as vertices are defined in a consistent manner)
                Loc normal = line1.crossProduct(line2);

                // The normal is then... normalized to get a relevant value range
                normal.normalize();

                // Calculates a vector that points from the camera to the triangle
                Loc cameraDir = triangle[0].copy();
                cameraDir.subtract(camera.getPos());

                // Dot product calculates how similar two vectors' directions are
                // This makes the triangle only draw if it is facing towards the camera
                // This applies the projection to each point, making it have perspective
                // (Technically dot product is the projection of one vector against another, but don't
                // worry about that)
                if (normal.dotProduct(cameraDir) < 0) {
                    // The direction the light is coming from. Instead of a light source in space,
                    // this is more like a wall of light that illuminates the entire world from one
                    // direction. (Kinda like the Sun would if the Earth was flat)
                    Loc lightDir = new Loc(0.0f, 0.0f, -1.0f);
                    lightDir.normalize();

                    // Calculates how much light shines on a face based on how much it faces the light source
                    float lightAmount = normal.dotProduct(lightDir);

                    int[] rgb_lit;
                    // Modulates the rgb value to darken depending on how far it faces from the light source
                    if (lightAmount > 0) {
                        rgb_lit = new int[] {(int) (model.color[0] * lightAmount),
                                (int) (model.color[1] * lightAmount),
                                (int) (model.color[2] * lightAmount)};
                    } else {
                        // Anything facing perpendicular or away from the source becomes black
                        rgb_lit = new int[] {0, 0, 0};
                    }

                    // Adds the triangle to a list. Once all triangles are calculated, this list will be sorted
                    updatingTriangles.add(new Triangle(triangle, rgbToHex(rgb_lit)));

                    // Projection transforms the triangle from 3D coordinate space to 2D screen space
                    // The type of projection being used here is perspective (as opposed to orthogonal)
                    // This make objects farther away look smaller and move less compared to closer objects
                    for (Loc vertex : triangle) {
                        vertex.multiply(camera.getProjectionMatrix());
                    }

                    // Translates the middle of the model to the middle of the view
                    for (Loc vertex : triangle) {
                        // Projection puts the values into the range of -1 to 1. Adding 1 makes that range 0 to 2.
                        vertex.add(1);
                        // Dividing by two brings the range down to 0 to 1, and then that value is scaled
                        // by the corresponding Window dimension
                        Vector screenScale = new Vector(0.5f * Window.width, 0.5f * Window.height, 1.0f);
                        vertex.multiply(screenScale);
                    }
                }
            }
        }
    }

    //Creates an instance of the runnable... and runs it
    public static void main(String[] args) {
        Main program = new Main();
        program.run();
    }
}
