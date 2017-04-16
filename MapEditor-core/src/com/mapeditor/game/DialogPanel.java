package com.mapeditor.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.mapeditor.map.Map;
import com.mapeditor.screen.Screen;

public class DialogPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public BufferedImage font;

	public int mouseX;
	public int mouseY;
	public boolean mouseClick;

	boolean accept;
	private BufferedImage back;
	private String text;
	private JTextArea textArea;
	private BufferedImage face;
	public String back_name = "null.png";
	public String face_name = "null.png";

	public DialogPanel(JTextArea t) {
		super();
		textArea = t;
		try {
			back = ImageIO.read(new File(MapEditor.s + "/cinematics/back.png"));
			face = ImageIO.read(new File(MapEditor.s + "/cinematics/faces/null.png"));
			font = ImageIO.read(new File(MapEditor.s + "/font.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void update() {

		if (mouseClick) {

			mouseClick = false;
		}

	}

	public void paint(Graphics g) {

		g.drawImage(back, 0, 0, null);

		g.drawImage(face, 144, 88, null);

		drawText(g, textArea.getText(), 128, 168);

	}

	public void drawText(Graphics g, String s, int x, int y) {

		final int maxWidth = 224;

		int x1 = x;
		for (int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);

			if (c == '\n') {
				x1 = x;
				y += 8;
			} else {

				// System.out.println((int) c);

				if (c > 128)
					c -= 103;

				int idx = ((c - 32) % 16) * 8;
				int idy = ((c - 32) / 16) * 8;
				BufferedImage tmp;
				tmp = font.getSubimage(idx, idy, 8, 8);
				g.drawImage(tmp, x1, y, null);

				x1 += 8;

				if (x1 >= x + maxWidth) {
					x1 = x;
					y += 9;
				}
			}
		}

	}

	public void load(int i, FileHandle fHandle) {
		try {
			if (i == 0) {
				face = ImageIO.read(fHandle.file());
				face_name = fHandle.name();
			} else {
				back = ImageIO.read(fHandle.file());
				back_name = fHandle.name();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}