package com.mapeditor.game;

import java.io.FileNotFoundException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mapeditor.screen.*;

public class MapEditor extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer shape;

	Screen screen;
	public static ScreenEditor editor;

	public static boolean hasFocus = true;

	@Override
	public void create() {

		batch = new SpriteBatch();
		shape = new ShapeRenderer();

		Gdx.graphics.setWindowedMode((Screen.Width + 216 + 32) * 2, (Screen.Height + 48 + 32) * 2);

		try {
			editor = new ScreenEditor();
			screen = editor;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void pause() {
		hasFocus = false;
	}

	@Override
	public void resume() {
		hasFocus = true;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		try {
			screen.update();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		batch.begin();
		screen.render(batch);
		batch.end();

		shape.begin(ShapeType.Line);
		screen.render(shape);
		shape.end();

	}

	public static int mx() {
		float fx = Gdx.graphics.getWidth() / (Screen.Width + 216);
		int mx = (int) (Gdx.input.getX() / fx);
		return mx;
	}

	public static int my() {
		float fy = Gdx.graphics.getHeight() / (Screen.Height + 44);
		int my = (int) ((Gdx.graphics.getHeight() - Gdx.input.getY()) / fy);
		return my;
	}

	@Override
	public void dispose() {
		screen.dispose();
		batch.dispose();
		shape.dispose();
	}
}
