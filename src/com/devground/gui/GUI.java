package com.devground.gui;

import com.devground.Window;
import com.devground.render.Camera;
import com.devground.render.Shader;
import com.devground.render.TileSheet;
import com.devground.utils.Utils;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class GUI {
    private Shader shader;
    private Camera camera;
    private TileSheet tileSheet;

    private Button b1;

    public GUI(Window window) {
        shader = new Shader("gui");
        camera = new Camera(window.getWidth(), window.getHeight());
        tileSheet = new TileSheet("gui.png", 9);

        b1 = new Button(new Vector2f(0, 0), new Vector2f(48*2, 16*2));
    }

    public void correctCammera(Window window) {
        camera.setProjection(window.getWidth(), window.getHeight());
    }

    public void render() {
        shader.bind();
        b1.render(camera, tileSheet, shader);
    }
}
