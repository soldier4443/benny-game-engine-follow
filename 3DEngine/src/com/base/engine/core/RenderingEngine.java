package com.base.engine.core;

import com.base.engine.rendering.BasicShader;
import com.base.engine.rendering.Camera;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.Window;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine {

    private Camera mainCamera;

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
    }

    public void input() {
        mainCamera.input();
    }

    public void render(GameObject object) {
        clearScreen();

        BasicShader.getInstance().setRenderingEngine(this);

        object.render(BasicShader.getInstance());
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

    public Camera getMainCamera() {
        return mainCamera;
    }

    public void setMainCamera(Camera mainCamera) {
        this.mainCamera = mainCamera;
    }
}
