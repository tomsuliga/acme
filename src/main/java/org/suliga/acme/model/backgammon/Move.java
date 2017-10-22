package org.suliga.acme.model.backgammon;

public class Move {
	public static final int FROM_BAR = -99;
	public static final int TO_BEAR = 88;
	private int fromPoint;
	private int toPoint;
	private boolean multiDiceUsed;
	
	public Move(int fromPoint, int toPoint, boolean multiDiceUsed) {
		this.fromPoint = fromPoint;
		this.toPoint = toPoint;
		this.multiDiceUsed = multiDiceUsed;
		
		// temp ?
		if (this.toPoint > 23 || this.toPoint < 0) {
			this.toPoint = TO_BEAR;
		}
	}
	
	public int getFromPoint() {
		return fromPoint;
	}

	public int getToPoint() {
		return toPoint;
	}
	
	public boolean isMultiDiceUsed() {
		return multiDiceUsed;
	}

	public void setMultiDiceUsed(boolean multiDiceUsed) {
		this.multiDiceUsed = multiDiceUsed;
	}

	@Override
	public String toString() {
		return "Move " + (fromPoint == FROM_BAR ? "Bar" : fromPoint) + ":" + (toPoint == TO_BEAR ? "Bear" : toPoint);
	}
}

