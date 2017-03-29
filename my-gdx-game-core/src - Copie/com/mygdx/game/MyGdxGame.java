package com.mygdx.game;

import java.awt.Color;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Enumeration;
import java.util.Vector;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.madroid.input.GestureRecognizerInputProcessor;

import $1.NDollarParameters;
import $1.NDollarRecognizer;
import $1.PointR;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer shape;
	Texture img;
	private InputProcessor2 processor;
	static NDollarRecognizer _rec;

	@Override
	public void create() {
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		img = new Texture("badlogic.jpg");

		_rec = new NDollarRecognizer();

		// create the set of filenames to read in
		/*
		 * File currentDir = Gdx.files.internal("samples/").file(); File[]
		 * allXMLFiles = currentDir.listFiles(new FilenameFilter() {
		 * 
		 * @Override public boolean accept(File dir, String name) { return
		 * name.toLowerCase().endsWith(".xml"); } });
		 * 
		 * System.out.println(allXMLFiles);
		 * 
		 * // read them for (int i = 0; i < allXMLFiles.length; ++i) {
		 * _rec.LoadGesture(allXMLFiles[i]); }
		 */

		FileHandle dirHandle;
		if (Gdx.app.getType() == ApplicationType.Android) {
			dirHandle = Gdx.files.internal("samples");
		} else {
			// ApplicationType.Desktop ..
			dirHandle = Gdx.files.internal("./bin/samples");
		}

		for (FileHandle entry : dirHandle.list()) {
			System.out.println(entry);
			_rec.LoadGesture(entry.read());
		}

		processor = new InputProcessor2();
		Gdx.input.setInputProcessor(processor);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shape.begin(ShapeType.Filled);

		Vector<Vector<PointR>> strokes = processor.strokes;
		Enumeration<Vector<PointR>> en = strokes.elements();
		while (en.hasMoreElements()) {
			Vector<PointR> pts = en.nextElement();
			for (int i = 0; i < (pts.size() - 1); ++i) {
				shape.line((int) pts.elementAt(i).X, Gdx.graphics.getHeight() - (int) pts.elementAt(i).Y,
						(int) pts.elementAt(i + 1).X, Gdx.graphics.getHeight() - (int) pts.elementAt(i + 1).Y);
			}
		}

		Vector<PointR> points = processor.points;
		if (points.size() >= 2) {
			for (int i = 0; i < (points.size() - 1); ++i) {

				shape.line((int) points.elementAt(i).X, Gdx.graphics.getHeight() - (int) points.elementAt(i).Y,
						(int) points.elementAt(i + 1).X, Gdx.graphics.getHeight() - (int) points.elementAt(i + 1).Y);
			}
		}
		shape.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}
}
