package com.base.engine.rendering;

import com.base.engine.core.*;

public class Camera {
    private Vector3f pos;
    private Vector3f forward;
    private Vector3f up;
    private Matrix4f projection;
    
    boolean mouseLocked = false;
    Vector2f centerPosition = new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2);
    
    public Camera(float fov, float aspect, float zNear, float zFar) {
        this.pos = Vector3f.ZERO;
        this.forward = Vector3f.Z.normalized();
        this.up = Vector3f.Y.normalized();
        this.projection = new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
    }
    
    public Matrix4f getViewProjection() {
        Matrix4f cameraRotation = new Matrix4f().initRotation(forward, up);
        Matrix4f cameraTranslation = new Matrix4f().initTranslation(pos.negate());
        
        return projection.mul(cameraRotation.mul(cameraTranslation));
    }
    
    public void input(float deltaTime) {
        float sensitivity = 0.2f;
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
        
        if (Input.getKey(Input.KEY_W)) move(getForward(), moveAmount);
        if (Input.getKey(Input.KEY_A)) move(getLeft(), moveAmount);
        if (Input.getKey(Input.KEY_S)) move(getForward(), -moveAmount);
        if (Input.getKey(Input.KEY_D)) move(getRight(), moveAmount);

//        if (Input.getKey(Input.KEY_UP))     rotateX(-rotationAmount);
//        if (Input.getKey(Input.KEY_DOWN))   rotateX(rotationAmount);
//        if (Input.getKey(Input.KEY_LEFT))   rotateY(-rotationAmount);
//        if (Input.getKey(Input.KEY_RIGHT))  rotateY(rotationAmount);
        
        if (mouseLocked) {
            Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);
            
            boolean rotY = deltaPos.getX() != 0;
            boolean rotX = deltaPos.getY() != 0;
            
            if (rotY)
                rotateY(deltaPos.getX() * sensitivity);
            
            if (rotX)
                rotateX(-deltaPos.getY() * sensitivity);
            
            if (rotX || rotY) {
                Input.setMousePosition(new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2));
            }
            
        }
        
        
    }
    
    public void move(Vector3f dir, float amount) {
        pos = pos.add(dir.mul(amount));
    }
    
    // Tiltiing left / right
    public void rotateY(float angle) {
        // 1. Calculate horizontal axis of the camera
        // 2. rotate around the horizontal axis
        // 3. calculate new up vector after rotation
        
        Vector3f horizontalAxis = Vector3f.Y.cross(forward).normalized();
        
        forward = forward.rotate(angle, Vector3f.Y).normalized();
        
        up = forward.cross(horizontalAxis).normalized();
    }
    
    // Tilting up / down
    public void rotateX(float angle) {
        // 1. Calculate horizontal axis of the camera
        // 2. rotate around the horizontal axis
        // 3. calculate new up vector after rotation
        
        Vector3f horizontalAxis = Vector3f.Y.cross(forward).normalized();
        
        forward = forward.rotate(angle, horizontalAxis).normalized();
        
        up = forward.cross(horizontalAxis).normalized();
    }
    
    public Vector3f getLeft() {
        return forward.cross(up).normalized();
    }
    
    public Vector3f getRight() {
        return up.cross(forward).normalized();
    }
    
    public Vector3f getPos() {
        return pos;
    }
    
    public void setPos(Vector3f pos) {
        this.pos = pos;
    }
    
    public Vector3f getForward() {
        return forward;
    }
    
    public void setForward(Vector3f forward) {
        this.forward = forward;
    }
    
    public Vector3f getUp() {
        return up;
    }
    
    public void setUp(Vector3f up) {
        this.up = up;
    }
}
