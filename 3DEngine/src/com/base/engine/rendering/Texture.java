package com.base.engine.rendering;

import com.base.engine.core.Util;
import com.base.engine.rendering.resource.TextureResources;
import org.newdawn.slick.opengl.TextureLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
    private static HashMap<String, TextureResources> loadedResources = new HashMap<>();
    private TextureResources resources;
    private String fileName;
    
    public Texture(String fileName) {
        this.fileName = fileName;
        TextureResources oldResource = loadedResources.get(fileName);

        if (oldResource != null) {
            resources = oldResource;
            resources.addReference();
        } else {
            resources = new TextureResources(loadTexture(fileName));
            loadedResources.put(fileName, resources);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (resources.removeReference() && !fileName.isEmpty()) {
            loadedResources.remove(fileName);
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, resources.getId());
    }
    
    public static int loadTexture(String fileName) {
        String[] splitArray = fileName.split("\\.");
        String ext = splitArray[splitArray.length - 1];
        
        try {
//            int id = TextureLoader.getTexture(ext, new FileInputStream(new File("res/textures/" + fileName))).getTextureID();

            // Our own texture loading
            BufferedImage image = ImageIO.read(new File("res/textures/" + fileName));
            int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

            ByteBuffer buffer = Util.createByteBuffer(image.getHeight() *  image.getWidth() * 4);

            boolean hasAlpha = image.getColorModel().hasAlpha();

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int pixel = pixels[y * image.getWidth() + x];

                    // RGBA format
                    buffer.put((byte)((pixel >> 16) & 0xff));
                    buffer.put((byte)((pixel >> 8) & 0xff));
                    buffer.put((byte)((pixel) & 0xff));

                    if (hasAlpha)
                        buffer.put((byte)((pixel >> 24) & 0xff));
                    else
                        buffer.put((byte)(0xff));
                }
            }

            buffer.flip();

            int id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

            return id;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        return 0;
    }
}
