package com.base.engine.components;

import com.base.engine.core.RenderingEngine;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.ForwardDirectionalShader;

public class DirectionalLight extends BaseLight {
    public DirectionalLight(Vector3f color, float intensity) {
        super(color, intensity);

        setShader(ForwardDirectionalShader.getInstance());
    }

    public Vector3f getDirection() {
        return getTransform().getRotation().getForward();
    }
}
