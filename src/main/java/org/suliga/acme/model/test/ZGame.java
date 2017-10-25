package org.suliga.acme.model.test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;

@Entity
public class ZGame {
	private static final Logger logger = LoggerFactory.getLogger(ZGame.class);
	
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private ZPlayer player1;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private ZPlayer player2;
	
	@JoinColumn(name="FK_ZGAME") // col is in ZTurn table
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)
	@OrderBy("id")
	private Set<ZTurn> zturns;
	
	public ZGame() {
		zturns = new LinkedHashSet<>(64);
	}
	
	public ZGame(ZPlayer player1, ZPlayer player2) {
		this();
		this.player1 = player1;
		this.player2 = player2;
	}

	public void addTurn(ZTurn turn) {
		zturns.add(turn);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ZGame " + player1 + ", " + player2 + ", num turns = " + zturns.size() + "\n");
		zturns.forEach(t -> sb.append(t.toString() + "\n"));
		return sb.toString();
	}
	
	public LinkedHashSet<ZTurn> getTurns() {
		return (LinkedHashSet<ZTurn>) zturns;
	}
}
