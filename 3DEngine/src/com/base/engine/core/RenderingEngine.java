package com.base.engine.core;

import com.base.engine.components.BaseLight;
import com.base.engine.components.Camera;
import com.base.engine.components.DirectionalLight;
import com.base.engine.components.PointLight;
import com.base.engine.rendering.*;
import com.base.engine.rendering.resource.MappedValues;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine extends MappedValues {

    private HashMap<String, Integer> samplerMap = new HashMap<>();
    private ArrayList<BaseLight> lights = new ArrayList<>();

    private Camera mainCamera;
    private BaseLight activeLight;

    private Shader forawrdAmbient;

    public RenderingEngine() {
        super();

        samplerMap.put("diffuse", 0);
        addVector3f("ambient", new Vector3f(0.2f, 0.2f, 0.2f));

        forawrdAmbient = new Shader("forwardAmbient");


        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        // front face : CLOCKWISE ORDER
        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        
        glEnable(GL_DEPTH_CLAMP);
        
        glEnable(GL_TEXTURE_2D);
//		glEnable(GL_FRAMEBUFFER_SRGB);
    }
    
    public void render(GameObject object) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        object.render(forawrdAmbient, this);
        
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE); // Add
        glDepthMask(false); // Disable depth testing
        glDepthFunc(GL_EQUAL);   // Write the pixels if it has exact same depth value as the pixel we found to be near to the screen
        
        for (BaseLight light : lights) {
            activeLight = light;
            
            object.render(light.getShader(), this);
        }
        
        glDepthFunc(GL_LESS);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }
    
    public void addLight(BaseLight light) {
        lights.add(light);
    }
    
    public Camera getMainCamera() {
        return mainCamera;
    }
    
    public void setMainCamera(Camera mainCamera) {
        this.mainCamera = mainCamera;
    }
    
    public BaseLight getActiveLight() {
        return this.activeLight;
    }

    public int getSamplerSlot(String unprefixedName) {
        return samplerMap.get(unprefixedName);
    }
}
