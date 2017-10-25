package org.suliga.acme.model.test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ZDice {
	@Id
	@GeneratedValue
	private long id;

	private int die1;
	
	private int die2;
	
	public ZDice() {}
	
	public ZDice(int die1, int die2) {
		this.die1 = die1;
		this.die2 = die2;
	}
}
