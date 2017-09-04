package org.suliga.acme.dao.dailydiet;

import org.springframework.data.repository.CrudRepository;
import org.suliga.acme.model.dailydiet.DailyDiet;

public interface DailyDietDao extends CrudRepository<DailyDiet, Long> {
	DailyDiet findByName(String name);
}
