package com.base.engine.core;

public class Transform {
    private Transform parent;

    private Vector3f position;
    private Quaternion rotation;
    private Vector3f scale;

    private Matrix4f transformation;

    private boolean isChanged;

    public Transform() {
        position = new Vector3f(0, 0, 0);
        rotation = new Quaternion(0, 0, 0, 1);
        scale = new Vector3f(1, 1, 1);

        setChanged(true);
    }
    
    public void rotate(Vector3f axis, float angle) {
        // The order of multiplication is really important..
        rotation = new Quaternion(axis, angle).mul(rotation).normalized();
    }

    public void lookAt(Vector3f point, Vector3f up) {
        rotation = getLookAtDirection(point, up);
    }

    public Quaternion getLookAtDirection(Vector3f point, Vector3f up) {
        return new Quaternion(new Matrix4f().initRotation(point.sub(position).normalized(), up));
    }

    public Matrix4f getTransformation() {
        if (isChanged()) {
            Matrix4f translationMatrix = new Matrix4f().initTranslation(
                    position.getX(),
                    position.getY(),
                    position.getZ());

            Matrix4f rotationMatrix = rotation.toRotationMatrix();

            Matrix4f scaleMatrix = new Matrix4f().initScale(
                    scale.getX(),
                    scale.getY(),
                    scale.getZ());

            this.transformation = translationMatrix.mul(rotationMatrix.mul(scaleMatrix));  // scale -> rotation -> position

            setChanged(false);
        }

        if (parent != null)
            return parent.getTransformation().mul(transformation);
        else
            return transformation;
    }

    public Vector3f getTransformedPosition() {
        if (parent != null)
            return parent.getTransformation().getTransformedPosition(position);
        else
            return position;
    }

    public Quaternion getTransformedRotation() {
        if (parent != null)
            return parent.getTransformedRotation().mul(rotation);
//            return rotation.mul( parent.getTransformedRotation() );
        else
            return rotation;
    }

    public void setParent(Transform parent) {
        this.parent = parent;
        setChanged(true);
    }

    public Vector3f getPosition() {
        return position;
    }
    
    public void setPosition(Vector3f position) {
        this.position = position;
        setChanged(true);
    }
    
    public Quaternion getRotation() {
        return rotation;
    }
    
    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
        setChanged(true);
    }
    
    public Vector3f getScale() {
        return scale;
    }
    
    public void setScale(Vector3f scale) {
        this.scale = scale;
        setChanged(true);
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }
}
