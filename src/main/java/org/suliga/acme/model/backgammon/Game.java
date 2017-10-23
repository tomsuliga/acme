package org.suliga.acme.model.backgammon;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Game {
	private static final Logger logger = LoggerFactory.getLogger(Game.class);

	private Player player1;
	private Player player2;
	private List<Turn> turns;
	private Turn currentTurn;
	private Board board;
	private boolean gameOver;
	
	public Game() {
	}
	
	public Game(boolean init) {
		if (init) {
			init();
		}
	}
	
	public void init() {
		board = new Board();
		turns = new ArrayList<>();
		gameOver = false;
		currentTurn = null;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public int getNumTurns() {
		return turns.size();
	}
	
	public void debugMoves() {
		turns.forEach(t -> logger.info(t.toString()));
	}
	
	public Turn getCurrentTurn() {
		return currentTurn;
	}
	
	public Dice roll() {
		// automatically switch sides with each dice roll
		PlayerSide ps = null;
		if (currentTurn.getPlayerSide() == PlayerSide.PLAYER_1) {
			ps = PlayerSide.PLAYER_2;
		} else {
			ps = PlayerSide.PLAYER_1;
		}
		currentTurn = new Turn(ps, new Dice());
		board.setCurrentPlayerSide(ps);
		turns.add(currentTurn);
		return currentTurn.getDice();
	}
	
	// Only done one time per game
	public Dice startOfGameFirstRoll() {
		Dice dice = new Dice();
		while (dice.isDouble()) {
			dice = new Dice();
		}
		if (dice.getDie(0) > dice.getDie(1)) {
			currentTurn = new Turn(PlayerSide.PLAYER_1, dice);
		} else {
			currentTurn = new Turn(PlayerSide.PLAYER_2, dice);
		}
		board.setCurrentPlayerSide(currentTurn.getPlayerSide());
		turns.add(currentTurn);
		return dice;
	}
	
	public Dice getDice() {
		return getCurrentTurn().getDice();
	}
}


