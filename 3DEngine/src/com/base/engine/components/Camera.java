package com.base.engine.components;

import com.base.engine.core.*;
import com.base.engine.rendering.Window;

public class Camera extends GameComponent {
    private Matrix4f projection;
    
    boolean mouseLocked = false;
    Vector2f centerPosition = new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2);
    
    public Camera(float fov, float aspect, float zNear, float zFar) {
        this.projection = new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
    }
    
    @Override
    public void addToRenderingEngine(RenderingEngine renderingEngine) {
        renderingEngine.setMainCamera(this);
    }
    
    public Matrix4f getViewProjection() {
        Matrix4f cameraRotation = getTransform().getRotation().toRotationMatrix();
        Matrix4f cameraTranslation = new Matrix4f().initTranslation(getTransform().getPosition().negate());
        
        return projection.mul(cameraRotation.mul(cameraTranslation));
    }
    
    public void input(float deltaTime) {
        float sensitivity = -0.5f;
        float moveAmount = (float) (10 * deltaTime);
//        float rotationAmount = (float)(100 * Time.getDelta());
        
        if (Input.getKey(Input.KEY_ESCAPE)) {
            Input.setCursor(true);
            mouseLocked = false;
        }
        
        if (Input.getMouseDown(0)) {
            Input.setCursor(false);
            mouseLocked = true;
        }
        
        if (Input.getKey(Input.KEY_W)) move(getTransform().getRotation().getForward(), moveAmount);
        if (Input.getKey(Input.KEY_A)) move(getTransform().getRotation().getLeft(), moveAmount);
        if (Input.getKey(Input.KEY_S)) move(getTransform().getRotation().getForward(), -moveAmount);
        if (Input.getKey(Input.KEY_D)) move(getTransform().getRotation().getRight(), moveAmount);
        
        if (mouseLocked) {
            Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);
    
            boolean rotY = deltaPos.getX() != 0;
            boolean rotX = deltaPos.getY() != 0;
    
            if(rotY)
                getTransform().setRotation(
                    getTransform().getRotation().mul( new Quaternion().initRotation(
                            Vector3f.Y,
                            (float) Math.toRadians(deltaPos.getX() * sensitivity))).normalized());
            if(rotX)
                getTransform().setRotation(
                    getTransform().getRotation().mul( new Quaternion().initRotation(
                        getTransform().getRotation().getRight(),
                        ((float) Math.toRadians(-deltaPos.getY() * sensitivity)))).normalized());
    
            if(rotY || rotX)
                Input.setMousePosition(new Vector2f(Window.getWidth()/2, Window.getHeight()/2));
        }
    }
    
    public void move(Vector3f dir, float amount) {
        getTransform().setPosition(
            getTransform().getPosition().add(dir.mul(amount)));
    }
}
