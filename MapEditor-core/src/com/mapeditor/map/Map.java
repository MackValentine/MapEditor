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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Map {

	public static boolean needRefresh = true;

	public int[][][] layer;

	public String name;

	public String parent_name = "";

	public Tileset tileset;

	public int width;
	public int height;

	private int max_layer;

	public Texture tilesetR;

	public TextureRegion[] tiles;

	public int[][] top;

	public Map() throws FileNotFoundException {
		width = 30;
		height = 16;

		max_layer = 3;

		layer = new int[max_layer][width][height];
		top = new int[width][height];

		loadTiles("tiles");

		File dir2 = new File(".\\data\\tilesets\\" + "tiles" + ".xml");
		FileInputStream f = new FileInputStream(dir2);

		tileset = new Tileset(f, "tiles");

		tileset.Save();

	}

	public Map(String string) {

		Load(Gdx.files.internal("data/maps/" + string));

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

		if (id < 0) {
			for (int j = 0; j < height; ++j) {
				for (int i = 0; i < width; ++i) {
					s += top[i][j];
					if (i < width - 1) {
						s += ",";
					}
				}
				s += ";";
			}
		} else
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
			// reader.require(XmlPullParser.START_TAG, null, null);

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

							if (s.equals("layer3")) {

								layer[2] = new int[width][height];
								String m = reader.getAttributeValue(i);
								String[] m2 = m.split(";");
								for (int j = 0; j < m2.length; ++j) {
									String[] m3 = m2[j].split(",");
									for (int l = 0; l < m3.length; ++l) {
										layer[2][l][j] = Integer.valueOf(m3[l]);
									}
								}
							}

							if (s.equals("top")) {

								top = new int[width][height];
								String m = reader.getAttributeValue(i);
								String[] m2 = m.split(";");
								for (int j = 0; j < m2.length; ++j) {
									String[] m3 = m2[j].split(",");
									for (int l = 0; l < m3.length; ++l) {
										top[l][j] = Integer.valueOf(m3[l]);
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
								FileInputStream f = new FileInputStream("data/tilesets/" + tileset.name + ".xml");
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
		
		needRefresh = true;
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
				String s = parent_name;
				if (s != "")
					s += "/";
				filename = s + "map" + (i++) + ".xml";
				f = new File("data/maps/" + filename);

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

			osw = new OutputStreamWriter(new FileOutputStream("data/maps/" + filename));
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
			writer.attribute(NAMESPACE, "layer3", to_s(2));

			writer.attribute(NAMESPACE, "top", to_s(-1));

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

	public void draw(SpriteBatch batch, int id, int i, int j, int m, int x, int y) {
		if (id > 0 && id <= 7 && tileset.autotiles[id - 1] != null) {
			int t[] = new int[8];

			if (i + 1 < width)
				t[0] = layer[m][i + 1][j];
			if (i - 1 >= 0)
				t[1] = layer[m][i - 1][j];

			if (j + 1 < height)
				t[2] = layer[m][i][j + 1];
			if (j - 1 >= 0)
				t[3] = layer[m][i][j - 1];

			if (i + 1 < width && j + 1 < height)
				t[4] = layer[m][i + 1][j + 1];
			if (i + 1 < width && j - 1 >= 0)
				t[5] = layer[m][i + 1][j - 1];

			if (i - 1 >= 0 && j - 1 >= 0)
				t[6] = layer[m][i - 1][j - 1];

			if (i - 1 >= 0 && j + 1 < height)
				t[7] = layer[m][i - 1][j + 1];
			// tileset.autotiles[id - 1].render(batch, x, y, t);

			tileset.autotiles[id - 1].render(batch, 16 * i + 16, 16 * j + 5 + 16, width, height, id, layer[m]);
		} else {
			batch.draw(tiles[id], x, y);
		}
	}

}
