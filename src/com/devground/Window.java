package com.devground;

import com.devground.exception.GameException;
import com.devground.input.Input;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;

public class Window {
    private long window;

    private int width;
    private int height;
    private boolean isFullscreen = false;
    private boolean isResized = false;
    private GLFWWindowSizeCallback windowSizeCallback;

    private Input input;

    public Window(int width, int height) {
        input = new Input(this);

        setSize(width, height);
    }

    public void createWindow(String title) {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        if(isFullscreen) {
            width = vidMode.width();
            height = vidMode.height();
        }

        window = glfwCreateWindow(width, height, title, isFullscreen ? glfwGetPrimaryMonitor() : 0L, 0L);

        if(window == 0L) {
            throw new GameException("윈도우를 생성하는데 실패하였습니다.");
        }

        glfwMakeContextCurrent(window);

        if(!isFullscreen) {
            setPosition((vidMode.width() - width)/2, (vidMode.height() - height)/2);
            glfwShowWindow(window);
        }

        setCallabcks();
    }

    public void update() {
        isResized = false;
        input.update();
        glfwPollEvents();
    }

    public void cleanUp() {
        glfwFreeCallbacks(window);
    }

    public void close() {
        glfwSetWindowShouldClose(window, true);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setFullscreen(boolean isFullscreen) {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        int w = width;
        int h = height;
        int x = (vidMode.width() - width)/2;
        int y = (vidMode.height() - height)/2;
        long monitor = 0L;

        if(isFullscreen) {
            w = vidMode.width();
            h = vidMode.height();
            x = 0;
            y = 0;
            monitor = glfwGetPrimaryMonitor();
        }

        this.isFullscreen = isFullscreen;
        glfwSetWindowMonitor(window, monitor, x, y, w, h, vidMode.refreshRate());
    }

    private void setCallabcks() {
        windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long argWindow, int argWidth, int argHeight) {
                window = argWindow;
                width = argWidth;
                height = argHeight;
                isResized = true;
            }
        };

        glfwSetWindowSizeCallback(window, windowSizeCallback);
    }

    public void setPosition(int x, int y) {
        glfwSetWindowPos(window, x, y);
    }

    public long getID() {
        return window;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public boolean getFullscreen() {
        return isFullscreen;
    }
    public boolean isResized() {
        return isResized;
    }
    public Input getInput() {
        return input;
    }
}
