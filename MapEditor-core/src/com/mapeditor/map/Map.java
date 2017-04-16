package com.mapeditor.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.mapeditor.game.MapEditor;

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

	public ArrayList<Event> events;

	private boolean skipTileset;

	public Map() throws FileNotFoundException {

		events = new ArrayList<Event>();

		width = 30;
		height = 16;

		max_layer = 3;

		layer = new int[max_layer][width][height];
		top = new int[width][height];

		loadTiles("tiles");

		File dir2 = new File(MapEditor.s + "\\data\\tilesets\\" + "tiles" + ".xml");
		if (!dir2.exists()) {
			tileset = new Tileset("tiles", tiles.length);
			tileset.Save();
		}

		FileInputStream f = new FileInputStream(dir2);

		tileset = new Tileset(f, "tiles");

		tileset.Save();

	}

	public Map(String string) {

		events = new ArrayList<Event>();

		Load(Files.get("/data/maps/" + string));

	}

	public Map(String string, boolean i) {

		skipTileset = i;

		events = new ArrayList<Event>();

		Load(Files.get("/data/maps/" + string));

	}

	public void loadTiles(String s) {
		tilesetR = new Texture(Files.get("/tilesets/" + s + ".png"));
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
					String n = reader.getName();

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
								if (!skipTileset) {
									loadTiles(reader.getAttributeValue(i));
									tileset = new Tileset(m, tiles.length);
									FileInputStream f = new FileInputStream(
											MapEditor.s + "/data/tilesets/" + tileset.name + ".xml");
									tileset.Load(f, true);
								} else {
									tileset = new Tileset(m, 1);
									FileInputStream f = new FileInputStream(
											MapEditor.s + "/data/tilesets/" + tileset.name + ".xml");
									tileset.Load(f, false);
								}
							}
						}
					} else if (n.equals("events")) {
						int max = 0;
						for (int i = 0; i < reader.getAttributeCount(); ++i) {
							String s = reader.getAttributeName(i);
							if (s.equals("n")) {
								max = Integer.valueOf(reader.getAttributeValue(i));
							}
						}
						for (int nb = 0; nb < max; ++nb) {
							tag = reader.next();
							n = reader.getName();
							Event e = new Event();
							for (int i = 0; i < reader.getAttributeCount(); ++i) {
								String s = reader.getAttributeName(i);
								if (s.equals("id")) {
									e.id = Integer.valueOf(reader.getAttributeValue(i));
								}
								if (s.equals("type")) {
									e.type = Integer.valueOf(reader.getAttributeValue(i));
								}

								if (s.equals("x")) {
									e.x = Integer.valueOf(reader.getAttributeValue(i));
								}
								if (s.equals("y")) {
									e.y = Integer.valueOf(reader.getAttributeValue(i));
								}
								if (s.equals("charas_name")) {
									String ch = reader.getAttributeValue(i);
									if (!ch.equals(""))
										e.loadCharaset(ch);
								}

								if (s.equals("dest_x")) {
									e.command.dest_x = Integer.valueOf(reader.getAttributeValue(i));
								}
								if (s.equals("dest_y")) {
									e.command.dest_y = Integer.valueOf(reader.getAttributeValue(i));
								}
								if (s.equals("dest_name")) {
									e.command.map_name = reader.getAttributeValue(i);
								}
								
								if (s.equals("dialog")) {
									e.command.dialog = reader.getAttributeValue(i);
								}
								if (s.equals("face")) {
									e.command.face = reader.getAttributeValue(i);
								}
								if (s.equals("back")) {
									e.command.back = reader.getAttributeValue(i);
								}
							}
							events.add(e);
							tag = reader.next();
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
				f = new File(MapEditor.s + "/data/maps/" + filename);

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

			File f = new File(MapEditor.s + "/data/maps/" + filename);

			osw = new OutputStreamWriter(new FileOutputStream(f));

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

			writer.startTag(NAMESPACE, "events");
			writer.attribute(NAMESPACE, "n", Integer.toString(events.size()));
			for (Event e : events) {
				writer.startTag(NAMESPACE, "event");
				saveEvent(e, writer);
				writer.endTag(NAMESPACE, "event");
			}

			writer.endTag(NAMESPACE, "events");
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

	private void saveEvent(Event e, XmlSerializer writer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		final String NAMESPACE = null;
		writer.attribute(NAMESPACE, "id", String.valueOf(e.id));
		writer.attribute(NAMESPACE, "type", String.valueOf(e.type));
		writer.attribute(NAMESPACE, "x", String.valueOf(e.x));
		writer.attribute(NAMESPACE, "y", String.valueOf(e.y));

		writer.attribute(NAMESPACE, "charas_name", e.charaset_name);

		if (e.type == 0) {
			writer.attribute(NAMESPACE, "dest_x", String.valueOf(e.command.dest_x));
			writer.attribute(NAMESPACE, "dest_y", String.valueOf(e.command.dest_y));
			writer.attribute(NAMESPACE, "dest_name", e.command.map_name);
		} else if (e.type == 3) {
			writer.attribute(NAMESPACE, "dialog", e.command.dialog);
			writer.attribute(NAMESPACE, "face", e.command.face);
			writer.attribute(NAMESPACE, "back", e.command.back);
		}

	}

	public void draw(SpriteBatch batch, int id, int i, int j, int m, int x, int y) {
		if (id > 0 && id <= 7 && tileset.autotiles[id - 1] != null) {
			tileset.autotiles[id - 1].render(batch, x, y);
		} else {
			batch.draw(tiles[id], x, y);
		}
	}

	public void dispose() {
		tileset.dispose();

		tilesetR.dispose();

	}

}
