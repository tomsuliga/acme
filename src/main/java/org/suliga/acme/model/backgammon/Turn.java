package org.suliga.acme.model.backgammon;

public class Turn {
	private Dice dice; // 2 die
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
	
	public void setDice(Dice dice) {
		this.dice = dice;
	}
	
	public PlayerSide getPlayerSide() {
		return playerSide;
	}
}
