package com.example.findit.ml;

/*
 * Простой алгоритм, сравнивающий векторы
 */
public class SimpleDeltaAlgorithm extends MLAlgorithm {

	private float percentage, up_percentage;

	{
		up_percentage = 1f;
	}

	public SimpleDeltaAlgorithm() {
		this.percentage = 0.6f;
	}

	public SimpleDeltaAlgorithm(String key, float percentage) {
		setKeyString(key);
		this.percentage = percentage;
	}

	/*
	 * Установить верхнюю границу правдоподобия
	 */
	public void setUpperLimitPercentage(float f) {
		up_percentage = f;
	}

	/*
	 * Вернуть верхнюю границу (величина правдоподобия, достигая которую
	 * алгоритм останавливается, не ищя его максимум)
	 */
	public float getUpperLimitPercentage() {
		return up_percentage;
	}

	/*
	 * Установить границу классификации [0; 1], после которой она считается
	 * успешной
	 */
	public void setPercentage(float f) {
		percentage = f;
	}

	/*
	 * Вернуть границу классификации
	 */
	public float getPercentage() {
		return percentage;
	}

	@Override
	public void learn() {
		// Не умею учиться
	}

	/*
	 * Разность по Бхаттачарию
	 */
	private MLResult getBhattachary(DataSet data) {
		float[] ds = data.getFloatVector(getKeyString(), 0, ";");
		float sum_h1 = 0;
		for (int i = 0; i < ds.length; i++) {
			sum_h1 += ds[i];
		}

		int sz = getDataSet().size(getKeyString());
		int max_index = 0;
		float max_percent = 0;
		for (int i = 0; i < sz; i++) {
			float[] d = getDataSet().getFloatVector(getKeyString(), i, ";");
			float sum_h2 = 0;
			for (int j = 0; j < ds.length; j++) {
				sum_h2 += d[j];
			}
			float sum_h1_h2 = sum_h2 * sum_h1;

			float sum = 0;
			for (int j = 0; j < ds.length; j++) {
				sum += Math.sqrt(d[j] * ds[j] / sum_h1_h2);
			}
			sum = (float) Math.sqrt(1f - sum);

			float percent = 1f - sum;
			if (percent > max_percent) {
				max_percent = percent;
				max_index = i;
			}
		}
		if (max_percent > percentage) {
			System.out.println("MAX PERCENT: " + max_percent);
			return new MLResult(max_index + 1);
		}
		return new MLResult(0);
	}

	/*
	 * Возвращает 0 если ничего не распознал, числа от 1 показывают класс
	 */
	@Override
	public MLResult getResult(DataSet data) {
		float[] ds = data.getFloatVector(getKeyString(), 0, ";"); // то, что
		// распознаём
		// Найдём разницу между векторами с помощью расстояния Бхаттачария
		float sum_h1 = 0;
		for (int i = 0; i < ds.length; i++) {
			sum_h1 += ds[i];
		}

		int sz = getDataSet().size(getKeyString());
		int max_index = 0;
		float max_percent = 0;
		for (int i = 0; i < sz; i++) {
			float[] d = getDataSet().getFloatVector(getKeyString(), i, ";");
			float sum_h2 = 0;
			for (int j = 0; j < ds.length; j++) {
				sum_h2 += d[j];
			}
			float sum_h1_h2 = sum_h2 * sum_h1;

			float sum = 0;
			for (int j = 0; j < ds.length; j++) {
				sum += Math.sqrt(d[j] * ds[j] / sum_h1_h2);
			}
			sum = (float) Math.sqrt(1f - sum);

			float percent = 1f - sum;
			if (percent > max_percent) {
				max_percent = percent;
				max_index = i;
			}
		}
		if (max_percent > percentage) {
			System.out.println("MAX PERCENT: " + max_percent);
			return new MLResult(max_index + 1);
		}
		return new MLResult(0);
	}
}
