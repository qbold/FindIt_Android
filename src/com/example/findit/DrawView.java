package com.example.findit;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

public class DrawView extends GLSurfaceView {

	public SurfaceTexture tex;

	public DrawView(Context context) {
		super(context);

		setEGLContextClientVersion(2);
		setRenderer(new Renderer());
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	private class Renderer implements GLSurfaceView.Renderer {

		private int programHandle; // адрес шейдерной программы
		private int tex_n; // номер резервированный текстуры
		private int indx, sampler2D, indx2, conv_ind; // адреса координат
														// вершин, текстуры,
														// координат текстуры,
														// свёртки
		private boolean is_upd;

		private FloatBuffer cords, texc, convolution; // координаты вершин,
														// текстур, свёртка
		private ShortBuffer ind; // индексы порядка отображения

		/*
		 * Загрузить шейдер с именем d из папки ресурсов shaders
		 */
		private String read(String d) {
			InputStream is = null;
			try {
				is = Core.core.getAssets().open("shaders/" + d);
				byte[] b = new byte[is.available()];
				is.read(b);
				return new String(b);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
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

		/*
		 * Он сурфэйс креатед :-), а точнее, инициализируем данные при создании
		 * поверхности отрисовки (non-Javadoc)
		 * 
		 * @see
		 * android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition
		 * .khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
		 */
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			String vertShader = read("standard.vert");
			String fragShader = read("convolution.frag");

			float[] coords = { -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f,
					-1.0f };
			short[] indeces = { 0, 1, 2, 0, 2, 3 };
			float[] Tex = { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f };
			float[] convolut = { 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f };

			int vertexShader_Handle = GLES20
					.glCreateShader(GLES20.GL_VERTEX_SHADER);
			int fragmentShader_Handle = GLES20
					.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

			GLES20.glShaderSource(vertexShader_Handle, vertShader);
			GLES20.glShaderSource(fragmentShader_Handle, fragShader);

			GLES20.glCompileShader(vertexShader_Handle);
			GLES20.glCompileShader(fragmentShader_Handle);

			System.out
					.println(GLES20.glGetShaderInfoLog(fragmentShader_Handle));
			System.out.println(GLES20.glGetProgramInfoLog(programHandle));

			programHandle = GLES20.glCreateProgram();
			GLES20.glAttachShader(programHandle, vertexShader_Handle);
			GLES20.glAttachShader(programHandle, fragmentShader_Handle);
			GLES20.glLinkProgram(programHandle);

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

			ByteBuffer buf3 = ByteBuffer.allocateDirect(4 * convolut.length);
			buf3.order(ByteOrder.nativeOrder());
			convolution = buf3.asFloatBuffer();
			convolution.put(convolut);
			convolution.position(0);

			ByteBuffer bf = ByteBuffer.allocateDirect(2 * indeces.length);
			bf.order(ByteOrder.nativeOrder());
			ind = bf.asShortBuffer();
			ind.put(indeces);
			ind.position(0);

			int[] textures = new int[1];

			GLES20.glGenTextures(1, textures, 0);
			GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);

			GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
					GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
					GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
					GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
			GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
					GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

			sampler2D = GLES20.glGetUniformLocation(programHandle, "u_img");
			indx = GLES20.glGetAttribLocation(programHandle, "pos");
			indx2 = GLES20.glGetAttribLocation(programHandle, "texture");
			conv_ind = GLES20.glGetAttribLocation(programHandle, "convolution");

			tex = new SurfaceTexture(textures[0]);

			tex.setOnFrameAvailableListener(new OnFrameAvailableListener() {

				@Override
				public void onFrameAvailable(SurfaceTexture surfaceTexture) {
					requestRender();
					is_upd = true;
				}
			});

			Core.core.startPreview();

			tex_n = textures[0];
		}

		/*
		 * При изменении размеров полотна, изменяем размер (non-Javadoc)
		 * 
		 * @see
		 * android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition
		 * .khronos.opengles.GL10, int, int)
		 */
		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			GLES20.glViewport(0, 0, width, height);
			Core.core.camera.startPreview();
		}

		/*
		 * Рисуем) (non-Javadoc)
		 * 
		 * @see
		 * android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition
		 * .khronos.opengles.GL10)
		 */
		@Override
		public void onDrawFrame(GL10 gl) {
			if (is_upd) {
				synchronized (this) {
					tex.updateTexImage();
					is_upd = false;
				}
			}

			GLES20.glUseProgram(programHandle);
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, tex_n);
			GLES20.glUniform1i(sampler2D, 0);
			GLES20.glUniformMatrix3fv(conv_ind, 1, false, convolution);

			GLES20.glVertexAttribPointer(indx, 2, GLES20.GL_FLOAT, false, 0,
					cords);
			GLES20.glVertexAttribPointer(indx2, 2, GLES20.GL_FLOAT, false, 0,
					texc);
			GLES20.glEnableVertexAttribArray(indx);
			GLES20.glEnableVertexAttribArray(indx2);

			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
			GLES20.glFlush();
		}
	}

}
