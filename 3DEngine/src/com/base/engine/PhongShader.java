package com.base.engine;

import static org.lwjgl.opengl.GL20.glUniform1i;

/**
 * Created by soldi on 2017-07-18.
 */

public class PhongShader extends Shader {
    
    public static final int MAX_POINT_LIGHTS = 4;

    private static class Singleton {
        private static final PhongShader instance = new PhongShader();
    }

    public static PhongShader getInstance() {
        return Singleton.instance;
    }
    
    private static Vector3f ambientLight = new Vector3f(0.1f, 0.1f, 0.1f);
    private static DirectionalLight directionalLight = new DirectionalLight(
        new BaseLight(new Vector3f(1, 1, 1), 0),
        new Vector3f(0 ,0 , 0));
    private static PointLight[] pointLights = new PointLight[] {};
    
    private PhongShader() {
        super();

        addVertexShader(ResourceLoader.loadShader("phongVertex.glsl"));
        addFragmentShader(ResourceLoader.loadShader("phongFragment.glsl"));
        compileShader();

        addUniform("transform");
        addUniform("projectedTransform");
        addUniform("baseColor");
        
        addUniform("ambientLight");
        
        addUniform("directionalLight.base.color");
        addUniform("directionalLight.base.intensity");
        addUniform("directionalLight.direction");
        
        addUniform("specularIntensity");
        addUniform("specularPower");
        addUniform("eyePos");
        
        for (int i = 0; i < MAX_POINT_LIGHTS; i++) {
            addUniform("pointLights[" + i + "].base.color");
            addUniform("pointLights[" + i + "].base.intensity");
            addUniform("pointLights[" + i + "].attenuation.constant");
            addUniform("pointLights[" + i + "].attenuation.linear");
            addUniform("pointLights[" + i + "].attenuation.exponent");
            addUniform("pointLights[" + i + "].position");
            addUniform("pointLights[" + i + "].range");
        }
    }

    @Override
    public void updateUniforms(Transform transform, Material material) {
        if (material.getTexture() != null)
            material.getTexture().bind();
        else
            RenderUtil.unbindTextures();

        setUniform("transform", transform.getTransformation());
        setUniform("projectedTransform", transform.getProjectedTransformation());
        setUniform("baseColor", material.getColor());
        
        setUniform("ambientLight", ambientLight);
        
        setUniform("directionalLight", directionalLight);
        
        setUniformf("specularIntensity", material.getSpecularIntensity());
        setUniformf("specularPower", material.getSpecularPower());
        setUniform("eyePos", transform.getCamera().getPos());
        
        for (int i = 0; i < pointLights.length; i++)
            setUniform("pointLights[" + i + "]", pointLights[i]);
    }
    
    public static void setAmbientLight(Vector3f ambientLight) {
        PhongShader.ambientLight = ambientLight;
    }
    
    public static void setDirectionalLight(DirectionalLight directionalLight) {
        PhongShader.directionalLight = directionalLight;
    }
    
    public static void setPointLights(PointLight[] pointLights) {
        if (pointLights.length > MAX_POINT_LIGHTS) {
            System.err.println("Error: You passed in too many point lights. Max allowed is " + MAX_POINT_LIGHTS);
            new Exception().printStackTrace();
            System.exit(1);
        }
        
        PhongShader.pointLights = pointLights;
    }
    
    public void setUniform(String uniformName, DirectionalLight directionalLight) {
        setUniform(uniformName + ".base", directionalLight.getBase());
        setUniform(uniformName + ".direction", directionalLight.getDirection());
    }
    
    private void setUniform(String uniformName, BaseLight baseLight) {
        setUniform( uniformName + ".color", baseLight.getColor());
        setUniformf(uniformName + ".intensity", baseLight.getIntentsity());
    }
    
    private void setUniform(String uniformName, PointLight pointLight) {
        setUniform( uniformName + ".base", pointLight.getBaseLight());
        setUniformf(uniformName + ".attenuation.constant", pointLight.getAttenuation().getConstant());
        setUniformf(uniformName + ".attenuation.linear", pointLight.getAttenuation().getLinear());
        setUniformf(uniformName + ".attenuation.exponent", pointLight.getAttenuation().getExponent());
        setUniform( uniformName + ".position", pointLight.getPosition());
        setUniformf(uniformName + ".range", pointLight.getRange());
    }
}
