package com.base.engine.rendering;

import com.base.engine.core.Matrix4f;
import com.base.engine.core.Transform;

public class BasicShader extends Shader {
    
    private static class Singleton {
        private static final BasicShader instance = new BasicShader();
    }
    
    public static BasicShader getInstance() {
        return Singleton.instance;
    }
    
    private BasicShader() {
        super();
        
        addVertexShaderFromFile("basicVertex.glsl");
        addFragmentShaderFromFile("basicFragment.glsl");
        compileShader();
        
        addUniform("transform");
        addUniform("color");
    }
    
    @Override
    public void updateUniforms(Transform transform, Material material) {
        Matrix4f projectedMatrix = getRenderingEngine().getMainCamera().getViewProjection().mul(transform.getTransformation());
        material.getTexture().bind();
        
        setUniform("transform", projectedMatrix);
        setUniform("color", material.getColor());
    }
}
