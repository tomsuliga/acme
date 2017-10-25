package org.suliga.acme.model.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class ZTurn {
	@Id
	@GeneratedValue
	private long id;

	@OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private ZDice dice;
	
	@JoinColumn(name="FK_ZTURN") // col is in ZMove table
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<ZMove> zmoves;
	
	public ZTurn() {
		zmoves = new ArrayList<>();
	}
	
	public void addMove(ZMove move) {
		if (dice == null) {
			int die1 = ThreadLocalRandom.current().nextInt(6) + 1;
			int die2 = ThreadLocalRandom.current().nextInt(6) + 1;
			dice = new ZDice(die1, die2);
		}
		zmoves.add(move);
	}
}
