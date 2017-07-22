package com.base.engine.rendering.resource;

import com.base.engine.rendering.Shader;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glCreateProgram;

/**
 * Created by soldi on 2017-07-22.
 */
public class ShaderResources {
    private int program = 0;
    private HashMap<String, Integer> uniformMap;
    private ArrayList<Shader.Component> uniformList;
    private int referenceCount;

    public ShaderResources() {
        this.program = glCreateProgram();
        this.uniformMap = new HashMap<>();
        this.uniformList = new ArrayList<>();

        if (program == 0) {
            System.out.println("Shader creation failed: Could not find valid memory location in constructor");
            System.exit(1);
        }

        this.referenceCount = 1;
    }

    @Override
    protected void finalize() throws Throwable {
        glDeleteBuffers(program);
    }

    public int getProgram() {
        return program;
    }

    public void addReference() {
        this.referenceCount++;
    }

    public boolean removeReference() {
        this.referenceCount--;
        return referenceCount == 0;
    }

    public HashMap<String, Integer> getUniformMap() {
        return uniformMap;
    }

    public ArrayList<Shader.Component> getUniformList() {
        return uniformList;
    }
}
