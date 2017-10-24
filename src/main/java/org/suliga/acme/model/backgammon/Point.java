package org.suliga.acme.model.backgammon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Point {
	private static final Logger logger = LoggerFactory.getLogger(Point.class);

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
	
	public void pop() {
		numPips--;
		
		if (numPips == 0) {
			playerSide = PlayerSide.NONE_0;
		}
		
		if (numPips < 0) {
			logger.error("**** numPips should not be less than 0: " + numPips, new Throwable());
			numPips = 0;
		}
	}
	
	public void push() {
		numPips++;
	}
	
	public PlayerSide getPlayerSide() {
		return playerSide;
	}
	
	public void setPlayerSide(PlayerSide ps) {
		playerSide = ps;
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
	
	@Override
	public String toString() {
		return index + " " + numPips + ((playerSide == null || playerSide == PlayerSide.NONE_0) ? "" : ("  p" + playerSide.ordinal()));
	}
}
