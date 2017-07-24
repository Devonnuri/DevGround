package com.devground.collision;

import org.joml.Vector2f;

public class Collision {
    public Vector2f distance;
    public boolean isCollide;

    public Collision(Vector2f distance, boolean isCollide) {
        this.distance = distance;
        this.isCollide = isCollide;
    }
}
