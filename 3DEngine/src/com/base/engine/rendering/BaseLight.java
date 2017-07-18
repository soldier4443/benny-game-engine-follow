package com.base.engine.rendering;

import com.base.engine.core.Vector3f;

/**
 * Created by nyh0111 on 2017-07-18.
 */

public class BaseLight {
    private Vector3f color;
    private float intentsity;
    
    public BaseLight(Vector3f color, float intentsity) {
        this.color = color;
        this.intentsity = intentsity;
    }
    
    public Vector3f getColor() {
        return color;
    }
    
    public void setColor(Vector3f color) {
        this.color = color;
    }
    
    public float getIntentsity() {
        return intentsity;
    }
    
    public void setIntentsity(float intentsity) {
        this.intentsity = intentsity;
    }
}
