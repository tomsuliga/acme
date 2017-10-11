package org.suliga.acme.model.backgammon;

public class Player {
	private TuringType turingType;
	private String name;
	
	public Player(String name, TuringType turingType) {
		this.name = name;
		this.turingType = turingType;
	}
	
	public String getName() {
		return name;
	}
	
	public TuringType getTuringType() {
		return turingType;
	}
}
