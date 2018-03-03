package com.mygdx.audioryder.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.audioryder.AudioRyder;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "AudioRyder";
		config.width = 1024;
		config.height = 600;
		new LwjglApplication(new AudioRyder(), config);
	}
}
