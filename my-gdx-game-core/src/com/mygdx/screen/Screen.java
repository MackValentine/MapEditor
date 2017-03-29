package com.mygdx.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Screen {

	public abstract void update();

	public abstract void render(SpriteBatch batch);

	public abstract void render(ShapeRenderer shape);
	
	public abstract void dispose();

}
