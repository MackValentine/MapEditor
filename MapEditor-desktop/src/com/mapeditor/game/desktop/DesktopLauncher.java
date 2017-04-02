package com.mapeditor.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mapeditor.game.MapEditor;
import com.mapeditor.screen.Screen;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Screen.Width + 216+32;
		config.height = Screen.Height + 44+32;
		config.resizable = false;
		new LwjglApplication(new MapEditor(), config);

	}
}
