package com.example.findit;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class FeatureSelector {

	public static byte[] colors;

	public static void feature(ByteBuffer pixels) {
		pixels.position(0);

		if (colors == null)
			colors = new byte[pixels.capacity()];

		int i = 0;
		while (pixels.hasRemaining()) {
			colors[i++] = pixels.get();
			pixels.get();
			pixels.get();
		}
		pixels.position(0);
	}
}
