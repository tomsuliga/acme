package org.suliga.acme.model.backgammon;

public class Game {
	private Player player1;
	private Player player2;
	private Turns turns;
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
		boolean init = true;
		board = new Board(init);
		turns = new Turns();
		turns.init();
		gameOver = false;
	}
	
	public Turns getTurns() {
		return turns;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public Board getBoard() {
		return board;
	}
}
