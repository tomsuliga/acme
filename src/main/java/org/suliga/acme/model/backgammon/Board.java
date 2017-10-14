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
		PlayerSide ps = points[pipFrom].getPlayerSide();
		points[pipFrom].pop(); // also clears playerSide if 0
		points[pipTo].push();
		points[pipTo].setPlayerSide(ps);
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
		Dice dice = currentTurn.getDice();
		PlayerSide ps = currentTurn.getPlayerSide();
		int temp = 1;
		if (ps == PlayerSide.PLAYER_2) {
			temp = -1;
		}
		int addOrSubtract = temp;
		
		Set<Integer> diceNums = new HashSet<>();
		if (!dice.isUsed(0)) diceNums.add(dice.getDie(0));
		if (!dice.isUsed(1)) diceNums.add(dice.getDie(1));
		if (!dice.isUsed(0) && !dice.isUsed(1)) diceNums.add(dice.getDie(0) + dice.getDie(1));
		if (dice.isDouble()) {
			int count = 0;
			for (int i=0;i<4;i++) {
				if (!dice.isUsed(i)) {
					count++;
				}
			}
			int value = dice.getDie(0);
			int max = value * count;
			for (int i=max;i>0;i-=value) {
				diceNums.add(i);
			}
		}
		// diceNums is now all possible die combinations
		for (int i=0;i<NUM_POINTS;i++) {
			Point pointFrom = points[i];
			if (pointFrom.isOwned(ps)) {
				// try all nums
				int index = i;
				diceNums.forEach(a -> {
					int indexTo = index + (a * addOrSubtract);
					if (isLegalPoint(pointFrom, indexTo, ps)) {
						Point pointTo = points[indexTo];
						Move move = new Move(pointFrom, pointTo);
						if (isMoveUnique(legalMoves, move)) {
							legalMoves.add(move);
							logger.info("getLegalMoves added: " + move.toString());
						}
					}
				});
			}
		}
		return legalMoves;
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
	
	private boolean isLegalPoint(Point pointFrom, int indexTo, PlayerSide ps) {
		if (indexTo >= NUM_POINTS 
		 || indexTo < 0) {
			return false;
		}
		
		Point pointTo = points[indexTo];
		
		if (pointTo.isEmpty()
		 || pointTo.isOwned(ps)
		 || pointTo.getNumPips() == 1) {
			return true;
		}
		
		return false;
	}
}



