package org.suliga.acme.service.backgammon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.suliga.acme.model.backgammon.Bar;
import org.suliga.acme.model.backgammon.Board;
import org.suliga.acme.model.backgammon.ClientServerMessage;
import org.suliga.acme.model.backgammon.Dice;
import org.suliga.acme.model.backgammon.Game;
import org.suliga.acme.model.backgammon.Move;
import org.suliga.acme.model.backgammon.PlayerSide;
import org.suliga.acme.model.backgammon.Point;

@Service
public class BackgammonServiceImpl implements BackgammonService {
	private static final Logger logger = LoggerFactory.getLogger(BackgammonServiceImpl.class);

	private Map<String, Game> games;
	
	public BackgammonServiceImpl() {
		logger.info("BackgammonServiceImpl Constructor called");
		games = new HashMap<>();
	}
	
	@Override
	public Game getGame(String sessionId) {
		if (!games.containsKey(sessionId)) {
			synchronized(BackgammonServiceImpl.class) {
				if (!games.containsKey(sessionId)) {
					boolean init = true;
					games.put(sessionId, new Game(init));
					logger.info("Added sessionId to new Game: " + sessionId);
				}
			}
		}
		return games.get(sessionId);
	}

	@Override
	public ClientServerMessage movePip(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		Game game = getGame(sessionId);
		Board board = game.getBoard();
		Dice dice = game.getDice();
		ClientServerMessage messageOut = new ClientServerMessage();
		messageOut.setSessionId(sessionId);
		messageOut.setSide(game.getCurrentTurn().getPlayerSide().ordinal());
		messageOut.setDiceRolledEx(dice);
		messageOut.setFromPoint(messageIn.getFromPoint());
		messageOut.setToPoint(messageIn.getToPoint());
		board.movePip(messageIn.getFromPoint(), messageIn.getToPoint());
		game.getCurrentTurn().pushMove(new Move(messageIn.getFromPoint(), messageIn.getToPoint()));
		messageOut.setBarHop(board.isBarHop());
		messageOut.setBar1Count(board.getBar().getPlayer1Count());
		messageOut.setBar2Count(board.getBar().getPlayer2Count());
		messageOut.calculateNumbersUsed(dice, board.isBarOff());
		messageOut.setBarOff(board.isBarOff());
		logger.info("num turns = " + game.getNumTurns());
		if (game.getNumTurns() == 1) {
			messageOut.setFirstMove(true);
		}
		return messageOut;
	}

	@Override
	public ClientServerMessage pipDeselected(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		logger.info("incoming=" + sessionId);
		Game game = getGame(sessionId);
		Board board = game.getBoard();
		Dice dice = game.getDice();
		ClientServerMessage messageOut = new ClientServerMessage();
		messageOut.setSessionId(sessionId);
		messageOut.setSide(game.getCurrentTurn().getPlayerSide().ordinal());
		messageOut.setDiceRolledEx(dice);
		messageOut.setMoveablePointsEx(board.getPossibleSelectIndexes(dice));
		messageOut.setBar1Count(board.getBar().getPlayer1Count());
		messageOut.setBar2Count(board.getBar().getPlayer2Count());
		return messageOut;
	}

	@Override
	public ClientServerMessage pipSelectedToMove(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		Game game = getGame(sessionId);
		Dice dice = game.getDice();
		Board board = game.getBoard();
		ClientServerMessage messageOut = new ClientServerMessage();
		messageOut.setSessionId(sessionId);
		messageOut.setSide(game.getCurrentTurn().getPlayerSide().ordinal());
		messageOut.setDiceRolledEx(dice);
		messageOut.setSelectedPoint(messageIn.getSelectedPoint());
		messageOut.setMoveablePointsEx(board.getPossibleMoveIndexes(dice, messageIn.getSelectedPoint()));
		messageOut.setBar1Count(board.getBar().getPlayer1Count());
		messageOut.setBar2Count(board.getBar().getPlayer2Count());
		messageOut.setBarOff(board.isBarOff());
		return messageOut;
	}

	@Override
	public void debugPoints(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		Game game = getGame(sessionId);
		Board board = game.getBoard();
		Point[] points = board.getPoints();
		for (int i=0;i<points.length;i++) {
			Point point = points[i];
			logger.info("point[" + i + "] = " + point.toString());
		}
		Bar bar = board.getBar();
		logger.info("bar 1 count = " + bar.getPlayer1Count());
		logger.info("bar 2 count = " + bar.getPlayer2Count());
		game.debugMoves();
	}
	
	private ClientServerMessage playerTurnCommon(String sessionId, Game game, ClientServerMessage messageOut) {
		messageOut.setSessionId(sessionId);
		messageOut.setSide(game.getCurrentTurn().getPlayerSide().ordinal());
		Dice dice = game.getDice();
		Board board = game.getBoard();
		messageOut.setDiceRolledEx(dice);
		messageOut.setMoveablePointsEx(board.getPossibleSelectIndexes(dice));
		messageOut.setBar1Count(board.getBar().getPlayer1Count());
		messageOut.setBar2Count(board.getBar().getPlayer2Count());
		messageOut.setBarOff(board.isBarOff());
		
		return messageOut;
	}
	
	public ClientServerMessage computerTurnCommon(String sessionId, Game game, ClientServerMessage messageOut) {
		messageOut.setSessionId(sessionId);
		messageOut.setSide(game.getCurrentTurn().getPlayerSide().ordinal());
		Dice dice = game.getDice();
		Board board = game.getBoard();
		messageOut.setDiceRolledEx(dice);
		messageOut.setMoveablePointsEx(board.getPossibleSelectIndexes(dice));
		
		// temp - random logic
		List<Move> legalMoves = board.getLegalMoves(dice);
		Optional<Move> any = legalMoves.stream().findAny();
		if (any.isPresent()) {
			any.ifPresent(m -> { 
				messageOut.setFromPoint(m.getFromPoint());
				messageOut.setToPoint(m.getToPoint());
				board.movePip(m.getFromPoint(), m.getToPoint());
				game.getCurrentTurn().pushMove(new Move(m.getFromPoint(), m.getToPoint()));
				messageOut.calculateNumbersUsed(dice, board.isBarOff());
			});
		} else {
			messageOut.setNoMove(true);
		}

		messageOut.setBarHop(board.isBarHop());
		messageOut.setBar1Count(board.getBar().getPlayer1Count());
		messageOut.setBar2Count(board.getBar().getPlayer2Count());
		messageOut.setBarOff(board.isBarOff());

		return messageOut;
	}

	@Override
	public ClientServerMessage continueComputerTurn(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		Game game = getGame(sessionId);
		ClientServerMessage messageOut = new ClientServerMessage();
		return computerTurnCommon(sessionId, game, messageOut);
	}

	@Override
	public ClientServerMessage switchToComputerSide(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		Game game = getGame(sessionId);
		game.roll();
		ClientServerMessage messageOut = new ClientServerMessage();
		messageOut.setStartTurn(true);
		return computerTurnCommon(sessionId, game, messageOut);		
	}

	@Override
	public ClientServerMessage continuePlayerTurn(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		Game game = getGame(sessionId);
		ClientServerMessage messageOut = new ClientServerMessage();
		return playerTurnCommon(sessionId, game, messageOut);
	}

	@Override
	public ClientServerMessage startPlayerTurn(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		Game game = getGame(sessionId);
		game.roll(); // roll dice, create turn, add turn to list
		ClientServerMessage messageOut = new ClientServerMessage();
		messageOut.setStartTurn(true);
		return playerTurnCommon(sessionId, game, messageOut);
	}

	@Override
	public ClientServerMessage startOfGameFirstRoll(ClientServerMessage messageIn) {
		String sessionId = messageIn.getSessionId();
		logger.info("startOfGameFirstRoll = " + sessionId);
		Game game = getGame(sessionId);
		game.startOfGameFirstRoll(); // roll dice, create turn, add turn to list
		ClientServerMessage messageOut = new ClientServerMessage();
		messageOut.setFirstMove(true);
		messageOut.setStartTurn(true);
		if (game.getCurrentTurn().getPlayerSide() == PlayerSide.PLAYER_1) {
			return playerTurnCommon(sessionId, game, messageOut);
		} else {
			return computerTurnCommon(sessionId, game, messageOut);
		}
	}
}



