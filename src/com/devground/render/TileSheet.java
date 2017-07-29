package com.devground.render;

import org.joml.Matrix4f;

public class TileSheet {
    private Texture texture;

    private Matrix4f scale;
    private Matrix4f translation;

    private int tilesCount;

    public TileSheet(String filename, int tilesCount) {
        this.texture = new Texture(filename);

        this.scale = new Matrix4f().scale(1.0f / (float) tilesCount);
        this.translation = new Matrix4f();
        this.tilesCount = tilesCount;
    }

    public void bindTile(Shader shader, int x, int y) {
        scale.translate(x, y, 0, translation);

        shader.setUniformVariable("sampler", 0);
        shader.setUniformVariable("texModifier", translation);
        texture.bind(0);
    }

    public void bindTile(Shader shader, int id) {
        bindTile(shader, id % tilesCount, id / tilesCount);
    }
}
