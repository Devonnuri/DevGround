package com.devground.entity;

import com.devground.Transform;
import com.devground.Window;
import com.devground.collision.Collision;
import com.devground.collision.CollisionBox;
import com.devground.render.*;
import com.devground.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Player extends Entity {
    public Player(World world) {
        super(world, new Animation(16, 10, "Cursor_Normal"), new Transform());
    }
}
