package com.example.findit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

/*
 * Основная активность приложения
 */
public class Core extends Activity {

	public Camera camera;

	public DrawView view;
	public int w, h;
	public int[] pix;
	public Bitmap bitmap;
	public static Core core;
	private WakeLock mWL;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle v) {
		super.onCreate(v);
		core = this;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mWL = ((PowerManager) getSystemService(Context.POWER_SERVICE))
				.newWakeLock(PowerManager.FULL_WAKE_LOCK, "WakeLock");
		mWL.acquire();

		view = new DrawView(this);

		setContentView(view);
	}

	@Override
	public void onResume() {
		super.onResume();
		mWL.acquire();

		camera = Camera.open(0);
		Parameters p = camera.getParameters();

		w = 0;
		h = 0;
		int max_w = 0;
		for (Size s : p.getSupportedPreviewSizes()) {
			if (
			// w == 0 ||
			(w > s.width || w == 0)) {
				w = s.width;
				h = s.height;
			}
			if (max_w < s.width) {
				max_w = s.width;
			}
			if (max_w < s.height) {
				max_w = s.height;
			}
		}

		DrawView.dx = 1f / max_w;
		DrawView.dy = 1f / max_w;

		pix = new int[w * h];
		// p.setPreviewSize(w, h);
		// camera.setParameters(p);
		view.onResume();
	}

	public void startPreview() {
		if (camera != null)
			try {
				camera.setPreviewTexture(view.tex);

				camera.startPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mWL.isHeld())
			mWL.release();

		if (camera != null) {
			camera.setPreviewCallback(null);
			camera.stopPreview();
			camera.release();
			camera = null;
		}
		view.onPause();

	}

}
