package com.base.engine.rendering;

import com.base.engine.core.Matrix4f;
import com.base.engine.core.RenderingEngine;
import com.base.engine.core.Transform;

public class ForwardAmbientShader extends Shader {
    
    private static class Singleton {
        private static final ForwardAmbientShader instance = new ForwardAmbientShader();
    }
    
    public static ForwardAmbientShader getInstance() {
        return ForwardAmbientShader.Singleton.instance;
    }
    
    private ForwardAmbientShader() {
        super();
        
        addVertexShaderFromFile("forwardAmbientVertex.glsl");
        addFragmentShaderFromFile("forwardAmbientFragment.glsl");
        compileShader();
        
        addUniform("MVP");
        addUniform("ambientIntensity");
    }
    
    @Override
    public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
        Matrix4f projectedMatrix = renderingEngine.getMainCamera().getViewProjection().mul(transform.getTransformation());
        material.getTexture("diffuse").bind();
        
        setUniform("MVP", projectedMatrix);
        setUniform("ambientIntensity", renderingEngine.getAmbientLight());
    }
}
