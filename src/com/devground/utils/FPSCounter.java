package com.devground.utils;

public class FPSCounter {
    private int curFrame = 0;
    private long lastTime = 0;
    public int fps = 0;

    public FPSCounter() {
        lastTime = System.currentTimeMillis();
    }

    public void update() {
        curFrame++;
        if(System.currentTimeMillis() > lastTime + 1000) {
            Debug.log("FPS: "+curFrame);

            fps = curFrame;
            curFrame = 0;
            lastTime += 1000;
        }
    }
}
