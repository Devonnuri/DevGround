package com.devground.main;

import com.devground.Transform;
import com.devground.Window;
import com.devground.entity.Entity;
import com.devground.entity.Player;
import com.devground.gui.GUI;
import com.devground.input.Input;
import com.devground.render.Camera;
import com.devground.render.Shader;
import com.devground.utils.FPSCounter;
import com.devground.utils.Timer;
import com.devground.utils.Utils;
import com.devground.world.Tile;
import com.devground.world.TileRenderer;
import com.devground.world.World;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Main implements Runnable {
    private static int width = 640;
    private static int height = 480;

    private static double FPS = 60;

    public static boolean DEBUG = true;

    private Thread thread;
    private boolean running = false;

    Input input;

    FPSCounter fpsCounter;

    Camera camera;
    Shader shader;

    World world;
    TileRenderer tileRenderer;

    GUI gui;

    Window window = new Window(width, height);

    private void start() {
        running = true;

        thread = new Thread(this, "DevGround");
        thread.start();
    }

    public void run() {
        double frameDelay = 1/FPS/2;
        double startTime = Timer.getTime();
        double unprocessed = 0;

        init();

        while(running) {
            if(window.shouldClose()) {
                running = false;
            }

            boolean canRender = false;

            double elapsed = Timer.getTime() - startTime;
            unprocessed += elapsed;
            startTime = Timer.getTime();

            while(unprocessed >= frameDelay) {
                unprocessed = 0;
                canRender = true;

                update();
            }

            if(canRender) {
                render();
            }
        }

        close();
    }

    private void init() {
        if(!glfwInit()) {
            System.err.println("오류: GLFW를 초기화하지 못했습니다.");
            System.exit(1);
        }

        window.createWindow("DevGround");
        window.setFullscreen(false);

        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));

        fpsCounter = new FPSCounter();

        camera = new Camera(window.getWidth(), window.getHeight());
        camera.setPosition(new Vector3f(0, 0, 0));
        shader = new Shader("shader");

        Utils.initModel();
        world = new World("test", camera);
        world.calculateViewRange(window);

        tileRenderer = new TileRenderer(world);

        gui = new GUI();
    }

    private void update() {
        if(window.isResized()) {
            camera.setProjection(window.getWidth(), window.getHeight());
            world.calculateViewRange(window);
            glViewport(0, 0, window.getWidth(), window.getHeight());
        }

        if(window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
            window.close();
        }

        world.update(FPS, window, camera);

        world.correctCamera(camera, window);
        window.update();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT);

        world.render(tileRenderer, shader, camera);
        gui.render(camera);

        window.swapBuffers();
        fpsCounter.update();
    }

    private void close() {
        Utils.removeModel();
        glfwTerminate();
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
