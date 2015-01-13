package com.example.findit.ml;

/*
 * ��������� ��������� ��������� ��������� ��������
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
	 * ��������� ������ ���������� �������������
	 */
	protected void setDataClassification(int cl) {
		class_ = cl;
	}

	/*
	 * ������� ��������� �������������
	 */
	public int getResultClassification() {
		if (type != CLASSIFICATION)
			return -1;
		return class_;
	}
}
