package com.base.engine;

import org.lwjgl.input.Keyboard;

public class Game {
	private Mesh mesh;
	
	public Game() {
		mesh = new Mesh();
		
		Vertex[] data = new Vertex[] {
				new Vertex(new Vector3f(-1, -1, 0)),
				new Vertex(new Vector3f(0, 1, 0)),
				new Vertex(new Vector3f(1, -1, 0))
		};
		
		mesh.addVertices(data);
	}
	
	// This is test
	public void input() {
		if (Input.getKeyDown(Keyboard.KEY_UP)) {
			System.out.println("We're just pressed up!");
		} else if (Input.getKeyUp(Keyboard.KEY_UP)) {
			System.out.println("We're just released up!");
		}
		
		if (Input.getMouseDown(1)) {
			System.out.println("We're just right clicked at " + Input.getMousePosition());
		} else if (Input.getMouseUp(1)) {
			System.out.println("We're just released right moues button!");
		}
	}
	
	public void update() {
		
	}
	
	public void render() {
		mesh.draw();
	}
}
