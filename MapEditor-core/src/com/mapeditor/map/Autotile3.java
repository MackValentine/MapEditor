package com.mapeditor.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mapeditor.screen.ScreenEditor;

public class Autotile3 {

	public String name;
	public TextureRegion[] tiles;
	private Texture tileset;
	private int id;
	private boolean use_animation;

	public Autotile3(String s, int l) {
		name = s;
		id = l;

		tileset = new Texture(Gdx.files.local("autotiles/" + s + ".png"));
		int w = tileset.getWidth() / 8;
		int h = tileset.getHeight() / 8;
		tiles = new TextureRegion[w * h];
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				tiles[i + j * w] = new TextureRegion(tileset, i * 8, j * 8, 8, 8);
			}
		}

		if (h > 5){
			use_animation = true;
		}
	}

	public void render(SpriteBatch b, int x, int y, int[] t) {
		int i = 0;
		if (use_animation)
			i = (ScreenEditor.animation%2)*20;
		
		if (t[0] == id && t[1] == id && t[2] == id && t[3] == id && t[4] == id && t[5] == id && t[6] == id
				&& t[7] == id) {
			b.draw(tiles[i+13], x, y + 8);
			b.draw(tiles[i+13], x + 8, y + 8);
			b.draw(tiles[i+13], x, y);
			b.draw(tiles[i+13], x + 8, y);
		} else if (t[1] == id && t[0] == id && t[2] == id && t[3] == id) {

			b.draw(tiles[i+2], x, y + 8);
			b.draw(tiles[i+3], x + 8, y + 8);
			b.draw(tiles[i+6], x, y);
			b.draw(tiles[i+7], x + 8, y);
		} else if (t[0] == id && t[2] == id && t[4] == id && t[5] != id) {
			b.draw(tiles[i+12], x, y + 8);
			b.draw(tiles[i+13], x + 8, y + 8);
			b.draw(tiles[i+4], x, y);
			b.draw(tiles[i+17], x + 8, y);

		} else if (t[0] == id && t[1] == id && t[2] == id) {
			b.draw(tiles[i+13], x, y + 8);
			b.draw(tiles[i+13], x + 8, y + 8);
			b.draw(tiles[i+17], x, y);
			b.draw(tiles[i+17], x + 8, y);

		} else if (t[0] == id && t[2] == id && t[3] == id) {
			b.draw(tiles[i+12], x, y + 8);
			b.draw(tiles[i+13], x + 8, y + 8);
			b.draw(tiles[i+12], x, y);
			b.draw(tiles[i+13], x + 8, y);
		} else if (t[1] == id && t[2] == id && t[3] == id) {
			b.draw(tiles[i+13], x, y + 8);
			b.draw(tiles[i+14], x + 8, y + 8);
			b.draw(tiles[i+13], x, y);
			b.draw(tiles[i+14], x + 8, y);
		} else if (t[0] == id && t[1] == id && t[3] == id) {
			b.draw(tiles[i+9], x, y + 8);
			b.draw(tiles[i+9], x + 8, y + 8);
			b.draw(tiles[i+13], x, y);
			b.draw(tiles[i+13], x + 8, y);
		} else if (t[1] == id && t[2] == id) {

			b.draw(tiles[i+2], x, y + 8);
			b.draw(tiles[i+14], x + 8, y + 8);
			b.draw(tiles[i+17], x, y);
			b.draw(tiles[i+18], x + 8, y);
		} else if (t[1] == id && t[3] == id) {
			b.draw(tiles[i+9], x, y + 8);
			b.draw(tiles[i+10], x + 8, y + 8);
			b.draw(tiles[i+6], x, y);
			b.draw(tiles[i+14], x + 8, y);
		} else if (t[0] == id && t[2] == id) {
			b.draw(tiles[i+12], x, y + 8);
			b.draw(tiles[i+3], x + 8, y + 8);
			b.draw(tiles[i+4], x, y);
			b.draw(tiles[i+17], x + 8, y);
		} else if (t[0] == id && t[3] == id) {
			b.draw(tiles[i+0], x, y + 8);
			b.draw(tiles[i+9], x + 8, y + 8);
			b.draw(tiles[i+12], x, y);
			b.draw(tiles[i+7], x + 8, y);
		} else if (t[1] == id && t[0] == id) {
			b.draw(tiles[i+9], x, y + 8);
			b.draw(tiles[i+9], x + 8, y + 8);
			b.draw(tiles[i+17], x, y);
			b.draw(tiles[i+17], x + 8, y);
		} else if (t[2] == id && t[3] == id) {
			b.draw(tiles[i+12], x, y + 8);
			b.draw(tiles[i+14], x + 8, y + 8);
			b.draw(tiles[i+12], x, y);
			b.draw(tiles[i+14], x + 8, y);

		} else if (t[1] == id) {
			b.draw(tiles[i+9], x, y + 8);
			b.draw(tiles[i+1], x + 8, y + 8);
			b.draw(tiles[i+17], x, y);
			b.draw(tiles[i+5], x + 8, y);
		} else if (t[2] == id) {
			b.draw(tiles[i+12], x, y + 8);
			b.draw(tiles[i+14], x + 8, y + 8);
			b.draw(tiles[i+4], x, y);
			b.draw(tiles[i+5], x + 8, y);
		} else if (t[3] == id) {
			b.draw(tiles[i+0], x, y + 8);
			b.draw(tiles[i+1], x + 8, y + 8);
			b.draw(tiles[i+12], x, y);
			b.draw(tiles[i+14], x + 8, y);
		} else if (t[0] == id) {
			b.draw(tiles[i+0], x, y + 8);
			b.draw(tiles[i+9], x + 8, y + 8);
			b.draw(tiles[i+4], x, y);
			b.draw(tiles[i+17], x + 8, y);
		} else {
			b.draw(tiles[i+0], x, y + 8);
			b.draw(tiles[i+1], x + 8, y + 8);
			b.draw(tiles[i+4], x, y);
			b.draw(tiles[i+5], x + 8, y);
		}

	}

	public void render(SpriteBatch b, int x, int y) {
		b.draw(tiles[0], x, y + 8);
		b.draw(tiles[1], x + 8, y + 8);
		b.draw(tiles[4], x, y);
		b.draw(tiles[5], x + 8, y);
	}

}
