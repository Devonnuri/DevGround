package com.devground;

import com.devground.exception.GameException;
import com.devground.input.Input;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private long window;

    private int width;
    private int height;

    private boolean isFullscreen = false;

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
    }

    public void update() {
        input.update();
        glfwPollEvents();
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

    public void setPosition(int x, int y) {
        glfwSetWindowPos(window, x, y);
    }

    public void setKeyboardCallback(GLFWKeyCallbackI callback) {
        glfwSetKeyCallback(window, callback);
    }

    public void setCursorPosCallback(GLFWCursorPosCallbackI callback) {
        glfwSetCursorPosCallback(window, callback);
    }

    public void setMouseButtonCallback(GLFWMouseButtonCallbackI callback) {
        glfwSetMouseButtonCallback(window, callback);
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

    public Input getInput() {
        return input;
    }
}
