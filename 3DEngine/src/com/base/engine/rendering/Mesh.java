package com.base.engine.rendering;

import com.base.engine.core.Util;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.mesh.IndexedModel;
import com.base.engine.rendering.mesh.OBJModel;

import com.base.engine.rendering.resource.MeshResources;
import org.lwjgl.opengl.GL15;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh {
    private static HashMap<String, MeshResources> loadedResources = new HashMap<>();
    private MeshResources resources;
    private String fileName;

    public Mesh(String fileName) {
        this.fileName = fileName;
        MeshResources oldResource = loadedResources.get(fileName);

        if (oldResource != null) {
            resources = oldResource;
            resources.addReference();
        } else {
            loadMesh(fileName);

            loadedResources.put(fileName, resources);
        }
    }
    
    public Mesh(Vertex[] vertices, int[] indices) {
        this(vertices, indices, false);
    }
    
    public Mesh(Vertex[] vertices, int[] indices, boolean calculateNormals) {
        fileName = "";
        addVertices(vertices, indices, calculateNormals);
    }

    @Override
    protected void finalize() throws Throwable {
        if (resources.removeReference() && !fileName.isEmpty())
            loadedResources.remove(fileName);
    }

    private void addVertices(Vertex[] vertices, int[] indices) {
        addVertices(vertices, indices, false);
    }
    
    private void addVertices(Vertex[] vertices, int[] indices, boolean calculateNormals) {
        if (calculateNormals)
            calculateNormals(vertices, indices);

        resources = new MeshResources(indices.length);
        
        // Java array != Native array
        // So this conversion..
        glBindBuffer(GL_ARRAY_BUFFER, resources.getVbo());
        GL15.glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resources.getIbo());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
    }
    
    public void draw() {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        
        glBindBuffer(GL_ARRAY_BUFFER, resources.getVbo());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12); // offset : 4 * 3
        glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20); // offset : 4 * 3

//		glDrawArrays(GL_TRIANGLES, 0, size);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resources.getIbo()); // What a critical fault!! I totally miss binding it..
        glDrawElements(GL_TRIANGLES, resources.getSize(), GL_UNSIGNED_INT, 0);
        
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
    }
    
    private void calculateNormals(Vertex[] vertices, int[] indices) {
        for (int i = 0; i < indices.length; i += 3) {
            int i0 = indices[i];
            int i1 = indices[i + 1];
            int i2 = indices[i + 2];
            
            Vector3f v1 = vertices[i1].getPosition().sub(vertices[i0].getPosition());  // First line
            Vector3f v2 = vertices[i2].getPosition().sub(vertices[i0].getPosition());  // Second line
            
            Vector3f normal = v1.cross(v2).normalized();
            
            vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
            vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
            vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
        }
        
        for (int i = 0; i < vertices.length; i++)
            vertices[i].setNormal(vertices[i].getNormal().normalized());
    }
    
    private void loadMesh(String fileName) {
        String[] splitArray = fileName.split("\\.");
        String ext = splitArray[splitArray.length - 1];
        
        if (!ext.equals("obj")) {
            System.err.println("Error: File format not supported for mesh data: " + ext);
            new Exception().printStackTrace();
            System.exit(1);
        }
        
        OBJModel test = new OBJModel("res/models/" + fileName);
        IndexedModel model = test.toIndexedModel();
        model.calculateNormals();
        
        ArrayList<Vertex> vertices = new ArrayList<>();
        
        for (int i = 0; i < model.getPositions().size(); i++) {
            vertices.add(new Vertex(model.getPositions().get(i),
                model.getTexturePositions().get(i),
                model.getNormals().get(i)));
        }
        
        Vertex[] vertexData = new Vertex[vertices.size()];
        vertices.toArray(vertexData);
        
        Integer[] indexData = new Integer[model.getIndices().size()];
        model.getIndices().toArray(indexData);
        
        addVertices(vertexData, Util.toIntArray(indexData), true);
    }
}
