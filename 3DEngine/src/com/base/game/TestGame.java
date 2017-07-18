package com.base.game;

import com.base.engine.components.*;
import com.base.engine.core.*;
import com.base.engine.rendering.*;

public class TestGame extends Game {


    public void init() {
        float fieldDepth = 10.0f;
        float fieldWidth = 10.0f;

        Vertex[] planeVertices = new Vertex[] { new Vertex( new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
                new Vertex( new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
                new Vertex( new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
                new Vertex( new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f))};

        int planeIndices[] = { 0, 1, 2,
                2, 1, 3};

        Mesh mesh = new Mesh(planeVertices, planeIndices, true);
        Material material = new Material(new Texture("test.png"), new Vector3f(1, 1, 1), 1, 8);

        MeshRenderer meshRenderer = new MeshRenderer(mesh, material);

        GameObject planeObject = new GameObject();
        planeObject.addComponent(meshRenderer);
        planeObject.getTransform().setPosition(0, -1, 5);


        GameObject directionalLightObject = new GameObject();
        directionalLightObject.addComponent(new DirectionalLight(new Vector3f(0, 0, 1), 0.2f, new Vector3f(1, 1, 1)));
        directionalLightObject.addComponent(new DirectionalLight(new Vector3f(1, 0, 0), 0.2f, new Vector3f(-1, 1, -1)));



        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                GameObject pointLightObject = new GameObject();
                pointLightObject.addComponent(new PointLight(
                        new Vector3f(0, 1, 0), 0.2f,
                        new Vector3f(0, 0, 1)
                ));

                pointLightObject.getTransform().setPosition(new Vector3f(i * 5, 0, j * 5));

                getRootObject().addChild(pointLightObject);
            }
        }


        GameObject spotLightObject = new GameObject();
        spotLightObject.addComponent(new SpotLight(
                new Vector3f(0, 1, 1), 0.4f,
                new Vector3f(0, 0, 0.08f),
                new Vector3f(-1, -1, -1), 0.9f));

        spotLightObject.getTransform().setPosition(24 ,5, 24);

        getRootObject().addChild(planeObject);
        getRootObject().addChild(directionalLightObject);
        getRootObject().addChild(spotLightObject);
    }
}
