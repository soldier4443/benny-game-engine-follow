package com.base.engine.core;

import com.base.engine.rendering.*;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine {

    private Camera mainCamera;
    private Vector3f ambientLight;
    private DirectionalLight directionalLight;
    private DirectionalLight directionalLight2;
    private PointLight pointLight;
    private SpotLight spotLight;

    private ArrayList<PointLight> pointLightList = new ArrayList<>();

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
        this.directionalLight = new DirectionalLight(new BaseLight(new Vector3f(0, 0, 1), 0.2f), new Vector3f(1, 1, 1));
        this.directionalLight2 = new DirectionalLight(new BaseLight(new Vector3f(1, 0, 0), 0.2f), new Vector3f(-1, 1, -1));

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                pointLightList.add(new PointLight(
                        new BaseLight(new Vector3f(0, 1, 0), 0.4f),
                        new Attenuation(0, 0, 1),
                        new Vector3f(i * 5, 0, j * 5),
                        100
                ));
            }
        }

        PointLight pl = new PointLight(
                new BaseLight(new Vector3f(0, 1, 1), 0.6f),
                new Attenuation(0, 0, 0.01f),
                new Vector3f(25, 5, 25),
                100);

        this.spotLight = new SpotLight(pl, new Vector3f(-1, -1, -1), 0.8f);
    }

    public Vector3f getAmbientLight() {
        return ambientLight;
    }

    public void input(float deltaTime) {
        mainCamera.input(deltaTime);
    }

    public void render(GameObject object) {
        clearScreen();

        ForwardAmbientShader.getInstance().setRenderingEngine(this);
        ForwardDirectionalShader.getInstance().setRenderingEngine(this);
        ForwardPointShader.getInstance().setRenderingEngine(this);
        ForwardSpotShader.getInstance().setRenderingEngine(this);

        object.render(ForwardAmbientShader.getInstance());

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE); // Add
        glDepthMask(false); // Disable depth testing
        glDepthFunc(GL_EQUAL);   // Write the pixels if it has exact same depth value as the pixel we found to be near to the screen

        object.render(ForwardDirectionalShader.getInstance());

        DirectionalLight temp = directionalLight;
        directionalLight = directionalLight2;
        directionalLight2 = temp;

        object.render(ForwardDirectionalShader.getInstance());

        temp = directionalLight;
        directionalLight = directionalLight2;
        directionalLight2 = temp;

        for (PointLight pl : pointLightList) {
            pointLight = pl;
            object.render(ForwardPointShader.getInstance());
        }

        object.render(ForwardSpotShader.getInstance());

        glDepthFunc(GL_LESS);
        glDepthMask(true);
        glDisable(GL_BLEND);

//        BasicShader.getInstance().setRenderingEngine(this);
//
//        object.render(BasicShader.getInstance());
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

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public PointLight getPointLight() {
        return pointLight;
    }

    public SpotLight getSpotLight() {
        return spotLight;
    }
}
