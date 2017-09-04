package org.suliga.acme.model.dailydiet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class NutrientItem {
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private double rda;
	
	private String units;
	
	private boolean selectable;

	public NutrientItem() {}
	
	public NutrientItem(String name, double rda, String units, boolean selectable) {
		this.name = name;
		this.rda = rda;
		this.units = units;
		this.selectable = selectable;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRda() {
		return rda;
	}

	public void setRda(double rda) {
		this.rda = rda;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}
	
	@Override
	public String toString() {
		return "NutrientItem: " + id + ", " + name;
	}
}







