package com.xxdmogxx.engine3d.config;

import com.xxdmogxx.engine3d.Main;
import com.xxdmogxx.engine3d.render.Canvas;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.inspector.TagInspector;

import java.awt.event.*;
import java.io.InputStream;

public class Controls {
    static ControlSettings settings;
    private static int mask[] = {0, 1024, 2048, 4096};
    private static int lastPanX = 0;
    private static int lastPanY = 0;
    private static int lastRotateX = 0;
    private static int lastRotateY = 0;

    public Controls() {
        loadSettings();
    }

    private void loadSettings() {
        var loaderoptions = new LoaderOptions();
        TagInspector taginspector =
                tag -> tag.getClassName().equals(ControlSettings.class.getName());
        loaderoptions.setTagInspector(taginspector);
        Yaml yaml = new Yaml(new Constructor(ControlSettings.class, loaderoptions));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("com/xxdmogxx/engine3d/config/ControlSettings.yaml");
        settings = yaml.load(inputStream);
    }

    public static void addListeners(Canvas panel) {
        // Zooms the model when the middle wheel is scrolled (uses a lambda)
        MouseWheelListener wheelListener = e -> {
            float amount = e.getWheelRotation() * e.getScrollAmount() * settings.zoomSensitivity;
            Main.camera.zoom(amount);
        };
        panel.addMouseWheelListener(wheelListener);

        // Does various things when mouse moves
        MouseMotionListener dragListener = new MouseMotionListener() {
            // Does various things when mouse buttons are dragged
            @Override
            public void mouseDragged(MouseEvent e) {
                int button = e.getModifiersEx();
                if (settings.mouse) {
                    if (button == mask[settings.panButtonMouse]) {
                        Main.camera.pan(calcPanX(e), calcPanY(e));
                    } else if (button == mask[settings.rotateButtonMouse]) {
                        Main.camera.rotate(calcRotateX(e), calcRotateY(e));
                    }
                } else if (settings.touchpad) {
                    if (button == mask[settings.panButtonTouchpad]) {
                        Main.camera.pan(calcPanX(e), calcPanY(e));
                    } else if (button == mask[settings.rotateButtonTouchpad]) {
                        Main.camera.rotate(calcRotateX(e), calcRotateY(e));
                    }
                } else if (settings.touchscreen) {
                    if (button == mask[settings.panButtonTouchscreen]) {
                        Main.camera.pan(calcPanX(e), calcPanY(e));
                    } else if (button == mask[settings.rotateButtonTouchscreen]) {
                        Main.camera.rotate(calcRotateX(e), calcRotateY(e));
                    }
                }
            }

            // Unused
            @Override
            public void mouseMoved(MouseEvent e) {}
        };
        panel.addMouseMotionListener(dragListener);

        // Does various things when a mouse button is pressed or released
        MouseListener clickListener = new MouseListener() {
            // Unused
            @Override
            public void mouseClicked(MouseEvent e) {}

            // Unused
            @Override
            public void mousePressed(MouseEvent e) {}

            // Resets position values when corresponding button is released to prevent camera jumping
            @Override
            public void mouseReleased(MouseEvent e) {
                int button = e.getButton();
                if (settings.mouse) {
                    if (button == settings.panButtonMouse) {
                        lastPanX = 0;
                        lastPanY = 0;
                    } else if (button == settings.rotateButtonMouse) {
                        lastRotateX = 0;
                        lastRotateY = 0;
                    }
                } else if (settings.touchpad) {
                    if (button == settings.panButtonTouchpad) {
                        lastPanX = 0;
                        lastPanY = 0;
                    } else if (button == settings.rotateButtonTouchpad) {
                        lastRotateX = 0;
                        lastRotateY = 0;
                    }
                } else if (settings.touchscreen) {
                    if (button == settings.panButtonTouchscreen) {
                        lastPanX = 0;
                        lastPanY = 0;
                    } else if (button == settings.rotateButtonTouchscreen) {
                        lastRotateX = 0;
                        lastRotateY = 0;
                    }
                }
            }

            // Unused
            @Override
            public void mouseEntered(MouseEvent e) {}

            // Unused
            @Override
            public void mouseExited(MouseEvent e) {}
        };
        panel.addMouseListener(clickListener);
    }

    private static float calcPanX(MouseEvent e) {
        int x = e.getX();
        int xdiff = 0;
        if (lastPanX != 0) { xdiff = x - lastPanX; }
        lastPanX = x;
        return -xdiff * settings.panSensitivity;
    }

    private static float calcPanY(MouseEvent e) {
        int y = e.getY();
        int ydiff = 0;
        if (lastPanY != 0) { ydiff = y - lastPanY; }
        lastPanY = y;
        return -ydiff * settings.panSensitivity;
    }

    private static float calcRotateX(MouseEvent e) {
        int x = e.getY();
        int xdiff = 0;
        if (lastRotateX != 0) { xdiff = x - lastRotateX; }
        lastRotateX = x;
        return xdiff * settings.rotateSensitivity;
    }

    private static float calcRotateY(MouseEvent e) {
        int y = e.getX();
        int ydiff = 0;
        if (lastRotateY != 0) { ydiff = y - lastRotateY; }
        lastRotateY = y;
        return -ydiff * settings.rotateSensitivity;
    }

    public float getZoomSensitivity() { return settings.zoomSensitivity; }
    public float getPanSensitivity() { return settings.panSensitivity; }
    public float getRotateSensitivity() { return settings.rotateSensitivity; }
}
