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

public abstract class Entity {
    private static Model model;
    private Animation[] animations;
    private int currentAnimation;
    protected Transform transform;
    protected World world;

    private CollisionBox collisionBox;

    public Entity(World world, int maxAnimation, Transform transform) {
        this.world = world;

        this.animations = new Animation[maxAnimation];
        this.currentAnimation = 0;

        this.transform = transform;
        this.transform.scale = new Vector3f(1, 1, 1);

        this.collisionBox = new CollisionBox(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x, transform.scale.y));
    }

    public abstract void update(double fps, Window window, Camera camera);

    public void render(World world, Shader shader, Camera camera) {
        this.world = world;

        shader.bind();
        shader.setUniformVariable("sampler", 0);
        shader.setUniformVariable("projection", transform.getProjection(camera.getProjection().mul(world.getWorldMatrix(), new Matrix4f())));
        animations[currentAnimation].bind(0);
        model.render();
    }

    public void addPosition(Vector2f amount) {
        transform.pos.add(new Vector3f(amount, 0));
        collisionBox.getCenter().set(transform.pos.x, transform.pos.y);
    }

    public void setPosition(Vector2f coord) {
        transform.pos.set(coord, 0);
        collisionBox.getCenter().set(transform.pos.x, transform.pos.y);
    }

    protected void initAnimation(int index, Animation animation) {
        animations[index] = animation;
    }

    public void useAnimation(int index) {
        this.currentAnimation = index;
    }

    public void correctPosition(World world) {
        CollisionBox[] boxes = new CollisionBox[25];
        for(int y=0; y<5; y++) {
            for(int x=0; x<5; x++) {
                int posX = (int) transform.pos.x/2 + x;
                int posY = (int) -transform.pos.y/2 + y;

                if(posY >= world.width || posX >= world.height || posX < 0 || posY < 0) continue;

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

        if(transform.pos.x < 0) transform.pos.x = 0;
        if(transform.pos.y > 0) transform.pos.y = 0;
        if(transform.pos.x > world.width*2-2) transform.pos.x = world.width*2-2;
        if(transform.pos.y < -world.width*2+2) transform.pos.y = -world.width*2+2;
    }

    public void collideWithEntity(Entity entity) {
        Collision collision = collisionBox.getCollision(entity.collisionBox);

        if(collision.isCollide) {
            collision.distance.x /= 2;
            collision.distance.y /= 2;

            collisionBox.correctPosition(entity.collisionBox, collision);
            transform.pos.set(collisionBox.getCenter().x, collisionBox.getCenter().y, 0);

            entity.collisionBox.correctPosition(collisionBox, collision);
            entity.transform.pos.set(entity.collisionBox.getCenter().x, entity.collisionBox.getCenter().y, 0);
        }
    }

    public static void initModel() {
        float[] vertices = new float[] {
                -1f, 1f, 0,     //TOP LEFT     0
                1f, 1f, 0,      //TOP RIGHT    1
                1f, -1f, 0,     //BOTTOM RIGHT 2
                -1f, -1f, 0,    //BOTTOM LEFT  3
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
