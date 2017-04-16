package com.mapeditor.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.mapeditor.map.Map;
import com.mapeditor.screen.Screen;

public class TPPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public Font font;
	public Map map;
	private BufferedImage[] img;
	public String mapName;
	public int mouseX;
	public int mouseY;
	public boolean mouseClick;

	public int tpX;
	public int tpY;
	boolean accept;

	public TPPanel(String s) {
		super();

		mapName = s;

		// load(mapName+"/map24.xml");
	}

	public void load(String s, int i) {

		Map m = new Map(s + "/map" + i + ".xml", true);
		map = m;

		img = new BufferedImage[8];

		try {
			img[0] = ImageIO.read(new File(MapEditor.s + "/tilesets/" + map.tileset.name + ".png"));
			for (int j = 0; j < map.tileset.autotiles_name.length; ++j) {
				if (map.tileset.autotiles_name[j] != null) {
					img[j + 1] = ImageIO
							.read(new File(MapEditor.s + "/autotiles/" + map.tileset.autotiles_name[j] + ".png"));
				}
			}
		} catch (IOException e) {
		}
		repaint();
	}

	public void update() {

		if (mouseClick) {

			if (mouseX < 88 && mouseX >= 8) {
				if (mouseY >= 64 && mouseY < 64 + 16 * 5) {
					int id = ((mouseX - 8) / 16) + ((mouseY - 64) / 16) * 5;
					load(mapName, id);
				}

				if (mouseY >= 240 && mouseY < 272) {
					accept = true;
				}
			} else if (mouseX > 89 && mouseX < 89 + Screen.Width) {
				tpX = (mouseX - 89) / 16;
				tpY = 16 - ((mouseY - 14) / 16);
			}

			mouseClick = false;
		}

	}

	public void paint(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, 1600, 1600);
		g.setColor(Color.white);

		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; ++j) {
				g.drawRect(8 + i * 16, j * 16 + 64, 16, 16);
			}
		}

		g.setColor(Color.RED);

		g.fillRect(8, 240, 88, 32);

		if (map != null) {
			for (int i = 0; i < map.width; ++i) {
				for (int j = 0; j < map.height; ++j) {
					for (int l = 0; l < 3; ++l) {
						int id = map.layer[l][i][j];
						int x = id % 8 * 16;
						int y = id / 8 * 16;
						BufferedImage tmp;
						if (id > 8 || id == 0)
							tmp = img[0].getSubimage(x, y, 16, 16);
						else
							tmp = img[id].getSubimage(0, 0, 16, 16);
						g.drawImage(tmp, i * 16 + 89, (16 - j) * 16 + 14, null);
					}
				}
			}

			g.drawRect(tpX * 16 + 89, (16 - tpY) * 16 + 14, 16, 16);
		}
		// g.fillRect(0, 0, 160, 160);

	}

}