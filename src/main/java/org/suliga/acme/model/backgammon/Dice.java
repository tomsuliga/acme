package org.suliga.acme.model.backgammon;

import java.util.concurrent.ThreadLocalRandom;

public class Dice {
	private static int diceCount = 0;
	private int[] die;
	private boolean[] used;
	
	public Dice() {
		diceCount++;
		die = new int[2];
		used = new boolean[4];
		die[0] = ThreadLocalRandom.current().nextInt(6) + 1;
		die[1] = ThreadLocalRandom.current().nextInt(6) + 1;
	}
	
	public void setDieUsed(int num) {
		if (die[0] == num && !used[0]) {
			used[0] = true;
		} else if (die[1] == num && !used[1]) {
			used[1] = true;
		}
	}
	
	public int getDie(int d) {
		return die[d];
	}
	
	public boolean isDouble() {
		return die[0] == die[1];
	}
	
	public int[] get() {
		return die;
	}
	
	public boolean isUsed(int d) {
		return used[d];
	}
	
	public void setUsed(int d) {
		used[d] = true;
	}
	
	@Override
	public String toString() {
		return "Dice: " + diceCount + ", used:" + used[0] + " " + used[1] + " " + used[2] + " " + used[3];
	}
}
