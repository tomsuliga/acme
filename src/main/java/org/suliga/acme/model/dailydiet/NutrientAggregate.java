package org.suliga.acme.model.dailydiet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class NutrientAggregate {
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne
	@JoinColumn(name="FK_NUTRIENT_ITEM_ID")
	private NutrientItem nutrientItem;
	
	private double amount;

	public NutrientAggregate() {}
	
	public NutrientAggregate(NutrientItem nutrientItem, double amount) {
		this.nutrientItem = nutrientItem;
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NutrientItem getNutrientItem() {
		return nutrientItem;
	}

	public void setNutrientItem(NutrientItem nutrientItem) {
		this.nutrientItem = nutrientItem;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return "NutrientAggregate: " + nutrientItem + ", amount: " + amount;
	}
}
