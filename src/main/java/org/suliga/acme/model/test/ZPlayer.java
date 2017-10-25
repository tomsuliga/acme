package org.suliga.acme.model.test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ZPlayer {
	@Id
	@GeneratedValue
	private long id;

	private String name;
	
	public ZPlayer() {}
	
	public ZPlayer(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
