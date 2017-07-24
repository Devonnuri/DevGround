package com.devground.input;

import com.devground.Window;
import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private Window window;

    private boolean keys[] = new boolean[GLFW_KEY_LAST];

    public Input(Window window) {
        this.window = window;
    }

    public boolean isKeyDown(int key) {
        return glfwGetKey(window.getID(), key) == 1;
    }

    public boolean isKeyPressed(int key) {
        return isKeyDown(key) && !keys[key];
    }

    public boolean isMouseButtonDown(int button) {
        return glfwGetMouseButton(window.getID(), button) == 1;
    }

    public void update() {
        for(int i=32; i<keys.length; i++) {
            keys[i] = isKeyDown(i);
        }
    }
}
