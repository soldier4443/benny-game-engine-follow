package com.base.engine.rendering;

import com.base.engine.core.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.*;

public class RenderUtil {
	public static void clearScreen() {
		// TODO : Stencil Buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public static void setTextures(boolean enabled) {
	    if (enabled)
	        glEnable(GL_TEXTURE_2D);
	    else
	        glDisable(GL_TEXTURE_2D);
    }

    public static void unbindTextures() {
	    glBindTexture(GL_TEXTURE_2D, 0);
    }

    public static void setClearColor(Vector3f color) {
        glClearColor(color.getX(), color.getY(), color.getZ(), 1.0f);
    }
	
	/**
	 * Initialize OpenGL
	 */
	public static void initGraphics() {
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
	
	public static String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}
}