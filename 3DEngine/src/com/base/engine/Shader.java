package com.base.engine;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

public class Shader {
	private int program;
	
	public Shader() {
		program = glCreateProgram();
		
		if (program == 0) {
			System.out.println("Shader creation failed: Could not find valid memory location in constructor");
			System.exit(1);
		}
	}
	
	public void bind() {
		glUseProgram(program);
	}
	
	public void addVertexShader(String source) {
		addProgram(source, GL_VERTEX_SHADER);
	}
	
	public void addGeometryShader(String source) {
		addProgram(source, GL_GEOMETRY_SHADER);
	}
	
	public void addFragmentShader(String source) {
		addProgram(source, GL_FRAGMENT_SHADER);		
	}
	
	public void compileShader() {
		glLinkProgram(program);

		if (glGetProgram(program, GL_LINK_STATUS) == 0) {
			System.err.println(glGetProgramInfoLog(program, 1024));
			System.exit(1);
		}
		
		glValidateProgram(program);

		if (glGetProgram(program, GL_VALIDATE_STATUS) == 0) {
			System.err.println(glGetProgramInfoLog(program, 1024));
			System.exit(1);
		}
	}
	
	private void addProgram(String source, int type) {
		int shader = glCreateShader(type);
		
		if (shader == 0) {
			System.out.println("Shader creation failed: Could not find valid memory location when adding shader");
			System.exit(1);			
		}
		
		glShaderSource(shader, source);
		glCompileShader(shader);
		
		if (glGetShader(shader, GL_COMPILE_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}
		
		glAttachShader(program, shader);
	}
}