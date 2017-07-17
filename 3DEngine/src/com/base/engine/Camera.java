package com.base.engine;

/**
 * Created by nyh0111 on 2017-07-17.
 */

public class Camera {
    private Vector3f pos;
    private Vector3f forward;
    private Vector3f up;
    
    public Camera() {
        this(Vector3f.ZERO, Vector3f.Z, Vector3f.Y);
    }
    
    public Camera(Vector3f pos, Vector3f forward, Vector3f up) {
        this.pos = pos;
        this.forward = forward;
        this.up = up;
        
        up.normalize();
        forward.normalize();
    }
    
    public void input() {
        float moveAmount = (float)(10 * Time.getDelta());
        float rotationAmount = (float)(100 * Time.getDelta());
    
        if (Input.getKey(Input.KEY_W))  move(getForward(), moveAmount);
        if (Input.getKey(Input.KEY_A))  move(getLeft(), moveAmount);
        if (Input.getKey(Input.KEY_S))  move(getForward(), -moveAmount);
        if (Input.getKey(Input.KEY_D))  move(getRight(), moveAmount);
        
        if (Input.getKey(Input.KEY_UP))     rotateX(-rotationAmount);
        if (Input.getKey(Input.KEY_DOWN))   rotateX(rotationAmount);
        if (Input.getKey(Input.KEY_LEFT))   rotateY(-rotationAmount);
        if (Input.getKey(Input.KEY_RIGHT))  rotateY(rotationAmount);
    }
    
    public void move(Vector3f dir, float amount) {
        pos = pos.add(dir.mul(amount));
    }
    
    // Tiltiing left / right
    public void rotateY(float angle) {
        // 1. Calculate horizontal axis of the camera
        // 2. rotate around the horizontal axis
        // 3. calculate new up vector after rotation
    
        Vector3f horizontalAxis = Vector3f.Y.cross(forward);
        horizontalAxis.normalize();
    
        forward.rotate(angle, Vector3f.Y).normalize();
    
        up = forward.cross(horizontalAxis);
        up.normalize();
    }
    
    // Tilting up / down
    public void rotateX(float angle) {
        // 1. Calculate horizontal axis of the camera
        // 2. rotate around the horizontal axis
        // 3. calculate new up vector after rotation
        
        Vector3f horizontalAxis = Vector3f.Y.cross(forward);
        horizontalAxis.normalize();
        
        forward.rotate(angle, horizontalAxis).normalize();
        
        up = forward.cross(horizontalAxis);
        up.normalize();
    }
    
    public Vector3f getLeft() {
        Vector3f left = forward.cross(up);
        return left.normalize();
    }
    
    public Vector3f getRight() {
        Vector3f right = up.cross(forward);
        return right.normalize();
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
