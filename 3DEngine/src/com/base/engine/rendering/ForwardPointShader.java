package com.base.engine.rendering;

import com.base.engine.core.Matrix4f;
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
    public void updateUniforms(Transform transform, Material material) {
        Matrix4f worldMatrix = transform.getTransformation();
        Matrix4f projectedMatrix = getRenderingEngine().getMainCamera().getViewProjection().mul(worldMatrix);
        material.getTexture().bind();

        setUniform("model", worldMatrix);
        setUniform("MVP", projectedMatrix);

        setUniformf("specularIntensity", material.getSpecularIntensity());
        setUniformf("specularPower", material.getSpecularPower());
        setUniform("eyePos", getRenderingEngine().getMainCamera().getPos());

        setUniform("pointLight", getRenderingEngine().getPointLight());
    }

    private void setUniform(String uniformName, PointLight pointLight) {
        setUniform( uniformName + ".base", pointLight.getBaseLight());
        setUniformf(uniformName + ".attenuation.constant", pointLight.getAttenuation().getConstant());
        setUniformf(uniformName + ".attenuation.linear", pointLight.getAttenuation().getLinear());
        setUniformf(uniformName + ".attenuation.exponent", pointLight.getAttenuation().getExponent());
        setUniform( uniformName + ".position", pointLight.getPosition());
        setUniformf(uniformName + ".range", pointLight.getRange());
    }

    private void setUniform(String uniformName, BaseLight baseLight) {
        setUniform( uniformName + ".color", baseLight.getColor());
        setUniformf(uniformName + ".intensity", baseLight.getIntensity());
    }
}
