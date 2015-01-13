package com.example.findit.ml;

/*
 * Система распознавания (постоянно работает и периодически обновляет результаты)
 */
public class RecognitionSystem extends Thread {

	private MLAlgorithm algorithm;

	public RecognitionSystem() {
	}

	public RecognitionSystem(MLAlgorithm alg) {
		algorithm = alg;
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

	@Override
	public void run() {

		while (true) {
			//
		}
	}
}
