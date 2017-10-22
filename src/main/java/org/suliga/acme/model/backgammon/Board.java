package org.suliga.acme.model.backgammon;

import static org.mockito.Matchers.booleanThat;

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
	private boolean barHop;
	private boolean barOff;
	private PlayerSide currentPlayerSide;
	
	public Board() {
		init();
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
		barHop = false;
		bear = new Bear();		
	}	
	
	public void setCurrentPlayerSide(PlayerSide currentPlayerSide) {
		this.currentPlayerSide = currentPlayerSide;
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
	
	public PlayerSide getPlayerSideFromPoint(int point) {
		if (point == Move.FROM_BAR) {
			return currentPlayerSide;
		}

		if (point == Move.TO_BEAR) {
			return currentPlayerSide;
		}

		return points[point].getPlayerSide();
	}
	
	public int getNumPips(int point) {
		if (point == Move.FROM_BAR) {
			return 1;
		}
		
		if (point == Move.TO_BEAR) {
			return 0;
		}
		
		return points[point].getNumPips();
	}
	
	public void movePip(int pipFrom, int pipTo) {
		if (barOff) {
			bar.pop(currentPlayerSide);
		} else {
			points[pipFrom].pop(); // also clears playerSide if 0
		}
		
		PlayerSide toPs = getPlayerSideFromPoint(pipTo);
		
		if (pipTo == Move.TO_BEAR) {
			bear.playerPush(toPs);
			return;
		}
		
		if (currentPlayerSide != toPs && toPs != PlayerSide.NONE_0 && toPs != null) {
			points[pipTo].pop();
			if (toPs == PlayerSide.PLAYER_1) {
				bar.player1Push();
			} else {
				bar.player2Push();
			}
			barHop = true;
			//logger.info("**** A - setting barHop to true");
		} else {
			barHop = false;
		}
		points[pipTo].push();
		points[pipTo].setPlayerSide(currentPlayerSide);
	}
	
	public Set<Integer> getPossibleSelectIndexes(Dice dice) {
		List<Move> legalMoves = getLegalMoves(dice);
		Set<Integer> indexes = new HashSet<>();
		legalMoves.forEach(m -> indexes.add(m.getFromPoint()));
		indexes.forEach(m -> logger.info("index=" + m));
		return indexes;
	}
	
	public Set<Integer> getPossibleMoveIndexes(Dice dice, int pointIndexFrom) {
		List<Move> legalMoves = getLegalMoves(dice);
		Set<Integer> indexes = new HashSet<>();
		legalMoves.forEach(m -> { if (m.getFromPoint() == pointIndexFrom) indexes.add(m.getToPoint()); });
		return indexes;
	}
	
	public List<Move> getLegalMoves(Dice dice) {
		List<Move> legalMoves = new ArrayList<>();
		int directionSign = currentPlayerSide == PlayerSide.PLAYER_1 ? 1 : -1;
		
		List<List<Integer>> diceNumsSeq = new ArrayList<>();
		
		if (!dice.isUsed(0)) {
			diceNumsSeq.add(Arrays.asList(dice.getDie(0)));
		}
		if (!dice.isUsed(1)) {
			diceNumsSeq.add(Arrays.asList(dice.getDie(1)));
		}
		if (!dice.isUsed(0) && !dice.isUsed(1)) {
			diceNumsSeq.add(Arrays.asList(dice.getDie(0), dice.getDie(1)));
			if (!dice.isDouble()) {
				diceNumsSeq.add(Arrays.asList(dice.getDie(1), dice.getDie(0)));
			}
		}
		if (dice.isDouble()) {
			// all 4 numbers are same
			if (dice.isUsed(0) && dice.isUsed(1) && (!dice.isUsed(2) || !dice.isUsed(3))) {
				diceNumsSeq.add(Arrays.asList(dice.getDie(0)));
			}
			if (!dice.isUsed(2) && !dice.isUsed(3)) {
				diceNumsSeq.add(Arrays.asList(dice.getDie(0), dice.getDie(0)));
			}
			if (!dice.isUsed(1) && !dice.isUsed(2) && !dice.isUsed(3)) {
				diceNumsSeq.add(Arrays.asList(dice.getDie(0), dice.getDie(0), dice.getDie(0)));
			}
			if (!dice.isUsed(0) && !dice.isUsed(1) && !dice.isUsed(2) && !dice.isUsed(3)) {
				diceNumsSeq.add(Arrays.asList(dice.getDie(0), dice.getDie(0), dice.getDie(0), dice.getDie(0)));
			}
		}
		
		int barCount = bar.getPlayerCount(currentPlayerSide);
		
		if (barCount > 0) {
			// where can pip go?
			// only the first seq num matters
			// check all in case first 3 of double are already used
			// die - 1 === pointTo index
			for (int i=0;i<diceNumsSeq.size();i++) {
				List<Integer> seq = diceNumsSeq.get(i);
				int indexTo = -1;
				if (currentPlayerSide == PlayerSide.PLAYER_1) {
					indexTo = seq.get(0) - 1;
				} else {
					indexTo = 24 - seq.get(0);
				}
				if (isLegalPoint(indexTo, currentPlayerSide)) {
					Move move = new Move(Move.FROM_BAR, indexTo, false);
					barOff = true;
					if (isMoveUnique(legalMoves, move)) {
						legalMoves.add(move);
						//logger.info("*** getLegalMoves added bar move: " + move.toString());
					}
				}
			}
		} else {
			barOff = false;
			boolean bearOffAllowed = isBearOffAllowed();
			
			for (int i=0;i<NUM_POINTS;i++) {
				Point pointFrom = points[i];
				if (pointFrom.isOwned(currentPlayerSide)) {
					int countDiceUsed = 0;
					for (int j=0;j<diceNumsSeq.size();j++) {
						// try all combo's from this point
						int total = 0;
						boolean allGood = true;
						List<Integer> seq = diceNumsSeq.get(j);
						int indexTo = 0;
						for (int k=0;k<seq.size();k++) {
							indexTo = i + ((total + seq.get(k)) * directionSign);
							countDiceUsed++;
							if (!isLegalPoint(indexTo, currentPlayerSide)) {
								allGood = false;
								break;
							} else if (bearOffAllowed) {
								// only use first move if bear off
								break;
							} else {
								total += seq.get(k);
							}
						}
						if (allGood) {
							Move move = new Move(i, indexTo, countDiceUsed > 1);
							if (isMoveUnique(legalMoves, move)) {
								legalMoves.add(move);
								logger.info("*** getLegalMoves added: " + move.toString());
							}
						}
					}
				}
			}
		}
		
		return legalMoves;
	}
	
	public boolean isBearOffAllowed() {
		if (currentPlayerSide == PlayerSide.PLAYER_1) {
			for (int i=0;i<=17;i++) {
				if (points[i].isOwned(PlayerSide.PLAYER_1)) {
					return false;
				}
			}
			return true;
		} else {
			for (int i=23;i>=6;i--) {
				if (points[i].isOwned(PlayerSide.PLAYER_2)) {
					return false;
				}
			}
			return true;
		}
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
	
	private boolean isLegalPoint(int indexTo, PlayerSide ps) {
		if ((indexTo > 23 && isBearOffAllowed())
		 || (indexTo < 0 && isBearOffAllowed())) {
			return true;
		}
		
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

	public boolean isBarHop() {
		return barHop;
	}

	public void setBarHop(boolean barHop) {
		//logger.info("**** B - setting barHop to " + barHop);
		this.barHop = barHop;
	}

	public boolean isBarOff() {
		return barOff;
	}

	public void setBarOff(boolean barOff) {
		this.barOff = barOff;
	}
}



