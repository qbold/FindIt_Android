package com.example.findit.ml;

/*
 * ����������� �������� ��������� ��������
 */
public abstract class MLAlgorithm {

	private DataSet set; // ��������� �������

	/*
	 * ���������� ��������� �������
	 */
	public void setDataSet(DataSet set) {
		this.set = set;
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
