package com.example.findit.ml;

/*
 * ������� ������������� (��������� �������� � ������������ ��������� ����������)
 */
public class RecognitionSystem extends Thread {

	private MLAlgorithm algorithm;

	public RecognitionSystem() {
	}

	public RecognitionSystem(MLAlgorithm alg) {
		algorithm = alg;
	}

	/*
	 * ���������� �������� �������������
	 */
	public void setAlgorithm(MLAlgorithm m) {
		algorithm = m;
	}

	/*
	 * ������� �������� �������������
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
