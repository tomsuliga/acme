package org.suliga.acme.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.suliga.acme.model.dailydiet.FoodItem;
import org.suliga.acme.model.dailydiet.StompNumServings;
import org.suliga.acme.model.dailydiet.NutrientAggregate;
import org.suliga.acme.model.dailydiet.NutrientDisplaySummary;
import org.suliga.acme.model.dailydiet.StompFoodItemId;
import org.suliga.acme.model.minesweeper.GameColRow;
import org.suliga.acme.model.minesweeper.GameGrid;
import org.suliga.acme.model.primegen.PrimegenStart;
import org.suliga.acme.service.dailydiet.DailyDietService;
import org.suliga.acme.service.earthquakes.EarthquakesService;
import org.suliga.acme.service.mazegen.MazegenService;
import org.suliga.acme.service.minesweeper.MinesweeperService;
import org.suliga.acme.service.primegen.PrimeNumberService;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class AcmeMainController {
	private static final Logger logger = LoggerFactory.getLogger(AcmeMainController.class);

	@Autowired private MinesweeperService minesweeperService;
	@Autowired private DailyDietService dailyDietService;
	@Autowired private MazegenService mazegenService;
	@Autowired private EarthquakesService earthquakesService;
	@Autowired private PrimeNumberService primeNumberService;
	@Autowired private SimpMessagingTemplate simpMessagingTemplate;

	@GetMapping({"/", "/index", "/home"})
	public String getIndex(Model model) {
		return "index";
	}
	
	@GetMapping("/minesweeper")
	public String getMinesweeper(Model model, HttpServletRequest req, String newGame) {
		String sessionId = req.getSession().getId();
		if (newGame != null && newGame.equalsIgnoreCase("y")) {
			minesweeperService.clearGameGrid(sessionId);
			return "redirect:minesweeper";
		}
		GameGrid gameGrid = minesweeperService.getGameGrid(sessionId);
		model.addAttribute("sessionId", sessionId);
		model.addAttribute("gameGrid", gameGrid);
		model.addAttribute("cells", gameGrid.getCells());
		return "minesweeper";
	}
	
	@MessageMapping("/minesweeper/newGame")
	@SendTo("/topic/result")
	public GameGrid handleMinesweeperNewGame(GameGrid incoming) {
		//logger.info("Received stomp msg: " + incoming);
		return minesweeperService.newGame(incoming.getSessionId(), incoming.getNumCols(), incoming.getNumRows());
	}
	
	@MessageMapping("/minesweeper/cellVisible")
	public void handleMinesweeperCellVisible(GameColRow gameColRow) {
		minesweeperService.setCellVisible(gameColRow);
	}
	
	@MessageMapping("/minesweeper/possibleMine")
	public void handleMinesweeperPossibleMine(GameColRow gameColRow) {
		minesweeperService.setPossibleMine(gameColRow);
	}
	
	@GetMapping("/dailydiet")
	public String getDailyDiet(Model model) {
		model.addAttribute("nutrientItems", dailyDietService.findAllNutrientItems());
		model.addAttribute("foodItems", dailyDietService.findAllFoodItems());
		model.addAttribute("dailyDiet", dailyDietService.findDailyDiet("temp-1234"));
		dailyDietService.clearAll();
		return "dailydiet";
	}
	
	@MessageMapping("/dailydiet/getOneServingNutrients")
	@SendTo("/topic/result/getOneServingNutrients")
	public List<NutrientAggregate> handleDailyDiet(StompFoodItemId stompFoodItemId) {
		FoodItem foodItem = dailyDietService.getFoodItemById(stompFoodItemId.getFoodItemId());
		return foodItem.getNutrients();
	}
	
	@MessageMapping("/dailydiet/numServingsChanged")
	@SendTo("/topic/result/nutrientDisplaySummary")
	public List<NutrientDisplaySummary> handleDailyDietNumServingsChanged(StompNumServings numServings) {
		FoodItem foodItem = dailyDietService.getFoodItemById(numServings.getNumServingsId());
		int count = numServings.getCount();
		if (!numServings.isSelected()) {
			count = 0;
		}
		return dailyDietService.getNutrientDisplaySummary(numServings.getDailyDietName(), foodItem, count);
	}
	
	@GetMapping("/mazegen")
	public String getMazeGen(Model model, HttpServletRequest req) {
		String sessionId = req.getSession().getId();
		model.addAttribute("sessionId", sessionId);
		model.addAttribute("mazeGrid", mazegenService.getMazeGrid());
		model.addAttribute("mazeCells", mazegenService.getMazeGrid().getMazeCells());
		return "mazegen";
	}
	
	@MessageMapping("/mazegen/generate")
	@SendTo("/topic/result/mazegendisplay")
	public void handleMazegenGenerate(String incoming) {
		mazegenService.generateNewMaze();
	}
	
	@MessageMapping("/mazegen/solve")
	@SendTo("/topic/result/mazegendisplay")
	public void handleMazegenSolve(String incoming) {
		mazegenService.solveMaze();
	}
	
	@GetMapping("/earthquakes")
	public String getEarthquakes(Model model) {
		model.addAttribute("rawJson", earthquakesService.getRawJson());
		model.addAttribute("formattedJson", earthquakesService.getFormattedJson());
		model.addAttribute("earthquakeFeatures", earthquakesService.getEarthquakeFeatures());
		return "earthquakes";
	}

	@GetMapping("/primegen")
	public String getPrimeGen(Model model) {
		return "primegen";
	}
	
	@MessageMapping("/primegen/start")
	public void handlePrimegenStart(PrimegenStart primegenStart) {
		logger.info("primegenStart=" + primegenStart);
		primeNumberService.generatePrimeNumber(primegenStart.getNumBits(), primegenStart.getNumThreads(), simpMessagingTemplate);
	}
	
	@MessageMapping("/primegen/stop")
	public void handlePrimegenStop() {
		primeNumberService.stopPreviousThreads();
	}
}


