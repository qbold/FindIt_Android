package com.example.findit.ml;

/*
 * ������� ��������, ������������ �������
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
	 * �������� �� �����������
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
	 * ���������� 0 ���� ������ �� ���������, ����� �� 1 ���������� �����
	 */
	@Override
	public MLResult getResult(DataSet data) {
		float[] ds = data.getFloatVector(getKeyString(), 0, ";"); // ��, ���
		// ���������
		// ����� ������� ����� ��������� � ������� ���������� �����������
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
