package org.suliga.acme.model.dailydiet;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class DailyDiet {
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	@OneToMany(fetch=FetchType.EAGER)
	private List<FoodAggregate> foodAggregates;

	public DailyDiet() {}
	
	public DailyDiet(String name) {
		this.name = name;
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

	public List<FoodAggregate> getFoodAggregates() {
		return foodAggregates;
	}

	public void setFoodAggregates(List<FoodAggregate> foodAggregates) {
		this.foodAggregates = foodAggregates;
	}
}
