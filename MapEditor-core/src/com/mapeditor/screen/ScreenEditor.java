package com.mapeditor.screen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mapeditor.game.FileOppener;
import com.mapeditor.game.MapEditor;
import com.mapeditor.map.Map;
import com.mapeditor.map.MapList;
import com.mapeditor.map.Tileset;

public class ScreenEditor extends Screen {

	private Texture back;
	private Texture back2;
	private Texture backMiniMap;
	private Texture selector;
	private Texture selector2;

	private Texture arrowL;
	private Texture arrowR;
	private Texture arrowU;
	private Texture arrowD;

	private Texture cross;
	private Texture circle;
	private Texture numbers_set;

	public Map[] map;
	private int display_y;

	private int index = 1;
	private int layer = 0;
	private int tool = 1;

	public int tile_editor_mode = 0;

	private boolean tile_selector;

	private int waitTouch;
	private TextureRegion[] numbers;
	private FileOppener f;
	private int animation;
	private int wait_anim;
	private Texture windows;
	private MapList listMap;

	public ScreenEditor() throws FileNotFoundException {
		super();

		map = new Map[5];

		back = new Texture("gfx/back.png");
		windows = new Texture("gfx/windows.png");
		back2 = new Texture("gfx/backTiles.png");

		backMiniMap = new Texture("gfx/backMiniMap.png");

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

		if (f != null && f.isShowing()) {

			if (MapEditor.hasFocus)
				f.setAlwaysOnTop(true);
			else
				f.setAlwaysOnTop(false);

			if (f.fHandle != null) {
				if (f.type == "tiles") {
					String s = f.fHandle.name().replace(".xml", "");
					File f = new File(".\\maps\\tilesets\\" + s + ".xml");

					map[0].loadTiles(s);
					map[0].tileset = new Tileset(s, map[0].tiles.length);
					FileInputStream fh2 = new FileInputStream(f);
					map[0].tileset.Load(fh2);
				} else if (f.type == "tileset") {

					String s = f.fHandle.name().replace(".png", "");
					File dir2 = new File(".\\maps\\tilesets\\" + s + ".xml");
					if (!dir2.exists()) {
						map[0].loadTiles(s);
						System.out.println(s);
						map[0].tileset = new Tileset(s, map[0].tiles.length);
						map[0].tileset.Save();
					} else {
						map[0].loadTiles(s);
						map[0].tileset = new Tileset(s, map[0].tiles.length);
						FileInputStream f = new FileInputStream(dir2);
						map[0].tileset.Load(f);
					}
				} else {
					listMap = new MapList(f.fHandle);
					listMap.index = 0;
					map[0] = new Map(listMap.name + "/map" + listMap.index + ".xml");

					for (int i = 1; i < 5; ++i) {
						map[i] = null;
					}

					if (listMap.index < listMap.width) {
						int i = listMap.index + 1;
						map[1] = new Map(listMap.name + "/map" + i + ".xml");
					}
					if (listMap.index > 0) {
						int i = listMap.index - 1;
						map[2] = new Map(listMap.name + "/map" + i + ".xml");
					}
					if (listMap.index - 4 > 0) {
						int i = listMap.index - 5;
						map[3] = new Map(listMap.name + "/map" + i + ".xml");
					}
					if (listMap.index + 4 < listMap.width) {
						int i = listMap.index + 4;
						map[4] = new Map(listMap.name + "/map" + i + ".xml");
					}
				}
				f.dispose();
			}

			return;
		}

		int mx = MapEditor.mx();
		int my = MapEditor.my();

		if (Gdx.input.isTouched()) {
			waitTouch++;
			{
				if (mx >= 16 && mx < Screen.Width + 17) {

					if (my >= 16 && my < Screen.Height + 17) {
						int tx = (mx - 16) / 16;
						int ty = (my - 24) / 16;

						if (map[0] != null) {

							if (tx >= map[0].width)
								tx = map[0].width - 1;
							if (ty >= map[0].height)
								ty = map[0].height - 1;

							if (Gdx.input.isButtonPressed(0)) {
								if (tool == 1) {
									map[0].layer[layer][tx][ty] = index;
								} else if (tool == 2) {
									paint(tx, ty, map[0].layer[layer][tx][ty]);
								} else {
									map[0].layer[layer][tx][ty] = 0;
								}
							} else {
								index = map[0].layer[layer][tx][ty];
							}
						}
					} else if (my >= 32 + Screen.Height + 24 && my < 32 + Screen.Height + 48) {

						if (mx >= 24 && mx < 48 && waitTouch == 1) {
							tile_selector = !tile_selector;
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
							if (map[0] != null) {
								map[0].tileset.Save();
							}
							listMap = new MapList();
							listMap.index = 0;

							map[0] = new Map(listMap.name + "/map" + listMap.index + ".xml");

							for (int i = 1; i < 5; ++i) {
								map[i] = null;
							}
							if (listMap.index < listMap.width) {
								int i = listMap.index + 1;
								map[1] = new Map(listMap.name + "/map" + i + ".xml");
							}
							if (listMap.index > 0) {
								int i = listMap.index - 1;
								map[2] = new Map(listMap.name + "/map" + i + ".xml");
							}
							if (listMap.index - 4 > 0) {
								int i = listMap.index - 5;
								map[3] = new Map(listMap.name + "/map" + i + ".xml");
							}
							if (listMap.index + 4 < listMap.width) {
								int i = listMap.index + 4;
								map[4] = new Map(listMap.name + "/map" + i + ".xml");
							}

							// map = new Map();
						}
						if (mx >= 24 * 10 && mx < 24 * 10 + 24) {
							if (waitTouch == 1) {
								if (map[0] != null) {
									map[0].tileset.Save();
								}
								f = new FileOppener("map");

							}

						}
						if (mx >= 24 * 11 && mx < 24 * 11 + 24) {
							if (map[0] != null) {
								map[0].tileset.Save();
								map[0].Save();
							}
						}
					}
				}
			}
			{

				if (mx >= Screen.Width + 32 && mx < 32 + 24 * 8 + Screen.Width) {
					if (my >= 8 && my < 24 * 9 - 16 - 24) {

						int tx = mx - Screen.Width - 32;
						int ty = 24 * 8 - my - 8 + (display_y / 24) * 24;

						if (tile_editor_mode == 0) {
							if (tiles() != null) {
								index = ((ty) / 24) * 8 + (tx / 24);

								if (index >= tiles().length)
									index = 0;
								if (index < 0)
									index = 0;
							}
						} else if (tile_editor_mode == 1) {
							if (tiles() != null) {
								int j = ((ty) / 24) * 8 + (tx / 24);
								if (j < 0)
									j = 0;
								if (j >= tiles().length)
									j = 0;

								if (waitTouch == 1) {
									map[0].tileset.passability[j] = 1 - map[0].tileset.passability[j];
								}
							}
						} else if (tile_editor_mode == 2) {
							if (tiles() != null) {
								int j = ((ty) / 24) * 8 + (tx / 24);
								if (j < 0)
									j = 0;
								if (j >= tiles().length)
									j = 0;

								if (waitTouch == 1) {
									map[0].tileset.region[j] = 1 + map[0].tileset.region[j];
									if (map[0].tileset.region[j] > 9)
										map[0].tileset.region[j] = 0;
								}
							}
						} else if (tile_editor_mode == 3) {
							if (tiles() != null) {
								int j = ((ty) / 24) * 8 + (tx / 24);
								if (j < 0)
									j = 0;
								if (j >= tiles().length)
									j = 0;

								if (waitTouch == 1) {
									map[0].tileset.depth[j] = 1 + map[0].tileset.depth[j];
									if (map[0].tileset.depth[j] > 9)
										map[0].tileset.depth[j] = 0;
								}
							}
						}
					}
				} else if (mx >= 28 * 24 + 32 && mx < 24 * 29 + 32) {
					if (my >= 24 * 7 && my < 24 * 8) {
						if (waitTouch == 1) {

							if (map[0] != null) {
								map[0].tileset.Save();
								f = new FileOppener("tileset");
							}

						}
					} else if (my >= 24 * 6 && my < 24 * 7) {
						if (waitTouch == 1) {
							if (map[0] != null) {
								map[0].tileset.Save();
								f = new FileOppener("tiles");
							}
						}
					} else if (my >= 24 * 5 && my < 24 * 6) {
						tile_editor_mode = 0;
					} else if (my >= 24 * 4 && my < 24 * 5) {
						tile_editor_mode = 1;
					} else if (my >= 24 * 3 && my < 24 * 4) {
						tile_editor_mode = 2;
					} else if (my >= 24 * 2 && my < 24 * 3) {
						tile_editor_mode = 3;
					}
				}
			}
			if (mx >= Screen.Width + 32 && mx < Screen.Width + 216 + 32) {
				if (waitTouch == 1) {
					if (my >= 192 + 32 && my < 192 + 122 + 32) {

						if (listMap != null)
							if (mx >= 544 + 32 && mx < 544 + 32 + (listMap.width) * 16) {
								if (my >= 192 + 30 + 32 && my < 192 + 30 + (listMap.height) * 16 + 32) {

									int ix = (mx - 544 - 32) / 16;
									int iy = ((listMap.height - 1) - (my - 192 - 30 - 32) / 16);
									int id = iy * (listMap.width) + ix;

									map[0].Save();

									listMap.index = id;
									map[0] = new Map(listMap.name + "/map" + id + ".xml");

									for (int i = 1; i < 5; ++i) {
										map[i] = null;
									}

									if (listMap.index < listMap.width * listMap.height - 1) {
										int i = listMap.index + 1;
										map[1] = new Map(listMap.name + "/map" + i + ".xml");
									}
									if (listMap.index > 0) {
										int i = listMap.index - 1;
										map[2] = new Map(listMap.name + "/map" + i + ".xml");
									}
									if (listMap.index - 4 > 0) {
										int i = listMap.index - 5;
										map[3] = new Map(listMap.name + "/map" + i + ".xml");
									}
									if (listMap.index + 4 < listMap.width * listMap.height - 1) {
										int i = listMap.index + 5;
										map[4] = new Map(listMap.name + "/map" + i + ".xml");
									}
								}
							}
					}

				}
			}
		} else {
			waitTouch = 0;
			{
				if (mx >= Screen.Width + 32 && mx < 24 * 8 + Screen.Width + 32) {
					if (my >= 0 && my < 16) {
						display_y += 2;
						int m = ((tiles().length / 8) - 7) * 24;
						if (display_y > m)
							display_y = m;
					}

					if (my >= 24 * 8 - 16 && my < 24 * 9 - 16) {
						display_y -= 2;
						if (display_y < 0)
							display_y = 0;
					}
				}
			}
		}
	}

	private void paint(int tx, int ty, int i) {
		if (map[0].layer[layer][tx][ty] != index) {
			map[0].layer[layer][tx][ty] = index;
			if (tx + 1 < map[0].width)
				if (map[0].layer[layer][tx + 1][ty] == i) {
					paint(tx + 1, ty, i);
				}
			if (tx - 1 >= 0)
				if (map[0].layer[layer][tx - 1][ty] == i) {
					paint(tx - 1, ty, i);
				}

			if (ty + 1 < map[0].height)
				if (map[0].layer[layer][tx][ty + 1] == i) {
					paint(tx, ty + 1, i);
				}
			if (ty - 1 >= 0)
				if (map[0].layer[layer][tx][ty - 1] == i) {
					paint(tx, ty - 1, i);
				}
		}
	}

	@Override
	public void render(SpriteBatch batch) {

		int mx = MapEditor.mx();
		int my = MapEditor.my();
		int tx = ((mx - 16) / 16) * 16;
		int ty = ((my - 24) / 16) * 16;

		batch.draw(back, 0, 0);

		batch.draw(backMiniMap, Screen.Width + 32, Screen.Height - 122 + 44 + 32);

		Color c = batch.getColor();
		batch.setColor(1f, 1f, 1f, 0.5f);
		if (map[1] != null) {
			for (int j = 0; j < map[1].height; ++j) {
				int id = map[1].layer[0][0][j];
				batch.draw(tiles(1)[id], map[0].width * 16 + 16, 16 * j + 5 + 16);
				id = map[1].layer[1][0][j];
				batch.draw(tiles(1)[id], map[0].width * 16 + 16, 16 * j + 5 + 16);
			}
		}

		if (map[2] != null) {
			for (int j = 0; j < map[2].height; ++j) {
				int id = map[2].layer[0][map[2].width - 1][j];
				batch.draw(tiles(2)[id], 0, 16 * j + 5 + 16);
				id = map[2].layer[1][map[2].width - 1][j];
				batch.draw(tiles(2)[id], 0, 16 * j + 5 + 16);
			}
		}

		if (map[3] != null) {
			for (int j = 0; j < map[3].width; ++j) {
				int id = map[3].layer[0][j][0];
				batch.draw(tiles(3)[id], 16 * j + 16, map[3].height * 16 + 16 + 5);
				id = map[3].layer[1][j][0];
				batch.draw(tiles(3)[id], 16 * j + 16, map[3].height * 16 + 16 + 5);
			}
		}
		if (map[4] != null) {
			for (int j = 0; j < map[4].width; ++j) {
				int id = map[4].layer[0][j][map[4].height - 1];
				batch.draw(tiles(4)[id], 16 * j + 16, 5);
				id = map[4].layer[1][j][map[4].height - 1];
				batch.draw(tiles(4)[id], 16 * j + 16, 5);
			}
		}
		batch.setColor(c);

		if (map[0] != null) {
			c = batch.getColor();
			if (layer != 0 && !tile_selector)
				batch.setColor(1f, 1f, 1f, 0.5f);
			for (int i = 0; i < map[0].width; ++i) {
				for (int j = 0; j < map[0].height; ++j) {
					int id = map[0].layer[0][i][j];
					if ((i != tx / 16 || j != ty / 16) || layer != 0)
						batch.draw(tiles()[id], 16 * i + 16, 16 * j + 5 + 16);

				}

			}

			wait_anim++;
			if (wait_anim > 5) {
				animation++;
				wait_anim = 0;
			}

			batch.setColor(c);

			if (layer != 1 && !tile_selector)
				batch.setColor(1f, 1f, 1f, 0.5f);
			for (int i = 0; i < map[0].width; ++i) {
				for (int j = 0; j < map[0].height; ++j) {
					int id = map[0].layer[1][i][j];
					if ((i != tx / 16 || j != ty / 16) || layer != 1)
						batch.draw(tiles()[id], 16 * i + 16, 16 * j + 5 + 16);
				}
			}
			batch.setColor(c);
		}

		batch.draw(windows, 0, 0);

		batch.setColor(c);

		if (map[0] != null) {
			if (layer == 0) {
				batch.draw(selector2, 48, Screen.Height + 20 + 32);
			} else if (layer == 1) {
				batch.draw(selector2, 48 + 24, Screen.Height + 20 + 32);
			}

			if (tool == 1) {
				batch.draw(selector2, 48 + 48, Screen.Height + 20 + 32);
			} else if (tool == 2) {
				batch.draw(selector2, 96 + 24, Screen.Height + 20 + 32);
			} else if (tool == 3) {
				batch.draw(selector2, 96 + 48, Screen.Height + 20 + 32);
			}

			c = batch.getColor();
			batch.setColor(1f, 1f, 1f, 0.5f);

			if (tx >= map[0].width * 16)
				tx = (map[0].width - 1) * 16;
			if (ty >= map[0].height * 16)
				ty = (map[0].height - 1) * 16;
			if (!tile_selector) {
				if (mx - 16 < Screen.Width && my - 16 < Screen.Height)
					batch.draw(tiles()[index], tx + 16, ty + 5 + 16);
			}
			batch.setColor(c);

		}

		if (listMap != null) {
			batch.draw(selector, (listMap.index % listMap.width) * 16 + 544 + 32,
					(listMap.height - listMap.index / listMap.height) * 16 + 192 + 10 + 32);
		}

		batch.draw(back2, Screen.Width + 32, 0);

		if (map[0] != null) {
			int y = 192 - 29;
			int x = Screen.Width + 4 + 32;

			int s = (display_y / 24) * 8;
			if (s < 0)
				s = 0;

			int m = s + 7 * 8;
			if (m > tiles().length)
				m = tiles().length;

			for (int i = s; i < m; ++i) {
				if (i < tiles().length)
					if (tiles()[i] != null) {
						batch.draw(tiles()[i], x, y);
						if (index == i)
							batch.draw(selector, x, y);

						if (tile_editor_mode == 1) {
							if (map[0].tileset.passability[i] == 0) {
								batch.draw(circle, x, y);
							} else if (map[0].tileset.passability[i] == 1) {
								batch.draw(cross, x, y);
							}
						} else if (tile_editor_mode == 2) {
							int j = map[0].tileset.region[i];
							batch.draw(numbers[j], x, y);
						} else if (tile_editor_mode == 3) {
							int j = map[0].tileset.depth[i];
							batch.draw(numbers[j], x, y);
						}

						x += 24;
						// if (x > 7 * 24 + 28 + 320) {
						if ((i - s) % 8 == 7) {
							x = Screen.Width + 4 + 32;
							y -= 24;
						}

					}

			}
			if (display_y > 4) {
				batch.draw(arrowU, 24 * 4 + Screen.Width + 32, 8 * 24 + 14 - 24);
			}

			if (display_y < ((tiles().length / 8) - 7) * 24) {
				batch.draw(arrowD, 24 * 4 + Screen.Width + 32, 0);
			}

		}

	}

	private TextureRegion[] tiles(int i) {
		if (map[i] != null)
			return map[i].tiles;
		else
			return null;
	}

	private TextureRegion[] tiles() {
		return tiles(0);
	}

	@Override
	public void render(ShapeRenderer shape) {

	}

	@Override
	public void dispose() {
	}
}
