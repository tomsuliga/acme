package org.suliga.acme.model.backgammon;

import java.util.concurrent.ThreadLocalRandom;

public class Dice {
	private int[] die;
	private boolean[] used;
	
	public Dice() {
		die = new int[2];
		used = new boolean[4];
		die[0] = ThreadLocalRandom.current().nextInt(6) + 1;
		die[1] = ThreadLocalRandom.current().nextInt(6) + 1;
	}
	
	public Dice(int d1, int d2) {
		die = new int[2];
		used = new boolean[4];
		die[0] = d1;
		die[1] = d2;
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
}
