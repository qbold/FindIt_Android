package com.example.findit;

import com.example.findit.ml.DataSet;
import com.example.findit.ml.RecognitionSystem;
import com.example.findit.ml.SimpleDeltaAlgorithm;

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
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;

/*
 * ќсновна€ активность приложени€
 */
public class Core extends Activity {

	public Camera camera;
	public DrawView view;
	private WakeLock mWL; // чтобы экран не потухал

	public int w, h; // выбранное разрешение снимка
	public int max_w, max_h; // максимальное разрешение камеры

	public static Core core;

	private DataSet trainingset;
	private RecognitionSystem rec;
	private SimpleDeltaAlgorithm alg;

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

		trainingset = DataSet.loadXML(R.xml.set); // загружаем выборку

		alg = new SimpleDeltaAlgorithm("Column2", 0.8f); // алгоритм
		alg.setDataSet(trainingset);

		rec = new RecognitionSystem(alg); // запускаем систему распознавани€
		rec.start();

		AbsoluteLayout abs = new AbsoluteLayout(this);
		view = new DrawView(this);
		abs.addView(view);

		setContentView(abs);
	}

	@Override
	public void onResume() {
		super.onResume();
		mWL.acquire();

		camera = Camera.open(0);
		Parameters p = camera.getParameters();

		w = 0;
		h = 0;
		max_w = 0;
		max_h = 0;
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
			if (max_h < s.height) {
				max_h = s.height;
			}
		}

		DrawView.dx = 1f / w;
		DrawView.dy = 1f / h;

		DrawView.dx2 = 1f / max_w;
		DrawView.dy2 = 1f / max_h;

		// p.setPreviewSize(w, h);
		// camera.setParameters(p);

		view.onResume();
	}

	/*
	 * –азрешает камере начать уже отправл€ть данные в текстуру
	 */
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
