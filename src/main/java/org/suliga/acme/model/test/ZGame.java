package org.suliga.acme.model.test;

import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	@CreationTimestamp
	private Timestamp createdOn;  // Set automatically by Hibernate
	
	@Transient
	private ArrayDeque<ZTurn> turns = new ArrayDeque<>();
	
	public ZGame() {}
	
	public ZGame(ZPlayer player1, ZPlayer player2) {
		this.player1 = player1;
		this.player2 = player2;
	}
	
	@PrePersist
	public void prePersist() {
		zturns = new LinkedHashSet<>(64);
		turns.forEach(t -> zturns.add(t));
	}
	
	@PostLoad
	public void postLoad() {
		turns = new ArrayDeque<>();
		zturns.forEach(t -> turns.add(t));
	}

	public void addTurn(ZTurn turn) {
		turns.add(turn);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ZGame " + player1 + " vs " + player2 + ", " + createdOn + ", num turns = " + zturns.size() + "\n");
		turns.forEach(t -> sb.append(t.toString() + "\n"));
		return sb.toString();
	}
	
	public ArrayDeque<ZTurn> getTurns() {
		return turns;
	}
}
