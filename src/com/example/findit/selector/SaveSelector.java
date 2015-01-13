package com.example.findit.selector;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;

/*
 * Селектор, сохраняющий изображение в файловую систему телефона
 */
public class SaveSelector implements Selector {

	private String path;
	private Random r;
	private int[] pix;

	public SaveSelector(String path) {
		this.path = path;
		r = new Random();
	}

	public SaveSelector(String path, Random r) {
		this.path = path;
		this.r = r;
	}

	/*
	 * Получить путь сохранения
	 */
	public String getPath() {
		return path;
	}

	/*
	 * Установить путь сохранения
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/*
	 * Обработать полученные пиксели и сохранить в ФС
	 */
	@Override
	public void select(byte[] data, int width) {
		if (pix == null || data.length != pix.length)
			pix = new int[data.length];
		for (int i = 0; i < pix.length; i++) {
			short clr = data[i];
			if (clr < 0) {
				clr += 256;
			}
			clr &= 0xff;
			pix[i] = 0xff000000 | (clr << 16) | (clr << 8) | clr;
		}

		Bitmap b = Bitmap.createBitmap(pix, width, data.length / width,
				Config.ARGB_8888);
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(path + Math.abs(r.nextInt()) + ".png");
			b.compress(CompressFormat.PNG, 100, os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (Exception s) {
				s.printStackTrace();
			}
		}
	}
}
