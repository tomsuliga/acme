package org.suliga.acme.model.backgammon;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BG_PLAYER")
public class Player {	
	@Id
	@GeneratedValue
	private long id;
	
	@Enumerated(EnumType.STRING)
	private TuringType turingType;
	
	private String name;
	
	public Player() {}
	
	public Player(String name, TuringType turingType) {
		this.name = name;
		this.turingType = turingType;
	}
	
	public String getName() {
		return name;
	}
	
	public TuringType getTuringType() {
		return turingType;
	}

	public void setTuringType(TuringType turingType) {
		this.turingType = turingType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
