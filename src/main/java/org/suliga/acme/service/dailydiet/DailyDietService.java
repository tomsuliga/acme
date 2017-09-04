package org.suliga.acme.service.dailydiet;

import java.util.List;

import org.suliga.acme.model.dailydiet.DailyDiet;
import org.suliga.acme.model.dailydiet.FoodItem;
import org.suliga.acme.model.dailydiet.NutrientDisplaySummary;
import org.suliga.acme.model.dailydiet.NutrientItem;

public interface DailyDietService {
	Iterable<NutrientItem> findAllNutrientItems();
	Iterable<FoodItem> findAllFoodItems();
	DailyDiet findDailyDiet(String name);
	FoodItem getFoodItemById(String foodItemId);
	List<NutrientDisplaySummary> getNutrientDisplaySummary(String dailyDietName, FoodItem foodItem, int count);
	void clearAll();
}
