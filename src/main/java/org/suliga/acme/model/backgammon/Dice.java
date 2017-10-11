package org.suliga.acme.model.backgammon;

import java.util.concurrent.ThreadLocalRandom;

public class Dice {
	private int first;
	private int second;
	
	public Dice() {
		first = ThreadLocalRandom.current().nextInt(6) + 1;
		second = ThreadLocalRandom.current().nextInt(6) + 1;
	}
	
	public Dice(int first, int second) {
		this.first = first;
		this.second = second;
	}
	
	public int getFirst() {
		return first;
	}
	
	public int getSecond() {
		return second;
	}
	
	public boolean isDouble() {
		return first == second;
	}
	
	public int[] getBoth() {
		int[] both = new int[2];
		both[0] = first;
		both[1] = second;
		return both;
	}
}
