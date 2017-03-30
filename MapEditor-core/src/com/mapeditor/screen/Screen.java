package com.mapeditor.screen;

import java.io.FileNotFoundException;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Screen {

	public abstract void update() throws FileNotFoundException;

	public abstract void render(SpriteBatch batch);

	public abstract void render(ShapeRenderer shape);
	
	public abstract void dispose();

}
