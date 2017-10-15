package org.suliga.acme.model.backgammon;

public class Move {
	private int fromPoint;
	private int toPoint;
	
	public Move(int fromPoint, int toPoint) {
		this.fromPoint = fromPoint;
		this.toPoint = toPoint;
	}
	
	public int getFromPoint() {
		return fromPoint;
	}

	public int getToPoint() {
		return toPoint;
	}
	
	@Override
	public String toString() {
		return "Move from:" + fromPoint + ", to:" + toPoint;
	}
}

