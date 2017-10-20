package org.suliga.acme.model.backgammon;

import java.util.ArrayList;
import java.util.List;

public class Turn {
	private PlayerSide playerSide;
	private Dice dice;
	private List<Move> moves;
	
	public Turn(PlayerSide playerSide, Dice dice) {
		this.playerSide = playerSide;
		this.dice = dice;
		this.moves = new ArrayList<>();
	}

	public PlayerSide getPlayerSide() {
		return playerSide;
	}

	public void setPlayerSide(PlayerSide playerSide) {
		this.playerSide = playerSide;
	}

	public Dice getDice() {
		return dice;
	}

	public void pushMove(Move move) {
		moves.add(move);
	}
	
	@Override
	public String toString() {
		return "Turn for " + playerSide + " " + dice.toString() + " " + moves;
	}
}
