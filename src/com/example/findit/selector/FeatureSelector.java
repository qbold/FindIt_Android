package com.example.findit.selector;

import java.util.Comparator;
import java.util.PriorityQueue;

/*
 * Находит локальные особенности
 */
public class FeatureSelector implements Selector {

	private PriorityQueue<Feature> features;

	private EvaluationFunction eval;
	private Comparator<Feature> comparator;
	private static EvaluationFunction default_eval;

	private static int DEFAULT_SIZE_QUEUE = 500;

	private int threshold;

	static {
		default_eval = new EvaluationFunction() {

			@Override
			public double evaluate(Feature f) {
				return f.intensity;
			}
		};
	}

	{
		eval = default_eval;

		comparator = new Comparator<Feature>() {

			@Override
			public int compare(Feature lhs, Feature rhs) {
				double a = eval.evaluate(lhs);
				double b = eval.evaluate(rhs);
				return a > b ? -1 : (a == b ? 0 : 1);
			}
		};
	}

	public FeatureSelector() {
		threshold = 0;
	}

	public FeatureSelector(int threshold) {
		this.threshold = threshold;
	}

	public FeatureSelector(EvaluationFunction f) {
		this.eval = f;
	}

	public FeatureSelector(EvaluationFunction f, int threshold) {
		this.eval = f;
		this.threshold = threshold;
	}

	/*
	 * Установить функцию оценки
	 */
	public void setEvaluationFunction(EvaluationFunction f) {
		this.eval = f;
	}

	/*
	 * Вернуть функцию оценки
	 */
	public EvaluationFunction getEvaluationFunction() {
		return eval;
	}

	/*
	 * Вернуть приоритетную очередь с признаками
	 */
	public PriorityQueue<Feature> getFeatures() {
		return features;
	}

	/*
	 * Установить порог отсева в пикселях
	 */
	public void setThreshold(int t) {
		threshold = t;
	}

	/*
	 * Вернуть порог отсева
	 */
	public int getThreshold() {
		return threshold;
	}

	/*
	 * Поиск особенностей
	 */
	@Override
	public void select(byte[] data, int width) {
		PriorityQueue<Feature> p = new PriorityQueue<>(DEFAULT_SIZE_QUEUE,
				comparator);
		int h = data.length / width;
		for (int i = 0; i < data.length; i++) {
			short clr = data[i];
			if (clr < 0) {
				clr += 256;
			}

			if (clr <= threshold)
				continue;

			if (i > 0) {
				p.add(new Feature(i % width, i / h, clr));
			}
		}

		// System.out.println(p.size());

		features = p;
	}

	public static interface EvaluationFunction {

		/*
		 * Функция оценки (чем выше, тем приоритетнее)
		 */
		public double evaluate(Feature f);
	}

	public static class Feature {

		public short intensity;
		public short x, y;

		public Feature(int x, int y, int I) {
			this.x = (short) x;
			this.y = (short) y;
			this.intensity = (short) I;
		}
	}
}
