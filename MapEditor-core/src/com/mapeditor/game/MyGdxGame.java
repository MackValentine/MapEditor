package com.mapeditor.game;

import java.io.FileNotFoundException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mapeditor.screen.*;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer shape;

	Screen screen;

	@Override
	public void create() {
		
		
		batch = new SpriteBatch();
		shape = new ShapeRenderer();

		Gdx.graphics.setWindowedMode(320*2, 288*2);
		
		try {
			screen = new ScreenEditor();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		float fx = Gdx.graphics.getWidth() / 320.0f;
		int mx = (int) (Gdx.input.getX() / fx);
		return mx;
	}
	public static int my() {
		float fy = Gdx.graphics.getHeight() / 288.0f;
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
