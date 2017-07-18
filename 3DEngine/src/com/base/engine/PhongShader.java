package com.base.engine;

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
    
    private static Vector3f ambientLight;
    
    private PhongShader() {
        super();

        addVertexShader(ResourceLoader.loadShader("phongVertex.glsl"));
        addFragmentShader(ResourceLoader.loadShader("phongFragment.glsl"));
        compileShader();

        addUniform("transform");
        addUniform("baseColor");
        addUniform("ambientLight");
    }

    @Override
    public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material) {
        if (material.getTexture() != null)
            material.getTexture().bind();
        else
            RenderUtil.unbindTextures();

        setUniform("transform", projectedMatrix);
        setUniform("baseColor", material.getColor());
        setUniform("ambientLight", ambientLight);
    }
    
    public static Vector3f getAmbientLight() {
        return ambientLight;
    }
    
    public static void setAmbientLight(Vector3f ambientLight) {
        PhongShader.ambientLight = ambientLight;
    }
}
