package com.base.engine;

import org.lwjgl.input.Keyboard;

public class Game {
	private Mesh mesh;
	private Shader shader;
	
	public Game() {
		mesh = new Mesh();
		shader = new Shader();
		
		Vertex[] data = new Vertex[] {
				new Vertex(new Vector3f(-1, -1, 0)),
				new Vertex(new Vector3f(0, 1, 0)),
				new Vertex(new Vector3f(1, -1, 0))
		};
		
		mesh.addVertices(data);
		
		shader.addVertexShader(ResourceLoader.loadShader("basicVertex.glsl"));
		shader.addFragmentShader(ResourceLoader.loadShader("basicFragment.glsl"));
		shader.compileShader();
		
		shader.addUniform("uniformFloat");
	}
	
	// This is test
	public void input() {
		if (Input.getKeyDown(Input.KEY_UP)) {
			System.out.println("We're just pressed up!");
		} else if (Input.getKeyUp(Input.KEY_UP)) {
			System.out.println("We're just released up!");
		}
		
		if (Input.getMouseDown(1)) {
			System.out.println("We're just right clicked at " + Input.getMousePosition());
		} else if (Input.getMouseUp(1)) {
			System.out.println("We're just released right moues button!");
		}
	}
	
	float temp = 0.0f;
	
	public void update() {
		temp += Time.getDelta();
		
		shader.setUniformf("uniformFloat", (float)Math.abs(Math.sin(temp)));
	}
	
	public void render() {
		shader.bind();
		mesh.draw();
	}
}
