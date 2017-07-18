package com.base.engine.rendering;

import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;

public class Vertex {
	public static final int SIZE = 8;
	
	private Vector3f pos;
	private Vector2f texturePos;
	private Vector3f normal;

	public Vertex(Vector3f pos) {
	    this(pos, new Vector2f(0, 0));
	}
	
	public Vertex(Vector3f pos, Vector2f texturePos) {
		this(pos, texturePos, new Vector3f(0, 0, 0));
	}
	
	public Vertex(Vector3f pos, Vector2f texturePos, Vector3f normal) {
		this.pos = pos;
		this.texturePos = texturePos;
		this.normal = normal;
	}

    public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

    public Vector2f getTexturePos() {
        return texturePos;
    }

    public void setTexturePos(Vector2f texturePos) {
        this.texturePos = texturePos;
    }
	
	public Vector3f getNormal() {
		return normal;
	}
	
	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}
}
