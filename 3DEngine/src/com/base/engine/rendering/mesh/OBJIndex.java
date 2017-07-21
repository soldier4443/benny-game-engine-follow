package com.base.engine.rendering.mesh;

/**
 * Created by nyh0111 on 2017-07-21.
 */

public class OBJIndex {
    public int positionIndex;
    public int texturePositionIndex;
    public int normalIndex;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof OBJIndex))
            return false;

//        return super.equals(obj);
        OBJIndex index = (OBJIndex) obj;

        return positionIndex == index.positionIndex
                && texturePositionIndex == index.texturePositionIndex
                && normalIndex == index.normalIndex;
    }

    @Override
    public int hashCode() {
        final int BASE = 17;
        final int MULTIPIER = 31;

        int result = BASE;

        result += MULTIPIER * result + positionIndex;
        result += MULTIPIER * result + texturePositionIndex;
        result += MULTIPIER * result + normalIndex;

        return super.hashCode();
    }
}
