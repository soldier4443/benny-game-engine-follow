package com.base.game;

import com.base.engine.core.*;
import com.base.engine.rendering.*;

public class TestGame implements Game {

    private Camera camera;

    private GameObject root;

    public void init() {
        root = new GameObject();
        camera = new Camera();

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

        root.addComponent(meshRenderer);

        Transform.setProjection(70.0f, Window.getWidth(), Window.getHeight(), 0.1f, 1000);
        Transform.setCamera(camera);
    }

    // This is test
    public void input() {
        camera.input();
        root.input();
    }

    public void update() {
        root.getTransform().setTranslation(0, -1, 5);
        root.update();
    }

    public void render() {
        root.render();
    }
}
