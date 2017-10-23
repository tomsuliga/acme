package org.suliga.acme.model.backgammon;

import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BG_DICE")
public class Dice {
	@Id
	@GeneratedValue
	private long id;
	
	private int die1;
	private int die2;
	private boolean used1;
	private boolean used2;
	private boolean used3;
	private boolean used4;
	
	public Dice() {
		die1 = ThreadLocalRandom.current().nextInt(6) + 1;
		die2 = ThreadLocalRandom.current().nextInt(6) + 1;
	}
	
	public int getDie1() {
		return die1;
	}
	
	public int getDie2() {
		return die2;
	}
	
	public boolean isDouble() {
		return die1 == die2;
	}
	
	public boolean isUsed1() {
		return used1;
	}
	public boolean isUsed2() {
		return used2;
	}
	public boolean isUsed3() {
		return used3;
	}
	public boolean isUsed4() {
		return used4;
	}
	
	public void setUsed1() {
		used1 = true;
	}
	public void setUsed2() {
		used2 = true;
	}
	public void setUsed3() {
		used3 = true;
	}
	public void setUsed4() {
		used4 = true;
	}
	public void setUsedUsingIndex(int index) {
		switch (index) {
		case 0: used1 = true; break;
		case 1: used2 = true; break;
		case 2: used3 = true; break;
		case 3: used4 = true; break;
		}
	}
	
	@Override
	public String toString() {
		return "Dice " + die1 + ":" + die2;
	}
}



