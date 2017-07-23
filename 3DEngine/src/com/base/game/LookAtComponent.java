package com.base.game;

import com.base.engine.components.GameComponent;
import com.base.engine.core.Quaternion;
import com.base.engine.core.RenderingEngine;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Shader;

/**
 * Created by soldi on 2017-07-23.
 */
public class LookAtComponent extends GameComponent
{
    RenderingEngine renderingEngine;

    @Override
    public void update(float delta)
    {
        if(renderingEngine != null)
        {
            Quaternion newRot = getTransform().getLookAtDirection(renderingEngine.getMainCamera().getTransform().getTransformedPosition(),
                    new Vector3f(0,1,0));
            //getTransform().getRot().getUp());

            getTransform().setRotation(getTransform().getRotation().nlerp(newRot, delta * 5.0f, true));
            //getTransform().setRot(getTransform().getRot().slerp(newRot, delta * 5.0f, true));
        }
    }

    @Override
    public void render(Shader shader, RenderingEngine renderingEngine)
    {
        this.renderingEngine = renderingEngine;
    }
}
