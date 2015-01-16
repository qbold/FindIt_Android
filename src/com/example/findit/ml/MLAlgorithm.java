package com.example.findit.ml;

/*
 * Абстрактный алгоритм машинного обучения
 */
public abstract class MLAlgorithm {

	private DataSet set; // обучающая выборка

	private String key; // Ключ к колонке таблицы DataSet, содержащий векторы

	/*
	 * Установить обучающую выборку
	 */
	public void setDataSet(DataSet set) {
		this.set = set;
	}

	/*
	 * Установить ключ в DataSet
	 */
	public void setKeyString(String w) {
		this.key = w;
	}

	/*
	 * Получить ключ
	 */
	public String getKeyString() {
		return key;
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
