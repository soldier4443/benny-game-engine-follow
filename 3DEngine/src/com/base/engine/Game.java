package com.base.engine;

import org.lwjgl.input.Keyboard;

public class Game {
	public Game() {
		
	}
	
	// This is test
	public void input() {
		if (Input.getKeyDown(Keyboard.KEY_UP)) {
			System.out.println("We're just pressed up!");
		} else if (Input.getKeyUp(Keyboard.KEY_UP)) {
			System.out.println("We're just released up!");
		}
		
		if (Input.getMouseDown(1)) {
			System.out.println("We're just right clicked at " + Input.getMousePosition());
		} else if (Input.getMouseUp(1)) {
			System.out.println("We're just released right moues button!");
		}
	}
	
	public void update() {
		
	}
	
	public void render() {
		
	}
}
