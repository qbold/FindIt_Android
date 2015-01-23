package com.example.findit.ml;

import libsvm.*;

public class SVMAlgorithm extends MLAlgorithm {

	private svm_problem problem; // обучающая выборка
	private svm_parameter parameter; // параметры машины (опорных векторов)
	private svm_model model; // свм_модель

	private svm_node[] current_nodes; // очень нужный массив для реал тайма,
										// позволяет избавиться от динамической
										// аллокации объектов
	private double[] current_probabilities; // массив для записи вероятностей
											// совпадения объекта с каждым из
											// классов

	private float percentage;

	public SVMAlgorithm() {
		percentage = 0.7f;
	}

	public SVMAlgorithm(String key, float percentage) {
		this.percentage = percentage;
		setKeyString(key);
	}

	@Override
	public void learn() {
		DataSet set = getDataSet();
		String key = getKeyString();
		int ln = set.getFloatVector(key, 0, ";").length;

		problem = new svm_problem();
		parameter = new svm_parameter();
		parameter.probability = 1;
		parameter.svm_type = svm_parameter.C_SVC;
		parameter.kernel_type = svm_parameter.RBF;
		parameter.degree = 3;
		parameter.gamma = 1f / ln; // 1/num_features
		parameter.coef0 = 0;
		parameter.nu = 0.5;
		parameter.cache_size = 100;
		parameter.C = 1;
		parameter.eps = 1e-3;
		parameter.p = 0.1;
		parameter.shrinking = 1;
		parameter.nr_weight = 0;
		parameter.weight_label = new int[0];
		parameter.weight = new double[0];

		problem.l = set.size(key);
		problem.y = new double[problem.l];
		problem.x = new svm_node[problem.l][ln];

		current_nodes = new svm_node[problem.x[0].length];
		current_probabilities = new double[set.getCountClasses()];
		for (int i = 0; i < current_nodes.length; i++) {
			current_nodes[i] = new svm_node();
			current_nodes[i].index = i + 1;
		}

		for (int i = 0; i < problem.l; i++) {
			float[] dt = set.getFloatVector(key, i, ";");
			for (int j = 0; j < problem.x[i].length; j++) {
				svm_node node = new svm_node();
				node.index = j + 1;
				node.value = dt[j];
				problem.x[i][j] = node;
				problem.y[i] = set.getClassOfObject(i);
			}
		}

		model = svm.svm_train(problem, parameter);
	}

	@Override
	public MLResult getResult(DataSet data) {
		float[] d = data.getFloatVector(getKeyString(), 0, ";");
		for (int i = 0; i < d.length; i++) {
			current_nodes[i].value = d[i];
		}
		svm.svm_predict_probability(model, current_nodes, current_probabilities);

		double max_probability = 0;
		int max_index = 0;
		for (int i = 0; i < current_probabilities.length; i++) {
			System.out.println(current_probabilities[i] + " c " + i);
			if (max_probability < current_probabilities[i]) {
				max_probability = current_probabilities[i];
				max_index = i;
			}
		}
		System.out.println(max_probability + " " + max_index);
		if (max_probability >= percentage) {
			return new MLResult(max_index + 1);
		}

		return new MLResult(0);
	}
}
