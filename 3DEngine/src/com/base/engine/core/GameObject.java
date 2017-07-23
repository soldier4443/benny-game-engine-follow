package com.base.engine.core;

import com.base.engine.components.GameComponent;
import com.base.engine.rendering.Shader;

import java.util.ArrayList;

public class GameObject {
    private ArrayList<GameObject> children;
    private ArrayList<GameComponent> components;
    private Transform transform;

    private CoreEngine engine;
    
    public GameObject() {
        children = new ArrayList<>();
        components = new ArrayList<>();
        transform = new Transform();

        engine = null;
    }
    
    public void addChild(GameObject child) {
        children.add(child);
        child.setEngine(engine);
        child.getTransform().setParent(transform);
    }
    
    public GameObject addComponent(GameComponent component) {
        components.add(component);
        component.setParent(this);
        
        return this;
    }
    
    public void inputAll(float deltaTime) {
        input(deltaTime);

        for (GameObject child : children)
            child.inputAll(deltaTime);
    }
    
    public void updateAll(float deltaTime) {
        update(deltaTime);
        
        for (GameObject child : children)
            child.updateAll(deltaTime);
    }
    
    public void renderAll(Shader shader, RenderingEngine renderingEngine) {
        render(shader, renderingEngine);

        for (GameObject child : children)
            child.renderAll(shader, renderingEngine);
    }

    public void input(float deltaTime) {
        for (GameComponent component : components)
            component.input(deltaTime);
    }

    public void update(float deltaTime) {
        for (GameComponent component : components)
            component.update(deltaTime);
    }

    public void render(Shader shader, RenderingEngine renderingEngine) {
        for (GameComponent component : components)
            component.render(shader, renderingEngine);
    }

    public void setEngine(CoreEngine engine) {
        if (this.engine != engine) {
            this.engine = engine;

            for (GameComponent component : components)
                component.addToEngine(engine);

            for (GameObject child : children)
                child.setEngine(engine);
        }
    }
    
    public Transform getTransform() {
        return transform;
    }
}
