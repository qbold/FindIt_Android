package com.example.findit;

/*
 * Описывает юниформу для шейдерной программы
 */
public class Uniform {

	public byte type;
	private float d_float;
	private int d_image;
	private int adr;
	public static final byte FLOAT = 0, IMAGE = 1;

	public Uniform(int type, int adr, double v) {
		this.type = (byte) type;
		this.adr = adr;
		if (type == FLOAT)
			d_float = (float) v;
		else if (type == IMAGE)
			d_image = (int) v;
	}

	public float getFloat() {
		return d_float;
	}

	public int getTexture() {
		return d_image;
	}

	public int getAddress() {
		return adr;
	}
}
