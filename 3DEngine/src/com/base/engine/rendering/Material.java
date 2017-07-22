package com.base.engine.rendering;

import com.base.engine.core.Vector3f;
import com.base.engine.rendering.resource.MappedValues;

import java.util.HashMap;

public class Material extends MappedValues {
    private HashMap<String, Texture> textureHashMap;
    
    public Material() {
        super();
        textureHashMap = new HashMap<>();
    }

    public void addTexture(String name, Texture texture) {
        textureHashMap.put(name, texture);
    }
    
    public Texture getTexture(String name) {
        Texture result = textureHashMap.get(name);
        
        if (result != null)
            return result;
        else
            return new Texture("test.png");
    }
}
