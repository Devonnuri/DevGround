package com.devground.render;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Texture {
    private int id;
    private int width;
    private int height;

    public Texture(String filename) {
        BufferedImage image;
        try {
            File file = new File("./textures/"+filename);
            if(!file.exists())
                file = new File("./textures/unknown.png");

            image = ImageIO.read(file);
            width = image.getWidth();
            height = image.getHeight();

            int[] rawPixels = image.getRGB(0, 0, width, height, null, 0, width);

            ByteBuffer pixels = BufferUtils.createByteBuffer(width*height*4);

            for(int y=0; y<height; y++) {
                for(int x=0; x<width; x++) {
                    int pixel = rawPixels[y*width+x];

                    pixels.put((byte) ((pixel >> 16) & 0xFF));  // Red
                    pixels.put((byte) ((pixel >> 8) & 0xFF));   // Green
                    pixels.put((byte) (pixel & 0xFF));          // Blue
                    pixels.put((byte) ((pixel >> 24) & 0xFF));  // Alpha
                }
            }

            pixels.flip();
            id = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, id);

            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void finalize() throws Throwable {
        glDeleteTextures(id);
        super.finalize();
    }

    public void bind(int sampler) {
        if(sampler >= 0 && sampler <= 31) {
            glActiveTexture(GL_TEXTURE0 + sampler);
            glBindTexture(GL_TEXTURE_2D, id);
        }
    }
}
