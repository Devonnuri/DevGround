package com.devground.gui;

import com.devground.render.Camera;
import com.devground.render.Shader;
import com.devground.render.TileSheet;
import com.devground.utils.Utils;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class NineSlice {
    private Camera camera;
    private TileSheet tileSheet;
    private int[][] tileSheetPos;
    private Shader shader;

    private Matrix4f transform;

    private static final int CENTER = 4, LEFT_SIDE = 3, RIGHT_SIDE = 5, TOP_SIDE = 1, BOTTOM_SIDE= 7, TOP_LEFT= 0, TOP_RIGHT= 2, BOTTOM_LEFT= 6, BOTTOM_RIGHT = 8;

    public NineSlice(Camera camera, TileSheet tileSheet, int[][] tileSheetPos, Shader shader, Matrix4f transform) {
        this.camera = camera;
        this.tileSheet = tileSheet;
        this.tileSheetPos = tileSheetPos;
        this.shader = shader;

        this.transform = transform;
    }

    public void render(Vector2f position, Vector2f scale) {
        transform.identity().translate(position.x, position.y, 0).scale(scale.x, scale.y, 1);
        shader.setUniformVariable("projection", camera.getProjection().mul(transform));

        // Render CENTER
        tileSheet.bindTile(shader, tileSheetPos[CENTER][0], tileSheetPos[CENTER][1]);
        Utils.getModel().render();

        renderSides(position, scale, camera, tileSheet, shader);
        renderCorners(position, scale, camera, tileSheet, shader);
    }

    public void renderSides(Vector2f position, Vector2f scale, Camera camera, TileSheet tileSheet, Shader shader) {
        transform.identity().translate(position.x, position.y + scale.y - 16, 0).scale(scale.x, 16, 1);     // Render TOP
        shader.setUniformVariable("projection", camera.getProjection().mul(transform));
        tileSheet.bindTile(shader, tileSheetPos[TOP_SIDE][0], tileSheetPos[TOP_SIDE][1]);
        Utils.getModel().render();

        transform.identity().translate(position.x, position.y - scale.y + 16, 0).scale(scale.x, 16, 1);     // Render BOTTOM
        shader.setUniformVariable("projection", camera.getProjection().mul(transform));
        tileSheet.bindTile(shader, tileSheetPos[BOTTOM_SIDE][0], tileSheetPos[BOTTOM_SIDE][1]);
        Utils.getModel().render();

        transform.identity().translate(position.x - scale.x + 16, position.y, 0).scale(16, scale.y, 1);     // Render LEFT
        shader.setUniformVariable("projection", camera.getProjection().mul(transform));
        tileSheet.bindTile(shader, tileSheetPos[LEFT_SIDE][0], tileSheetPos[LEFT_SIDE][1]);
        Utils.getModel().render();

        transform.identity().translate(position.x + scale.x - 16, position.y, 0).scale(16, scale.y, 1);     // Render RIGHT
        shader.setUniformVariable("projection", camera.getProjection().mul(transform));
        tileSheet.bindTile(shader, tileSheetPos[RIGHT_SIDE][0], tileSheetPos[RIGHT_SIDE][1]);
        Utils.getModel().render();
    }

    public void renderCorners(Vector2f position, Vector2f scale, Camera camera, TileSheet tileSheet, Shader shader) {
        transform.identity().translate(position.x - scale.x + 16, position.y + scale.y - 16, 0).scale(16, 16, 1);     // Render TOP LEFT
        shader.setUniformVariable("projection", camera.getProjection().mul(transform));
        tileSheet.bindTile(shader, tileSheetPos[TOP_LEFT][0], tileSheetPos[TOP_LEFT][1]);
        Utils.getModel().render();

        transform.identity().translate(position.x + scale.x - 16, position.y + scale.y - 16, 0).scale(16, 16, 1);     // Render TOP RIGHT
        shader.setUniformVariable("projection", camera.getProjection().mul(transform));
        tileSheet.bindTile(shader, tileSheetPos[TOP_RIGHT][0], tileSheetPos[TOP_RIGHT][1]);
        Utils.getModel().render();

        transform.identity().translate(position.x - scale.x + 16, position.y - scale.y + 16, 0).scale(16, 16, 1);     // Render BOTTOM LEFT
        shader.setUniformVariable("projection", camera.getProjection().mul(transform));
        tileSheet.bindTile(shader, tileSheetPos[BOTTOM_LEFT][0], tileSheetPos[BOTTOM_LEFT][1]);
        Utils.getModel().render();

        transform.identity().translate(position.x + scale.x - 16, position.y - scale.y + 16, 0).scale(16, 16, 1);     // Render BOTTOM RIGHT
        shader.setUniformVariable("projection", camera.getProjection().mul(transform));
        tileSheet.bindTile(shader, tileSheetPos[BOTTOM_RIGHT][0], tileSheetPos[BOTTOM_RIGHT][1]);
        Utils.getModel().render();
    }
}
