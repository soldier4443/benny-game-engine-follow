package com.base.engine.rendering;

import com.base.engine.core.Matrix4f;
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
    public void updateUniforms(Transform transform, Material material) {
        Matrix4f projectedMatrix = getRenderingEngine().getMainCamera().getViewProjection().mul( transform.getTransformation() );
        material.getTexture().bind();

        setUniform("MVP", projectedMatrix);
        setUniform("ambientIntensity", getRenderingEngine().getAmbientLight());
    }
}
