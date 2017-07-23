package com.base.engine.core;

// Generic container for every game!
public abstract class Game {
    
    private GameObject root = new GameObject();
    
    public void init() {
    
    }
    
    public void input(float deltaTime) {
        getRootObject().inputAll(deltaTime);
    }
    
    public void update(float deltaTime) {
        getRootObject().updateAll(deltaTime);
    }
    
    public void render(RenderingEngine renderingEngine) {
        renderingEngine.render(getRootObject());
    }
    
    public void addObject(GameObject object) {
        getRootObject().addChild(object);
    }
    
    private GameObject getRootObject() {
        return root;
    }

    public void setEngine(CoreEngine engine) {
        root.setEngine(engine);
    }
}
