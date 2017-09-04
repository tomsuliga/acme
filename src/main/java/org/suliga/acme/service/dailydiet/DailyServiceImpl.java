package org.suliga.acme.service.dailydiet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.suliga.acme.dao.dailydiet.DailyDietDao;
import org.suliga.acme.dao.dailydiet.FoodItemDao;
import org.suliga.acme.dao.dailydiet.NutrientItemDao;
import org.suliga.acme.model.dailydiet.DailyDiet;
import org.suliga.acme.model.dailydiet.FoodAggregate;
import org.suliga.acme.model.dailydiet.FoodItem;
import org.suliga.acme.model.dailydiet.NutrientAggregate;
import org.suliga.acme.model.dailydiet.NutrientDisplaySummary;
import org.suliga.acme.model.dailydiet.NutrientItem;

@Service
public class DailyServiceImpl implements DailyDietService {
	private static final Logger logger = LoggerFactory.getLogger(DailyServiceImpl.class);
	
	private Map<String, FoodAggregate> foodItems = new HashMap<>();
	
	@Autowired
	private NutrientItemDao nutirentItemDao;
	
	@Autowired
	private FoodItemDao foodItemDao;
	
	@Autowired
	private DailyDietDao dailyDietDao;
	
	@Override
	public Iterable<FoodItem> findAllFoodItems() {
		return foodItemDao.findAll();
	}
	
	@Override
	public Iterable<NutrientItem> findAllNutrientItems() {
		return nutirentItemDao.findAll();
	}

	@Override
	public DailyDiet findDailyDiet(String name) {
		return dailyDietDao.findByName(name);
	}

	@Override
	public FoodItem getFoodItemById(String foodItemId) {
		Long id = Long.valueOf(foodItemId);
		return foodItemDao.findOne(id);
	}

	@Override
	public List<NutrientDisplaySummary> getNutrientDisplaySummary(String dailyDietName, FoodItem foodItem, int count) {
		// Need to loop through all previous selected FoodItems
		List<NutrientDisplaySummary> list = new ArrayList<>();
		foodItems.put(foodItem.getName(), new FoodAggregate(foodItem, count));
		Map<String,NutrientDisplaySummary> summaries = new HashMap<>();
		foodItems.forEach((String fiName, FoodAggregate fa) -> {
			//if (fa.getCount() > 0) {
				List<NutrientAggregate> nutrients = fa.getFoodItem().getNutrients();
				nutrients.forEach(na -> {
					//logger.info("display nut = " + a);
					double percent = 0;
					if (na.getNutrientItem().getRda() != 0) {
						percent = Math.round((na.getAmount()*fa.getCount()) / na.getNutrientItem().getRda()*100);
					}
					//logger.info("percent: " + percent);
					String id = "nutrientTotal-" + na.getNutrientItem().getId();
					NutrientDisplaySummary summary = summaries.get(id);
					if (summary == null) {
						summary = new NutrientDisplaySummary(id, na.getAmount()*fa.getCount(), "nutrientPercent-" + na.getNutrientItem().getId(), percent);
						summaries.put(id, summary);
					} else {
						double d1 = summary.getTotalAmount() + na.getAmount()*fa.getCount();
						summary.setTotalAmount(d1);
						if (na.getNutrientItem().getRda() != 0) {
							double p1 = Math.round(d1 / na.getNutrientItem().getRda()*100);
							summary.setPercentAmount(p1);
						}
					}
					//logger.info("summary: " + summary);
					list.add(summary);
				});
			//}
		});
		
		return list;
	}

	@Override
	public void clearAll() {
		foodItems = new HashMap<>();
	}
}




