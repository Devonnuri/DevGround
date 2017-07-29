package com.devground.gui;

import com.devground.collision.CollisionBox;
import com.devground.render.Camera;
import com.devground.render.Shader;
import com.devground.render.TileSheet;
import com.devground.utils.Utils;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Button {
    private CollisionBox collisionBox;
    private State state;
    private NineSlice nineSlice;

    private static Matrix4f transform = new Matrix4f();

    public Button(Vector2f position, Vector2f scale) {
        this.collisionBox = new CollisionBox(position, scale);
    }

    public void render(Camera camera, TileSheet tileSheet, Shader shader) {
        this.nineSlice = new NineSlice(camera, tileSheet, new int[][]{
                {0, 0}, {1, 0}, {2, 0}, {0, 1}, {1, 1}, {2, 1}, {0, 2}, {1, 2}, {2, 2}
        }, shader, transform);

//        this.nineSlice = new NineSlice(camera, tileSheet, new int[][]{
//                {0, 0}, {1, 0}, {2, 0}, {0, 1}, {1, 1}, {2, 1}, {0, 2}, {1, 2}, {2, 2}
//        }, shader, transform);

        this.nineSlice.render(collisionBox.getCenter(), collisionBox.getHalfExtent());
    }

    public enum State {
        NONE, SELECTED, CLICKED
    }
}
