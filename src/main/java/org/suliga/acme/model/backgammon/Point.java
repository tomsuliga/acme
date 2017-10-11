package org.suliga.acme.model.backgammon;

public class Point {
	private int index;
	private PlayerSide playerSide;
	private int numPips;
	
	public Point(int index) {
		this.index = index;
		
	}
	public void set(PlayerSide playerSide, int numPips) {
		this.playerSide = playerSide;
		this.numPips = numPips;
	}
	
	public PlayerSide getPlayerSide() {
		return playerSide;
	}
	
	public int getNumPips() {
		return numPips;
	}
	
	public boolean isEmpty() {
		return numPips == 0;
	}
	
	public boolean isOwned(PlayerSide playerSide) {
		return this.playerSide == playerSide;
	}
	
	public boolean isNotOwned(PlayerSide playerSide) {
		return this.playerSide != playerSide;
	}
	
	public boolean canHit(PlayerSide playerSide) {
		return this.playerSide != null && playerSide != this.playerSide && numPips == 1;
	}
	
	public int getIndex() {
		return index;
	}
}
