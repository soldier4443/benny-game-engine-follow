package com.base.engine.core;

// Generic container for every game!
public abstract class Game {
    
    private GameObject root;
    
    public void init() {
    
    }
    
    public void input(float deltaTime) {
        getRootObject().input(deltaTime);
    }
    
    public void update(float deltaTime) {
        getRootObject().update(deltaTime);
    }
    
    public GameObject getRootObject() {
        if (root == null) {
            root = new GameObject();
        }
        
        return root;
    }
}
