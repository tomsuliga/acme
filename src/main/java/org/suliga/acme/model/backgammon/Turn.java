package org.suliga.acme.model.backgammon;

public class Turn {
	private Dice dice; // 2 die
	private Move move; // 0 to 4 moves
	private PlayerSide playerSide;
	
	public Turn(PlayerSide playerSide) {
		this.playerSide = playerSide;
	}
	
	public Dice roll() {
		dice = new Dice();
		return dice;
	}
	
	public Dice getDice() {
		return dice;
	}
	
	public PlayerSide getPlayerSide() {
		return playerSide;
	}
}
