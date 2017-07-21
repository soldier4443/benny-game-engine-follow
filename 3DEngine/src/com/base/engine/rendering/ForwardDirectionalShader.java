package com.base.engine.rendering;

import com.base.engine.components.BaseLight;
import com.base.engine.components.DirectionalLight;
import com.base.engine.core.Matrix4f;
import com.base.engine.core.RenderingEngine;
import com.base.engine.core.Transform;

public class ForwardDirectionalShader extends Shader {
    
    private static class Singleton {
        private static final ForwardDirectionalShader instance = new ForwardDirectionalShader();
    }
    
    public static ForwardDirectionalShader getInstance() {
        return ForwardDirectionalShader.Singleton.instance;
    }
    
    private ForwardDirectionalShader() {
        super();
        
        addVertexShaderFromFile("forwardDirectionalVertex.glsl");
        addFragmentShaderFromFile("forwardDirectionalFragment.glsl");
        compileShader();
        
        addUniform("model");
        addUniform("MVP");
        
        addUniform("specularIntensity");
        addUniform("specularPower");
        addUniform("eyePos");
        
        addUniform("directionalLight.base.color");
        addUniform("directionalLight.base.intensity");
        addUniform("directionalLight.direction");
    }
    
    @Override
    public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
        Matrix4f worldMatrix = transform.getTransformation();
        Matrix4f projectedMatrix = renderingEngine.getMainCamera().getViewProjection().mul(worldMatrix);
        material.getTexture("diffuse").bind();
        
        setUniform("model", worldMatrix);
        setUniform("MVP", projectedMatrix);
        
        setUniformf("specularIntensity", material.getFloat("specularIntensity"));
        setUniformf("specularPower", material.getFloat("specularPower"));
        setUniform("eyePos", renderingEngine.getMainCamera().getTransform().getTransformedPosition());
        
        setUniformDirectionalLight("directionalLight", (DirectionalLight) renderingEngine.getActiveLight());
    }
    
    private void setUniformBaseLight(String uniformName, BaseLight baseLight) {
        setUniform(uniformName + ".color", baseLight.getColor());
        setUniformf(uniformName + ".intensity", baseLight.getIntensity());
    }
    
    private void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight) {
        setUniformBaseLight(uniformName + ".base", directionalLight);
        setUniform(uniformName + ".direction", directionalLight.getDirection());
    }
}
