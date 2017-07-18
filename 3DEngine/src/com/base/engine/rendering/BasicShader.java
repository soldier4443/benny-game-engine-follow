package com.base.engine.rendering;

import com.base.engine.core.Transform;

/**
 * Created by soldi on 2017-07-18.
 */

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
        if (material.getTexture() != null)
            material.getTexture().bind();
        else
            RenderUtil.unbindTextures();

        setUniform("transform", transform.getProjectedTransformation());
        setUniform("color", material.getColor());
    }
}
