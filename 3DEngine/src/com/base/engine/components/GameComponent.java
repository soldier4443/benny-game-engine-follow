package com.base.engine.components;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.GameObject;
import com.base.engine.core.RenderingEngine;
import com.base.engine.core.Transform;
import com.base.engine.rendering.Shader;

public abstract class GameComponent {
    
    private GameObject parent;
    
    public void input(float deltaTime) {
    
    }
    
    public void update(float deltaTime) {
    
    }
    
    public void render(Shader shader, RenderingEngine renderingEngine) {
    
    }
    
    public void setParent(GameObject parent) {
        this.parent = parent;
    }
    
    public Transform getTransform() {
        return parent.getTransform();
    }
    
    // Temporary
    public void addToEngine(CoreEngine engine) {
    
    }
}
