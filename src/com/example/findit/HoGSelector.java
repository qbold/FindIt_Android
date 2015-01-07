package com.example.findit;

public class HoGSelector implements Selector {

	private int count_columns, width_cells, height_cells, width_blocks,
			height_blocks, width_image, height_image;
	private int count_cells_w, count_cells_h, count_blocks_w, count_blocks_h;

	private byte[] histogram;

	{
		count_columns = 9;
		width_cells = 6;
		height_cells = 6;
		width_blocks = 3;
		height_blocks = 3;
	}

	public HoGSelector(int width_image, int height_image) {
		this.width_image = width_image;
		this.height_image = height_image;
		gen();
	}

	public HoGSelector(int count_columns, int width_image, int height_image) {
		this.count_columns = count_columns;
		this.width_image = width_image;
		this.height_image = height_image;
		gen();
	}

	public HoGSelector(int count_columns, int width_cells, int height_cells,
			int width_blocks, int height_blocks, int width_image,
			int height_image) {
		this.count_columns = count_columns;
		this.width_image = width_image;
		this.width_cells = width_cells;
		this.height_cells = height_cells;
		this.width_blocks = width_blocks;
		this.height_blocks = height_blocks;
		this.height_image = height_image;
		gen();
	}

	private void gen() {
		count_cells_w = width_image / width_cells;
		count_cells_h = height_image / height_cells;

		count_blocks_w = count_cells_w / width_blocks;
		count_blocks_h = count_cells_h / height_blocks;

		histogram = new byte[count_columns * count_cells_w * count_cells_h];
	}

	@Override
	public void select(byte[] data, int width) {
		// TODO: Наверное это будет лучше сделать через NDK в нативном коде..

		// Формируем гистограмму ориентированных градиентов по значениям
		// направлений градиентов

		for (int i = 0; i < data.length; i++) {
			int x = i % width;
			int y = i / width;

			int index_cell_x = x / count_cells_w;
			int index_cell_y = y / count_cells_h;
			int index_cell = index_cell_x + index_cell_y * count_cells_w;

			histogram[index_cell * count_columns + data[i] / count_columns]++; // заполняем
																				// гистограмму
																				// (по
																				// порядку
																				// в
																				// массиве
																				// идут
																				// по
																				// count_columns
																				// бинов
																				// гистограммы
																				// для
																				// каждого
																				// индекса
																				// ячейки)
		}
	}
}
