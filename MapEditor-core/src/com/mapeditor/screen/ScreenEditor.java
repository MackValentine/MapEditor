package com.mapeditor.screen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mapeditor.game.MyGdxGame;
import com.mapeditor.map.Map;
import com.mapeditor.map.Tileset;

public class ScreenEditor extends Screen {

	private Texture back;
	private Texture back2;
	private Texture selector;
	private Texture selector2;

	private Texture arrowL;
	private Texture arrowR;
	private Texture arrowU;
	private Texture arrowD;

	private Texture cross;
	private Texture circle;
	private Texture numbers_set;

	public Map map;
	private int display_x;
	private int display_y;

	private int index = 1;
	private int layer = 0;
	private int tool = 1;

	public int tile_editor_mode = 0;

	private boolean tile_selector;

	private int waitTouch;
	private TextureRegion[] numbers;

	public ScreenEditor() throws FileNotFoundException {
		super();

		map = new Map();

		back = new Texture("gfx/back.png");
		back2 = new Texture("gfx/backTiles.png");

		arrowL = new Texture("gfx/arrowL.png");
		arrowR = new Texture("gfx/arrowR.png");
		arrowD = new Texture("gfx/arrowD.png");
		arrowU = new Texture("gfx/arrowU.png");

		circle = new Texture("gfx/circle.png");
		cross = new Texture("gfx/cross.png");
		numbers_set = new Texture("gfx/number.png");
		int w = 10;
		numbers = new TextureRegion[w];
		for (int i = 0; i < w; ++i) {
			numbers[i] = new TextureRegion(numbers_set, i * 16, 0, 16, 16);

		}

		selector = new Texture("gfx/selector.png");
		selector2 = new Texture("gfx/selector2.png");

	}

	@Override
	public void update() throws FileNotFoundException {

		int mx = MyGdxGame.mx();
		int my = MyGdxGame.my();

		if (Gdx.input.isTouched()) {
			waitTouch++;
			if (!tile_selector) {
				if (mx >= 0 && mx < 320) {

					if (my >= 0 && my < 240) {
						int tx = (mx) / 16;
						int ty = my / 16;

						if (tool == 1) {
							map.layer[layer][tx][ty] = index;
						} else if (tool == 2) {
							paint(tx, ty, map.layer[layer][tx][ty]);
						} else {
							map.layer[layer][tx][ty] = 0;
						}

					} else if (my >= 240 && my < 240 + 24) {
						index = ((mx - 12 + display_x) / 24);
					} else if (my >= 240 + 24 && my < 240 + 48) {

						if (mx >= 24 && mx < 48 && waitTouch == 1) {
							tile_selector = true;
						}
						if (mx >= 48 && mx < 48 + 24) {
							layer = 0;
						} else if (mx >= 48 + 24 && mx < 48 + 48) {
							layer = 1;
						}

						if (mx >= 96 && mx < 96 + 24) {
							tool = 1;
						} else if (mx >= 96 + 24 && mx < 96 + 48) {
							tool = 2;
						} else if (mx >= 96 + 48 && mx < 96 + 48 + 24) {
							tool = 3;
						}

						if (mx >= 24 * 9 && mx < 24 * 9 + 24) {
							map = new Map();
						}
						if (mx >= 24 * 10 && mx < 24 * 10 + 24) {
							if (waitTouch == 1) {
								JFileChooser fc = new JFileChooser();
								File dir = new File(".\\maps");
								fc.setCurrentDirectory(dir);

								fc.setVisible(true);

								int returnVal = fc.showOpenDialog(null);

								if (returnVal == JFileChooser.APPROVE_OPTION) {
									File file = fc.getSelectedFile();
									FileHandle fh = new FileHandle(file);
									map.Load(fh);
								}
							}

						}
						if (mx >= 24 * 11 && mx < 24 * 11 + 24) {
							map.Save();
						}
					}
				}
			} else {
				if ((mx >= 24 && mx < 48 && waitTouch == 1) && (my >= 240 + 24 && my < 240 + 48)) {
					tile_selector = false;
					map.tileset.Save();
				}

				if (mx >= 24 && mx < 24 * 9) {
					if (my >= 40 && my < 24 * 9 - 16) {

						int tx = mx - 24;
						int ty = 24 * 9 - 12 - my + (display_y / 24) * 24;

						if (tile_editor_mode == 0) {

							index = ((ty) / 24) * 8 + (tx / 24);

							if (index >= tiles().length)
								index = 0;
							if (index < 0)
								index = 0;

							display_x = (index - 5) * 24;

							if (display_x < 0)
								display_x = 0;
							if (display_x > 24 * (tiles().length - 12))
								display_x = 24 * (tiles().length - 12);
						} else if (tile_editor_mode == 1) {
							int j = ((ty) / 24) * 8 + (tx / 24);
							if (j < 0)
								j = 0;
							if (j >= tiles().length)
								j = 0;

							if (waitTouch == 1) {
								map.tileset.passability[j] = 1 - map.tileset.passability[j];
							}
						} else if (tile_editor_mode == 2) {
							int j = ((ty) / 24) * 8 + (tx / 24);
							if (j < 0)
								j = 0;
							if (j >= tiles().length)
								j = 0;

							if (waitTouch == 1) {
								map.tileset.region[j] = 1 + map.tileset.region[j];
								if (map.tileset.region[j] > 9)
									map.tileset.region[j] = 0;
							}
						} else if (tile_editor_mode == 3) {
							int j = ((ty) / 24) * 8 + (tx / 24);
							if (j < 0)
								j = 0;
							if (j >= tiles().length)
								j = 0;

							if (waitTouch == 1) {
								map.tileset.depth[j] = 1 + map.tileset.depth[j];
								if (map.tileset.depth[j] > 9)
									map.tileset.depth[j] = 0;
							}
						}
					}
				} else if (mx >= 24 * 9 && mx < 24 * 10) {
					if (my >= 24 * 8 && my < 24 * 9) {
						if (waitTouch == 1) {

							map.tileset.Save();

							JFileChooser fc = new JFileChooser();
							File dir = new File(".\\tilesets");
							fc.setCurrentDirectory(dir);

							fc.setVisible(true);

							int returnVal = fc.showOpenDialog(null);

							if (returnVal == JFileChooser.APPROVE_OPTION) {
								File file = fc.getSelectedFile();
								FileHandle fh = new FileHandle(file);

								String s = fh.name().replace(".png", "");
								File dir2 = new File(".\\maps\\tilesets\\" + s + ".xml");
								if (!dir2.exists()) {
									map.loadTiles(s);
									System.out.println(s);
									map.tileset = new Tileset(s, map.tiles.length);
									map.tileset.Save();
								} else {
									map.loadTiles(s);
									map.tileset = new Tileset(s, map.tiles.length);
									FileInputStream f = new FileInputStream(dir2);
									map.tileset.Load(f);
								}
							}
						}
					} else if (my >= 24 * 7 && my < 24 * 8) {
						if (waitTouch == 1) {

							map.tileset.Save();

							JFileChooser fc = new JFileChooser();
							File dir = new File(".\\maps\\tilesets");
							fc.setCurrentDirectory(dir);

							fc.setVisible(true);

							int returnVal = fc.showOpenDialog(null);

							if (returnVal == JFileChooser.APPROVE_OPTION) {
								File file = fc.getSelectedFile();
								FileHandle fh = new FileHandle(file);

								String s = fh.name().replace(".xml", "");
								File f = new File(".\\maps\\tilesets\\" + s + ".xml");

								map.loadTiles(s);
								map.tileset = new Tileset(s, map.tiles.length);
								FileInputStream fh2 = new FileInputStream(f);
								map.tileset.Load(fh2);
							}
						}
					} else if (my >= 24 * 6 && my < 24 * 7) {
						tile_editor_mode = 0;
					} else if (my >= 24 * 5 && my < 24 * 6) {
						tile_editor_mode = 1;
					} else if (my >= 24 * 4 && my < 24 * 5) {
						tile_editor_mode = 2;
					} else if (my >= 24 * 3 && my < 24 * 4) {
						tile_editor_mode = 3;
					}
				}
			}
		} else {
			waitTouch = 0;
			if (!tile_selector) {
				if (my >= 240 && my < 240 + 24) {
					if (mx < 16) {
						display_x -= 2;
						if (display_x < 0)
							display_x = 0;
					} else if (mx > 320 - 16) {
						display_x += 2;
						if (display_x > 24 * (tiles().length - 12))
							display_x = 24 * (tiles().length - 12);
					}
				}
			} else {
				if (mx >= 24 && mx < 24 * 9) {
					if (my >= 16 && my < 40) {
						display_y += 2;
						int m = ((tiles().length / 8) - 7) * 24;
						if (display_y > m)
							display_y = m;
					}

					if (my >= 24 * 8 + 16 && my < 24 * 9) {
						display_y -= 2;
						if (display_y < 0)
							display_y = 0;
					}
				}
			}
		}
	}

	private void paint(int tx, int ty, int i) {
		if (map.layer[layer][tx][ty] != index) {
			map.layer[layer][tx][ty] = index;
			if (tx + 1 < map.width)
				if (map.layer[layer][tx + 1][ty] == i) {
					paint(tx + 1, ty, i);
				}
			if (tx - 1 >= 0)
				if (map.layer[layer][tx - 1][ty] == i) {
					paint(tx - 1, ty, i);
				}

			if (ty + 1 < map.height)
				if (map.layer[layer][tx][ty + 1] == i) {
					paint(tx, ty + 1, i);
				}
			if (ty - 1 >= 0)
				if (map.layer[layer][tx][ty - 1] == i) {
					paint(tx, ty - 1, i);
				}
		}
	}

	@Override
	public void render(SpriteBatch batch) {

		int mx = MyGdxGame.mx();
		int my = MyGdxGame.my();
		int tx = (mx / 16) * 16;
		int ty = (my / 16) * 16;

		batch.draw(back, 0, 0);
		Color c = batch.getColor();
		if (layer != 0)
			batch.setColor(1f, 1f, 1f, 0.5f);
		for (int i = 0; i < map.width; ++i) {
			for (int j = 0; j < map.height; ++j) {
				int id = map.layer[0][i][j];
				if ((i != tx / 16 || j != ty / 16) || layer != 0)
					batch.draw(tiles()[id], 16 * i, 16 * j);
			}
		}

		batch.setColor(c);

		if (layer != 1)
			batch.setColor(1f, 1f, 1f, 0.5f);
		for (int i = 0; i < map.width; ++i) {
			for (int j = 0; j < map.height; ++j) {
				int id = map.layer[1][i][j];
				if ((i != tx / 16 || j != ty / 16) || layer != 1)
					batch.draw(tiles()[id], 16 * i, 16 * j);
			}
		}
		batch.setColor(c);

		for (int i = 0; i < tiles().length; ++i) {
			if (tiles()[i] != null) {
				batch.draw(tiles()[i], 16 + i * 24 - display_x, 240 + 4);
				if (index == i)
					batch.draw(selector, 16 + i * 24 - display_x, 240 + 4);
			}
		}

		if (display_x > 4) {
			batch.draw(arrowL, 0, 240 + 4);
		}
		if (display_x < (tiles().length - 12) * 24) {
			batch.draw(arrowR, 320 - 8, 240 + 4);
		}

		batch.setColor(c);

		if (layer == 0) {
			batch.draw(selector2, 48, 240 + 24);
		} else if (layer == 1) {
			batch.draw(selector2, 48 + 24, 240 + 24);
		}

		if (tool == 1) {
			batch.draw(selector2, 48 + 48, 240 + 24);
		} else if (tool == 2) {
			batch.draw(selector2, 96 + 24, 240 + 24);
		} else if (tool == 3) {
			batch.draw(selector2, 96 + 48, 240 + 24);
		}

		c = batch.getColor();
		batch.setColor(1f, 1f, 1f, 0.5f);
		if (!tile_selector) {
			if (mx < 320 && my < 240 && tool == 1)
				batch.draw(tiles()[index], tx, ty);
		}
		batch.setColor(c);

		if (tile_selector) {

			batch.draw(back2, 0, 0);
			int y = 240 - 44 - 12;
			int x = 28;

			int s = (display_y / 24) * 8;
			if (s < 0)
				s = 0;
			int m = s + 7 * 8;
			if (m > tiles().length)
				m = tiles().length;

			for (int i = s; i < m; ++i) {
				if (tiles()[i] != null) {
					batch.draw(tiles()[i], x, y);
					if (index == i)
						batch.draw(selector, x, y);

					if (tile_editor_mode == 1) {
						if (map.tileset.passability[i] == 0) {
							batch.draw(circle, x, y);
						} else if (map.tileset.passability[i] == 1) {
							batch.draw(cross, x, y);
						}
					} else if (tile_editor_mode == 2) {
						int j = map.tileset.region[i];
						batch.draw(numbers[j], x, y);
					} else if (tile_editor_mode == 3) {
						int j = map.tileset.depth[i];
						batch.draw(numbers[j], x, y);
					}

					x += 24;
					if (i % 8 == 7) {
						x = 28;
						y -= 24;
					}
				}
			}

			if (display_y > 4) {
				batch.draw(arrowU, 24 * 4 + 12, 8 * 24 + 14);
			}

			if (display_y < ((tiles().length / 8) - 7) * 24) {
				batch.draw(arrowD, 24 * 4 + 12, 28);
			}
		}

	}

	private TextureRegion[] tiles() {
		return map.tiles;
	}

	@Override
	public void render(ShapeRenderer shape) {

	}

	@Override
	public void dispose() {
	}
}
