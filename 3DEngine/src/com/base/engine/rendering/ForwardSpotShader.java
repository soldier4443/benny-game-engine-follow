package com.base.engine.rendering;

import com.base.engine.components.BaseLight;
import com.base.engine.components.PointLight;
import com.base.engine.components.SpotLight;
import com.base.engine.core.Matrix4f;
import com.base.engine.core.Transform;

public class ForwardSpotShader extends Shader {

    private static class Singleton {
        private static final ForwardSpotShader instance = new ForwardSpotShader();
    }

    public static ForwardSpotShader getInstance() {
        return ForwardSpotShader.Singleton.instance;
    }

    private ForwardSpotShader() {
        super();

        addVertexShaderFromFile("forwardSpotVertex.glsl");
        addFragmentShaderFromFile("forwardSpotFragment.glsl");
        compileShader();

        addUniform("model");
        addUniform("MVP");

        addUniform("specularIntensity");
        addUniform("specularPower");
        addUniform("eyePos");

        addUniform("spotLight.pointLight.base.color");
        addUniform("spotLight.pointLight.base.intensity");
        addUniform("spotLight.pointLight.attenuation.constant");
        addUniform("spotLight.pointLight.attenuation.linear");
        addUniform("spotLight.pointLight.attenuation.exponent");
        addUniform("spotLight.pointLight.position");
        addUniform("spotLight.pointLight.range");
        addUniform("spotLight.direction");
        addUniform("spotLight.cutoff");
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

        setUniformSpotLight("spotLight", (SpotLight) getRenderingEngine().getActiveLight());
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
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniformf(uniformName + ".range", pointLight.getRange());
    }

    private void setUniformSpotLight(String uniformName, SpotLight spotLight) {
        setUniformPointLight(uniformName + ".pointLight", spotLight);
        setUniform(uniformName + ".direction", spotLight.getDirection());
        setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
    }
}
