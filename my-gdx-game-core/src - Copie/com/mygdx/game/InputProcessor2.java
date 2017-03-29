package com.mygdx.game;

import java.awt.Point;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Vector;

import com.badlogic.gdx.InputAdapter;

import $1.NBestList;
import $1.PointR;
import $1.Utils;

public class InputProcessor2 extends InputAdapter {

	public Vector<PointR> points = new Vector<PointR>();;
	Vector<Vector<PointR>> strokes = new Vector<Vector<PointR>>();

	public InputProcessor2() {
		super();
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {

		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		Point point = new Point(x, y);
		points.add(new PointR(point.x, point.y));
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {

		if (button == 0) {
			if (points.size() > 1) {
				strokes.add(new Vector<PointR>(points));
			}
			points.clear();
		} else {
			if (strokes.size() > 0) {
				Vector<PointR> allPoints = new Vector<PointR>();
				Enumeration<Vector<PointR>> en = strokes.elements();
				while (en.hasMoreElements()) {
					Vector<PointR> pts = en.nextElement();
					allPoints.addAll(pts);
				}
				NBestList result = MyGdxGame._rec.Recognize(allPoints, strokes.size());
				String resultTxt;
				if (result.getScore() == -1) {
					resultTxt = MessageFormat.format("No Match!\n[{0} out of {1} comparisons made]",
							result.getActualComparisons(), result.getTotalComparisons());
					// recLabel.setText("No Match!");
				} else {
					resultTxt = MessageFormat.format(
							"{0}: {1} ({2}px, {3}{4})  [{5,number,integer} out of {6,number,integer} comparisons made]",
							result.getName(), Utils.round(result.getScore(), 2), Utils.round(result.getDistance(), 2),
							Utils.round(result.getAngle(), 2), (char) 176, result.getActualComparisons(),
							result.getTotalComparisons());
					// recLabel.setText("Result: " + result.getName() + " ("
					// + Utils.round(result.getScore(), 2) + ")");
				}
				System.out.println(resultTxt);
				points.clear();
			}
			return false;
		}
		return false;
	}

}
