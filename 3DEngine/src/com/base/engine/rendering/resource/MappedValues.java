package com.base.engine.rendering.resource;

import com.base.engine.core.Vector3f;

import java.util.HashMap;

/**
 * Created by soldi on 2017-07-22.
 */
public abstract class MappedValues {
    private HashMap<String, Vector3f> vector3fHashMap = new HashMap<>();
    private HashMap<String, Float> floatHashMap = new HashMap<>();

    public void addVector3f(String name, Vector3f vector3f) {
        vector3fHashMap.put(name, vector3f);
    }

    public void addFloat(String name, Float floatValue) {
        floatHashMap.put(name, floatValue);
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
