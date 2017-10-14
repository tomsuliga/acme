package org.suliga.acme.model.backgammon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Board {
	private static final Logger logger = LoggerFactory.getLogger(Board.class);

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
		currentTurn = new Turn(PlayerSide.PLAYER_1);
		return currentTurn.roll();
	}
	
	
	public Turn getCurrentTurn() {
		return currentTurn;
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
	
	public void movePip(int pipFrom, int pipTo) {
		points[pipFrom].pop(); // also clears playerSide if 0
		points[pipTo].push();
		points[pipTo].setPlayerSide(points[pipFrom].getPlayerSide());
	}
	
	public Set<Integer> getPossibleSelectIndexes() {
		List<Move> legalMoves = getLegalMoves();
		Set<Integer> indexes = new HashSet<>();
		legalMoves.forEach(m -> indexes.add(m.getFromPoint().getIndex()));
		indexes.forEach(m -> logger.info("index=" + m));
		return indexes;
	}
	
	public Set<Integer> getPossibleMoveIndexes(int pointIndexFrom) {
		List<Move> legalMoves = getLegalMoves();
		Set<Integer> indexes = new HashSet<>();
		legalMoves.forEach(m -> { if (m.getFromPoint().getIndex() == pointIndexFrom) indexes.add(m.getToPoint().getIndex()); });
		return indexes;
	}
	
	public List<Move> getLegalMoves() {
		List<Move> legalMoves = new ArrayList<>();
		int[] both = currentTurn.getDice().get();
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
					if (isLegalMove(pointFrom, indexTo)) {
						Point pointTo = points[indexTo];
						Move move = new Move(pointFrom, pointTo);
						if (isMoveUnique(legalMoves, move)) {
							legalMoves.add(move);
						}
						// try combination of moves
						int otherDiceIndex = (j + 1) % 2;
						int otherIndexTo = indexTo + (both[otherDiceIndex] * addOrSubtract);
						// pointTo is now the pointFrom
						if (isLegalMove(pointTo, otherIndexTo)) {
							Point otherPointTo2 = points[otherIndexTo];
							// original pointFrom and combo of dice 1 and 2
							Move otherMove2 = new Move(pointFrom, otherPointTo2);
							if (isMoveUnique(legalMoves, otherMove2)) {
								legalMoves.add(otherMove2);
							}
							// doubles require 2 more checks
							if (currentTurn.getDice().isDouble()) {
								// The 3rd number
								Point otherPointTo3 = tryNextMove(otherPointTo2, both, addOrSubtract, pointFrom, legalMoves);
								if (otherPointTo3 != null) {
									// Again for 4th number
									tryNextMove(otherPointTo3, both, addOrSubtract, pointFrom, legalMoves);
								}
							}
						}
					}
				}
			}
		}
		
		return legalMoves;
	}
	
	private Point tryNextMove(Point otherPointFrom3, int[] both, int addOrSubtract, Point pointFrom, List<Move> legalMoves) {
		int otherIndexTo3 = otherPointFrom3.getIndex() + (both[0] * addOrSubtract);
		if (isLegalMove(otherPointFrom3, otherIndexTo3)) {
			Point otherPointTo3 = points[otherIndexTo3];
			Move otherMove3 = new Move(pointFrom, otherPointTo3);
			if (isMoveUnique(legalMoves, otherMove3)) {
				legalMoves.add(otherMove3);
			}
			return otherPointTo3;
		}
		return null;
	}
	
	private boolean isMoveUnique(List<Move> moves, Move move) {
		for (int i=0;i<moves.size();i++) {
			Move m = moves.get(i);
			if (m.getFromPoint().getIndex() == move.getFromPoint().getIndex()
			 && m.getToPoint().getIndex() == move.getToPoint().getIndex()) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isLegalMove(Point pointFrom, int indexTo) {
		if (indexTo >= NUM_POINTS 
		 || indexTo < 0) {
			return false;
		}
		
		Point pointTo = points[indexTo];
		
		if (pointTo.isEmpty()
		 || pointTo.isOwned(currentTurn.getPlayerSide())
		 || pointTo.getNumPips() == 1) {
			return true;
		}
		
		return false;
	}
}



