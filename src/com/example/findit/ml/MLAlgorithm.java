package com.example.findit.ml;

/*
 * Абстрактный алгоритм машинного обучения
 */
public abstract class MLAlgorithm {

	private DataSet set; // обучающая выборка

	/*
	 * Установить обучающую выборку
	 */
	public void setDataSet(DataSet set) {
		this.set = set;
	}

	/*
	 * Вернуть обучающую выборку
	 */
	public DataSet getDataSet() {
		return set;
	}

	/*
	 * Обучиться на выборке (оставлено на реализацию детям в качестве
	 * упражнения)
	 */
	public abstract void learn();

	/*
	 * Получить результат применения алгоритма к выборке data
	 */
	public abstract MLResult getResult(DataSet data);
}
