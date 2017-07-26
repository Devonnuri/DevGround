package com.devground.gui;

import com.devground.render.Camera;
import com.devground.render.Shader;
import com.devground.utils.Utils;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class GUI {
    private Shader shader;

    public GUI() {
        shader = new Shader("gui");
    }

    public void render(Camera camera) {
        Matrix4f mat = new Matrix4f();
        camera.getRawProjection().scale(87, mat);

        shader.bind();
        shader.setUniformVariable("projection", mat);
        shader.setUniformVariable("color", new Vector4f(0, 0, 1, 1f));
        Utils.getModel().render();
    }
}
