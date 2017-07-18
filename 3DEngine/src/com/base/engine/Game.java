package com.base.engine;

import org.lwjgl.input.Keyboard;

public class Game {
	private Mesh mesh;
	private Shader shader;
	private Material material;
	private Transform transform;
	private Camera camera;
    
    PointLight pointLight1 = new PointLight(
        new BaseLight(new Vector3f(1, 0, 0),0.8f),
        new Attenuation(0, 0, 1),
        new Vector3f(-2, 0, 5f),
        10);
    
    PointLight pointLight2 = new PointLight(
        new BaseLight(new Vector3f(0, 0, 1),0.8f),
        new Attenuation(0, 0, 1),
        new Vector3f(2, 0, 7f),
        10);
    
    PointLight pointLightForSpot = new PointLight(
        new BaseLight(new Vector3f(1, 1, 1),0.8f),
        new Attenuation(0, 0, 0.01f),
        new Vector3f(2, 0, 7f),
        30);
    
    SpotLight spotLight1 = new SpotLight(pointLightForSpot,
        new Vector3f(1, 1, 1),
        0.8f);
	
	public Game() {
//        mesh = ResourceLoader.loadMesh("box.obj");
//        texture = ResourceLoader.loadTexture("test.png");
        material = new Material(new Texture("test.png"), new Vector3f(1, 1, 1), 1, 8);
		shader = PhongShader.getInstance();
		camera = new Camera();
        transform = new Transform();
        
//        Vertex[] vertices = new Vertex[] { new Vertex( new Vector3f( -1.0f, -1.0f, 0.5773f ), new Vector2f( 0.0f, 0.0f ) ),
//                                           new Vertex( new Vector3f( 0.0f, -1.0f, -1.15475f ), new Vector2f( 0.5f, 0.0f ) ),
//                                           new Vertex( new Vector3f( 1.0f, -1.0f, 0.5773f ),new Vector2f( 1.0f, 0 ) ),
//                                           new Vertex( new Vector3f( 0.0f, 1.0f, 0.0f ), new Vector2f( 0.5f, 1.0f ) ) };
//
//        int[] indices = new int[] { 0, 3, 1,
//                                    1, 3, 2,
//                                    2, 3, 0,
//                                    1, 2, 0 };
        
        float fieldDepth = 10.0f;
        float fieldWidth = 10.0f;

        Vertex[] planeVertices = new Vertex[] { new Vertex( new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
                                                new Vertex( new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
                                                new Vertex( new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
                                                new Vertex( new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f))};
        int planeIndices[] = { 0, 1, 2,
                               2, 1, 3};

        mesh = new Mesh(planeVertices, planeIndices, true);
		
		transform.setProjection(70.0f, Window.getWidth(), Window.getHeight(), 0.1f, 1000);
		transform.setCamera(camera);
		
		PhongShader.setAmbientLight(new Vector3f(0.01f, 0.01f, 0.01f));
		PhongShader.setDirectionalLight(new DirectionalLight(
		    new BaseLight(new Vector3f(1, 1, 1), 0.1f),
            new Vector3f(1, 1, 1)
        ));
        
        PhongShader.setPointLights(new PointLight[]{pointLight1, pointLight2});
        PhongShader.setSpotLights(new SpotLight[]{spotLight1});
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

		transform.setTranslation(0, -1, 5);
//		transform.setRotation(0, sinTemp * 360, 0);
        
        pointLight1.setPosition(new Vector3f(3, 0, 8.0f * (float)(Math.sin(temp) + 0.5) + 10));
        pointLight2.setPosition(new Vector3f(7, 0, 8.0f * (float)(Math.cos(temp) + 0.5) + 10));
        
        spotLight1.getPointLight().setPosition(camera.getPos());
        spotLight1.setDirection(camera.getForward());
	}
	
	public void render() {
	    RenderUtil.setClearColor(transform.getCamera().getPos().div(2048f).abs());
	    shader.bind();
	    shader.updateUniforms(transform, material);
		mesh.draw();
	}
}
