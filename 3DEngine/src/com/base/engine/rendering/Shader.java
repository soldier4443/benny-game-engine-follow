package com.base.engine.rendering;

import com.base.engine.core.*;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class Shader {
    private int program;
    private HashMap<String, Integer> uniforms;
    
    public Shader() {
        program = glCreateProgram();
        uniforms = new HashMap<>();
        
        if (program == 0) {
            System.out.println("Shader creation failed: Could not find valid memory location in constructor");
            System.exit(1);
        }
    }
    
    public void bind() {
        glUseProgram(program);
    }
    
    public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
    
    }
    
    public void addUniform(String uniform) {
        int uniformLocation = glGetUniformLocation(program, uniform);
        
        if (uniformLocation == 0xFFFFFFFF) {
            System.err.println("Error: Could not find uniform " + uniform);
            new Exception().printStackTrace();
            System.exit(1);
        }
        
        uniforms.put(uniform, uniformLocation);
    }
    
    public void addVertexShaderFromFile(String source) {
        addProgram(loadShader(source), GL_VERTEX_SHADER);
    }
    
    public void addGeometryShaderFromFile(String source) {
        addProgram(loadShader(source), GL_GEOMETRY_SHADER);
    }
    
    public void addFragmentShaderFromFile(String source) {
        addProgram(loadShader(source), GL_FRAGMENT_SHADER);
    }
    
    public void addVertexShader(String source) {
        addProgram(source, GL_VERTEX_SHADER);
    }
    
    public void addGeometryShader(String source) {
        addProgram(source, GL_GEOMETRY_SHADER);
    }
    
    public void addFragmentShader(String source) {
        addProgram(source, GL_FRAGMENT_SHADER);
    }
    
    public void compileShader() {
        glLinkProgram(program);
        
        if (glGetProgram(program, GL_LINK_STATUS) == 0) {
            System.err.println(glGetProgramInfoLog(program, 1024));
            System.exit(1);
        }
        
        glValidateProgram(program);
        
        if (glGetProgram(program, GL_VALIDATE_STATUS) == 0) {
            System.err.println(glGetProgramInfoLog(program, 1024));
            System.exit(1);
        }
    }
    
    private void addProgram(String source, int type) {
        int shader = glCreateShader(type);
        
        if (shader == 0) {
            System.out.println("Shader creation failed: Could not find valid memory location when adding shader");
            System.exit(1);
        }
        
        glShaderSource(shader, source);
        glCompileShader(shader);
        
        if (glGetShader(shader, GL_COMPILE_STATUS) == 0) {
            System.err.println(glGetShaderInfoLog(shader, 1024));
            System.exit(1);
        }
        
        glAttachShader(program, shader);
    }
    
    private static String loadShader(String fileName) {
        StringBuilder shaderSource = new StringBuilder();
        BufferedReader shaderReader = null;
        
        try {
            shaderReader = new BufferedReader(new FileReader("res/shaders/" + fileName));
            
            String line;
            while ((line = shaderReader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            
            shaderReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        
        return shaderSource.toString();
    }
    
    public void setUniformi(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }
    
    public void setUniformf(String uniformName, float value) {
        glUniform1f(uniforms.get(uniformName), value);
    }
    
    public void setUniform(String uniformName, Vector3f value) {
        glUniform3f(uniforms.get(uniformName), value.getX(), value.getY(), value.getZ());
    }
    
    public void setUniform(String uniformName, Matrix4f value) {
        // true - row major order
        glUniformMatrix4(uniforms.get(uniformName), true, Util.createFlippedBuffer(value));
    }
}
