package com.devground.render;

import com.devground.exception.GameException;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int program;
    private int vertexShader;
    private int fragmentShader;

    public Shader(String filename) {
        program = glCreateProgram();

        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, readFile(filename+".vert"));
        glCompileShader(vertexShader);
        if(glGetShaderi(vertexShader, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(vertexShader));
            System.exit(1);
        }

        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, readFile(filename+".frag"));
        glCompileShader(fragmentShader);
        if(glGetShaderi(fragmentShader, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(fragmentShader));
            System.exit(1);
        }

        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);

        glBindAttribLocation(program, 0, "vertices");
        glBindAttribLocation(program, 1, "textures");

        glLinkProgram(program);
        if(glGetProgrami(program, GL_LINK_STATUS) != 1) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }

        glValidateProgram(program);
        if(glGetProgrami(program, GL_VALIDATE_STATUS) != 1) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
    }

    protected void finalize() {
        glDetachShader(program, vertexShader);
        glDetachShader(program, fragmentShader);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        glDeleteProgram(program);
    }

    public void setUniformVariable(String variable, int value) {
        int location = glGetUniformLocation(program, variable);

        if (location != -1) {
            glUniform1i(location, value);
        }
    }

    public void setUniformVariable(String variable, Matrix4f value) {
        int location = glGetUniformLocation(program, variable);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);

        if (location != -1) {
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    public void setUniformVariable(String variable, Vector4f value) {
        int location = glGetUniformLocation(program, variable);

        if (location != -1) {
            glUniform4f(location, value.x, value.y, value.z, value.w);
        }
    }

    public void bind() {
        glUseProgram(program);
    }

    private String readFile(String filename) {
        InputStream stream = getClass().getResourceAsStream("/resource/shaders/"+filename);
        return new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
    }
}
