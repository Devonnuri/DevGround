package com.devground.entity;

import com.devground.Transform;
import com.devground.Window;
import com.devground.render.*;
import com.devground.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Player extends Entity {
    public static final int ANIM_IDLE = 0;
    public static final int ANIM_WALK = 1;
    public static final int ANIM_SIZE = 2;

    public Player(World world) {
        this(world, new Transform());
    }

    public Player(World world, Transform transform) {
        super(world, ANIM_SIZE, transform);
        initAnimation(ANIM_IDLE, new Animation(1, 10, "Pointer_Normal/idle"));
        initAnimation(ANIM_WALK, new Animation(15, 10, "Pointer_Normal/walk"));
    }

    @Override
    public void update(double fps, Window window, Camera camera) {
        float delta = 1/(float) fps;

        Vector2f movement = new Vector2f();
        if(window.getInput().isKeyDown(GLFW.GLFW_KEY_A))
            movement.add(-10*delta, 0);
        if(window.getInput().isKeyDown(GLFW.GLFW_KEY_D))
            movement.add(10*delta, 0);
        if(window.getInput().isKeyDown(GLFW.GLFW_KEY_W))
            movement.add(0, 10*delta);
        if(window.getInput().isKeyDown(GLFW.GLFW_KEY_S))
            movement.add(0, -10*delta);
        addPosition(movement);

        if(movement.x != 0 || movement.y != 0)
            useAnimation(ANIM_WALK);
        else
            useAnimation(ANIM_IDLE);

        camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.05f);
    }
}
