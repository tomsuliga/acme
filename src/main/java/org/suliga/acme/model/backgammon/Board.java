package org.suliga.acme.model.backgammon;

import java.util.ArrayList;
import java.util.Arrays;
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
	private PlayerSide currentPlayerSide;
	
	public Board() {
	}
	
	public Board(boolean init) {
		if (init) {
			init();
		}
	}
	
	public Dice roll() {
		currentTurn = new Turn(currentPlayerSide);
		return currentTurn.roll();
	}
	
	
	public Turn getCurrentTurn() {
		return currentTurn;
	}
	
	public void switchSides() {
		currentPlayerSide = currentPlayerSide == PlayerSide.PLAYER_1 ? PlayerSide.PLAYER_2 : PlayerSide.PLAYER_1;
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
		currentPlayerSide = PlayerSide.PLAYER_1;
		currentTurn = null;
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
		legalMoves.forEach(m -> indexes.add(m.getFromPoint()));
		indexes.forEach(m -> logger.info("index=" + m));
		return indexes;
	}
	
	public Set<Integer> getPossibleMoveIndexes(int pointIndexFrom) {
		List<Move> legalMoves = getLegalMoves();
		Set<Integer> indexes = new HashSet<>();
		legalMoves.forEach(m -> { if (m.getFromPoint() == pointIndexFrom) indexes.add(m.getToPoint()); });
		return indexes;
	}
	
	public List<Move> getLegalMoves() {
		List<Move> legalMoves = new ArrayList<>();
		Dice dice = currentTurn.getDice();
		PlayerSide ps = currentTurn.getPlayerSide();
		int directionSign = ps == PlayerSide.PLAYER_1 ? 1 : -1;
		
		List<List<Integer>> nums = new ArrayList<>();
		if (!dice.isUsed(0)) {
			nums.add(Arrays.asList(dice.getDie(0)));
		}
		if (!dice.isUsed(1)) {
			nums.add(Arrays.asList(dice.getDie(1)));
		}
		if (!dice.isUsed(0) && !dice.isUsed(1)) {
			nums.add(Arrays.asList(dice.getDie(0), dice.getDie(1)));
			if (!dice.isDouble()) {
				nums.add(Arrays.asList(dice.getDie(1), dice.getDie(0)));
			}
		}
		if (dice.isDouble()) {
			// all 4 numbers are same
			if (dice.isUsed(0) && dice.isUsed(1) && (!dice.isUsed(2) || !dice.isUsed(3))) {
				nums.add(Arrays.asList(dice.getDie(0)));
			}
			if (!dice.isUsed(2) && !dice.isUsed(3)) {
				nums.add(Arrays.asList(dice.getDie(0), dice.getDie(0)));
			}
			if (!dice.isUsed(1) && !dice.isUsed(2) && !dice.isUsed(3)) {
				nums.add(Arrays.asList(dice.getDie(0), dice.getDie(0), dice.getDie(0)));
			}
			if (!dice.isUsed(0) && !dice.isUsed(1) && !dice.isUsed(2) && !dice.isUsed(3)) {
				nums.add(Arrays.asList(dice.getDie(0), dice.getDie(0), dice.getDie(0), dice.getDie(0)));
			}
		}
		
		for (int i=0;i<NUM_POINTS;i++) {
			Point pointFrom = points[i];
			if (pointFrom.isOwned(ps)) {
				for (int j=0;j<nums.size();j++) {
					// try all combo's from this point
					int total = 0;
					boolean allGood = true;
					List<Integer> seq = nums.get(j);
					for (int k=0;k<seq.size();k++) {
						int indexTo = i + ((total + seq.get(k) * directionSign));
						if (!isLegalPoint(pointFrom, indexTo, ps)) {
							allGood = false;
							break;
						} else {
							total += seq.get(k);
						}
					}
					if (allGood) {
						int indexTo = i + (total * directionSign);
						Move move = new Move(i, indexTo);
						if (isMoveUnique(legalMoves, move)) {
							legalMoves.add(move);
							logger.info("*** getLegalMoves added: " + move.toString());
						}
					}
				}
			}
		}
		
		return legalMoves;
	}
	
	private boolean isMoveUnique(List<Move> moves, Move move) {
		for (int i=0;i<moves.size();i++) {
			Move m = moves.get(i);
			if (m.getFromPoint() == move.getFromPoint()
			 && m.getToPoint() == move.getToPoint()) {
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



