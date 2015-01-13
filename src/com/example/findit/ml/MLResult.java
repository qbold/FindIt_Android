package com.example.findit.ml;

/*
 * Описывает результат алгоритма машинного обучения
 */
public class MLResult {

	public static final byte CLASSIFICATION = 0;

	private byte type;

	private int class_;

	protected MLResult(int d) {
		this.type = CLASSIFICATION;
		class_ = d;
	}

	/*
	 * Установка данных результата классификации
	 */
	protected void setDataClassification(int cl) {
		class_ = cl;
	}

	/*
	 * Вернуть результат классификации
	 */
	public int getResultClassification() {
		if (type != CLASSIFICATION)
			return -1;
		return class_;
	}
}
