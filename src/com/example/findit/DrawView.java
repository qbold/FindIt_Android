package com.example.findit;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.findit.selector.HoGSelector;
import com.example.findit.selector.Selector;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

/*
 * Отображение снимка с камеры и графические преобразования через OpenGL
 */
public class DrawView extends GLSurfaceView {

	public static SurfaceTexture tex; // отображаемая текстура
	public static float dx, dy; // размер текселя
	public static boolean get_pix;

	public DrawView(Context context) {
		super(context);

		setEGLContextClientVersion(2);
		setRenderer(new Renderer());
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	private class Renderer implements GLSurfaceView.Renderer {

		public ArrayList<RenderStage> stages;
		private boolean is_upd;

		private String read(String d) {
			InputStream is = null;
			try {
				is = Core.core.getAssets().open("shaders/" + d);
				byte[] b = new byte[is.available()];
				is.read(b);
				return new String(b);
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			return null;
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			String vertShader = read("standard.vert");

			// String fragShader = read("gaussian_linear.frag");
			// String fragShader2 = read("harris.frag");
			// String fragShader3 = read("gaussian_linear2.frag");
			String fragShader4 = read("standard.frag");

			String simple_grad = read("simple_gradient.frag");
			String hog = read("hog.frag");

			// ShaderProgram program1 = new ShaderProgram(vertShader,
			// fragShader,
			// "pos", "texture"); // gaussian linear
			// ShaderProgram program2 = new ShaderProgram(vertShader,
			// fragShader2,
			// "pos", "texture"); // harris
			// ShaderProgram program3 = new ShaderProgram(vertShader,
			// fragShader3,
			// "pos", "texture"); // gaussian linear 2
			ShaderProgram program5 = new ShaderProgram(vertShader, fragShader4,
					"pos", "texture"); // standard

			ShaderProgram program6 = new ShaderProgram(vertShader, simple_grad,
					"pos", "texture"); // simple grad

			ShaderProgram program7 = new ShaderProgram(vertShader, hog, "pos",
					"texture"); // hog

			// program1.addUniform(Uniform.FLOAT, "dx", dx);
			// program1.addUniform(Uniform.FLOAT, "dy", dy);

			// program2.addUniform(Uniform.FLOAT, "dx", dx);
			// program2.addUniform(Uniform.FLOAT, "dy", dy);

			// program3.addUniform(Uniform.FLOAT, "dx", dx);
			// program3.addUniform(Uniform.FLOAT, "dy", dy);

			program5.addUniform(Uniform.FLOAT, "dx", dx);
			program5.addUniform(Uniform.FLOAT, "dy", dy);

			program6.addUniform(Uniform.FLOAT, "dx", dx);
			program6.addUniform(Uniform.FLOAT, "dy", dy);

			program7.addUniform(Uniform.FLOAT, "dx", dx);
			program7.addUniform(Uniform.FLOAT, "dy", dy);

			// program4.addUniform(Uniform.FLOAT, "dx", dx);
			// program4.addUniform(Uniform.FLOAT, "dy", dy);

			stages = new ArrayList<>();

			int tx[] = new int[1];
			GLES20.glGenTextures(1, tx, 0);

			GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, tx[0]);
			GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
					GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
					GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
					GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
			GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
					GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

			// RenderStage s1 = new RenderStage(program1, tx[0],
			// GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE0, false);
			// s1.addUniform(Uniform.IMAGE, "u_img", 0);
			// s1.addUniform(Uniform.FLOAT, "hor", 1);

			// RenderStage s2 = new RenderStage(program3, s1.textureOutput,
			// GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE1, true);
			// s2.addUniform(Uniform.IMAGE, "u_img", 1);
			// s2.addUniform(Uniform.FLOAT, "hor", 0);

			// RenderStage s4 = new RenderStage(program1, s2.textureOutput,
			// GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE2, true);
			// s4.addUniform(Uniform.IMAGE, "u_img", 2);

			// RenderStage s5 = new RenderStage(program3, s4.textureOutput,
			// GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE3, true);
			// s5.addUniform(Uniform.IMAGE, "u_img", 3);

			// RenderStage s3 = new RenderStage(program6, s2.textureOutput,
			// GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE2, false);
			// s3.as_output = true;
			// s3.addUniform(Uniform.IMAGE, "u_img", 2);

			RenderStage s6 = new RenderStage(program5, tx[0],
					GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE0,
					false);
			s6.addUniform(Uniform.FLOAT, "type", 0);

			// RenderStage s62 = new RenderStage(program6, tx[0],
			// GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE0, true);
			// s62.as_output = true;
			// s62.addUniform(Uniform.FLOAT, "type", 2);
			// s62.addUniform(Uniform.IMAGE, "u_img", 0);

			// RenderStage s61 = new RenderStage(program6, tx[0],
			// GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE0, true);
			// s61.as_output = true;
			// s61.addUniform(Uniform.FLOAT, "type", 1);
			// s61.addUniform(Uniform.IMAGE, "u_img", 0);

			RenderStage s7 = new RenderStage(program7, tx[0],
					GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE0, true);
			s7.as_output = true;
			s7.setSelector(new HoGSelector(Core.core.w, Core.core.h));

			// RenderStage s7 = new RenderStage(program7, new int[] {
			// s61.textureOutput, s62.textureOutput }, new int[] {
			// GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_2D }, new int[] {
			// GLES20.GL_TEXTURE1, GLES20.GL_TEXTURE2 }, false);
			s7.addUniform(Uniform.IMAGE, "u_img", 0);
			// s7.addUniform(Uniform.IMAGE, "a", 1);
			// s7.addUniform(Uniform.IMAGE, "b", 2);

			// stages.add(s1);
			// stages.add(s2);
			// stages.add(s4);
			// stages.add(s5);
			// stages.add(s3);
			// stages.add(s61);
			// stages.add(s62);

			stages.add(s7);
			stages.add(s6);

			tex = new SurfaceTexture(tx[0]);

			tex.setOnFrameAvailableListener(new OnFrameAvailableListener() {

				@Override
				public void onFrameAvailable(SurfaceTexture surfaceTexture) {
					requestRender();
					is_upd = true;
				}
			});

			Core.core.startPreview();
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			GLES20.glViewport(0, 0, width, height);
			Core.core.camera.startPreview();
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			if (is_upd) {
				synchronized (this) {
					tex.updateTexImage();
					is_upd = false;
				}
			}

			for (RenderStage r : stages) {
				r.paint();
			}

			GLES20.glFlush();
		}
	}

	private static class RenderStage {

		private int framebuffer, textureOutput, texture_glActive[],
				texture_glBindTexture[], type_texture_glBindTexture[];

		private ShaderProgram program;

		private static FloatBuffer cords, texc;
		private static ByteBuffer pixels;
		// private static ShortBuffer ind;

		private Selector selector;

		private ArrayList<Uniform> uniforms;

		public boolean as_output;

		static {
			float[] coords = { -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f,
					-1.0f };
			// short[] indeces = { 0, 1, 2, 0, 2, 3 };
			float[] Tex = { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f };

			ByteBuffer buf = ByteBuffer.allocateDirect(4 * coords.length);
			buf.order(ByteOrder.nativeOrder());
			cords = buf.asFloatBuffer();
			cords.put(coords);
			cords.position(0);

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
						GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
						tx[0], 0);

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
						GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
						tx[0], 0);

				textureOutput = tx[0];
			}
		}

		public void setSelector(Selector s) {
			selector = s;
		}

		public Selector getSelector() {
			return selector;
		}

		public void paint() {
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer);
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT
					| GLES20.GL_DEPTH_BUFFER_BIT);
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

			GLES20.glVertexAttribPointer(program.indx, 2, GLES20.GL_FLOAT,
					false, 0, cords);
			GLES20.glVertexAttribPointer(program.indx2, 2, GLES20.GL_FLOAT,
					false, 0, texc);
			GLES20.glEnableVertexAttribArray(program.indx);
			GLES20.glEnableVertexAttribArray(program.indx2);
			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

			if (as_output && tex != null && selector != null) {
				get_pix = true;

				pixels.position(0);
				// long s = System.currentTimeMillis();
				GLES20.glReadPixels(0, 0, Core.core.w, Core.core.h,
						GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, pixels);

				// GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB,
				// Core.core.w, Core.core.h, 0, GLES20.GL_RGB,
				// GLES20.GL_UNSIGNED_BYTE, pixels);

				sendPixelsToSelector();

				// System.out.println(System.currentTimeMillis() - s);

				get_pix = false;
			}
		}

		private void sendPixelsToSelector() {
			if (selector == null)
				return;
			pixels.position(0);

			byte[] colors = new byte[pixels.capacity() / 3];

			int i = 0;
			// boolean s = false;
			while (pixels.hasRemaining()) {
				colors[i++] = pixels.get();
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

		public void addUniform(int type, String s, double v) {
			uniforms.add(new Uniform(type, GLES20.glGetUniformLocation(
					program.program, s), v));
		}
	}

	private static class ShaderProgram {

		public int program, indx, indx2;

		public ArrayList<Uniform> uniforms;

		public ShaderProgram(String vert, String frag, String pos,
				String texture) {
			uniforms = new ArrayList<>();

			int vertexShader_Handle = GLES20
					.glCreateShader(GLES20.GL_VERTEX_SHADER);
			int fragmentShader_Handle = GLES20
					.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
			GLES20.glShaderSource(vertexShader_Handle, vert);
			GLES20.glShaderSource(fragmentShader_Handle, frag);

			GLES20.glCompileShader(vertexShader_Handle);
			GLES20.glCompileShader(fragmentShader_Handle);

			System.out
					.println(GLES20.glGetShaderInfoLog(fragmentShader_Handle));
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
			uniforms.add(new Uniform(type, GLES20.glGetUniformLocation(program,
					s), v));
		}
	}

	private static class Uniform {

		public byte type;
		private float d_float;
		private int d_image;
		private int adr;
		public static final byte FLOAT = 0, IMAGE = 1;

		public Uniform(int type, int adr, double v) {
			this.type = (byte) type;
			this.adr = adr;
			if (type == FLOAT)
				d_float = (float) v;
			else if (type == IMAGE)
				d_image = (int) v;
		}

		public float getFloat() {
			return d_float;
		}

		public int getTexture() {
			return d_image;
		}

		public int getAddress() {
			return adr;
		}
	}

}
