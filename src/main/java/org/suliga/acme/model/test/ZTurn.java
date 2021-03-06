package org.suliga.acme.model.test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

@Entity
public class ZTurn {
	@Id
	@GeneratedValue
	private long id;

	@OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private ZDice dice;
	
	@JoinColumn(name="FK_ZTURN") // col is in ZMove table
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)
	@OrderBy("id")	
	private Set<ZMove> zmoves;
	
	public ZTurn() {
		zmoves = new LinkedHashSet<>();
	}
	
	public void addMove(ZMove move) {
		if (dice == null) {
			int die1 = ThreadLocalRandom.current().nextInt(6) + 1;
			int die2 = ThreadLocalRandom.current().nextInt(6) + 1;
			dice = new ZDice(die1, die2);
		}
		zmoves.add(move);
	}
	
	@Override
	public String toString() {
		return "Turn:" + id + " " + zmoves;
	}
}






