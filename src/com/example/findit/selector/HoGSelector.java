package com.example.findit.selector;

/*
 * —троит гистограмму ориентированных градиентов по данным изображени€
 */
public class HoGSelector implements Selector {

	private int count_columns, width_cells, height_cells, width_blocks,
			height_blocks, width_image, height_image;
	private int count_cells_w, count_cells_h, count_blocks_w, count_blocks_h,
			step_blocks;

	private byte[] cells_histogram; // гистограмма по каждой €чейке

	private float[] blocks_histogram; // гистограмма по блокам

	{
		count_columns = 9;
		width_cells = 8;
		height_cells = 8;
		width_blocks = 4;
		height_blocks = 4;
		step_blocks = 2;
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
			int height_image, int step_blocks) {
		this.count_columns = count_columns;
		this.width_image = width_image;
		this.width_cells = width_cells;
		this.height_cells = height_cells;
		this.width_blocks = width_blocks;
		this.height_blocks = height_blocks;
		this.height_image = height_image;
		this.step_blocks = step_blocks;
		gen();
	}

	private void gen() {
		count_cells_w = width_image / width_cells;
		count_cells_h = height_image / height_cells;

		count_blocks_w = (count_cells_w - width_blocks + step_blocks)
				/ step_blocks;
		count_blocks_h = (count_cells_h - height_blocks + step_blocks)
				/ step_blocks;

		cells_histogram = new byte[count_columns * count_cells_w
				* count_cells_h];
		blocks_histogram = new float[count_columns * count_blocks_w
				* count_blocks_h];
	}

	@Override
	public void select(byte[] data, int width) {
		// ‘ормируем гистограмму ориентированных градиентов

		for (int i = 0; i < data.length; i++) {
			int x = i % width;
			int y = i / width;

			int index_cell_x = x / width_cells;
			int index_cell_y = y / height_cells;
			int index_cell = index_cell_x + index_cell_y * count_cells_w;

			short data2 = data[i];
			if (data2 < 0) {
				data2 = (short) (data2 + 256);
			}
			int num_bin = (int) (data2 * count_columns / 256d); // номер
																// бина
																// в
																// гистограмме

			cells_histogram[index_cell * count_columns + num_bin]++; // заполн€ем
			// гистограмму
			// (по
			// пор€дку
			// в
			// массиве
			// идут
			// по
			// count_columns
			// бинов
			// гистограммы
			// дл€
			// каждого
			// индекса
			// €чейки)
		}
		int s = count_blocks_w * count_blocks_h;
		for (int i = 0; i < s; i++) {
			int x_block = i % count_blocks_w;
			int y_block = i / count_blocks_w;
			int x_cell = x_block * step_blocks; // x €чейки, с которой
												// начинаетс€ данный блок
			int y_cell = y_block * step_blocks; // y €чейки, с которой
												// начинаетс€ данный блок
			int index_block = i * count_columns;
			for (int x = 0; x < width_blocks; x++) {
				for (int y = 0; y < height_blocks; y++) {
					int x_ = x + x_cell; // x текущей €чейки
					int y_ = y + y_cell; // у текущей €чейки
					int index_ = x_ + y_ * count_cells_w;
					for (int k = 0; k < count_columns; k++) {
						blocks_histogram[k + index_block] += cells_histogram[index_
								+ k];
					}
				}
			}

			// Ќормализаци€ гистограммы
			float div = 0f;
			for (int k = 0; k < count_columns; k++) {
				div += blocks_histogram[index_block + k]
						* blocks_histogram[index_block + k];
			}
			div = (float) Math.sqrt(div);
			for (int k = 0; k < count_columns; k++) {
				blocks_histogram[index_block + k] /= div;
			}
		}
	}
}
