package com.devground.world;

import com.devground.render.Camera;
import com.devground.render.Model;
import com.devground.render.Shader;
import com.devground.render.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashMap;

public class TileRenderer {
    private HashMap<Tile, Texture> texture;
    private Model model;

    public TileRenderer(World world) {
        texture = new HashMap<>();

        float[] vertCoord = new float[] {
                -1f, 1f, 0,     // 왼쪽 위
                1f, 1f, 0,      // 오른쪽 위
                1f, -1f, 0,     // 왼쪽 아래
                -1f, -1f, 0     // 오른쪽 아래
        };

        float[] texCoord = new float[] {
                0, 0,
                1, 0,
                1, 1,
                0, 1
        };

        int[] indices = new int[] {
                0, 1, 2,
                2, 3, 0
        };

        model = new Model(vertCoord, texCoord, indices);

        for(Tile material : Tile.values()) {
            texture.put(material, new Texture(material.getName()+".png"));
        }
    }

    public void render(Tile tile, int x, int y, Shader shader, Matrix4f matrix, Camera camera) {
        shader.bind();

        if(texture.containsKey(tile)) {    // 텍스쳐가 있으면
            texture.get(tile).bind(0);
        }

        Matrix4f position = new Matrix4f().translate(new Vector3f(x*2, y*2, 0));
        Matrix4f target = new Matrix4f();

        camera.getProjection().mul(matrix, target);
        target.mul(position);

        shader.setUniformVariable("sampler", 0);
        shader.setUniformVariable("projection", target);

        model.render();
    }

}
