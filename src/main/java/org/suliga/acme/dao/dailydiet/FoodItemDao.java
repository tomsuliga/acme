package org.suliga.acme.dao.dailydiet;

import org.springframework.data.repository.CrudRepository;
import org.suliga.acme.model.dailydiet.FoodItem;

public interface FoodItemDao extends CrudRepository<FoodItem, Long> {
	public FoodItem findByName(String name);
}
