package com.example.findit.ml;

/*
 * ������� ��������, ������������ �������
 */
public class SimpleDeltaAlgorithm extends MLAlgorithm {

	private float percentage, up_percentage;

	private String key; // ���� � ������� ������� DataSet, ���������� �������

	{
		up_percentage = 1f;
	}

	public SimpleDeltaAlgorithm() {
		this.percentage = 0.6f;
	}

	public SimpleDeltaAlgorithm(String key, float percentage) {
		this.key = key;
		this.percentage = percentage;
	}

	/*
	 * ���������� ������� ������� �������������
	 */
	public void setUpperLimitPercentage(float f) {
		up_percentage = f;
	}

	/*
	 * ������� ������� ������� (�������� �������������, �������� �������
	 * �������� ���������������, �� ��� ��� ��������)
	 */
	public float getUpperLimitPercentage() {
		return up_percentage;
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
	 * ���������� ������� ������������� [0; 1], ����� ������� ��� ���������
	 * ��������
	 */
	public void setPercentage(float f) {
		percentage = f;
	}

	/*
	 * ������� ������� �������������
	 */
	public float getPercentage() {
		return percentage;
	}

	@Override
	public void learn() {
		// �� ���� �������
	}

	/*
	 * ���������� 0 ���� ������ �� ���������, ����� �� 1 ���������� �����
	 */
	@Override
	public MLResult getResult(DataSet data) {
		float[] ds = data.getFloatVector(key, 0, ";");

		float sum = 0;
		for (int i = 0; i < ds.length; i++) {
			sum += ds[i] * ds[i];
		}

		DataSet my = getDataSet();
		int size = my.size(key);

		float max_percent = 0f;
		int max_index = 0;

		for (int i = 0; i < size; i++) {
			float[] sess = my.getFloatVector(key, i, ";");
			float delta = 0;
			for (int j = 0; j < sess.length; j++) {
				float e = sess[j] - ds[j];
				delta += e * e;
			}
			delta /= sum;
			delta = 1f - delta; // �������
			if (delta > max_percent) {
				max_percent = delta;
				max_index = i;

				if (max_percent >= up_percentage)
					break;
			}
		}

		if (max_percent >= percentage) {
			return new MLResult(max_index + 1);
		}

		return new MLResult(0);
	}
}
