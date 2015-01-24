package com.example.findit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.findit.selector.FeatureSelector;
import com.example.findit.selector.HoGSelector;
import com.example.findit.selector.SaveSelector;

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
	public static float dx, dy, dx2, dy2; // размер текселя
	public static boolean get_pix;

	public HoGSelector HoG;

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
			String fragShader2 = read("harris.frag");
			// String fragShader3 = read("gaussian_linear2.frag");
			String fragShader4 = read("standard.frag");

			String simple_grad = read("simple_gradient.frag");
			String hog = read("hog.frag");

			// ShaderProgram program1 = new ShaderProgram(vertShader,
			// fragShader,
			// "pos", "texture"); // gaussian linear
			ShaderProgram program2 = new ShaderProgram(vertShader, fragShader2,
					"pos", "texture"); // harris
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

			program2.addUniform(Uniform.FLOAT, "dx", dx);
			program2.addUniform(Uniform.FLOAT, "dy", dy);

			// program3.addUniform(Uniform.FLOAT, "dx", dx);
			// program3.addUniform(Uniform.FLOAT, "dy", dy);

			program5.addUniform(Uniform.FLOAT, "dx", dx2);
			program5.addUniform(Uniform.FLOAT, "dy", dy2);

			program6.addUniform(Uniform.FLOAT, "dx", dx);
			program6.addUniform(Uniform.FLOAT, "dy", dy);

			program7.addUniform(Uniform.FLOAT, "dx", dx);
			program7.addUniform(Uniform.FLOAT, "dy", dy);

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

			RenderStage s2 = new RenderStage(program2, tx[0],
					GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE0,
					false);
			s2.as_output = true;
			s2.addSelector(new FeatureSelector());
			s2.addUniform(Uniform.FLOAT, "threshold", 0.1);
			s2.addUniform(Uniform.IMAGE, "u_img", 0); // Харрис

			RenderStage s6 = new RenderStage(program5, tx[0],
					GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE0,
					false);
			s6.setVertexCoords(1);
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
					GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE0,
					false);
			s7.as_output = true;
			// s7.addSelector(new SaveSelector("/sdcard/_files/q/"));
			s7.addSelector((HoG = new HoGSelector(Core.core.w, Core.core.h)));
			Core.core.rec.setSelector(HoG);

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
			stages.add(s2);
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
			try {
				GLES20.glViewport(0, 0, width, height);
				Core.core.camera.startPreview();
			} catch (RuntimeException exp) {
			}
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
}
