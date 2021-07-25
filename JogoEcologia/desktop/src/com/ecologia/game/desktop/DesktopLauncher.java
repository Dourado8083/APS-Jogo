package com.ecologia.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ecologia.game.JogoEcologia;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new JogoEcologia(), config);
		config.title = "Jogo Ecologia";
		config.useGL30 = false;
		config.width = 800;
		config.height = 480;
	}
}
