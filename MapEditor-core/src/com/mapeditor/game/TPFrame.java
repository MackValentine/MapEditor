package com.mapeditor.game;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.mapeditor.map.Map;
import com.mapeditor.map.MapList;
import com.mapeditor.screen.Screen;

public class TPFrame extends JDialog implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7244025052949037200L;

	private static final long TIME_INT = 1000000L;
	private static final long TIME_LONG = 1000000000L;

	private Graphics backContext;
	private Image backBuffer;

	private int fps;
	private long wait;
	private long frameDelay;
	private long milli;
	private int nano;

	private TPPanel panel;

	public TPFrame(MapList m) {

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setTitle("Loading");

		panel = new TPPanel(m.name);

		this.setSize(Screen.Width + 90 + 8, Screen.Height + 24);

		this.setLocationRelativeTo(null);

		this.setVisible(true);

		this.setContentPane(panel);

		addMouseListener(this);

		this.setTitle("");

		backBuffer = this.createImage(Screen.Width + 83 + 8, Screen.Height + 24);
		backContext = backBuffer.getGraphics();

		Color c = new Color(0, 0, 0);
		getGraphics().setColor(c);
		backContext.setColor(c);
		if (getDesiredRate() == 0) {
			this.frameDelay = 0;
		} else {
			this.frameDelay = TIME_LONG / getDesiredRate();
		}

		Thread t = new Thread() {

			public void run() {
				go();
			}
		};
		t.start();

	}

	public void paint(Graphics g) {

		int x = 0;
		int y = 0;

		if (g != null)
			g.drawImage(backBuffer, x, y, null);

	}

	private void go() {
		long n = 0L;
		fps = 0;
		int f = 0;
		do {
			do {
				long old_time = System.currentTimeMillis();

				long lastTime = System.nanoTime();

				update(backContext);
				Graphics g = getGraphics();
				if (g != null)
					paint(g);

				this.sync(System.nanoTime() - lastTime);

				long new_time = System.currentTimeMillis();
				n += new_time - old_time;
				f++;
			} while (n <= 1000L);
			n = 0L;
			fps = f;
			f = 0;
			setTitle("Set destination (" + fps + "fps)");
		} while (true);
	}

	private void sync(long time) {
		if (getDesiredRate() != 0) {
			try {
				this.wait = this.frameDelay - time;
				if (this.wait > 0) {
					this.milli = this.wait / TIME_INT;
					this.nano = (int) (this.wait % TIME_INT);
					Thread.sleep(this.milli, this.nano);
				}
			} catch (InterruptedException e) {
			}
		}
	}

	private int getDesiredRate() {
		return 60;
	}

	public void update(Graphics g) {
		super.update(g);
		panel.update();
		panel.paint(g);

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

		panel.mouseX = arg0.getX();
		panel.mouseY = arg0.getY();
		panel.mouseClick = true;

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		panel.mouseX = arg0.getX();
		panel.mouseY = arg0.getY();
		panel.mouseClick = true;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public boolean accept() {
		return panel.accept;
	}

	public int getTPX() {
		return panel.tpX;
	}

	public int getTPY() {
		return panel.tpY;
	}

	public String getMapName() {
		return panel.map.name;
	}

}