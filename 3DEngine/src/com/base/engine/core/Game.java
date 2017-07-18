package com.base.engine.core;

// Generic container for every game!
public interface Game {
	void init();
	void input();
	void update();
	void render();
}
