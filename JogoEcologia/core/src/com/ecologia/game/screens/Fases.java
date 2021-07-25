package com.ecologia.game.screens;

public abstract class Fases {

	public abstract void fasePassada();
	public abstract void playerMorreu();
	public static boolean passouFase = false;
	public static boolean playerMorto = false;
}
