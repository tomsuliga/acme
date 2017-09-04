package org.suliga.acme.dao.dailydiet;

import org.springframework.data.repository.CrudRepository;
import org.suliga.acme.model.dailydiet.NutrientAggregate;

public interface NutrientAggregateDao extends CrudRepository<NutrientAggregate, Long> {

}
