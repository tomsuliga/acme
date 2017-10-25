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
	
	private int from_point;
	
	private int to_point;

	public ZMove() {}
	
	public ZMove(int from_point, int to_point) {
		this.from_point = from_point;
		this.to_point = to_point;
	}
}
