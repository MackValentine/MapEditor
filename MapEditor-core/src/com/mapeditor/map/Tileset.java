package com.mapeditor.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public class Tileset {
	public String name;

	public int[] passability;
	public int[] depth;
	public int[] region;

	public int max;

	public Autotile[] autotiles;

	public Tileset(String n, int l) {
		name = n;
		max = l;

		passability = new int[max];
		depth = new int[max];
		region = new int[max];

		autotiles = new Autotile[7];

		autotiles = new Autotile[7];
		try {
			autotiles[0] = new Autotile("test2", 1);
			autotiles[1] = new Autotile("water", 2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Tileset(FileInputStream fh, String s) {
		name = s;
		Load(fh);

		autotiles = new Autotile[7];

		autotiles = new Autotile[7];
		try {
			autotiles[0] = new Autotile("test2", 1);
			autotiles[1] = new Autotile("water", 2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public boolean Save() {
		final String NAMESPACE = null;
		final String VERSION = "1.0";

		String filename = name;

		if (name == null) {

			File f;
			int i = 0;
			do {
				filename = "new" + (i++);
				f = new File("data/tilesets/" + filename + ".xml");

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

			osw = new OutputStreamWriter(new FileOutputStream("data/tilesets/" + filename + ".xml"));
			writer.setOutput(osw);
			writer.startTag(NAMESPACE, "Tileset");
			writer.attribute(NAMESPACE, "Name", filename);
			writer.attribute(NAMESPACE, "AppVer", VERSION);
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			writer.attribute(NAMESPACE, "Date", dateFormat.format(GregorianCalendar.getInstance().getTime()));
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			writer.attribute(NAMESPACE, "TimeOfDay", timeFormat.format(GregorianCalendar.getInstance().getTime()));
			if (indentation)
				writer.text("\n");

			writer.startTag(NAMESPACE, "tiles");
			writer.attribute(NAMESPACE, "max", Integer.toString(max));

			writer.attribute(NAMESPACE, "passability", p_to_s());
			writer.attribute(NAMESPACE, "depth", d_to_s());
			writer.attribute(NAMESPACE, "region", r_to_s());

			writer.endTag(NAMESPACE, "tiles");
			if (indentation)
				writer.text("\n");
			writer.endTag(NAMESPACE, "Tileset");
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

	private String p_to_s() {
		String s = "";

		for (int j = 0; j < max; ++j) {
			s += passability[j];
			s += ";";
		}

		return s;
	}

	private String r_to_s() {
		String s = "";

		for (int j = 0; j < max; ++j) {
			s += region[j];
			s += ";";
		}

		return s;
	}

	private String d_to_s() {
		String s = "";

		for (int j = 0; j < max; ++j) {
			s += depth[j];
			s += ";";
		}

		return s;
	}

	public boolean Load(FileInputStream fileHandle) {

		boolean success = true;
		MXParser reader = null;
		try {
			reader = new MXParser();
			reader.setInput(fileHandle, "UTF-8");

			reader.nextTag();
			reader.require(XmlPullParser.START_TAG, null, null);

			if (reader.getName().equals("Map")) {
				for (int i = 0; i < reader.getAttributeCount(); ++i) {
					String s = reader.getAttributeName(i);
					if (s.equals("name")) {
						name = reader.getAttributeValue(i);
						System.out.println(name);
					}
				}
			}

			int tag = reader.getEventType();

			while (tag != XmlPullParser.END_DOCUMENT) {
				if (tag == XmlPullParser.START_TAG) {
					final String n = reader.getName();
					if (n.equals("tiles")) {
						for (int i = 0; i < reader.getAttributeCount(); ++i) {
							String s = reader.getAttributeName(i);
							if (s.equals("max")) {
								max = Integer.valueOf(reader.getAttributeValue(i));
							}

							if (s.equals("passability")) {

								passability = new int[max];
								String m = reader.getAttributeValue(i);
								String[] m2 = m.split(";");
								for (int j = 0; j < m2.length; ++j) {
									passability[j] = Integer.valueOf(m2[j]);
								}
							}
							if (s.equals("depth")) {

								depth = new int[max];
								String m = reader.getAttributeValue(i);
								String[] m2 = m.split(";");
								for (int j = 0; j < m2.length; ++j) {
									depth[j] = Integer.valueOf(m2[j]);
								}
							}
							if (s.equals("region")) {

								region = new int[max];
								String m = reader.getAttributeValue(i);
								String[] m2 = m.split(";");
								for (int j = 0; j < m2.length; ++j) {
									region[j] = Integer.valueOf(m2[j]);
								}
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
}
