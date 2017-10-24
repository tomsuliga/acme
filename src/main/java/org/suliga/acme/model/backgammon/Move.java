package org.suliga.acme.model.backgammon;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

@Entity
@Table(name="BG_MOVE")
public class Move {
	public static final int FROM_BAR = -99;
	public static final int TO_BEAR = 88;
	
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	private Turn turn;
	
	private int fromPoint;
	private int toPoint;
	
	@Transient
	private transient boolean multiDiceUsed;
	
	public Move() {}
	
	public Move(Turn turn, int fromPoint, int toPoint, boolean multiDiceUsed) {
		this.turn = turn;
		this.fromPoint = fromPoint;
		this.toPoint = toPoint;
		this.multiDiceUsed = multiDiceUsed;
		
		// temp ?
		if (this.toPoint > 23 || this.toPoint < 0) {
			this.toPoint = TO_BEAR;
		}
	}
	
	public int getFromPoint() {
		return fromPoint;
	}

	public int getToPoint() {
		return toPoint;
	}
	
	public boolean isMultiDiceUsed() {
		return multiDiceUsed;
	}

	@Override
	public String toString() {
		return "Move " + (fromPoint == FROM_BAR ? "Bar" : fromPoint) + ":" + (toPoint == TO_BEAR ? "Bear" : toPoint);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Turn getTurn() {
		return turn;
	}

	public void setTurn(Turn turn) {
		this.turn = turn;
	}

	public void setFromPoint(int fromPoint) {
		this.fromPoint = fromPoint;
	}

	public void setToPoint(int toPoint) {
		this.toPoint = toPoint;
	}

	public void setMultiDiceUsed(boolean multiDiceUsed) {
		this.multiDiceUsed = multiDiceUsed;
	}
}

