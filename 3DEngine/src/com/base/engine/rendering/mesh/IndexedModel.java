package com.base.engine.rendering.mesh;

import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;

import java.util.ArrayList;

/**
 * Created by nyh0111 on 2017-07-21.
 */

class IndexedModel {
    private ArrayList<Vector3f> positions;
    private ArrayList<Vector2f> texturePositions;
    private ArrayList<Vector3f> normals;
    private ArrayList<Integer> indices;
    
    public IndexedModel() {
        positions = new ArrayList<>();
        texturePositions = new ArrayList<>();
        normals = new ArrayList<>();
        indices = new ArrayList<>();
    }
    
    public ArrayList<Vector3f> getPositions() {
        return positions;
    }
    
    public ArrayList<Vector2f> getTexturePositions() {
        return texturePositions;
    }
    
    public ArrayList<Vector3f> getNormals() {
        return normals;
    }
    
    public ArrayList<Integer> getIndices() {
        return indices;
    }
}
