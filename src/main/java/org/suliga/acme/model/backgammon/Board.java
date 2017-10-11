package org.suliga.acme.model.backgammon;

import java.util.ArrayList;
import java.util.List;

public class Board {
	private static final int NUM_POINTS = 24;
	private Point[] points;
	private Bar bar;
	private Bear bear;
	private Turn currentTurn;
	
	public Board() {
	}
	
	public Board(boolean init) {
		if (init) {
			init();
		}
	}
	
	public Dice roll() {
		currentTurn = new Turn(PlayerSide.PLAYER_2);
		return currentTurn.roll();
	}
	
	public void init() {
		points = new Point[NUM_POINTS];
		
		for (int i=0;i<NUM_POINTS;i++) {
			points[i] = new Point(i);
		}
		
		// player pips
		int[][] a1 = { {0,2}, {11,5}, {16,3}, {18,5} };
		int[][] a2 = { {5,5}, {7,3}, {12,5}, {23,2} };
		for (int i=0;i<4;i++) {
			points[a1[i][0]].set(PlayerSide.PLAYER_1, a1[i][1]);
			points[a2[i][0]].set(PlayerSide.PLAYER_2, a2[i][1]);
		}
		
		bar = new Bar();
		bear = new Bear();
		
		// temp
		bar.setPlayer1Count(3);
		bar.setPlayer2Count(2);
		bear.setPlayer1Count(5);
		bear.setPlayer2Count(4);
	}
	
	public Point[] getPoints() {
		return points;
	}
	
	public Point getPoint(int index) {
		return points[index];
	}
	
	public Bar getBar() {
		return bar;
	}
	
	public Bear getBear() {
		return bear;
	}
	
	public List<Move> getLegalMoves() {
		List<Move> legalMoves = new ArrayList<>();
		int[] both = currentTurn.getDice().getBoth();
		PlayerSide ps = currentTurn.getPlayerSide();
		int addOrSubtract = 1;
		if (ps == PlayerSide.PLAYER_2) {
			addOrSubtract = -1;
		}
		
		for (int i=0;i<NUM_POINTS;i++) {
			Point pointFrom = points[i];
			if (pointFrom.isOwned(ps)) {
				for (int j=0;j<2;j++) {
					int indexTo = i + (both[j] * addOrSubtract);
					if (indexTo >= NUM_POINTS 
					 || indexTo < 0) {
						continue;
					}
					Point pointTo = points[indexTo];
					if (pointTo.isEmpty()
					 || pointTo.isOwned(ps)
					 || pointTo.getNumPips() == 1) {
						legalMoves.add(new Move(pointFrom, pointTo));
					}
				}
			}
		}
		
		return legalMoves;
	}
}


