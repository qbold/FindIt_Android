package com.example.findit.selector;

/*
 * јбстрактный селектор, принимает на вход массив байт gray scale изображени€ и делает какие-то вычислени€
 */
public interface Selector {

	public void select(byte[] data, int width);
}
