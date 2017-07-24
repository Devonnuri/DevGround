package com.devground.entity;

import com.devground.Transform;
import com.devground.Window;
import com.devground.collision.Collision;
import com.devground.collision.CollisionBox;
import com.devground.render.*;
import com.devground.world.World;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Entity {
    private static Model model;
    //private Texture texture;
    private Animation texture;
    private Transform transform;
    private World world;

    private CollisionBox collisionBox;

    public Entity(World world, Animation animation, Transform transform) {
        this.world = world;
        this.texture = animation;

        this.transform = transform;
        this.transform.scale = new Vector3f(world.getScale(), world.getScale(),1);

        this.collisionBox = new CollisionBox(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x, transform.scale.y));
    }

    public void update(double FPS, Window window, Camera camera) {
        float delta = 1/(float) FPS;
        if(window.getInput().isKeyDown(GLFW.GLFW_KEY_A))
            transform.pos.add(new Vector3f(-10*delta, 0, 0));

        if(window.getInput().isKeyDown(GLFW.GLFW_KEY_D))
            transform.pos.add(new Vector3f(10*delta, 0, 0));


        if(window.getInput().isKeyDown(GLFW.GLFW_KEY_W))
            transform.pos.add(new Vector3f(0, 10*delta, 0));

        if(window.getInput().isKeyDown(GLFW.GLFW_KEY_S))
            transform.pos.add(new Vector3f(0, -10*delta, 0));

        collisionBox.getCenter().set(transform.pos.x, transform.pos.y);

        CollisionBox[] boxes = new CollisionBox[25];
        for(int y=0; y<5; y++) {
            for(int x=0; x<5; x++) {
                int posX = (int) transform.pos.x/2 + x;
                int posY = (int) -transform.pos.y/2 + y;

                if(posX >= world.width || posY >= world.height || posX < 0 || posY < 0) continue;

                boxes[y*5+x] = world.getCollisionBox(posX, posY);
            }
        }

        CollisionBox box = null;
        for(CollisionBox b : boxes) {
            if(b == null) continue;

            if(box == null) box = b;

            Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
            Vector2f length2 = b.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

            if(length1.lengthSquared() > length2.lengthSquared()) {
                box = b;
            }
        }

        if(box != null) {
            Collision data = collisionBox.getCollision(box);
            if(data.isCollide) {
                collisionBox.correctPosition(box, data);
                transform.pos.set(collisionBox.getCenter(), 0);
            }

            for(CollisionBox b : boxes) {
                if(b == null) continue;
                Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                Vector2f length2 = b.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

                if(length1.lengthSquared() > length2.lengthSquared()) {
                    box = b;
                }
            }

            data = collisionBox.getCollision(box);
            if (data.isCollide) {
                collisionBox.correctPosition(box, data);
                transform.pos.set(collisionBox.getCenter(), 0);
            }
        }

        camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.05f);
    }

    public void render(Shader shader, Camera camera) {
        Matrix4f target = camera.getProjection();
        target.mul(world.getWorldMatrix());

        shader.bind();
        shader.setUniformVariable("sampler", 0);
        shader.setUniformVariable("projection", transform.getProjection(target));
        texture.bind(0);
        model.render();
    }

    public static void initModel() {
        float[] vertices = new float[] {
                -1f, 1f, 0, //TOP LEFT     0
                1f, 1f, 0,  //TOP RIGHT    1
                1f, -1f, 0, //BOTTOM RIGHT 2
                -1f, -1f, 0,//BOTTOM LEFT  3
        };

        float[] texture = new float[] {
                0,0,
                1,0,
                1,1,
                0,1,
        };

        int[] indices = new int[] {
                0,1,2,
                2,3,0
        };

        model = new Model(vertices, texture, indices);
    }

    public static void removeModel() {
        model = null;
    }
}
