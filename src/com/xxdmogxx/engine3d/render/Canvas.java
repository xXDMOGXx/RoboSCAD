package com.xxdmogxx.engine3d.render;

import com.xxdmogxx.engine3d.Main;
import com.xxdmogxx.engine3d.datatypes.Triangle;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Canvas extends JPanel {
    public Canvas() {
        initUI();
    }
    private void initUI() {}

    // Overrides the rendering of the panel
    @Override
    public void paintComponent(Graphics g) {
        // Required line
        super.paintComponent(g);
        // Creates a copy of the current queued triangles in main to prevent concurrent access
        ArrayList<Triangle> renderingTriangles = new ArrayList<>(Main.trianglesToDraw);
        // Goes through each triangle and draws it
        for (Triangle t : renderingTriangles) {
            // Doesn't draw the triangle if it doesn't exist
            if (t != null) {
                // Puts the points into a form that the paint draw functions accept
                int[] xPoints = new int[]{(int) t.points[0].getX(), (int) t.points[1].getX(), (int) t.points[2].getX()};
                int[] yPoints = new int[]{(int) t.points[0].getY(), (int) t.points[1].getY(), (int) t.points[2].getY()};
                // Fills in the triangle with it's shaded color
                if (Main.camera.getFilled()) {
                    g.setColor(Color.decode(t.hex));
                    g.fillPolygon(xPoints, yPoints, 3);
                }
                // When wireframe is on, draws a black outline around the triangle
                if (Main.camera.getWireframe()) {
                    g.setColor(Color.BLACK);
                    g.drawPolygon(xPoints, yPoints, 3);
                }
            }
        }
        // Lets main know that rendering is done
        Main.renderReady = false;
    }
}
