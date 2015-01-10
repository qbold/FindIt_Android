package com.example.findit.selector;

/*
 * ѕростой селектор, возвращает массив с пиксел€ми
 */
public class SimpleSelector implements Selector {

	private byte[] data;

	public SimpleSelector() {
	}

	public byte[] getData() {
		return data;
	}

	@Override
	public void select(byte[] data, int width) {
		this.data = data;
	}
}
