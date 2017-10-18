package org.suliga.acme.controller;

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
import org.suliga.acme.model.backgammon.Board;
import org.suliga.acme.model.backgammon.Dice;
import org.suliga.acme.model.backgammon.Game;
import org.suliga.acme.model.backgammon.Move;
import org.suliga.acme.model.backgammon.PlayerSide;
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
	
	@GetMapping("/backgammon")
	public String getBackgammon(Model model, HttpServletRequest req) {
		String sessionId = req.getSession().getId();
		model.addAttribute("sessionId", sessionId);
		return "backgammon";
	}
	
	//////////////////////////////////////////////////////////////////
	@MessageMapping("/backgammon/firstMove")
	public void handleBackgammonFirstMove(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		logger.info("incoming firstMove = " + sessionId);
		Board board = backgammonService.getGame(sessionId).getBoard();
		board.firstRoll();
		ClientServerMessage messageOut = new ClientServerMessage();
		messageOut.setFirstMove(true);
		messageOut.setStartTurn(true);
		
		if (board.getCurrentTurn().getPlayerSide() == PlayerSide.PLAYER_1) {
			playerTurnCommon(sessionId, board, messageOut);
		} else {
			computerTurnCommon(sessionId, board, messageOut);
		}
	}
	
	@MessageMapping("/backgammon/startPlayerTurn")
	public void handleBackgammonStartPlayerTurn(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		logger.info("startPlayerTurn: incoming startTurn = " + sessionId);
		Game game = backgammonService.getGame(sessionId);
		Board board = game.getBoard();
		logger.info("startPlayerTurn: previous current turn: " + board.getCurrentTurn().getPlayerSide().ordinal()); // s/b 2?
		game.getTurns().push(board.getCurrentTurn());
		board.roll(PlayerSide.PLAYER_1);
		ClientServerMessage messageOut = new ClientServerMessage();
		playerTurnCommon(sessionId, board, messageOut);
	}
	
	@MessageMapping("/backgammon/continueTurn")
	public void handleBackgammonContinueTurn(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		logger.info("incoming continueTurn = " + sessionId);
		Board board = backgammonService.getGame(sessionId).getBoard();
		ClientServerMessage messageOut = new ClientServerMessage();
		playerTurnCommon(sessionId, board, messageOut);
	}

	private void playerTurnCommon(String sessionId, Board board, ClientServerMessage messageOut) {
		messageOut.setSessionId(sessionId);
		messageOut.setSide(board.getCurrentTurn().getPlayerSide().ordinal());
		Dice dice = board.getCurrentTurn().getDice();
		messageOut.setDiceRolledEx(dice);
		messageOut.setMoveablePointsEx(board.getPossibleSelectIndexes());
		messageOut.setBar1Count(board.getBar().getPlayer1Count());
		messageOut.setBar2Count(board.getBar().getPlayer2Count());
		simpMessagingTemplate.convertAndSend("/topic/backgammon/showPipsAllowedToMove", messageOut);
	}

	@MessageMapping("/backgammon/pipSelectedToMove")
	public void handleBackgammonPointClicked(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		Board board = backgammonService.getGame(sessionId).getBoard();
		Dice dice = board.getCurrentTurn().getDice();
		ClientServerMessage messageOut = new ClientServerMessage();
		messageOut.setSessionId(sessionId);
		messageOut.setSide(board.getCurrentTurn().getPlayerSide().ordinal());
		messageOut.setDiceRolledEx(dice);
		messageOut.setSelectedPoint(messageIn.getSelectedPoint());
		messageOut.setMoveablePointsEx(board.getPossibleMoveIndexes(messageIn.getSelectedPoint()));
		messageOut.setBar1Count(board.getBar().getPlayer1Count());
		messageOut.setBar2Count(board.getBar().getPlayer2Count());
		simpMessagingTemplate.convertAndSend("/topic/backgammon/selectDestinationPoint", messageOut);
	}
	
	@MessageMapping("/backgammon/pipDeselected")
	public void handleBackgammonPipDeseleced(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		logger.info("incoming=" + sessionId);
		Board board = backgammonService.getGame(sessionId).getBoard();
		Dice dice = board.getCurrentTurn().getDice();
		ClientServerMessage messageOut = new ClientServerMessage();
		messageOut.setSessionId(sessionId);
		messageOut.setSide(board.getCurrentTurn().getPlayerSide().ordinal());
		messageOut.setDiceRolledEx(dice);
		messageOut.setMoveablePointsEx(board.getPossibleSelectIndexes());
		messageOut.setBar1Count(board.getBar().getPlayer1Count());
		messageOut.setBar2Count(board.getBar().getPlayer2Count());
		simpMessagingTemplate.convertAndSend("/topic/backgammon/showPipsAllowedToMove", messageOut);
	}
	
	@MessageMapping("/backgammon/movePip")
	public void handleBackgammonMovePip(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		Board board = backgammonService.getGame(sessionId).getBoard();
		Dice dice = board.getCurrentTurn().getDice();
		//dice.setDieUsed(Math.abs(messageIn.getFromPoint() - messageIn.getToPoint()));
		ClientServerMessage messageOut = new ClientServerMessage();
		messageOut.setSessionId(sessionId);
		messageOut.setSide(board.getCurrentTurn().getPlayerSide().ordinal());
		messageOut.setDiceRolledEx(dice);
		messageOut.setFromPoint(messageIn.getFromPoint());
		messageOut.setToPoint(messageIn.getToPoint());
		board.movePip(messageIn.getFromPoint(), messageIn.getToPoint());
		messageOut.setBarHop(board.isBarHop());
		messageOut.setBar1Count(board.getBar().getPlayer1Count());
		messageOut.setBar2Count(board.getBar().getPlayer2Count());
		messageOut.calculateNumbersUsed(dice);
		simpMessagingTemplate.convertAndSend("/topic/backgammon/movingPip", messageOut);
	}
	
	@MessageMapping("/backgammon/newGame")
	public void handleBackgammonNewGame(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		backgammonService.getGame(sessionId).init();
		backgammonService.getGame(sessionId).getBoard().init();
	}
	
	@MessageMapping("/backgammon/switchToComputerSide")
	public void handleBackgammonSwitchSides(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		Game game = backgammonService.getGame(sessionId);
		Board board = game.getBoard();
		game.getTurns().push(board.getCurrentTurn());
		board.roll(PlayerSide.PLAYER_2);
		ClientServerMessage messageOut = new ClientServerMessage();
		messageOut.setStartTurn(true);
		computerTurnCommon(sessionId, board, messageOut);
	}
	
	@MessageMapping("/backgammon/continueComputerSide")
	public void handleBackgammonContinueComputerSide(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		Game game = backgammonService.getGame(sessionId);
		Board board = game.getBoard();
		ClientServerMessage messageOut = new ClientServerMessage();
		computerTurnCommon(sessionId, board, messageOut);
	}
	
	public void computerTurnCommon(String sessionId, Board board, ClientServerMessage messageOut) {
		messageOut.setSessionId(sessionId);
		messageOut.setSide(board.getCurrentTurn().getPlayerSide().ordinal());
		Dice dice = board.getCurrentTurn().getDice();
		messageOut.setDiceRolledEx(dice);
		messageOut.setMoveablePointsEx(board.getPossibleSelectIndexes());
		
		// temp - random logic
		List<Move> legalMoves = board.getLegalMoves();
		Optional<Move> any = legalMoves.stream().findAny();
		if (any.isPresent()) {
			any.ifPresent(m -> { 
				messageOut.setFromPoint(m.getFromPoint());
				messageOut.setToPoint(m.getToPoint());
				board.movePip(messageOut.getFromPoint(), messageOut.getToPoint());
			});
		} else {
			messageOut.setNoMove(true);
		}

		messageOut.setBarHop(board.isBarHop());
		messageOut.setBar1Count(board.getBar().getPlayer1Count());
		messageOut.setBar2Count(board.getBar().getPlayer2Count());
		messageOut.calculateNumbersUsed(dice);		
		simpMessagingTemplate.convertAndSend("/topic/backgammon/doComputerSide", messageOut);
	}
}



