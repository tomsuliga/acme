package org.suliga.acme.model.minesweeper;

public class GameColRow {
	private String sessionId;
	
	private int col;
	
	private int row;
	
	private boolean possibleMine;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
	public boolean isPossibleMine() {
		return possibleMine;
	}

	public void setPossibleMine(boolean possibleMine) {
		this.possibleMine = possibleMine;
	}

	@Override
	public String toString() {
		return "GameColRow: " + sessionId + " " + col + " " + row;
	}
}
