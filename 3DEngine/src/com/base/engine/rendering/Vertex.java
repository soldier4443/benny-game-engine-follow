package com.base.engine.rendering;

import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;

public class Vertex {
	public static final int SIZE = 8;
	
	private Vector3f position;
	private Vector2f texturePosition;
	private Vector3f normal;

	public Vertex(Vector3f position) {
	    this(position, new Vector2f(0, 0));
	}
	
	public Vertex(Vector3f position, Vector2f texturePosition) {
		this(position, texturePosition, new Vector3f(0, 0, 0));
	}
	
	public Vertex(Vector3f position, Vector2f texturePosition, Vector3f normal) {
		this.position = position;
		this.texturePosition = texturePosition;
		this.normal = normal;
	}

    public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

    public Vector2f getTexturePosition() {
        return texturePosition;
    }

    public void setTexturePosition(Vector2f texturePosition) {
        this.texturePosition = texturePosition;
    }
	
	public Vector3f getNormal() {
		return normal;
	}
	
	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}
}
