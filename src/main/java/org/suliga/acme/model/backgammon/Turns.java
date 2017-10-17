package org.suliga.acme.model.backgammon;

import java.util.ArrayList;
import java.util.List;

public class Turns {
	private List<Turn> turns;
	
	public void init() {
		turns = new ArrayList<>();
		
		// temp ?
		turns.add(new Turn(PlayerSide.PLAYER_1));
	}
	
	public void push(Turn turn) {
		turns.add(turn);
	}
	
	public Turn pop() {
		if (turns.size() > 0) {
			return turns.remove(turns.size()-1);
		}
		return null;
	}
	
	public Turn next() {
		return null;
	}
	
	public Turn prev() {
		return null;
	}
	
	public Turn current() {
		return turns.get(turns.size()-1);
	}
	
	public int getNumTurns() {
		return turns.size();
	}
}
