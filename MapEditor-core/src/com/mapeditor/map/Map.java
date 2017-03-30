package com.mapeditor.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Map {

	public int[][][] layer;

	public String name;

	public Tileset tileset;

	public int width;
	public int height;

	private int max_layer;

	public Texture tilesetR;

	public TextureRegion[] tiles;

	public Map() throws FileNotFoundException {
		width = 20;
		height = 15;

		max_layer = 2;

		layer = new int[max_layer][width][height];

		loadTiles("tiles");

		
		File dir2 = new File(".\\maps\\tilesets\\" + "tiles" + ".xml");
		FileInputStream f = new FileInputStream(dir2);
		
		tileset = new Tileset(f,"tiles");

		tileset.Save();

	}

	public Map(String string) {

		Load(Gdx.files.internal("test.xml"));

	}

	public void loadTiles(String s) {
		tilesetR = new Texture(Gdx.files.local("tilesets/" + s + ".png"));
		int w = tilesetR.getWidth() / 16;
		int h = tilesetR.getHeight() / 16;
		tiles = new TextureRegion[w * h];
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				tiles[i + j * w] = new TextureRegion(tilesetR, i * 16, j * 16, 16, 16);
			}
		}
	}

	public String to_s(int id) {
		String s = "";

		for (int j = 0; j < height; ++j) {
			for (int i = 0; i < width; ++i) {
				s += layer[id][i][j];
				if (i < width - 1) {
					s += ",";
				}
			}
			s += ";";
		}

		return s;
	}

	public boolean Load(FileHandle fileHandle) {

		boolean success = true;
		MXParser reader = null;
		try {
			reader = new MXParser();
			reader.setInput(fileHandle.read(), "UTF-8");

			reader.nextTag();
			//reader.require(XmlPullParser.START_TAG, null, null);

			if (reader.getName().equals("Map")) {
				for (int i = 0; i < reader.getAttributeCount(); ++i) {
					String s = reader.getAttributeName(i);
					if (s.equals("Name")) {
						name = reader.getAttributeValue(i);
					}
				}
			}

			int tag = reader.getEventType();

			while (tag != XmlPullParser.END_DOCUMENT) {
				if (tag == XmlPullParser.START_TAG) {
					final String n = reader.getName();

					if (n.equals("layers")) {
						for (int i = 0; i < reader.getAttributeCount(); ++i) {
							String s = reader.getAttributeName(i);

							if (s.equals("width")) {
								width = Integer.valueOf(reader.getAttributeValue(i));
							}
							if (s.equals("height")) {
								height = Integer.valueOf(reader.getAttributeValue(i));
							}
							if (s.equals("max")) {
								max_layer = Integer.valueOf(reader.getAttributeValue(i));
								layer = new int[max_layer][][];
							}

							if (s.equals("layer1")) {
								layer[0] = new int[width][height];
								String m = reader.getAttributeValue(i);
								String[] m2 = m.split(";");
								for (int j = 0; j < m2.length; ++j) {
									String[] m3 = m2[j].split(",");
									for (int l = 0; l < m3.length; ++l) {
										layer[0][l][j] = Integer.valueOf(m3[l]);
									}
								}
							}
							if (s.equals("layer2")) {

								layer[1] = new int[width][height];
								String m = reader.getAttributeValue(i);
								String[] m2 = m.split(";");
								for (int j = 0; j < m2.length; ++j) {
									String[] m3 = m2[j].split(",");
									for (int l = 0; l < m3.length; ++l) {
										layer[1][l][j] = Integer.valueOf(m3[l]);
									}
								}
							}
						}
					} else if (n.equals("tileset")) {
						for (int i = 0; i < reader.getAttributeCount(); ++i) {
							String s = reader.getAttributeName(i);
							if (s.equals("name")) {
								String m = reader.getAttributeValue(i);
								loadTiles(reader.getAttributeValue(i));
								tileset = new Tileset(m, tiles.length);
								FileInputStream f = new FileInputStream("maps/tilesets/" + tileset.name + ".xml");
								tileset.Load(f);
							}
						}
					}
				}
				tag = reader.next();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		} finally {

		}
		return success;
	}

	public boolean Save() {
		final String NAMESPACE = null;
		final String VERSION = "1.0";

		String filename = name;

		if (name == null) {

			File f;
			int i = 0;
			do {
				filename = "new" + (i++) + ".xml";
				f = new File("maps/" + filename);

			} while (f.exists());

			name = filename;
		}

		boolean success = true;
		boolean indentation = true;
		XmlSerializer writer = null;
		OutputStreamWriter osw = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory
					.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
			writer = factory.newSerializer();

			osw = new OutputStreamWriter(new FileOutputStream("maps/" + filename));
			writer.setOutput(osw);
			writer.startTag(NAMESPACE, "Map");
			writer.attribute(NAMESPACE, "Name", filename);
			writer.attribute(NAMESPACE, "AppVer", VERSION);
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			writer.attribute(NAMESPACE, "Date", dateFormat.format(GregorianCalendar.getInstance().getTime()));
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			writer.attribute(NAMESPACE, "TimeOfDay", timeFormat.format(GregorianCalendar.getInstance().getTime()));
			if (indentation)
				writer.text("\n");

			writer.startTag(NAMESPACE, "tileset");
			writer.attribute(NAMESPACE, "name", tileset.name);
			writer.endTag(NAMESPACE, "tileset");
			if (indentation)
				writer.text("\n");

			writer.startTag(NAMESPACE, "layers");
			writer.attribute(NAMESPACE, "max", Integer.toString(max_layer));
			writer.attribute(NAMESPACE, "width", Integer.toString(width));
			writer.attribute(NAMESPACE, "height", Integer.toString(height));
			writer.attribute(NAMESPACE, "layer1", to_s(0));
			writer.attribute(NAMESPACE, "layer2", to_s(1));

			writer.endTag(NAMESPACE, "layers");
			if (indentation)
				writer.text("\n");
			writer.endTag(NAMESPACE, "Map");
			if (indentation)
				writer.text("\n");
			writer.endDocument();
			writer.flush();
			if (osw != null)
				osw.close();
		} catch (IOException xex) {
			xex.printStackTrace();
			success = false;
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		}
		return success;
	}

}
