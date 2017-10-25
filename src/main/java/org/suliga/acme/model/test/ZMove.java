package org.suliga.acme.model.test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="zmove")
public class ZMove {
	@Id
	@GeneratedValue
	private long id;
	
	private int fromPoint;
	
	private int toPoint;

	public ZMove() {}
	
	public ZMove(int fromPoint, int toPoint) {
		this.fromPoint = fromPoint;
		this.toPoint = toPoint;
	}
	
	@Override
	public String toString() {
		return "Move[" + fromPoint + ":" + toPoint + "]";
	}
}
