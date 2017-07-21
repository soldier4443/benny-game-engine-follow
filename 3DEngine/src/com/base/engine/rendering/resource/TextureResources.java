package com.base.engine.rendering.resource;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

/**
 * Created by soldi on 2017-07-22.
 */
public class TextureResources {
    private int id = 0;
    private int referenceCount;

    public TextureResources(int id) {
        this.id = id;
        this.referenceCount = 1;
    }

    @Override
    protected void finalize() throws Throwable {
        glDeleteBuffers(id);
    }

    public int getId() {
        return id;
    }

    public void addReference() {
        this.referenceCount++;
    }

    public boolean removeReference() {
        this.referenceCount--;
        return referenceCount == 0;
    }
}
