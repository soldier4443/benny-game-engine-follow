package com.base.engine.components;

import com.base.engine.components.BaseLight;
import com.base.engine.components.PointLight;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.ForwardSpotShader;

public class SpotLight extends PointLight {
    private Vector3f direction;
    private float cutoff;
    
    public SpotLight(Vector3f color, float intensity, Vector3f attenuation, Vector3f direction, float cutoff) {
        super(color, intensity, attenuation);
        this.direction = direction;
        this.cutoff = cutoff;
        
        setShader(ForwardSpotShader.getInstance());
    }
    
    public Vector3f getDirection() {
        return direction;
    }
    
    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }
    
    public float getCutoff() {
        return cutoff;
    }
    
    public void setCutoff(float cutoff) {
        this.cutoff = cutoff;
    }
}
