package com.xxdmogxx.engine3d.render;

import com.xxdmogxx.engine3d.config.Controls;

import javax.swing.*;
import java.awt.Dimension;

public class Window {
    // The JFrame is the base container for the window contents
    private static final JFrame frame = new JFrame("RoboSCAD");
    // The canvas is a JPanel that I can draw on
    private static Canvas canvas;
    public static int width = 960;
    public static int height = 540;

    public static void createWindow() {
        // Creates the canvas. This will be the main view we draw on
        canvas = new Canvas();
        // Gives the canvas a size. The frame will follow this size
        canvas.setPreferredSize(new Dimension(width, height));
        // Makes the frame fullscreen
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Puts the canvas into the frame
        frame.add(canvas);
        // Compacts the frame to the size of the canvas
        frame.pack();
        // Resizing the JFrame wouldn't resize the canvas at the moment, so I'm disabling it
        frame.setResizable(false);
        // The default for the x button in the top right isn't to close the window without this.... yeah
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Moves the frame to the center of the screen
        frame.setLocationRelativeTo(null);
        // Adds mouse listeners to capture mouse/touchpad/touchscreen input
        Controls.addListeners(canvas);
        // Finally shows the frame to the user
        frame.setVisible(true);
    }

    // Notifies the canvas to queue a redraw
    public static void render() {
        if (canvas != null) {
            canvas.repaint();
        }
    }

    // Deletes the frame (and everything in it)
    public static void closeWindow() {
        frame.dispose();
    }
}
