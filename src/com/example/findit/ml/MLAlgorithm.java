package com.example.findit.ml;

/*
 * ����������� �������� ��������� ��������
 */
public abstract class MLAlgorithm {

	private DataSet set; // ��������� �������

	private String key; // ���� � ������� ������� DataSet, ���������� �������

	/*
	 * ���������� ��������� �������
	 */
	public void setDataSet(DataSet set) {
		this.set = set;
	}

	/*
	 * ���������� ���� � DataSet
	 */
	public void setKeyString(String w) {
		this.key = w;
	}

	/*
	 * �������� ����
	 */
	public String getKeyString() {
		return key;
	}

	/*
	 * ������� ��������� �������
	 */
	public DataSet getDataSet() {
		return set;
	}

	/*
	 * ��������� �� ������� (��������� �� ���������� ����� � ��������
	 * ����������)
	 */
	public abstract void learn();

	/*
	 * �������� ��������� ���������� ��������� � ������� data
	 */
	public abstract MLResult getResult(DataSet data);
}
