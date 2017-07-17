package com.base.engine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh {
	private int vbo;
	private int ibo;
	private int size;

	public Mesh() {
		vbo = glGenBuffers();
		ibo = glGenBuffers();
		size = 0;
	}
	
	public void addVertices(Vertex[] vertices, int[] indices) {
		size = indices.length;
        
        // Java array != Native array
        // So this conversion..
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
	}
	
	public void draw() {
		glEnableVertexAttribArray(0);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
		
//		glDrawArrays(GL_TRIANGLES, 0, size);
        glDrawElements(GL_TRIANGLE_FAN, size, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
	}
}
