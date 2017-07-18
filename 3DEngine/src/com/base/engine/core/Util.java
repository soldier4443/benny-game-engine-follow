package com.base.engine.core;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import com.base.engine.rendering.Vertex;
import org.lwjgl.BufferUtils;

public class Util {
	// This method do nothing now, but later we can easily change source with this method!
    public static FloatBuffer createFloatBuffer(int size) {
        return BufferUtils.createFloatBuffer(size);
    }
    
    public static IntBuffer createIntBuffer(int size) {
        return BufferUtils.createIntBuffer(size);
    }
	
	public static FloatBuffer createFlippedBuffer(Vertex[] vertices) {
		FloatBuffer buffer = createFloatBuffer(vertices.length * Vertex.SIZE);
		
		for (int i = 0; i < vertices.length; i++) {
			buffer.put(vertices[i].getPosition().getX());
			buffer.put(vertices[i].getPosition().getY());
			buffer.put(vertices[i].getPosition().getZ());

			buffer.put(vertices[i].getTexturePosition().getX());
			buffer.put(vertices[i].getTexturePosition().getY());
			
			buffer.put(vertices[i].getNormal().getX());
			buffer.put(vertices[i].getNormal().getY());
			buffer.put(vertices[i].getNormal().getZ());
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Matrix4f value) {
		FloatBuffer buffer = createFloatBuffer(4 * 4);
		
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				buffer.put(value.get(i, j));
		
		buffer.flip();
		
		return buffer;
	}
	
	public static IntBuffer createFlippedBuffer(int... values) {
        IntBuffer buffer = createIntBuffer(values.length);
        
        buffer.put(values);
        buffer.flip();
        
        return buffer;
    }
	
	public static String[] removeEmptyStrings(String[] data) {
		ArrayList<String> result = new ArrayList<>();
		
		for (int i = 0; i < data.length; i++)
			if (!data[i].equals(""))
				result.add(data[i]);
		
		return result.toArray(new String[0]);
	}
	
	public static int[] toIntArray(Integer[] indexData) {
		int[] result = new int[indexData.length];
		
		for (int i = 0; i < result.length; i++)
			result[i] = indexData[i];
		
		return result;
	}
}
