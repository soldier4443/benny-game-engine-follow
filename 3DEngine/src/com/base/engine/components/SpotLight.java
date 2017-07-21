package com.base.engine.components;

import com.base.engine.components.BaseLight;
import com.base.engine.components.PointLight;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.ForwardSpotShader;

public class SpotLight extends PointLight {
    private float cutoff;
    
    public SpotLight(Vector3f color, float intensity, Vector3f attenuation, float cutoff) {
        super(color, intensity, attenuation);
        this.cutoff = cutoff;
        
        setShader(ForwardSpotShader.getInstance());
    }
    
    public Vector3f getDirection() {
        return getTransform().getTransformedRotation().getForward();
    }
    
    public float getCutoff() {
        return cutoff;
    }
    
    public void setCutoff(float cutoff) {
        this.cutoff = cutoff;
    }
}
