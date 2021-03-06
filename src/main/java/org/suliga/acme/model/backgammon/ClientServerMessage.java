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
	private int bar1Count;
	private int bar2Count;
	private int[] diceRolled;
	private boolean[] diceUsed;
	private boolean turnOver;
	private boolean gameOver;
	private boolean barHop;
	private boolean barOff;
	private boolean noMove;
	private boolean bearOff;
	private boolean startTurn; // to reveal dice
	private String state; // Roll
	private boolean firstMove; // only at start of game
	private long gameId;
	private boolean replay;
	
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
		if (set.size() == 0) {
			turnOver = true;
			return;
		}
		
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
			diceRolled[0] = dice.getDie1();
			diceRolled[1] = dice.getDie2();
			diceRolled[2] = dice.getDie1();
			diceRolled[3] = dice.getDie2();
			diceUsed = new boolean[4];
			diceUsed[0] = dice.isUsed1();
			diceUsed[1] = dice.isUsed2();
			diceUsed[2] = dice.isUsed3();
			diceUsed[3] = dice.isUsed4();
		} else {
			diceRolled = new int[2];
			diceRolled[0] = dice.getDie1();
			diceRolled[1] = dice.getDie2();
			diceUsed = new boolean[2];
			diceUsed[0] = dice.isUsed1();
			diceUsed[1] = dice.isUsed2();
		}
	}
	
	public void calculateNumbersUsed(Dice dice, boolean barOff) {
		
		int num = Math.abs(fromPoint - toPoint);

		boolean found = false;

		if (toPoint == Move.TO_BEAR) {
			int numNeeded = 1;
			if (fromPoint >= 18) {
				numNeeded = 24 - fromPoint;
			} else {
				numNeeded = fromPoint + 1;
			}
			for (int i=numNeeded;i<=6;i++) {
				for (int j=0;j<4;j++) {
					if (j >= 2 && diceRolled[0] != diceRolled[1]) {
						break;
					}
					if (diceUsed[j] == false && diceRolled[j] == i) {
						diceUsed[j] = true;
						dice.setUsedUsingIndex(j);
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}
		} else if (barOff) {
			num = toPoint + 1;
			if (num > 6) {
				num = 25 - num;
			}
		}
			
		if (!found) {
			// not double
			if (!diceUsed[0] && num == diceRolled[0]) {
				diceUsed[0] = true;
				dice.setUsedUsingIndex(0);
				found = true;
			} else if (!diceUsed[1] && num == diceRolled[1]) {
				diceUsed[1] = true;
				dice.setUsedUsingIndex(1);
				found = true;
			} else if (!diceUsed[0] && !diceUsed[1] && num == (diceRolled[0] + diceRolled[1])) {
				diceUsed[0] = true;
				diceUsed[1] = true;
				dice.setUsedUsingIndex(0);
				dice.setUsedUsingIndex(1);
				found = true;
			}
		}
		
		if (diceUsed[0] && diceUsed[1] && diceRolled[0] != diceRolled[1]) {
			turnOver = true;
		}
		
		if (found || (diceRolled[0] != diceRolled[1])) {
			if (diceRolled[0] == diceRolled[1] && diceUsed[0] && diceUsed[1] && diceUsed[2] && diceUsed[3]) {
				turnOver = true;
			}
			return;
		}
		
		// double
		if (!diceUsed[2] && num == diceRolled[2]) {
			diceUsed[2] = true;
			dice.setUsedUsingIndex(2);
			found = true;
		} else if (!diceUsed[3] && num == diceRolled[3]) {
			diceUsed[3] = true;
			dice.setUsedUsingIndex(3);
			found = true;
		} else if (!diceUsed[2] && !diceUsed[3] && num == (diceRolled[2] + diceRolled[3])) {
			diceUsed[2] = true;
			diceUsed[3] = true;
			dice.setUsedUsingIndex(2);
			dice.setUsedUsingIndex(3);
			found = true;
		} else if (!diceUsed[0] && !diceUsed[1] && !diceUsed[2] && num == (diceRolled[0] + diceRolled[1] + diceRolled[2])) {
			diceUsed[0] = true;
			diceUsed[1] = true;
			diceUsed[2] = true;
			dice.setUsedUsingIndex(0);
			dice.setUsedUsingIndex(1);
			dice.setUsedUsingIndex(2);
			found = true;
		} else if (!diceUsed[1] && !diceUsed[2] && !diceUsed[3] && num == (diceRolled[1] + diceRolled[2] + diceRolled[3])) {
			diceUsed[1] = true;
			diceUsed[2] = true;
			diceUsed[3] = true;
			dice.setUsedUsingIndex(1);
			dice.setUsedUsingIndex(2);
			dice.setUsedUsingIndex(3);
			found = true;
		} else if (!diceUsed[0] && !diceUsed[1] && !diceUsed[2] && !diceUsed[3] && num == (diceRolled[0] + diceRolled[1] + diceRolled[2] + diceRolled[3])) {
			diceUsed[0] = true;
			diceUsed[1] = true;
			diceUsed[2] = true;
			diceUsed[3] = true;
			dice.setUsedUsingIndex(0);
			dice.setUsedUsingIndex(1);
			dice.setUsedUsingIndex(2);
			dice.setUsedUsingIndex(3);
			found = true;
		}
		
		if (diceUsed[0] && diceUsed[1] && diceUsed[2] && diceUsed[3]) {
			turnOver = true;
		}
	}

	public boolean[] getDiceUsed() {
		return diceUsed;
	}

	public void setDiceUsed(boolean[] diceUsed) {
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

	public boolean isTurnOver() {
		return turnOver;
	}

	public void setTurnOver(boolean turnOver) {
		this.turnOver = turnOver;
	}

	public boolean isBarHop() {
		return barHop;
	}

	public void setBarHop(boolean barHop) {
		this.barHop = barHop;
	}

	public int getBar1Count() {
		return bar1Count;
	}

	public void setBar1Count(int bar1Count) {
		this.bar1Count = bar1Count;
	}

	public int getBar2Count() {
		return bar2Count;
	}

	public void setBar2Count(int bar2Count) {
		this.bar2Count = bar2Count;
	}

	public boolean isFirstMove() {
		return firstMove;
	}

	public void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}

	public boolean isNoMove() {
		return noMove;
	}
	
	public void setNoMove(boolean noMove) {
		this.noMove = noMove;
	}

	public boolean isStartTurn() {
		return startTurn;
	}

	public void setStartTurn(boolean startTurn) {
		this.startTurn = startTurn;
	}

	public boolean isBarOff() {
		return barOff;
	}

	public void setBarOff(boolean barOff) {
		this.barOff = barOff;
	}

	public boolean isBearOff() {
		return bearOff;
	}

	public void setBearOff(boolean bearOff) {
		this.bearOff = bearOff;
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public boolean isReplay() {
		return replay;
	}

	public void setReplay(boolean replay) {
		this.replay = replay;
	}
}












