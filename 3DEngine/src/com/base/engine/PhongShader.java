package com.base.engine;

import static org.lwjgl.opengl.GL20.glUniform1i;

/**
 * Created by soldi on 2017-07-18.
 */

public class PhongShader extends Shader {

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
    }

    @Override
    public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material) {
        if (material.getTexture() != null)
            material.getTexture().bind();
        else
            RenderUtil.unbindTextures();

        setUniform("transform", worldMatrix);
        setUniform("projectedTransform", projectedMatrix);
        setUniform("baseColor", material.getColor());
        
        setUniform("ambientLight", ambientLight);
        
        setUniform("directionalLight", directionalLight);
    }
    
    public static Vector3f getAmbientLight() {
        return ambientLight;
    }
    
    public static void setAmbientLight(Vector3f ambientLight) {
        PhongShader.ambientLight = ambientLight;
    }
    
    public static DirectionalLight getDirectionalLight() {
        return directionalLight;
    }
    
    public static void setDirectionalLight(DirectionalLight directionalLight) {
        PhongShader.directionalLight = directionalLight;
    }
    
    public void setUniform(String uniformName, DirectionalLight directionalLight) {
        setUniform(uniformName + ".base", directionalLight.getBase());
        setUniform(uniformName + ".direction", directionalLight.getDirection());
    }
    
    private void setUniform(String uniformName, BaseLight baseLight) {
        setUniform(uniformName + ".color", baseLight.getColor());
        setUniformf(uniformName + ".intensity", baseLight.getIntentsity());
    }
}
