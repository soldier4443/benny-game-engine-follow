package com.base.engine.rendering.resource;

import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

/**
 * Created by soldi on 2017-07-22.
 */
public class TextureResources {
    private int id;
    private int referenceCount;

    public TextureResources() {
        this.id = glGenTextures();
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
