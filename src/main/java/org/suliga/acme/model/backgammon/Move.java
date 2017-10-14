package org.suliga.acme.model.backgammon;

public class Move {
	private Point fromPoint;
	private Point toPoint;
	
	public Move(Point fromPoint, Point toPoint) {
		this.fromPoint = fromPoint;
		this.toPoint = toPoint;
	}
	
	public Point getFromPoint() {
		return fromPoint;
	}

	public Point getToPoint() {
		return toPoint;
	}
	
	@Override
	public String toString() {
		return "Move from:" + fromPoint.getIndex() + ", to:" + toPoint.getIndex();
	}
}

