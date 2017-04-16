package com.mapeditor.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mapeditor.game.MapEditor;

public class Files {

	public static FileHandle get(String string) {
		// TODO Auto-generated method stub
		if (MapEditor.s == ".") {
			return Gdx.files.local(MapEditor.s + string);
		} else
			return Gdx.files.absolute(MapEditor.s + string);
	}

}
