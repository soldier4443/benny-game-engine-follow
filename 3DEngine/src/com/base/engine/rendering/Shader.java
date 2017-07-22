package com.base.engine.rendering;

import com.base.engine.components.BaseLight;
import com.base.engine.components.DirectionalLight;
import com.base.engine.components.PointLight;
import com.base.engine.components.SpotLight;
import com.base.engine.core.*;
import com.base.engine.rendering.resource.ShaderResources;
import com.base.engine.rendering.resource.TextureResources;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Shader {
    public static class Component {
        String type;
        String name;

        Component(String type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    private static final String UNIFORM_KEYWORD = "uniform";
    private static final String STRUCT_KEYWORD = "struct";

    private static HashMap<String, ShaderResources> loadedShaders = new HashMap<>();

    private ShaderResources resources;
    private String fileName;

    public Shader(String fileName) {
        this.fileName = fileName;
        ShaderResources oldResource = loadedShaders.get(fileName);

        if (oldResource != null) {
            resources = oldResource;
            resources.addReference();
        } else {

            resources = new ShaderResources();

            String vertexShaderText = loadShader(fileName + ".vs");
            String fragmentShaderText = loadShader(fileName + ".fs");

            addVertexShader(vertexShaderText);
            addFragmentShader(fragmentShaderText);
            compileShader();

            addAllUniforms(vertexShaderText);
            addAllUniforms(fragmentShaderText);

            loadedShaders.put(fileName, resources);
        }
    }
    
    public void bind() {
        glUseProgram(resources.getProgram());
    }
    
    public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
        Matrix4f modelMatrix = transform.getTransformation();
        Matrix4f projectedMatrix = renderingEngine.getMainCamera().getViewProjection().mul( modelMatrix );

        for (Component component : resources.getUniformList()) {
            if (component.type.equals("sampler2D")) {
                int samplerSlot = renderingEngine.getSamplerSlot(component.name);

                material.getTexture(component.name).bind(samplerSlot);
                setUniformi(component.name, samplerSlot);
            }
            else if (component.name.startsWith("T_")) {
                if (component.name.equals("T_MVP")) {
                    setUniform(component.name, projectedMatrix);
                } else if (component.name.equals("T_model")) {
                    setUniform(component.name, modelMatrix);
                } else {
                    throw new IllegalArgumentException(component.name + " is not a valid component of Transform");
                }
            } else if (component.name.startsWith("R_")) {
                String unprefixedName = component.name.substring(2);

                if (component.type.equals("vec3")) {
                    setUniform(component.name, renderingEngine.getVector3f(unprefixedName));
                } else if (component.type.equals("float")) {
                    setUniformf(component.name, renderingEngine.getFloat(unprefixedName));
                } else if (component.type.equals("DirectionalLight")) {
                    setUniformDirectionalLight(component.name, (DirectionalLight) renderingEngine.getActiveLight());
                } else if (component.type.equals("PointLight")) {
                    setUniformPointLight(component.name, (PointLight) renderingEngine.getActiveLight());
                } else if (component.type.equals("SpotLight")) {
                    setUniformSpotLight(component.name, (SpotLight) renderingEngine.getActiveLight());
                } else {
                    throw new IllegalArgumentException(component.type + " is not a valid component in RenderingEngine");
                }
            } else if (component.name.startsWith("C_")) {
                if (component.name.equals("C_eyePos")) {
                    setUniform(component.name, renderingEngine.getMainCamera().getTransform().getTransformedPosition());
                } else {
                    throw new IllegalArgumentException(component.type + " is not a valid component in Camera");
                }
            } else {
                if (component.type.equals("vec3")) {
                    setUniform(component.name, material.getVector3f(component.name));
                } else if (component.type.equals("float")) {
                    setUniformf(component.name, material.getFloat(component.name));
                } else {
                    throw new IllegalArgumentException(component.type + " is not a valid component in Material");
                }
            }
        }
    }

    private HashMap<String, ArrayList<Component>> findUniformStructs(String shaderText) {
        HashMap<String, ArrayList<Component>> result = new HashMap<>();

        int structStartLocation = shaderText.indexOf(STRUCT_KEYWORD);

        while (structStartLocation != -1) {
            int nameBegin = structStartLocation + STRUCT_KEYWORD.length() + 1;
            int braceBegin = shaderText.indexOf("{", nameBegin);
            int braceEnd = shaderText.indexOf("}", braceBegin);

            String structName = shaderText.substring(nameBegin, braceBegin).trim();
            ArrayList<Component> structComponent = new ArrayList<>();

            int end = shaderText.indexOf(";", braceBegin);

            while (end != -1 && end < braceEnd) {
                int componentNameStart = end;

                while (!Character.isWhitespace(shaderText.charAt(componentNameStart - 1)))
                    componentNameStart--;

                int componentTypeEnd = componentNameStart - 1;
                int componentTypeStart = componentTypeEnd;

                while (!Character.isWhitespace(shaderText.charAt(componentTypeStart - 1)))
                    componentTypeStart--;

                String name = shaderText.substring(componentNameStart, end);
                String type = shaderText.substring(componentTypeStart, componentTypeEnd);

                structComponent.add(new Component(type, name));

                end = shaderText.indexOf(";", end + 1);
            }

            result.put(structName, structComponent);

            structStartLocation = shaderText.indexOf(STRUCT_KEYWORD, structStartLocation + STRUCT_KEYWORD.length());
        }

        return result;
    }

    private void addAllUniforms(String shaderText) {
        HashMap<String, ArrayList<Component>> structs = findUniformStructs(shaderText);
        int uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD);

        while (uniformStartLocation != -1) {
            int begin = uniformStartLocation + UNIFORM_KEYWORD.length() + 1;
            int end = shaderText.indexOf(";", begin);

            String line = shaderText.substring(begin, end);

            int whileSpacePos = line.indexOf(' ');

            String name = line.substring(whileSpacePos + 1, line.length());
            String type = line.substring(0, whileSpacePos);

            resources.getUniformList().add(new Component(type, name));
            addUniform(type, name, structs);

            uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD, uniformStartLocation + UNIFORM_KEYWORD.length());
        }
    }

    private void addUniform(String type, String name, HashMap<String, ArrayList<Component>> structs) {
        ArrayList<Component> components = structs.get(type);

        if (components != null) {
            for (Component component : components) {
                addUniform(component.type, name + "." + component.name, structs);
            }
        } else {
            int uniformLocation = glGetUniformLocation(resources.getProgram(), name);

            if (uniformLocation == 0xFFFFFFFF) {
                System.err.println("Error: Could not find uniform " + name);
                new Exception().printStackTrace();
                System.exit(1);
            }

            resources.getUniformMap().put(name, uniformLocation);
        }
    }
    
    private void addVertexShader(String source) {
        addProgram(source, GL_VERTEX_SHADER);
    }
    
    private void addGeometryShader(String source) {
        addProgram(source, GL_GEOMETRY_SHADER);
    }
    
    private void addFragmentShader(String source) {
        addProgram(source, GL_FRAGMENT_SHADER);
    }
    
    private void compileShader() {
        glLinkProgram(resources.getProgram());
        
        if (glGetProgram(resources.getProgram(), GL_LINK_STATUS) == 0) {
            System.err.println(glGetProgramInfoLog(resources.getProgram(), 1024));
            System.exit(1);
        }
        
        glValidateProgram(resources.getProgram());
        
        if (glGetProgram(resources.getProgram(), GL_VALIDATE_STATUS) == 0) {
            System.err.println(glGetProgramInfoLog(resources.getProgram(), 1024));
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
        
        glAttachShader(resources.getProgram(), shader);
    }
    
    private static String loadShader(String fileName) {
        StringBuilder shaderSource = new StringBuilder();
        BufferedReader shaderReader = null;

        final String INCLUDE_DIRECTIVE = "#include";

        try {
            shaderReader = new BufferedReader(new FileReader("res/shaders/" + fileName));
            
            String line;
            while ((line = shaderReader.readLine()) != null) {
                if (line.startsWith(INCLUDE_DIRECTIVE)) {
                    String[] lines = line.split(" ");
                    shaderSource.append(loadShader(lines[1].substring(1, lines[1].length() - 1)));
                } else
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
        glUniform1i(resources.getUniformMap().get(uniformName), value);
    }
    
    public void setUniformf(String uniformName, float value) {
        glUniform1f(resources.getUniformMap().get(uniformName), value);
    }
    
    public void setUniform(String uniformName, Vector3f value) {
        glUniform3f(resources.getUniformMap().get(uniformName), value.getX(), value.getY(), value.getZ());
    }
    
    public void setUniform(String uniformName, Matrix4f value) {
        // true - row major order
        glUniformMatrix4(resources.getUniformMap().get(uniformName), true, Util.createFlippedBuffer(value));
    }

    private void setUniformBaseLight(String uniformName, BaseLight baseLight) {
        setUniform(uniformName + ".color", baseLight.getColor());
        setUniformf(uniformName + ".intensity", baseLight.getIntensity());
    }

    private void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight) {
        setUniformBaseLight(uniformName + ".base", directionalLight);
        setUniform(uniformName + ".direction", directionalLight.getDirection());
    }

    private void setUniformPointLight(String uniformName, PointLight pointLight) {
        setUniformBaseLight(uniformName + ".base", pointLight);
        setUniformf(uniformName + ".attenuation.constant", pointLight.getAttenuation().getConstant());
        setUniformf(uniformName + ".attenuation.linear", pointLight.getAttenuation().getLinear());
        setUniformf(uniformName + ".attenuation.exponent", pointLight.getAttenuation().getExponent());
        setUniform(uniformName + ".position", pointLight.getTransform().getPosition());
        setUniformf(uniformName + ".range", pointLight.getRange());
    }

    private void setUniformSpotLight(String uniformName, SpotLight spotLight) {
        setUniformPointLight(uniformName + ".pointLight", spotLight);
        setUniform(uniformName + ".direction", spotLight.getDirection());
        setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
    }
}
