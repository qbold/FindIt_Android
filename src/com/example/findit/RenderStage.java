package com.example.findit;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.example.findit.selector.Selector;

import android.opengl.GLES20;

/*
 * Описывает уровень отображения
 */
public class RenderStage {

	private int framebuffer, textureOutput, texture_glActive[],
			texture_glBindTexture[], type_texture_glBindTexture[];

	private ShaderProgram program;

	private static FloatBuffer cords, cords2, texc;
	private static ByteBuffer pixels;
	// private static ShortBuffer ind;

	public static byte FULL_SCREEN = 1, SMALL_PREVIEW = 0;

	private Selector selector;

	private ArrayList<Uniform> uniforms;

	public boolean as_output;
	private byte index_coords;

	static {
		float w_coef = (float) Core.core.w / (float) Core.core.max_w;
		float h_coef = (float) Core.core.h / (float) Core.core.max_h;

		float pos_x = -1f + w_coef * 2;
		float pos_y = -1f + h_coef * 2;
		float[] coords = { -1.0f, pos_y, -1.0f, -1.0f, pos_x, pos_y, pos_x,
				-1.0f };
		float[] coords2 = { -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f };
		// short[] indeces = { 0, 1, 2, 0, 2, 3 };
		float[] Tex = { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f };

		ByteBuffer buf = ByteBuffer.allocateDirect(4 * coords.length);
		buf.order(ByteOrder.nativeOrder());
		cords = buf.asFloatBuffer();
		cords.put(coords);
		cords.position(0);

		ByteBuffer buf3 = ByteBuffer.allocateDirect(4 * coords2.length);
		buf3.order(ByteOrder.nativeOrder());
		cords2 = buf3.asFloatBuffer();
		cords2.put(coords2);
		cords2.position(0);

		ByteBuffer buf2 = ByteBuffer.allocateDirect(4 * Tex.length);
		buf2.order(ByteOrder.nativeOrder());
		texc = buf2.asFloatBuffer();
		texc.put(Tex);
		texc.position(0);

		// ByteBuffer bf = ByteBuffer.allocateDirect(2 * indeces.length);
		// bf.order(ByteOrder.nativeOrder());
		// ind = bf.asShortBuffer();
		// ind.put(indeces);
		// ind.position(0);

		pixels = ByteBuffer.allocateDirect(Core.core.w * Core.core.h * 3);
		pixels.order(ByteOrder.nativeOrder());
		pixels.position(0);
	}

	public RenderStage(ShaderProgram program, int texture_glBindTexture[],
			int type_texture_glBindTexture[], int texture_glActive[],
			boolean gen_buffer) {

		uniforms = new ArrayList<>();

		this.texture_glActive = texture_glActive;
		this.program = program;
		this.type_texture_glBindTexture = type_texture_glBindTexture;
		this.texture_glBindTexture = texture_glBindTexture;

		if (gen_buffer) {
			int[] fb = new int[1];
			GLES20.glGenFramebuffers(1, fb, 0);
			framebuffer = fb[0];

			int tx[] = new int[1];
			GLES20.glGenTextures(1, tx, 0);

			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tx[0]);
			GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB,
					Core.core.w, Core.core.h, 0, GLES20.GL_RGB,
					GLES20.GL_UNSIGNED_BYTE, null);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
			GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,
					GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, tx[0], 0);

			textureOutput = tx[0];
		}
	}

	public RenderStage(ShaderProgram program, int texture_glBindTexture,
			int type_texture_glBindTexture, int texture_glActive,
			boolean gen_buffer) {

		uniforms = new ArrayList<>();

		this.texture_glActive = new int[] { texture_glActive };
		this.program = program;
		this.type_texture_glBindTexture = new int[] { type_texture_glBindTexture };
		this.texture_glBindTexture = new int[] { texture_glBindTexture };

		if (gen_buffer) {
			int[] fb = new int[1];
			GLES20.glGenFramebuffers(1, fb, 0);
			framebuffer = fb[0];

			int tx[] = new int[1];
			GLES20.glGenTextures(1, tx, 0);

			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tx[0]);
			GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB,
					Core.core.w, Core.core.h, 0, GLES20.GL_RGB,
					GLES20.GL_UNSIGNED_BYTE, null);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
			GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,
					GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, tx[0], 0);

			textureOutput = tx[0];
		}
	}

	/*
	 * Какой массив с вершинами использовать: служебный или для стандартного
	 * видеопотока?
	 */
	public void setVertexCoords(int c) {
		index_coords = (byte) c;
	}

	/*
	 * Вернуть текущий индекс массива с вершинами
	 */
	public byte getVertexCoords() {
		return index_coords;
	}

	/*
	 * Установить селектор
	 */
	public void setSelector(Selector s) {
		selector = s;
	}

	/*
	 * Вернуть селектор
	 */
	public Selector getSelector() {
		return selector;
	}

	/*
	 * Отрисовка
	 */
	public void paint() {
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glClearColor(0, 0, 0, 0);

		GLES20.glUseProgram(program.program);
		for (int i = 0; i < texture_glActive.length; i++) {
			GLES20.glActiveTexture(texture_glActive[i]);
			GLES20.glBindTexture(type_texture_glBindTexture[i],
					texture_glBindTexture[i]);
		}

		for (Uniform u : uniforms) {
			if (u.type == Uniform.FLOAT) {
				GLES20.glUniform1f(u.getAddress(), u.getFloat());
			} else if (u.type == Uniform.IMAGE) {
				GLES20.glUniform1i(u.getAddress(), u.getTexture());
			}
		}
		for (Uniform u : program.uniforms) {
			if (u.type == Uniform.FLOAT) {
				GLES20.glUniform1f(u.getAddress(), u.getFloat());
			} else if (u.type == Uniform.IMAGE) {
				GLES20.glUniform1i(u.getAddress(), 0);
			}
		}

		GLES20.glVertexAttribPointer(program.indx, 2, GLES20.GL_FLOAT, false,
				0, index_coords == 0 ? cords : cords2);
		GLES20.glVertexAttribPointer(program.indx2, 2, GLES20.GL_FLOAT, false,
				0, texc);
		GLES20.glEnableVertexAttribArray(program.indx);
		GLES20.glEnableVertexAttribArray(program.indx2);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

		if (as_output && DrawView.tex != null && selector != null) {
			DrawView.get_pix = true;

			pixels.position(0);
			// long s = System.currentTimeMillis();
			GLES20.glReadPixels(0, 0, Core.core.w, Core.core.h, GLES20.GL_RGB,
					GLES20.GL_UNSIGNED_BYTE, pixels);

			// GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB,
			// Core.core.w, Core.core.h, 0, GLES20.GL_RGB,
			// GLES20.GL_UNSIGNED_BYTE, pixels);

			sendPixelsToSelector();

			// System.out.println(System.currentTimeMillis() - s);

			DrawView.get_pix = false;
		}
	}

	/*
	 * Отправление пикселей селектору для обработки
	 */
	private void sendPixelsToSelector() {
		if (selector == null)
			return;
		pixels.position(0);

		byte[] colors = new byte[Core.core.w * Core.core.h];

		int x = 0, indy = (Core.core.h - 1) * Core.core.w;
		// boolean s = false;
		while (pixels.hasRemaining()) {
			colors[x + indy] = pixels.get();
			x++;
			if (x >= Core.core.w) {
				x = 0;
				indy -= Core.core.w;
			}
			// if (colors[i - 1] > 0)
			// s = true;
			// if (
			pixels.get();
			// > 0)
			// s = true;
			// if (
			pixels.get();
			// > 0)
			// s = true;
		}
		// System.out.println(s);

		pixels.position(0);

		selector.select(colors, Core.core.w);
	}

	/*
	 * Добавить юниформу, действующую только для текущего RenderStage
	 */
	public void addUniform(int type, String s, double v) {
		uniforms.add(new Uniform(type, GLES20.glGetUniformLocation(
				program.program, s), v));
	}
}
