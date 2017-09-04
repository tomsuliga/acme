package org.suliga.acme.dao.dailydiet;

import org.springframework.data.repository.CrudRepository;
import org.suliga.acme.model.dailydiet.NutrientItem;

public interface NutrientItemDao extends CrudRepository<NutrientItem, Long> {
	public NutrientItem findByName(String name);
}
