package com.base.engine.components;

import com.base.engine.core.Input;
import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Window;

/**
 * Created by soldi on 2017-07-23.
 */
public class FreeLook extends GameComponent {

    private boolean mouseLocked = false;
    private float sensitivity;
    private int unlockMouseKey;

    public FreeLook(float sensitivity) {
        this(sensitivity, Input.KEY_ESCAPE);
    }

    public FreeLook(float sensitivity, int unlockMouseKey) {
        this.sensitivity = sensitivity;
        this.unlockMouseKey = unlockMouseKey;
    }

    @Override
    public void input(float delta) {
        Vector2f centerPosition = new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2);

        if (Input.getKey(unlockMouseKey)) {
            Input.setCursor(true);
            mouseLocked = false;
        }

        if (Input.getMouseDown(0)) {
            Input.setMousePosition(centerPosition);
            Input.setCursor(false);
            mouseLocked = true;
        }

        if (mouseLocked) {
            Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);

            boolean rotY = deltaPos.getX() != 0;
            boolean rotX = deltaPos.getY() != 0;

            if (rotY)
                getTransform().rotate(Vector3f.Y, (float) Math.toRadians(deltaPos.getX() * sensitivity));

            if (rotX)
                getTransform().rotate(getTransform().getRotation().getRight(), (float) Math.toRadians(-deltaPos.getY() * sensitivity));

            if (rotY || rotX)
                Input.setMousePosition(new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2));
        }
    }
}
