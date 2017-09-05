package org.suliga.acme.model.dailydiet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suliga.acme.dao.dailydiet.DailyDietDao;
import org.suliga.acme.dao.dailydiet.FoodItemDao;
import org.suliga.acme.dao.dailydiet.NutrientAggregateDao;
import org.suliga.acme.dao.dailydiet.NutrientItemDao;

@Component
@Transactional
public class DatabaseLoad {
	private static final Logger logger = LoggerFactory.getLogger(DatabaseLoad.class);

	@Autowired
	private FoodItemDao foodItemDao;

	@Autowired
	private DailyDietDao dailyDietDao;

	@Autowired
	private NutrientItemDao nutrientItemDao;

	@Autowired
	private NutrientAggregateDao nutrientAggregateDao;

	@PostConstruct
	public void init() {
		logger.info("************ DatabaseLoad init **********");
		DailyDiet dailyDiet = new DailyDiet("temp-1234");
		dailyDietDao.save(dailyDiet);

		try (Stream<String> lines = Files.lines(Paths.get(ClassLoader.getSystemResource("data-load.txt").toURI()));) {
			lines.forEach(a -> {
				// logger.info(a);
				String[] words = a.split(" ");
				switch (words[0]) {
				case "NutrientItem:":
					NutrientItem nutrientItem = new NutrientItem(words[1], Double.parseDouble(words[2]), words[3],
							words[4].equalsIgnoreCase("true") ? true : false);
					nutrientItemDao.save(nutrientItem);
					break;
				case "FoodItem:":
					FoodItem foodItem = new FoodItem(words[1].replace('_', ' '), null);
					foodItemDao.save(foodItem);
					break;
				case "FoodNutrient:":
					FoodItem fi = foodItemDao.findByName(words[1].replace('_', ' '));
					// logger.info("fi=" + fi);
					NutrientItem ni = nutrientItemDao.findByName(words[2]);
					// logger.info("ni=" + ni);
					String amountStr = words[3];
					double amount = 0;
					if (amountStr.endsWith("%")) {
						amountStr = amountStr.substring(0, amountStr.length() - 1);
						int amountInt = Integer.parseInt(amountStr);
						if (ni.getRda() > 0) {
							amount = amountInt * ni.getRda() / 100.0;
						}
					} else {
						amount = Double.parseDouble(amountStr);
					}
					NutrientAggregate nutrientAggregate = new NutrientAggregate(ni, amount);
					nutrientAggregateDao.save(nutrientAggregate);
					fi.addNutrientAggregate(nutrientAggregate);
					foodItemDao.save(fi);
					break;
				default:
					// logger.info("Unknown keyword = " + words[0]);
				}
			});
			//lines.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Calculate Calories
		Iterable<FoodItem> foodItems = foodItemDao.findAll();
		foodItems.forEach(fi -> {
			List<NutrientAggregate> na = fi.getNutrients();
			double[] cals = new double[1];
			na.forEach(naTemp -> {
				switch (naTemp.getNutrientItem().getName()) {
				case "Fat":
					cals[0] += naTemp.getAmount() * 9;
					break;
				case "Protein":
				case "Carbs":
					cals[0] += naTemp.getAmount() * 4;
					break;
				}
			});
			NutrientItem niCals = nutrientItemDao.findByName("Calories");
			NutrientAggregate naCals = new NutrientAggregate(niCals, cals[0]);
			nutrientAggregateDao.save(naCals);
			fi.addNutrientAggregate(naCals);
			foodItemDao.save(fi);
		});
	}
}










