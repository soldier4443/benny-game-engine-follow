package com.base.engine.core;

import com.base.engine.components.BaseLight;
import com.base.engine.components.DirectionalLight;
import com.base.engine.components.PointLight;
import com.base.engine.rendering.*;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine {

    private Camera mainCamera;
    private Vector3f ambientLight;

    //
    private ArrayList<DirectionalLight> directionalLights = new ArrayList<>();
    private ArrayList<PointLight> pointLights = new ArrayList<>();

    private ArrayList<BaseLight> lights = new ArrayList<>();
    private BaseLight activeLight;

    public RenderingEngine() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // front face : CLOCKWISE ORDER
        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);

        glEnable(GL_DEPTH_CLAMP);

        glEnable(GL_TEXTURE_2D);
//		glEnable(GL_FRAMEBUFFER_SRGB);

        this.mainCamera = new Camera((float)Math.toRadians(70), (float)(Window.getWidth() / Window.getHeight()), 0.1f, 1000f);
        this.ambientLight = new Vector3f(0.2f, 0.2f, 0.2f);
    }

    public Vector3f getAmbientLight() {
        return ambientLight;
    }

    public void input(float deltaTime) {
        mainCamera.input(deltaTime);
    }

    public void render(GameObject object) {
        clearScreen();

        lights.clear();
        object.addToRenderingEngine(this);

        ForwardAmbientShader.getInstance().setRenderingEngine(this);
        object.render(ForwardAmbientShader.getInstance());

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE); // Add
        glDepthMask(false); // Disable depth testing
        glDepthFunc(GL_EQUAL);   // Write the pixels if it has exact same depth value as the pixel we found to be near to the screen

        for (BaseLight light : lights) {
            light.getShader().setRenderingEngine(this);

            activeLight = light;

            object.render(light.getShader());
        }

        glDepthFunc(GL_LESS);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }

    private static void clearScreen() {
        // TODO : Stencil Buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private static void setTextures(boolean enabled) {
        if (enabled)
            glEnable(GL_TEXTURE_2D);
        else
            glDisable(GL_TEXTURE_2D);
    }

    private static void unbindTextures() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private static void setClearColor(Vector3f color) {
        glClearColor(color.getX(), color.getY(), color.getZ(), 1.0f);
    }

    public static String getOpenGLVersion() {
        return glGetString(GL_VERSION);
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
}
