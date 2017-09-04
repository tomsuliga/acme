package org.suliga.acme.model.dailydiet;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class FoodItem {
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	@OneToMany(fetch=FetchType.EAGER)
	private List<NutrientAggregate> nutrients;

	public FoodItem() {}
	
	public FoodItem(String name, List<NutrientAggregate> nutrients) {
		this.name = name;
		this.nutrients = nutrients;
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

	public List<NutrientAggregate> getNutrients() {
		return nutrients;
	}

	public void setNutrients(List<NutrientAggregate> nutrients) {
		this.nutrients = nutrients;
	}
	
	public void addNutrientAggregate(NutrientAggregate e) {
		if (nutrients == null) {
			nutrients = new ArrayList<>();
		}
		nutrients.add(e);
	}
	
	@Override
	public String toString() {
		return "FoodItem: " + id + ", " + name + ", nutrients=" + nutrients;
	}
}
