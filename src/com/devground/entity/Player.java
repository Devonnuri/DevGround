package com.devground.entity;

import com.devground.Transform;
import com.devground.Window;
import com.devground.render.*;
import com.devground.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Player extends Entity {
    public Player(World world) {
        super(world, new Animation(16, 10, "Pointer_Normal"), new Transform());
    }

    @Override
    public void update(double fps, Window window, Camera camera) {
        float delta = 1/(float) fps;

        Vector2f movement = new Vector2f();
        if(window.getInput().isKeyDown(GLFW.GLFW_KEY_A))
            movement.add(-8*delta, 0);
        if(window.getInput().isKeyDown(GLFW.GLFW_KEY_D))
            movement.add(8*delta, 0);
        if(window.getInput().isKeyDown(GLFW.GLFW_KEY_W))
            movement.add(0, 8*delta);
        if(window.getInput().isKeyDown(GLFW.GLFW_KEY_S))
            movement.add(0, -8*delta);

        addPosition(movement);

        super.update(fps, window, camera);

        camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.05f);
    }
}
