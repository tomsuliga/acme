package org.suliga.acme.model.backgammon;

import java.util.Iterator;
import java.util.Set;

public class ClientServerMessage {
	private String sessionId;
	private int side;
	private int selectedPoint;
	private int[] moveablePoints; 
	private int[] destinationPoints;
	private int fromPoint;
	private int toPoint;
	private int[] diceRolled;
	private int[] diceUsed;
	private boolean gameOver;
	private String state; // Roll
	
	public ClientServerMessage() {}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int getSide() {
		return side;
	}

	public void setSide(int side) {
		this.side = side;
	}

	public int getSelectedPoint() {
		return selectedPoint;
	}

	public void setSelectedPoint(int selectedPoint) {
		this.selectedPoint = selectedPoint;
	}

	public int[] getMoveablePoints() {
		return moveablePoints;
	}

	public void setMoveablePoints(int[] moveablePoints) {
		this.moveablePoints = moveablePoints;
	}
	
	public void setMoveablePointsEx(Set<Integer> set) {
		moveablePoints = new int[set.size()];
		int index = 0;
		Iterator<Integer> iter = set.iterator();
		while (iter.hasNext()) {
			moveablePoints[index++] = iter.next();
		}
	}

	public int[] getDestinationPoints() {
		return destinationPoints;
	}

	public void setDestinationPoints(int[] destinationPoints) {
		this.destinationPoints = destinationPoints;
	}

	public int getFromPoint() {
		return fromPoint;
	}

	public void setFromPoint(int fromPoint) {
		this.fromPoint = fromPoint;
	}

	public int getToPoint() {
		return toPoint;
	}

	public void setToPoint(int toPoint) {
		this.toPoint = toPoint;
	}

	public int[] getDiceRolled() {
		return diceRolled;
	}

	public void setDiceRolled(int[] diceRolled) {
		this.diceRolled = diceRolled;
	}
	
	public void setDiceRolledEx(Dice dice) {		
		if (dice.isDouble()) {
			diceRolled = new int[4];
			diceRolled[0] = dice.getDie(0);
			diceRolled[1] = dice.getDie(1);
			diceRolled[2] = dice.getDie(0);
			diceRolled[3] = dice.getDie(1);
		} else {
			diceRolled = new int[2];
			diceRolled[0] = dice.getDie(0);
			diceRolled[1] = dice.getDie(1);
		}
	}
	
	public void calculateNumbersUsed() {
		
	}

	public int[] getDiceUsed() {
		return diceUsed;
	}

	public void setDiceUsed(int[] diceUsed) {
		this.diceUsed = diceUsed;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
