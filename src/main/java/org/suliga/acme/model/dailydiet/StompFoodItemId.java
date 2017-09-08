package org.suliga.acme.model.dailydiet;

public class StompFoodItemId {
	private String foodItemId;

	public String getFoodItemId() {
		return foodItemId.substring("foodItem-".length());
	}

	public void setFoodItemId(String foodItemId) {
		this.foodItemId = foodItemId;
	}
}
