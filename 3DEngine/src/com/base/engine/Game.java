package com.base.engine;

import org.lwjgl.input.Keyboard;

public class Game {
	private Mesh mesh;
	private Shader shader;
	private Transform transform;
	private Camera camera;
	
	public Game() {
//		mesh = new Mesh();
        mesh = ResourceLoader.loadMesh("box.obj");
		shader = new Shader();
		camera = new Camera();
		
//		Vertex[] vertices = new Vertex[] {
//				new Vertex(new Vector3f(-1, -1, 0)),
//				new Vertex(new Vector3f(0, 1, 0)),
//				new Vertex(new Vector3f(1, -1, 0)),
//                new Vertex(new Vector3f(0, -1, 1))
//		};
//
//		int[] indices = new int[]{
//		    0, 1, 3,
//            3, 1, 2,
//            2, 1, 0,
//            0, 2, 3
//        };
//
//		mesh.addVertices(vertices, indices);
		
		transform = new Transform();
		transform.setProjection(70.0f, Window.getWidth(), Window.getHeight(), 0.1f, 1000);
		transform.setCamera(camera);
		
		shader.addVertexShader(ResourceLoader.loadShader("basicVertex.glsl"));
		shader.addFragmentShader(ResourceLoader.loadShader("basicFragment.glsl"));
		shader.compileShader();
		
		shader.addUniform("transform");
	}
	
	// This is test
	public void input() {
	    camera.input();
	    
//		if (Input.getKeyDown(Input.KEY_UP)) {
//			System.out.println("We're just pressed up!");
//		} else if (Input.getKeyUp(Input.KEY_UP)) {
//			System.out.println("We're just released up!");
//		}
//
//		if (Input.getMouseDown(1)) {
//			System.out.println("We're just right clicked at " + Input.getMousePosition());
//		} else if (Input.getMouseUp(1)) {
//			System.out.println("We're just released right moues button!");
//		}
	}
	
	float temp = 0.0f;
	
	public void update() {
		temp += Time.getDelta();
		
		float sinTemp = (float) Math.sin(temp);

		transform.setTranslation(0, 0, 5);
		transform.setRotation(0, sinTemp * 360, sinTemp * 360);
	}
	
	public void render() {
		shader.bind();
		shader.setUniform("transform", transform.getProjectedTransformation());
		mesh.draw();
	}
}
