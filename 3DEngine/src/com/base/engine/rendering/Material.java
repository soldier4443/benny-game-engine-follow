package com.base.engine.rendering;

import com.base.engine.core.Vector3f;

import java.util.HashMap;

public class Material {
    private HashMap<String, Texture> textureHashMap;
    private HashMap<String, Vector3f> vector3fHashMap;
    private HashMap<String, Float> floatHashMap;
    
    public Material() {
        textureHashMap = new HashMap<>();
        vector3fHashMap = new HashMap<>();
        floatHashMap = new HashMap<>();
    }
    
    public void addTexture(String name, Texture texture) {
        textureHashMap.put(name, texture);
    }
    
    public void addVector3f(String name, Vector3f vector3f) {
        vector3fHashMap.put(name, vector3f);
    }
    
    public void addFloat(String name, Float floatValue) {
        floatHashMap.put(name, floatValue);
    }
    
    public Texture getTexture(String name) {
        Texture result = textureHashMap.get(name);
        
        if (result != null)
            return result;
        else
            return new Texture("test.png");
    }
    
    public Vector3f getVector3f(String name) {
        Vector3f result = vector3fHashMap.get(name);
    
        if (result != null)
            return result;
        else
            return new Vector3f(0, 0, 0);
    }
    
    public Float getFloat(String name) {
        Float result = floatHashMap.get(name);
    
        if (result != null)
            return result;
        else
            return 0.0f;
    }
}
