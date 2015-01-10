package com.example.findit;

import java.util.ArrayList;

import android.opengl.GLES20;

/*
 * Шейдерная программа (связка вершинный шейдер + фрагментный шейдер)
 */
public class ShaderProgram {

	public int program, indx, indx2;

	public ArrayList<Uniform> uniforms;

	public ShaderProgram(String vert, String frag, String pos, String texture) {
		uniforms = new ArrayList<>();

		int vertexShader_Handle = GLES20
				.glCreateShader(GLES20.GL_VERTEX_SHADER);
		int fragmentShader_Handle = GLES20
				.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		GLES20.glShaderSource(vertexShader_Handle, vert);
		GLES20.glShaderSource(fragmentShader_Handle, frag);

		GLES20.glCompileShader(vertexShader_Handle);
		GLES20.glCompileShader(fragmentShader_Handle);

		System.out.println(GLES20.glGetShaderInfoLog(fragmentShader_Handle));
		System.out.println(GLES20.glGetShaderInfoLog(vertexShader_Handle));

		program = GLES20.glCreateProgram();
		GLES20.glAttachShader(program, vertexShader_Handle);
		GLES20.glAttachShader(program, fragmentShader_Handle);
		GLES20.glLinkProgram(program);

		System.out.println(GLES20.glGetProgramInfoLog(program));

		indx = GLES20.glGetAttribLocation(program, pos);
		indx2 = GLES20.glGetAttribLocation(program, texture);
	}

	public void addUniform(int type, String s, double v) {
		uniforms.add(new Uniform(type, GLES20.glGetUniformLocation(program, s),
				v));
	}
}
