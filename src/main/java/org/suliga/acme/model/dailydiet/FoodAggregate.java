package org.suliga.acme.model.dailydiet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class FoodAggregate {
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne
	@JoinColumn(name="FK_FOOD_ITEM_ID")
	private FoodItem foodItem;
	
	public FoodAggregate() {}
	
	public FoodAggregate(FoodItem foodItem, int count) {
		this.foodItem = foodItem;
		this.count = count;
	}

	private int count;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FoodItem getFoodItem() {
		return foodItem;
	}

	public void setFoodItem(FoodItem foodItem) {
		this.foodItem = foodItem;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return "FoodAggregate: " + id + ", " + count + ", " + foodItem;
	}
}
