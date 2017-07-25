package com.devground.render;

import com.devground.utils.Timer;

public class Animation {
    private Texture[] frames;
    private int currentFrame;

    private double elapsedTime;
    private double currentTime;
    private double lastTime;
    private double fps;

    public Animation(int frames, int fps, String filename) {
        this.currentFrame = 0;
        this.elapsedTime = 0;
        this.currentTime = 0;
        this.lastTime = Timer.getTime();
        this.fps = 1.0/fps;

        this.frames = new Texture[frames];
        for(int i=0; i<frames; i++) {
            this.frames[i] = new Texture(filename+"/"+filename.split("/")[0]+"_"+i+".png");
        }
    }

    public void bind() {
        bind(0);
    }

    public void bind(int sampler) {
        this.currentTime = Timer.getTime();
        this.elapsedTime += currentTime - lastTime;

        if(elapsedTime >= fps) {
            elapsedTime -= fps;
            currentFrame++;
        }

        if(currentFrame >= frames.length) currentFrame = 0;

        this.lastTime = currentTime;

        frames[currentFrame].bind(sampler);
    }

}
