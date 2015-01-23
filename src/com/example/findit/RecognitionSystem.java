package com.example.findit;

import java.util.ArrayList;

import com.example.findit.ml.DataSet;
import com.example.findit.ml.MLAlgorithm;
import com.example.findit.ml.MLResult;
import com.example.findit.selector.HoGSelector;

/*
 * Система распознавания (постоянно работает и периодически обновляет результаты)
 */
public class RecognitionSystem extends Thread {

	private MLAlgorithm algorithm;
	private DrawListener draw;
	private HoGSelector hog;
	private ArrayList<RecognizesObject> objects;

	private long sleep;

	private long dead_time;

	{
		objects = new ArrayList<>();
		dead_time = 1000L;
		sleep = 2000L;
	}

	public RecognitionSystem() {
	}

	public RecognitionSystem(MLAlgorithm alg) {
		algorithm = alg;
	}

	/*
	 * Установить период распознавания
	 */
	public void setSleep(long s) {
		sleep = s;
	}

	/*
	 * Вернуть период распознавания
	 */
	public long getSleep() {
		return sleep;
	}

	/*
	 * Установить листенер, который будет отвечать за добавление меток на экран
	 */
	public void setDrawListener(DrawListener d) {
		draw = d;
	}

	/*
	 * Вернуть текущий листенер
	 */
	public DrawListener getDrawListener() {
		return draw;
	}

	/*
	 * Установить время, в течение которого объект держится на экране после
	 * распознавания
	 */
	public void setDeadTime(long t) {
		dead_time = t;
	}

	/*
	 * Вернуть деад тиме
	 */
	public long getDeadTime() {
		return dead_time;
	}

	/*
	 * Установить селектор
	 */
	public void setSelector(HoGSelector h) {
		hog = h;
	}

	/*
	 * Вернуть селектор
	 */
	public HoGSelector getSelector() {
		return hog;
	}

	/*
	 * Установить алгоритм классификации
	 */
	public void setAlgorithm(MLAlgorithm m) {
		algorithm = m;
	}

	/*
	 * Вернуть алгоритм классификации
	 */
	public MLAlgorithm getAlgorithm() {
		return algorithm;
	}

	/*
	 * Добавить в список распознанных
	 */
	private void recognize(int cl) {
		if (draw == null)
			return;
		// если уже есть такой объект, обновляем информацию о нём
		for (RecognizesObject obj : objects) {
			if (obj.id == cl) {
				obj.date = System.currentTimeMillis();
				// потом ещё координаты изменить
				return;
			}
		}
		// если нет, добавляем
		objects.add(new RecognizesObject(System.currentTimeMillis(), cl, 0, 0));
		draw.addObject(cl, 0, 0);
	}

	/*
	 * Удалить из списка распознанных (и метку с экрана)
	 */
	private void removeFromRecognizes(int obj) {
		objects.remove(obj);
		draw.removeObject(obj);
	}

	@Override
	public void run() {
		DataSet data = new DataSet();
		String key = algorithm.getKeyString();
		data.add(key, "");
		while (true) {
			try {
				if (hog == null)
					continue;
				if (hog.begin_replacing)
					continue;
				float[] vec = hog.getVector();
				data.set(key, 0, vec);
				MLResult result = algorithm.getResult(data);
				int cl = 0;
				if ((cl = result.getResultClassification()) > 0) {
					// Что-то распознали
					System.out.println("recognize" + cl);
					recognize(--cl);
				}
				// Удаляем старые метки и объекты
				long s = System.currentTimeMillis();
				for (int i = 0; i < objects.size(); i++) {
					RecognizesObject obj = objects.get(i);
					if (s - obj.date - dead_time > 0) {
						removeFromRecognizes(i);
						i--;
					}
				}
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Описывает распознанный объект
	 */
	private class RecognizesObject {

		public long date;
		public int id, x, y;

		public RecognizesObject(long d, int id, int x, int y) {
			date = d;
			this.id = id;
			this.x = x;
			this.y = y;
		}
	}
}
