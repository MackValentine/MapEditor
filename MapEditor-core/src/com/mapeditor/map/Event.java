package com.mapeditor.map;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mapeditor.game.MapEditor;

public class Event {

	public int id;
	public int x;
	public int y;
	public int type;

	public EventCommand command;

	public String charaset_name = "";
	public Texture charaset;
	public TextureRegion[][] charas;

	public Event(int k, int i, int j, int t) {
		id = k;

		x = i;
		y = j;

		type = t;

		command = new EventCommand();
	}

	public Event() {
		command = new EventCommand();
	}

	public void loadCharaset(String s) {
		charaset_name = s;
		charaset = new Texture(MapEditor.s + "/characters/" + s);
		int w = charaset.getWidth() / 16;
		int h = charaset.getHeight() / 16;

		charas = new TextureRegion[w][h];
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				charas[i][j] = new TextureRegion(charaset, i * 16, j * 16, 16, 16);
			}
		}
	}

	public void render(SpriteBatch batch) {
		if (charaset != null) {
			batch.draw(charas[0][0], x * 16 + 16, y * 16 + 21);
		}
	}

	public void render(ShapeRenderer shape) {
		Rectangle h = hitbox();
		float x = h.x;
		float y = h.y;
		float width = h.width;
		float height = h.height;
		shape.rect(x, y, width, height);
	}

	public Rectangle hitbox() {
		int h = 8;
		int w = 8;
		return new Rectangle(x * 16 - w / 2 + 24, y * 16 + 26, w, h);
	}

}
