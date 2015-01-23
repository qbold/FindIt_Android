package com.example.findit;

import java.util.ArrayList;

import com.example.findit.ml.DataSet;
import com.example.findit.ml.MLAlgorithm;
import com.example.findit.ml.MLResult;
import com.example.findit.selector.HoGSelector;

/*
 * ������� ������������� (��������� �������� � ������������ ��������� ����������)
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
	 * ���������� ������ �������������
	 */
	public void setSleep(long s) {
		sleep = s;
	}

	/*
	 * ������� ������ �������������
	 */
	public long getSleep() {
		return sleep;
	}

	/*
	 * ���������� ��������, ������� ����� �������� �� ���������� ����� �� �����
	 */
	public void setDrawListener(DrawListener d) {
		draw = d;
	}

	/*
	 * ������� ������� ��������
	 */
	public DrawListener getDrawListener() {
		return draw;
	}

	/*
	 * ���������� �����, � ������� �������� ������ �������� �� ������ �����
	 * �������������
	 */
	public void setDeadTime(long t) {
		dead_time = t;
	}

	/*
	 * ������� ���� ����
	 */
	public long getDeadTime() {
		return dead_time;
	}

	/*
	 * ���������� ��������
	 */
	public void setSelector(HoGSelector h) {
		hog = h;
	}

	/*
	 * ������� ��������
	 */
	public HoGSelector getSelector() {
		return hog;
	}

	/*
	 * ���������� �������� �������������
	 */
	public void setAlgorithm(MLAlgorithm m) {
		algorithm = m;
	}

	/*
	 * ������� �������� �������������
	 */
	public MLAlgorithm getAlgorithm() {
		return algorithm;
	}

	/*
	 * �������� � ������ ������������
	 */
	private void recognize(int cl) {
		if (draw == null)
			return;
		// ���� ��� ���� ����� ������, ��������� ���������� � ��
		for (RecognizesObject obj : objects) {
			if (obj.id == cl) {
				obj.date = System.currentTimeMillis();
				// ����� ��� ���������� ��������
				return;
			}
		}
		// ���� ���, ���������
		objects.add(new RecognizesObject(System.currentTimeMillis(), cl, 0, 0));
		draw.addObject(cl, 0, 0);
	}

	/*
	 * ������� �� ������ ������������ (� ����� � ������)
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
					// ���-�� ����������
					System.out.println("recognize" + cl);
					recognize(--cl);
				}
				// ������� ������ ����� � �������
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
	 * ��������� ������������ ������
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
