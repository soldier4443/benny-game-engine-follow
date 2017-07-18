package com.base.engine;

import org.lwjgl.input.Keyboard;

public class Game {
	private Mesh mesh;
	private Shader shader;
	private Material material;
	private Transform transform;
	private Camera camera;
	
	public Game() {
		mesh = new Mesh();
//        mesh = ResourceLoader.loadMesh("box.obj");
//        texture = ResourceLoader.loadTexture("test.png");
        material = new Material(ResourceLoader.loadTexture("test.png"), new Vector3f(0, 1, 1));
		shader = PhongShader.getInstance();
		camera = new Camera();
        transform = new Transform();
        
        Vertex[] vertices = new Vertex[] { new Vertex( new Vector3f( -1.0f, -1.0f, 0.5773f ), new Vector2f( 0.0f, 0.0f ) ),
            new Vertex( new Vector3f( 0.0f, -1.0f, -1.15475f ), new Vector2f( 0.5f, 0.0f ) ),
            new Vertex( new Vector3f( 1.0f, -1.0f, 0.5773f ),new Vector2f( 1.0f, 0 ) ),
            new Vertex( new Vector3f( 0.0f, 1.0f, 0.0f ), new Vector2f( 0.5f, 1.0f ) ) };
        
        int[] indices = new int[] { 0, 3, 1,
            1, 3, 2,
            2, 3, 0,
            1, 2, 0 };

		mesh.addVertices(vertices, indices, true);
		
		transform.setProjection(70.0f, Window.getWidth(), Window.getHeight(), 0.1f, 1000);
		transform.setCamera(camera);
		
		PhongShader.setDirectionalLight(new DirectionalLight(
		    new BaseLight(new Vector3f(1, 1, 1), 0.8f),
            new Vector3f(1, 1, 1)
        ));
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
//		transform.setRotation(0, sinTemp * 360, 0);
	}
	
	public void render() {
	    RenderUtil.setClearColor(transform.getCamera().getPos().div(2048f).abs());
	    shader.bind();
	    shader.updateUniforms(transform, material);
		mesh.draw();
	}
}
