package org.suliga.acme.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.suliga.acme.model.backgammon.Bar;
import org.suliga.acme.model.backgammon.Board;
import org.suliga.acme.model.backgammon.Dice;
import org.suliga.acme.model.backgammon.Game;
import org.suliga.acme.model.backgammon.Move;
import org.suliga.acme.model.backgammon.PlayerSide;
import org.suliga.acme.model.backgammon.Point;
import org.suliga.acme.model.backgammon.ClientServerMessage;
import org.suliga.acme.model.crossword.CrosswordGrid;
import org.suliga.acme.model.dailydiet.FoodItem;
import org.suliga.acme.model.dailydiet.StompNumServings;
import org.suliga.acme.model.dailydiet.NutrientAggregate;
import org.suliga.acme.model.dailydiet.NutrientDisplaySummary;
import org.suliga.acme.model.dailydiet.StompFoodItemId;
import org.suliga.acme.model.minesweeper.GameColRow;
import org.suliga.acme.model.minesweeper.GameGrid;
import org.suliga.acme.model.primegen.PrimegenStart;
import org.suliga.acme.service.backgammon.BackgammonService;
import org.suliga.acme.service.crossword.CrosswordPuzzleService;
import org.suliga.acme.service.dailydiet.DailyDietService;
import org.suliga.acme.service.earthquakes.EarthquakesService;
import org.suliga.acme.service.javatest.JavaTestService;
import org.suliga.acme.service.mazegen.MazegenService;
import org.suliga.acme.service.minesweeper.MinesweeperService;
import org.suliga.acme.service.primegen.PrimeNumberService;
import org.suliga.acme.service.rss.RssService;

@Controller
public class AcmeMainController {
	private static final Logger logger = LoggerFactory.getLogger(AcmeMainController.class);

	@Autowired private MinesweeperService minesweeperService;
	@Autowired private DailyDietService dailyDietService;
	@Autowired private MazegenService mazegenService;
	@Autowired private EarthquakesService earthquakesService;
	@Autowired private PrimeNumberService primeNumberService;
	@Autowired private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired private CrosswordPuzzleService crosswordPuzzleService;
	@Autowired private RssService rssService;
	@Autowired private JavaTestService javaTestService;
	@Autowired private BackgammonService backgammonService;

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
	
	@GetMapping("/crossword")
	public String getCrossword(Model model, HttpServletRequest req) {
		String sessionId = req.getSession().getId();
		CrosswordGrid crosswordGrid = crosswordPuzzleService.getCrosswordGrid(sessionId);
		model.addAttribute("crosswordGrid", crosswordGrid);
		model.addAttribute("crosswordCells", crosswordGrid.getCrosswordCells());
		model.addAttribute("acrossClues", crosswordGrid.getAcrossClues());
		model.addAttribute("downClues", crosswordGrid.getDownClues());
		model.addAttribute("sessionId", sessionId);
		return "crossword";
	}
	
	@GetMapping("/rss")
	public String getRss(Model model) {
		model.addAttribute("articleText", rssService.getArticleSummaries());
		return "rss";
	}
	
	@GetMapping("/javatest")
	public String getJavaTest(Model model) {
		model.addAttribute("factorial", javaTestService.factorial(6));
		model.addAttribute("anagram", javaTestService.anagram("toms"));
		model.addAttribute("priorityQueue", javaTestService.priorityQueue(5,8,3,2,1,9));
		return "javatest";
	}
	
	//////////////////////////////////////////////////////////////////
	////   B A C K G A M M O N  //////////////////////////////////////
	//////////////////////////////////////////////////////////////////
	@GetMapping("/backgammon")
	public String getBackgammon(Model model, HttpServletRequest req) {
		String sessionId = req.getSession().getId();
		logger.info("<><><> sessionId: " + sessionId);
		model.addAttribute("sessionId", sessionId);
		model.addAttribute("savedGames", backgammonService.getAllGames());
		return "backgammon";
	}
	
	@MessageMapping("/backgammon/newGame")
	public void handleBackgammonNewGame(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		backgammonService.newGame(sessionId);
	}
	
	@MessageMapping("/backgammon/saveGame")
	public void handleBackgammonSaveGame(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		backgammonService.saveGame(sessionId);
	}
	
	@MessageMapping("/backgammon/startOfGameFirstRoll")
	public void handleBackgammonFirstMove(ClientServerMessage messageIn) {
		ClientServerMessage messageOut = backgammonService.startOfGameFirstRoll(messageIn);
		if (messageOut.getSide() == 1) {
			simpMessagingTemplate.convertAndSend("/topic/backgammon/showPipsAllowedToMove", messageOut);
		} else {
			simpMessagingTemplate.convertAndSend("/topic/backgammon/doComputerSide", messageOut);
		}
	}
	
	@MessageMapping("/backgammon/startPlayerTurn") // new player turn of many
	public void handleBackgammonStartPlayerTurn(ClientServerMessage messageIn) {
		ClientServerMessage messageOut = backgammonService.startPlayerTurn(messageIn);
		simpMessagingTemplate.convertAndSend("/topic/backgammon/showPipsAllowedToMove", messageOut);
	}
	
	@MessageMapping("/backgammon/continuePlayerTurn")
	public void handleBackgammonContinueTurn(ClientServerMessage messageIn) {
		ClientServerMessage messageOut = backgammonService.continuePlayerTurn(messageIn);
		simpMessagingTemplate.convertAndSend("/topic/backgammon/showPipsAllowedToMove", messageOut);
	}
	
	@MessageMapping("/backgammon/pipDeselected")
	public void handleBackgammonPipDeseleced(ClientServerMessage messageIn) {
		ClientServerMessage messageOut = backgammonService.pipDeselected(messageIn);
		simpMessagingTemplate.convertAndSend("/topic/backgammon/showPipsAllowedToMove", messageOut);
	}
	
	@MessageMapping("/backgammon/pipSelectedToMove")
	public void handleBackgammonPointClicked(ClientServerMessage messageIn) {
		ClientServerMessage messageOut = backgammonService.pipSelectedToMove(messageIn);
		simpMessagingTemplate.convertAndSend("/topic/backgammon/selectDestinationPoint", messageOut);
	}
	
	@MessageMapping("/backgammon/movePip")
	public void handleBackgammonMovePip(ClientServerMessage messageIn) {
		ClientServerMessage messageOut = backgammonService.movePip(messageIn);
		simpMessagingTemplate.convertAndSend("/topic/backgammon/movingPip", messageOut);		
	}
	
	@MessageMapping("/backgammon/switchToComputerSide")
	@SendTo("/topic/backgammon/doComputerSide")
	public ClientServerMessage handleBackgammonSwitchSides(ClientServerMessage messageIn) {
		ClientServerMessage messageOut = backgammonService.switchToComputerSide(messageIn);
		return messageOut;
	}
	
	@MessageMapping("/backgammon/continueComputerTurn")
	@SendTo("/topic/backgammon/doComputerSide")
	public ClientServerMessage handleBackgammonContinueComputerSide(ClientServerMessage messageIn) {
		ClientServerMessage messageOut = backgammonService.continueComputerTurn(messageIn);
		return messageOut;
	}	

	@MessageMapping("/backgammon/debugPoints")
	public void handleBackgammonDebugPoints(ClientServerMessage messageIn) {
		backgammonService.debugPoints(messageIn);
	}
	
	//simpMessagingTemplate.convertAndSendToUser(user, destination, payload);
}





