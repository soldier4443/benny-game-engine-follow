package com.base.game;

import com.base.engine.core.CoreEngine;
import com.base.game.Game;

/**
 * Created by soldi on 2017-07-19.
 */
public class Main {
    public static void main(String[] args) {
        // So.. this
        CoreEngine engine = new CoreEngine(1200, 600, 60, new TestGame());

        engine.createWindow("3D Game Engine");
        engine.start();
    }
}
