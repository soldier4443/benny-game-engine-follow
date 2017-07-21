package com.base.engine.rendering.mesh;

import com.base.engine.core.Util;
import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nyh0111 on 2017-07-21.
 */

public class OBJModel {
    private ArrayList<Vector3f> positions;
    private ArrayList<Vector2f> texturePositions;
    private ArrayList<Vector3f> normals;
    private ArrayList<OBJIndex> indices;
    
    private boolean hasTexCoords;
    private boolean hasNormals;
    
    public OBJModel(String fileName) {
        positions = new ArrayList<>();
        texturePositions = new ArrayList<>();
        normals = new ArrayList<>();
        indices = new ArrayList<>();
        
        hasTexCoords = false;
        hasNormals = false;
        
        BufferedReader meshReader = null;
        
        try {
            meshReader = new BufferedReader(new FileReader(fileName));
            String line;
            
            while ((line = meshReader.readLine()) != null) {
                String[] tokens = line.split(" ");
                tokens = Util.removeEmptyStrings(tokens);
                
                if (tokens.length == 0 || tokens[0].equals("#"))
                    continue;
                else if (tokens[0].equals("v")) {
                    positions.add(new Vector3f(Float.valueOf(tokens[1]),
                        Float.valueOf(tokens[2]),
                        Float.valueOf(tokens[3])));
                } else if (tokens[0].equals("vt")) {
                    texturePositions.add(new Vector2f(Float.valueOf(tokens[1]),
                        Float.valueOf(tokens[2])));
                } else if (tokens[0].equals("vn")) {
                    normals.add(new Vector3f(Float.valueOf(tokens[1]),
                        Float.valueOf(tokens[2]),
                        Float.valueOf(tokens[3])));
                } else if (tokens[0].equals("f")) {
                    for (int i = 0; i < tokens.length - 3; i++) {
                        indices.add(parseOBJIndex(tokens[1]));
                        indices.add(parseOBJIndex(tokens[2 + i]));
                        indices.add(parseOBJIndex(tokens[3 + i]));
                    }
                }
            }
            
            meshReader.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public IndexedModel toIndexedModel() {
        IndexedModel model = new IndexedModel();
        IndexedModel normalModel = new IndexedModel();
        HashMap<OBJIndex, Integer> modelIndexMap = new HashMap<>();
        HashMap<Integer, Integer> normalIndexMap = new HashMap<>();
        HashMap<Integer, Integer> indexMap = new HashMap<>();

        for (int i = 0; i < indices.size(); i++) {
            OBJIndex currentIndex = indices.get(i);
            
            Vector3f currentPosition = positions.get(currentIndex.positionIndex);
            Vector2f currentTexCoord;
            Vector3f currentNormal;
            
            if (hasTexCoords)
                currentTexCoord = texturePositions.get(currentIndex.texturePositionIndex);
            else
                currentTexCoord = new Vector2f(0, 0);
            
            if (hasNormals)
                currentNormal = normals.get(currentIndex.normalIndex);
            else
                currentNormal = new Vector3f(0, 0, 0);

            Integer modelIndex = modelIndexMap.get(currentIndex);

            if (modelIndex == null) {
                // result.getPosition().size() == <last index + 1>
                modelIndexMap.put(currentIndex, model.getPositions().size());
                modelIndex = model.getPositions().size();

                model.getPositions().add(currentPosition);
                model.getTexturePositions().add(currentTexCoord);
                if (hasNormals)
                    model.getNormals().add(currentNormal);
            }

            Integer normalModelIndex = normalIndexMap.get(currentIndex.positionIndex);

            if (normalModelIndex == null) {
                // result.getPosition().size() == <last index + 1>
                normalIndexMap.put(currentIndex.positionIndex, normalModel.getPositions().size());
                normalModelIndex = normalModel.getPositions().size();

                normalModel.getPositions().add(currentPosition);
                normalModel.getTexturePositions().add(currentTexCoord);
                normalModel.getNormals().add(currentNormal);
            }

            model.getIndices().add(modelIndex);
            normalModel.getIndices().add(normalModelIndex);

            indexMap.put(modelIndex, normalModelIndex);
        }

        if (!hasNormals) {
            normalModel.calculateNormals();

            for (int i = 0; i < model.getPositions().size(); i++) {
                model.getNormals().add(normalModel.getNormals().get(indexMap.get(i)));
            }
        }

        return model;
    }
    
    private OBJIndex parseOBJIndex(String token) {
        String[] values = token.split("/");
        
        OBJIndex result = new OBJIndex();
        result.positionIndex = Integer.parseInt(values[0]) - 1;
        
        if (values.length > 1) {
            hasTexCoords = true;
            result.texturePositionIndex = Integer.parseInt(values[1]) - 1;
            
            if (values.length > 2) {
                hasNormals = true;
                result.normalIndex = Integer.parseInt(values[2]) - 1;
            }
        }
        
        return result;
    }
}
