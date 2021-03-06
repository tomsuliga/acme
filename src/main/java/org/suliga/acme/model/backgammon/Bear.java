package org.suliga.acme.model.backgammon;

public class Bear {
	private int player1Count;
	private int player2Count;
	
	public int getPlayer1Count() {
		return player1Count;
	}
	
	public void setPlayer1Count(int player1Count) {
		this.player1Count = player1Count;
	}
	
	public int getPlayer2Count() {
		return player2Count;
	}
	
	public void setPlayer2Count(int player2Count) {
		this.player2Count = player2Count;
	}
	
	public void player1Push() {
		player1Count++;
	}
	public void player2Push() {
		player2Count++;
	}
	
	public void playerPush(PlayerSide ps) {
		if (ps == PlayerSide.PLAYER_1) {
			player1Count++;
		} else {
			player2Count++;
		}
	}
}

