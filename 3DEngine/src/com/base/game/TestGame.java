package com.base.game;

import com.base.engine.components.*;
import com.base.engine.core.*;
import com.base.engine.rendering.*;

public class TestGame extends Game {

    GameObject plane1;
    GameObject plane2;
    GameObject cameraObject;

    public void init() {
        float fieldDepth = 10.0f;
        float fieldWidth = 10.0f;

        Vertex[] planeVertices = new Vertex[]{new Vertex(new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
                new Vertex(new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f))};

        int planeIndices[] = {0, 1, 2,
                2, 1, 3};

        Vertex[] planeVertices2 = new Vertex[]{new Vertex(new Vector3f(-fieldWidth / 10, 0.0f, -fieldDepth / 10), new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f(-fieldWidth / 10, 0.0f, fieldDepth / 10 * 3), new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f(fieldWidth / 10 * 3, 0.0f, -fieldDepth / 10), new Vector2f(1.0f, 0.0f)),
                new Vertex(new Vector3f(fieldWidth / 10 * 3, 0.0f, fieldDepth / 10 * 3), new Vector2f(1.0f, 1.0f))};

        int planeIndices2[] = {0, 1, 2,
                2, 1, 3};
        
        Mesh mesh = new Mesh(planeVertices, planeIndices, true);
        Mesh mesh2 = new Mesh(planeVertices2, planeIndices2, true);
        
        Material material = new Material();//new Texture("test.png"), new Vector3f(1, 1, 1), 1, 8);
        material.addTexture("diffuse", new Texture("test.png"));
        material.addFloat("specularIntensity", 1.0f);
        material.addFloat("specularPower", 8.0f);
        
        
        GameObject planeObject = new GameObject();
        planeObject.addComponent(new MeshRenderer(mesh, material));
        planeObject.getTransform().setPosition(new Vector3f(0, -1, 5));


        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(0, 0, 1), 0.4f);
        GameObject directionalLightObject = new GameObject();
        directionalLightObject.addComponent(directionalLight);
        
        directionalLight.getTransform().setRotation(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-45)));
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                GameObject pointLightObject = new GameObject();
                pointLightObject.addComponent(new PointLight(
                    new Vector3f(0, 1, 0), 0.2f,
                    new Vector3f(0, 0, 1)
                ));
                
                pointLightObject.getTransform().setPosition(new Vector3f(i * 5, 0, j * 5));

                addObject(pointLightObject);
            }
        }
        
        
        GameObject spotLightObject = new GameObject();
        spotLightObject.addComponent(new SpotLight(
            new Vector3f(0, 1, 1), 0.8f,
            new Vector3f(0, 0, 0.08f), 0.7f));
        
        spotLightObject.getTransform().setPosition(new Vector3f(5, 0, 5));
        spotLightObject.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), (float) Math.toRadians(90.0f)));
        
        Camera camera = new Camera((float) Math.toRadians(70), (float) (Window.getWidth() / Window.getHeight()), 0.1f, 1000f);
        cameraObject = new GameObject().addComponent(camera);

//        cameraObject.getTransform().setRotation(new Quaternion(Vector3f.X, (float)Math.toRadians(90)));
//        cameraObject.getTransform().setPosition(new Vector3f(5, 10, 5));

        plane1 = new GameObject().addComponent(new MeshRenderer(mesh2, material));
        plane2 = new GameObject().addComponent(new MeshRenderer(mesh2, material));

//        plane1.getTransform().setPosition(new Vector3f(5, 5,5 ));
        plane2.getTransform().setPosition(new Vector3f(0, 0, 5));

        plane1.addChild(plane2);
        plane2.addChild(cameraObject);
//        addObject(cameraObject);
        
        plane1.getTransform().setRotation(new Quaternion(Vector3f.Y, (float)Math.toRadians(45)));
        cameraObject.getTransform().setRotation(new Quaternion(Vector3f.Y, (float)Math.toRadians(45)));
    
        addObject(planeObject);
        addObject(directionalLightObject);
        addObject(spotLightObject);
        addObject(plane1);
    }

    float time = 0;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        time += deltaTime;

//        plane1.getTransform().setRotation(new Quaternion(Vector3f.Y, (float)Math.toRadians(time * 30)));
    }
}
