package com.mapeditor.screen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mapeditor.game.DialogFrame;
import com.mapeditor.game.FileOppener;
import com.mapeditor.game.MapEditor;
import com.mapeditor.game.NamingWindows;
import com.mapeditor.game.TPFrame;
import com.mapeditor.map.Autotile;
import com.mapeditor.map.Event;
import com.mapeditor.map.EventCommand;
import com.mapeditor.map.Map;
import com.mapeditor.map.MapList;
import com.mapeditor.map.Tileset;

public class ScreenEditor extends Screen {

	private Texture back;
	private Texture back2;
	private Texture backMiniMap;

	private Texture backEvent;
	private Texture backNewEvent;
	private Texture backEventTP;
	private Texture backEventChest;
	private Texture backEventDialog;

	private Texture selector;
	private Texture selector2;

	private Texture arrowL;
	private Texture arrowR;
	private Texture arrowU;
	private Texture arrowD;

	private Texture cross;
	private Texture circle;

	private Texture passability_set;
	private TextureRegion[] passability;
	private Texture numbers_set;
	private TextureRegion[] numbers;

	public Map[] map;
	private int display_y;

	private int index = 0;
	private int[][] indexT;

	private int layer = -2;
	private int tool = 1;

	public int tile_editor_mode = 0;

	private boolean tile_selector;

	private int waitTouch;

	private FileOppener f;
	private NamingWindows n;
	private TPFrame tpFrame;

	public static int animation;
	private int wait_anim;
	private Texture windows;
	private MapList listMap;
	private int oldX;
	private int oldY;
	private ArrayList<int[]> undoAction = new ArrayList<int[]>();

	private int undoIndex;

	public int eventX;
	public int eventY;
	private DialogFrame d;

	public ScreenEditor() throws FileNotFoundException {
		super();

		map = new Map[5];

		back = new Texture("gfx/back.png");
		windows = new Texture("gfx/windows.png");
		back2 = new Texture("gfx/backTiles.png");

		backMiniMap = new Texture("gfx/backMiniMap.png");
		backEvent = new Texture("gfx/backEvent.png");
		backNewEvent = new Texture("gfx/backNewEvent.png");

		backEventTP = new Texture("gfx/backEventTP.png");
		backEventChest = new Texture("gfx/backEventChest.png");
		backEventDialog = new Texture("gfx/backEventDialog.png");

		arrowL = new Texture("gfx/arrowL.png");
		arrowR = new Texture("gfx/arrowR.png");
		arrowD = new Texture("gfx/arrowD.png");
		arrowU = new Texture("gfx/arrowU.png");

		circle = new Texture("gfx/circle.png");
		cross = new Texture("gfx/cross.png");
		passability_set = new Texture("gfx/passability.png");
		numbers_set = new Texture("gfx/number.png");
		int w = 10;
		numbers = new TextureRegion[w];
		for (int i = 0; i < w; ++i) {
			numbers[i] = new TextureRegion(numbers_set, i * 16, 0, 16, 16);

		}
		w = 10;
		passability = new TextureRegion[w];
		for (int i = 0; i < w; ++i) {
			passability[i] = new TextureRegion(passability_set, i * 16, 0, 16, 16);

		}

		selector = new Texture("gfx/selector.png");
		selector2 = new Texture("gfx/selector2.png");

		FileHandle fh = new FileHandle(MapEditor.s + "/data/maps/new2/");
		listMap = new MapList(fh);
		listMap.index = 24;
		map[0] = new Map(listMap.name + "/map" + listMap.index + ".xml");

		reloadMaps();

	}

	@Override
	public void update() throws FileNotFoundException {

		if (getEvent(eventX, eventY) != null) {
			EventCommand c = getEvent(eventX, eventY).command;
			// System.out.println(c.map_name + " : " + c.dest_x + " / " +
			// c.dest_y);
		}

		if (tpFrame != null && tpFrame.isShowing()) {
			if (MapEditor.hasFocus)
				tpFrame.setAlwaysOnTop(true);
			else
				tpFrame.setAlwaysOnTop(false);

			if (tpFrame.accept()) {
				Event e = getEvent(eventX, eventY);
				EventCommand c = e.command;
				c.dest_x = tpFrame.getTPX();
				c.dest_y = tpFrame.getTPY();
				c.map_name = tpFrame.getMapName();
				tpFrame.dispose();
			}

			return;
		}

		if (f != null && f.isShowing()) {

			if (MapEditor.hasFocus)
				f.setAlwaysOnTop(true);
			else
				f.setAlwaysOnTop(false);

			if (f.fHandle != null) {

				if (f.type == "autotiles") {
					String s = f.fHandle.name().replace(".png", "");
					map[0].tileset.autotiles[index - 1] = new Autotile(s, index);

					Map.needRefresh = true;
					map[0].tileset.Save();
				} else if (f.type == "tiles") {
					String s = f.fHandle.name().replace(".xml", "");
					File f = new File(MapEditor.s + "\\data\\tilesets\\" + s + ".xml");

					map[0].loadTiles(s);
					map[0].tileset = new Tileset(s, map[0].tiles.length);
					FileInputStream fh2 = new FileInputStream(f);
					map[0].tileset.Load(fh2, true);
				} else if (f.type == "tileset") {

					String s = f.fHandle.name().replace(".png", "");
					File dir2 = new File(MapEditor.s + "\\data\\tilesets\\" + s + ".xml");
					if (!dir2.exists()) {
						map[0].loadTiles(s);
						map[0].tileset = new Tileset(s, map[0].tiles.length);
						map[0].tileset.Save();
					} else {
						map[0].loadTiles(s);
						map[0].tileset = new Tileset(s, map[0].tiles.length);
						FileInputStream f = new FileInputStream(dir2);
						map[0].tileset.Load(f, true);
					}
				} else if (f.type == "teleport") {

					MapList listMap2 = new MapList(f.fHandle);
					tpFrame = new TPFrame(listMap2);
				} else if (f.type == "charaset") {

					Event e = getEvent(eventX, eventY);
					e.loadCharaset(f.fileName);

				} else {
					listMap = new MapList(f.fHandle);
					listMap.index = 0;
					map[0] = new Map(listMap.name + "/map" + listMap.index + ".xml");

					reloadMaps();

					undoAction = new ArrayList<int[]>();
				}
				f.dispose();
			}

			return;
		}

		if (n != null && n.isShowing()) {

			if (MapEditor.hasFocus)
				n.setAlwaysOnTop(true);
			else
				n.setAlwaysOnTop(false);

			if (n.fileName != null) {

				if (map[0] != null) {
					map[0].tileset.Save();
				}
				listMap = new MapList(n.fileName);
				listMap.index = 0;

				map[0] = new Map(listMap.name + "/map" + listMap.index + ".xml");

				reloadMaps();

				n.dispose();
			}
			return;
		}
		
		if (d != null && d.isShowing()) {
			if (MapEditor.hasFocus)
				d.setAlwaysOnTop(true);
			else
				d.setAlwaysOnTop(false);
			d.update();
			return;
		}

		int mx = MapEditor.mx();
		int my = MapEditor.my();

		if (Gdx.input.isKeyJustPressed(Input.Keys.Z) && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
			undo();
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.Y) && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
			redo();
		}

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
							if (layer == -2) {
								if (waitTouch == 1) {
									eventX = tx;
									eventY = ty;
								} else {
									if (getEvent(eventX, eventY) != null) {
										if (getEvent(tx, ty) == null) {
											Event e = getEvent(eventX, eventY);
											e.x = tx;
											e.y = ty;

											eventX = tx;
											eventY = ty;
										}
									}
								}

							} else {
								if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {

									int t[][];
									if (layer < 0) {
										t = map[0].top;
									} else {
										t = map[0].layer[layer];
									}
									if (tool == 1) {
										if (indexT != null) {
											for (int i = 0; i < indexT.length; ++i) {
												for (int j = 0; j < indexT[i].length; ++j) {
													int id = indexT[i][j];
													if (tx + i < map[0].width && tx + i >= 0 && ty - j < map[0].height
															&& ty - j >= 0) {
														if (t[tx + i][ty - j] != id)
															addUndo(id, t[tx + i][ty - j], tx + i, ty - j);
														t[tx + i][ty - j] = id;
													}
												}
											}
										} else {
											if (t[tx][ty] != index)
												addUndo(index, t[tx][ty], tx, ty);
											t[tx][ty] = index;
										}
									} else if (tool == 2) {
										paint(tx, ty, t[tx][ty]);
									} else {
										t[tx][ty] = 0;
									}
									Map.needRefresh = true;

								} else {
									int t[][];
									if (layer < 0) {
										t = map[0].top;
									} else {
										t = map[0].layer[layer];
									}
									if (waitTouch > 1) {
										int x = oldX;
										int y = oldY;

										int x2 = tx;
										int y2 = ty;

										if (x > x2) {
											int k = x;
											x = x2;
											x2 = k;
										}

										if (y > y2) {
											int k = y;
											y = y2;
											y2 = k;
										}

										indexT = new int[x2 - x + 1][y2 - y + 1];
										for (int l = 0; l <= x2 - x; ++l) {
											for (int l2 = 0; l2 <= y2 - y; ++l2) {
												if (y2 - y - l2 < indexT[l].length)
													indexT[l][y2 - y - l2] = t[x + l][y + l2];
											}
										}
									} else {
										index = t[tx][ty];
										oldX = tx;
										oldY = ty;
										indexT = null;
									}
								}
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
						} else if (mx >= 96 && mx < 96 + 24) {
							layer = 2;
						} else if (mx >= 96 + 24 && mx < 96 + 48) {
							layer = -1;
						} else if (mx >= 96 + 24 + 24 && mx < 96 + 48 + 24) {
							layer = -2;
						}

						if (mx >= 96 + 48 + 48 && mx < 96 + 24 + 48 + 48) {
							tool = 1;
						} else if (mx >= 96 + 24 + 48 + 48 && mx < 96 + 48 + 48 + 48) {
							tool = 2;
						} else if (mx >= 96 + 48 + 48 + 48 && mx < 96 + 48 + 24 + 48 + 48) {
							tool = 3;
						}

						if (mx >= 24 * 9 + 48 && mx < 24 * 9 + 24 + 48) {
							if (waitTouch == 1) {
								if (map[0] != null) {
									map[0].tileset.Save();
								}
								n = new NamingWindows();

							}
						}
						if (mx >= 24 * 10 + 48 && mx < 24 * 10 + 24 + 48) {
							if (waitTouch == 1) {
								if (map[0] != null) {
									map[0].tileset.Save();
								}
								f = new FileOppener("map");

							}

						}
						if (mx >= 24 * 11 + 48 && mx < 24 * 11 + 24 + 48) {
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

						if (layer == -2) {
							int tx = (mx - Screen.Width - 32) / 16;
							int ty = (my) / 16;
							if (waitTouch == 1) {
								if (getEvent(eventX, eventY) == null) {
									if (tx >= 1 && tx < 6) {
										if (ty >= 7 && ty < 9) {
											System.out.println("TP");
											Event e = new Event(map[0].events.size(), eventX, eventY, 0);

											map[0].events.add(e);
										} else if (ty >= 4 && ty < 6) {
											System.out.println("Mobs");
										}
									} else if (tx >= 7 && tx < 12) {
										if (ty >= 7 && ty < 9) {
											System.out.println("Chest");

											Event e = new Event(map[0].events.size(), eventX, eventY, 1);
											e.loadCharaset("Chest.png");

											map[0].events.add(e);
										} else if (ty >= 4 && ty < 6) {
											System.out.println("Dialogs");
											Event e = new Event(map[0].events.size(), eventX, eventY, 3);
											//e.loadCharaset("Chest.png");

											map[0].events.add(e);
										}
									}
								} else {
									if (getEvent(eventX, eventY).type == 0) {
										if (tx >= 4 && tx < 11) {
											if (ty >= 8 && ty < 10) {

												// tpFrame = new
												// TPFrame(listMap);

												f = new FileOppener("teleport");

											}
										}
										if (tx >= 4 && tx < 9) {
											if (ty >= 1 && ty < 3) {
												map[0].events.remove(getEvent(eventX, eventY));
												System.out.println("Delete");
											}
										}
									} else if (getEvent(eventX, eventY).type == 1) {
										if (tx >= 4 && tx < 11) {
											if (ty >= 8 && ty < 10) {
												f = new FileOppener("charaset");

											}
										}
										if (tx >= 4 && tx < 9) {
											if (ty >= 1 && ty < 3) {
												map[0].events.remove(getEvent(eventX, eventY));
												System.out.println("Delete");
											}
										}
									} else if (getEvent(eventX, eventY).type == 3) {
										if (tx >= 4 && tx < 11) {
											if (ty >= 8 && ty < 10) {
												f = new FileOppener("charaset");

											}
										}
										if (tx >= 4 && tx < 11) {
											if (ty >= 5 && ty < 7) {
												d = new DialogFrame(getEvent(eventX, eventY));

											}
										}
										if (tx >= 4 && tx < 9) {
											if (ty >= 1 && ty < 3) {
												map[0].events.remove(getEvent(eventX, eventY));
												System.out.println("Delete");
											}
										}
									}

									// 
								}
							}
						} else {
							int tx = mx - Screen.Width - 32;
							int ty = 24 * 8 - my - 8 + (display_y / 24) * 24;
							if (tile_editor_mode == 0) {
								if (tiles() != null) {
									int i = ((ty) / 24) * 8 + (tx / 24);

									if (i > 0 && i < 8 && Gdx.input.isButtonPressed(1)) {
										f = new FileOppener("autotiles");
										indexT = null;
										index = i;
									} else {
										if (i > 0 && i < 8 && map[0].tileset.autotiles[i - 1] == null) {
											f = new FileOppener("autotiles");
											indexT = null;
											index = i;
										} else if (waitTouch > 1) {
											if (index != i) {
												int x = index % 8;
												int y = index / 8;

												int x2 = i % 8;
												int y2 = i / 8;

												if (x > x2) {
													int k = x;
													x = x2;
													x2 = k;
												}

												if (y > y2) {
													int k = y;
													y = y2;
													y2 = k;
												}

												indexT = new int[x2 - x + 1][y2 - y + 1];
												for (int l = 0; l <= x2 - x; ++l) {
													for (int l2 = 0; l2 <= y2 - y; ++l2) {
														indexT[l][l2] = (x + l) + (y + l2) * 8;
													}
												}
											}
										} else {
											index = i;

											indexT = null;// new int[][] { { i }
															// };
										}
										if (index >= tiles().length)
											index = 0;
										if (index < 0)
											index = 0;
									}
								}
							} else if (tile_editor_mode == 1) {
								if (tiles() != null) {
									int j = ((ty) / 24) * 8 + (tx / 24);
									if (j < 0)
										j = 0;
									if (j >= tiles().length)
										j = 0;

									if (waitTouch == 1) {
										if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
											map[0].tileset.passability[j] = (map[0].tileset.passability[j] + 1)
													% passability.length;
										} else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
											map[0].tileset.passability[j] = (map[0].tileset.passability[j]
													+ passability.length - 1) % passability.length;
										}
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

									undoAction = new ArrayList<int[]>();

									reloadMaps();

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

	private void reloadMaps() {
		for (int i = 1; i < 5; ++i) {
			if (map[i] != null) {
				map[i].dispose();
			}
			map[i] = null;
		}

		if (listMap.index + 1 < listMap.width * listMap.height) {
			int i = listMap.index + 1;
			map[1] = new Map(listMap.name + "/map" + i + ".xml");
		}
		if (listMap.index - 1 >= 0) {
			int i = listMap.index - 1;
			map[2] = new Map(listMap.name + "/map" + i + ".xml");
		}
		if (listMap.index - 5 > 0) {
			int i = listMap.index - 5;
			map[3] = new Map(listMap.name + "/map" + i + ".xml");
		}
		if (listMap.index + 5 < listMap.width * listMap.height) {
			int i = listMap.index + 5;
			map[4] = new Map(listMap.name + "/map" + i + ".xml");
		}

		System.gc();
	}

	private void paint(int tx, int ty, int i) {
		if (layer < 0) {
			if (map[0].top[tx][ty] != index) {
				map[0].top[tx][ty] = index;
				if (tx + 1 < map[0].width)
					if (map[0].top[tx + 1][ty] == i) {
						paint(tx + 1, ty, i);
					}
				if (tx - 1 >= 0)
					if (map[0].top[tx - 1][ty] == i) {
						paint(tx - 1, ty, i);
					}

				if (ty + 1 < map[0].height)
					if (map[0].top[tx][ty + 1] == i) {
						paint(tx, ty + 1, i);
					}
				if (ty - 1 >= 0)
					if (map[0].top[tx][ty - 1] == i) {
						paint(tx, ty - 1, i);
					}
			}
		} else {
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
	}

	@Override
	public void render(SpriteBatch batch) {

		int mx = MapEditor.mx();
		int my = MapEditor.my();
		int tx = ((mx - 16) / 16) * 16;
		int ty = ((my - 24) / 16) * 16;

		batch.draw(back, 0, 0);

		batch.draw(backMiniMap, Screen.Width + 32, Screen.Height - 122 + 44 + 32);

		wait_anim++;
		if (wait_anim > 10) {
			animation++;
			wait_anim = 0;
			Map.needRefresh = false;
		}

		Color c = batch.getColor();
		batch.setColor(1f, 1f, 1f, 0.5f);

		if (map[1] != null) {
			for (int j = 0; j < map[1].height; ++j) {

				for (int i = 0; i < 3; i++) {
					int id = map[1].layer[i][0][j];
					map[1].draw(batch, id, 0, j, 0, map[0].width * 16 + 16, 16 * j + 5 + 16);
				}

			}
		}

		if (map[2] != null) {
			for (int j = 0; j < map[2].height; ++j) {

				for (int i = 0; i < 3; i++) {
					int id = map[2].layer[i][map[2].width - 1][j];
					map[2].draw(batch, id, map[2].width - 1, j, 0, 0, 16 * j + 5 + 16);
				}

			}
		}

		if (map[3] != null) {
			for (int j = 0; j < map[3].width; ++j) {
				for (int i = 0; i < 3; i++) {
					int id = map[3].layer[i][j][0];
					map[3].draw(batch, id, j, 0, 0, 16 * j + 16, map[3].height * 16 + 16 + 5);
				}

			}
		}
		if (map[4] != null) {
			for (int j = 0; j < map[4].width; ++j) {
				for (int i = 0; i < 3; i++) {
					int id = map[4].layer[i][j][map[4].height - 1];
					map[4].draw(batch, id, j, map[4].height - 1, 0, 16 * j + 16, 5);
				}
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
						if (id > 0 && id <= 7 && map[0].tileset.autotiles[id - 1] != null) {
							map[0].tileset.autotiles[id - 1].render(batch, i, j, map[0].width, map[0].height, id,
									map[0].layer[0]);
						} else {
							batch.draw(tiles()[id], 16 * i + 16, 16 * j + 5 + 16);
						}

				}

			}

			batch.setColor(c);

			if (layer != 1 && !tile_selector)
				batch.setColor(1f, 1f, 1f, 0.5f);
			for (int i = 0; i < map[0].width; ++i) {
				for (int j = 0; j < map[0].height; ++j) {
					int id = map[0].layer[1][i][j];
					if ((i != tx / 16 || j != ty / 16) || layer != 1)
						if (id > 0 && id <= 7 && map[0].tileset.autotiles[id - 1] != null) {
							map[0].tileset.autotiles[id - 1].render(batch, i, j, map[0].width, map[0].height, id,
									map[0].layer[1]);
						} else {
							batch.draw(tiles()[id], 16 * i + 16, 16 * j + 5 + 16);
						}
				}
			}
			batch.setColor(c);

			if (layer != 2 && !tile_selector)
				batch.setColor(1f, 1f, 1f, 0.5f);
			for (int i = 0; i < map[0].width; ++i) {
				for (int j = 0; j < map[0].height; ++j) {
					int id = map[0].layer[2][i][j];
					if ((i != tx / 16 || j != ty / 16) || layer != 2)
						if (id > 0 && id <= 7 && map[0].tileset.autotiles[id - 1] != null) {

							map[0].tileset.autotiles[id - 1].render(batch, i, j, map[0].width, map[0].height, id,
									map[0].layer[2]);
						} else {
							batch.draw(tiles()[id], 16 * i + 16, 16 * j + 5 + 16);
						}
				}

			}
			batch.setColor(c);

			for (Event e : map[0].events) {
				e.render(batch);
			}

			if (layer == -1) {
				for (int i = 0; i < map[0].width; ++i) {
					for (int j = 0; j < map[0].height; ++j) {
						int id = map[0].top[i][j];
						if ((i != tx / 16 || j != ty / 16) || layer != -1)
							if (id > 0 && id <= 7 && map[0].tileset.autotiles[id - 1] != null) {
								map[0].tileset.autotiles[id - 1].render(batch, i, j, map[0].width, map[0].height, id,
										map[0].top);

							} else {
								batch.draw(tiles()[id], 16 * i + 16, 16 * j + 5 + 16);
							}
					}
				}
			}

		}

		batch.draw(windows, 0, 0);

		batch.setColor(c);

		if (map[0] != null) {
			if (layer == 0) {
				batch.draw(selector2, 48, Screen.Height + 20 + 32);
			} else if (layer == 1) {
				batch.draw(selector2, 48 + 24, Screen.Height + 20 + 32);
			} else if (layer == 2) {
				batch.draw(selector2, 48 + 48, Screen.Height + 20 + 32);
			} else if (layer == -1) {
				batch.draw(selector2, 48 + 48 + 24, Screen.Height + 20 + 32);
			} else if (layer == -2) {
				batch.draw(selector2, 48 + 48 + 48, Screen.Height + 20 + 32);
			}

			if (tool == 1) {
				batch.draw(selector2, 48 + 48 + 48 + 48, Screen.Height + 20 + 32);
			} else if (tool == 2) {
				batch.draw(selector2, 96 + 24 + 48 + 48, Screen.Height + 20 + 32);
			} else if (tool == 3) {
				batch.draw(selector2, 96 + 48 + 48 + 48, Screen.Height + 20 + 32);
			}

			c = batch.getColor();
			batch.setColor(1f, 1f, 1f, 0.5f);

			if (tx >= map[0].width * 16)
				tx = (map[0].width - 1) * 16;
			if (ty >= map[0].height * 16)
				ty = (map[0].height - 1) * 16;
			if (!tile_selector && layer >= -1) {
				if (mx - 16 < Screen.Width && my - 16 < Screen.Height) {
					int id = index;
					int i = tx / 16;
					int j = ty / 16;
					if (i < 0)
						i = 0;
					if (j < 0)
						j = 0;
					if (id > 0 && id <= 7 && map[0].tileset.autotiles[id - 1] != null) {

						if (layer < 0)
							map[0].tileset.autotiles[id - 1].render(batch, i, j, map[0].width, map[0].height, id,
									map[0].top);
						else
							map[0].tileset.autotiles[id - 1].render(batch, i, j, map[0].width, map[0].height, id,
									map[0].layer[layer]);
					} else {
						if (indexT == null)
							batch.draw(tiles()[index], tx + 16, ty + 5 + 16);
						else
							for (int i2 = 0; i2 < indexT.length; ++i2) {
								for (int j2 = 0; j2 < indexT[i2].length; ++j2) {
									int id2 = indexT[i2][j2];
									if (tx + i2 * 16 < map[0].width * 16 && ty - j2 * 16 >= 0)
										batch.draw(tiles()[id2], tx + 16 + 16 * i2, ty + 5 + 16 - 16 * j2);
								}
							}
					}

				}
			}
			batch.setColor(c);

		}

		if (listMap != null) {
			batch.draw(selector, (listMap.index % listMap.width) * 16 + 544 + 32,
					(listMap.height - listMap.index / listMap.height) * 16 + 192 + 10 + 32);
		}

		if (layer == -2) {
			if (getEvent(eventX, eventY) != null) {
				if (getEvent(eventX, eventY).type == 0)
					batch.draw(backEventTP, Screen.Width + 32, 0);
				else if (getEvent(eventX, eventY).type == 1)
					batch.draw(backEventChest, Screen.Width + 32, 0);
				else if (getEvent(eventX, eventY).type == 3)
					batch.draw(backEventDialog, Screen.Width + 32, 0);
				else
					batch.draw(backEvent, Screen.Width + 32, 0);
			} else
				batch.draw(backNewEvent, Screen.Width + 32, 0);

			batch.draw(selector, eventX * 16 + 16, eventY * 16 + 16 + 6);

		} else
			batch.draw(back2, Screen.Width + 32, 0);

		if (map[0] != null && layer != -2) {
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
						if (i >= 1 && i <= 7 && map[0].tileset.autotiles[i - 1] != null) {
							map[0].tileset.autotiles[i - 1].render(batch, x, y);
						} else
							batch.draw(tiles()[i], x, y);
						if (indexT == null) {
							if (index == i) {
								batch.draw(selector, x, y);
							}
						} else {
							if (contains(indexT, i)) {
								batch.draw(selector, x, y);
							}
						}
						if (tile_editor_mode == 1) {
							int p = map[0].tileset.passability[i];
							batch.draw(passability[p], x, y);
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

	@Override
	public void render(ShapeRenderer shape) {
		for (int i = 0; i < map[0].events.size(); ++i) {
			Event e = map[0].events.get(i);

			e.render(shape);
		}
	}

	private boolean contains(int[][] t, int id) {
		for (int i = 0; i < t.length; ++i) {
			for (int j = 0; j < t[i].length; ++j) {
				if (t[i][j] == id)
					return true;
			}
		}
		return false;
	}

	private TextureRegion[] tiles(int i) {
		if (map[i] != null)
			return map[i].tiles;
		else
			return null;
	}

	public void addUndo(int i, int j, int x, int y) {
		int[] t = new int[] { i, x, y, j, layer };

		// System.out.println("A : " + i + " / " + j + " / " + x + " / " + y + "
		// / " + layer);

		if (undoIndex - undoAction.size() < -1) {
			ArrayList<int[]> test = new ArrayList<int[]>();
			for (int l = 0; l <= undoIndex; ++l) {
				test.add(undoAction.get(l));
			}

			undoAction = test;
		}

		undoIndex = undoAction.size();

		undoAction.add(t);

	}

	public void undo() {

		if (undoIndex >= undoAction.size()) {
			undoIndex = undoAction.size() - 1;
		}
		if (undoIndex < 0) {
			undoIndex = 0;
		}
		{
			int[] t = undoAction.get(undoIndex);
			int x = t[1];
			int y = t[2];
			// int id1 = t[0];
			int id2 = t[3];
			int l = t[4];

			int m[][];
			if (layer < 0) {
				m = map[0].top;
			} else {
				m = map[0].layer[l];
			}

			m[x][y] = id2;

			undoIndex--;

		}

	}

	public void redo() {

		if (undoIndex >= undoAction.size()) {
			undoIndex = undoAction.size() - 1;
		}
		if (undoIndex < 0) {
			undoIndex = 0;
		}
		{
			int[] t = undoAction.get(undoIndex);
			int x = t[1];
			int y = t[2];
			int id1 = t[0];
			// int id2 = t[3];
			int l = t[4];

			int m[][];
			if (layer < 0) {
				m = map[0].top;
			} else {
				m = map[0].layer[l];
			}

			m[x][y] = id1;

			undoIndex++;
		}
	}

	public Event getEvent(int x, int y) {
		for (int i = 0; i < map[0].events.size(); ++i) {
			Event e = map[0].events.get(i);
			if (e.x == x && e.y == y)
				return e;
		}

		return null;
	}

	private TextureRegion[] tiles() {
		return tiles(0);
	}

	@Override
	public void dispose() {
	}
}
