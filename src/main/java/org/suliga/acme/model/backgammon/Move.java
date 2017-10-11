package org.suliga.acme.model.backgammon;

public class Move {
	private Point fromPoint;
	private Point toPoint;
	
	public Move(Point fromPoint, Point toPoint) {
		this.fromPoint = fromPoint;
		this.toPoint = toPoint;
	}
	
	@Override
	public String toString() {
		return "(" + fromPoint.getIndex() + "," + toPoint.getIndex() + ")";
	}
}

