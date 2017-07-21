package com.base.engine.rendering.resource;

import static org.lwjgl.opengl.GL15.*;

/**
 * Created by soldi on 2017-07-22.
 */
public class MeshResources {
    private int vbo;
    private int ibo;
    private int size;
    private int referenceCount;

    public MeshResources(int size) {
        vbo = glGenBuffers();
        ibo = glGenBuffers();
        this.size = size;
        this.referenceCount = 1;
    }

    @Override
    protected void finalize() throws Throwable {
        glDeleteBuffers(vbo);
        glDeleteBuffers(ibo);
    }

    public int getVbo() {
        return vbo;
    }

    public int getIbo() {
        return ibo;
    }

    public int getSize() {
        return size;
    }

    public void addReference() {
        this.referenceCount++;
    }

    public boolean removeReference() {
        this.referenceCount--;
        return referenceCount == 0;
    }
}
