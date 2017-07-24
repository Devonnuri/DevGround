package com.devground.collision;

import org.joml.Vector2f;

public class CollisionBox {
    private Vector2f center;
    private Vector2f halfExtent;

    public CollisionBox(Vector2f center, Vector2f halfExtent) {
        this.center = center;
        this.halfExtent = halfExtent;
    }

    public Collision getCollision(CollisionBox target) {
        Vector2f distance = target.center.sub(center, new Vector2f());
        distance.x = Math.abs(distance.x);
        distance.y = Math.abs(distance.y);

        distance.sub(halfExtent.add(target.halfExtent, new Vector2f()));

        return new Collision(distance, distance.x < 0 && distance.y < 0);
    }

    public void correctPosition(CollisionBox target, Collision data) {
        Vector2f correction = target.center.sub(center, new Vector2f());
        if(data.distance.x > data.distance.y) {
            if(correction.x > 0) {
                center.add(data.distance.x, 0);
            } else {
                center.sub(data.distance.x, 0);
            }
        } else {
            if(correction.y > 0) {
                center.add(0, data.distance.y);
            } else {
                center.sub(0, data.distance.y);
            }
        }
    }

    public Vector2f getCenter() {
        return center;
    }

    public Vector2f getHalfExtent() {
        return halfExtent;
    }

}
