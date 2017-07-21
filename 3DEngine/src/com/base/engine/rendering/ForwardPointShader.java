package com.base.engine.rendering;

import com.base.engine.components.BaseLight;
import com.base.engine.components.PointLight;
import com.base.engine.core.Matrix4f;
import com.base.engine.core.RenderingEngine;
import com.base.engine.core.Transform;

public class ForwardPointShader extends Shader {
    
    private static class Singleton {
        private static final ForwardPointShader instance = new ForwardPointShader();
    }
    
    public static ForwardPointShader getInstance() {
        return ForwardPointShader.Singleton.instance;
    }
    
    private ForwardPointShader() {
        super();
        
        addVertexShaderFromFile("forwardPointVertex.glsl");
        addFragmentShaderFromFile("forwardPointFragment.glsl");
        compileShader();
        
        addUniform("model");
        addUniform("MVP");
        
        addUniform("specularIntensity");
        addUniform("specularPower");
        addUniform("eyePos");
        
        addUniform("pointLight.base.color");
        addUniform("pointLight.base.intensity");
        addUniform("pointLight.attenuation.constant");
        addUniform("pointLight.attenuation.linear");
        addUniform("pointLight.attenuation.exponent");
        addUniform("pointLight.position");
        addUniform("pointLight.range");
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
        
        setUniformPointLight("pointLight", (PointLight) renderingEngine.getActiveLight());
    }
    
    private void setUniformBaseLight(String uniformName, BaseLight baseLight) {
        setUniform(uniformName + ".color", baseLight.getColor());
        setUniformf(uniformName + ".intensity", baseLight.getIntensity());
    }
    
    private void setUniformPointLight(String uniformName, PointLight pointLight) {
        setUniformBaseLight(uniformName + ".base", pointLight);
        setUniformf(uniformName + ".attenuation.constant", pointLight.getConstant());
        setUniformf(uniformName + ".attenuation.linear", pointLight.getLinear());
        setUniformf(uniformName + ".attenuation.exponent", pointLight.getExponent());
        setUniform(uniformName + ".position", pointLight.getTransform().getPosition());
        setUniformf(uniformName + ".range", pointLight.getRange());
    }
}
