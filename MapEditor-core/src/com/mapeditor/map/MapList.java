package com.mapeditor.map;

import java.io.File;
import java.io.FileNotFoundException;

import com.badlogic.gdx.files.FileHandle;

public class MapList {

	public int width = 5;
	public int height = 5;

	public String name;

	public String[][] id;
	public int index;

	public MapList(String s) throws FileNotFoundException {
		name = s;
		File f = new File("data/maps/" + s);
		if (!f.exists())
			f.mkdirs();

		create();
	}

	public MapList(FileHandle fHandle) {
		name = fHandle.name();
	}

	public MapList() throws FileNotFoundException {

		int i = 0;
		File f;
		String filename;
		do {

			filename = "new" + (i++);
			f = new File("data/maps/" + filename);

		} while (f.exists());

		f.mkdir();

		name = filename;

		create();
	}

	private void create() throws FileNotFoundException {
		id = new String[width][height];

		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				Map m = new Map();
				m.parent_name = name;
				m.Save();
			}
		}

	}

}
