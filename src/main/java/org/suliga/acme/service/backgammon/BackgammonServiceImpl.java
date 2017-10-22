package org.suliga.acme.service.backgammon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

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
		game.getCurrentTurn().pushMove(new Move(messageIn.getFromPoint(), messageIn.getToPoint(), false));
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
		messageOut.setBearOff(board.isBearOffAllowed());
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

		logger.info("bear 1 count = " + board.getBear().getPlayer1Count());
		logger.info("bear 2 count = " + board.getBear().getPlayer2Count());
		
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
		
		List<Move> legalMoves = board.getLegalMoves(dice);
		Optional<Move> bestMove = getBestComputerMove(board, legalMoves);
		if (bestMove.isPresent()) {
			bestMove.ifPresent(m -> { 
				messageOut.setFromPoint(m.getFromPoint());
				messageOut.setToPoint(m.getToPoint());
				board.movePip(m.getFromPoint(), m.getToPoint());
				game.getCurrentTurn().pushMove(new Move(m.getFromPoint(), m.getToPoint(), false));
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
	
	private Optional<Move> getBestComputerMove(Board board, List<Move> legalMoves) {
		Move bestMove = null;
		
		if (legalMoves.size() == 0) {
			// do nothing
		} else if (legalMoves.size() == 1) {
			bestMove = legalMoves.get(0);
		} else {
			Move coverOwnPip = null;
			Move hitOtherPip = null;
			Move safePip = null;
			Move singleToMultiplePip = null;
			Move bearOffMove = null;
			Move randomMove = legalMoves.get(ThreadLocalRandom.current().nextInt(legalMoves.size()));
			for (int i=0;i<legalMoves.size();i++) {
				Move m = legalMoves.get(i);
				if (m.isMultiDiceUsed()) {
					// currently only check best single move
					continue;
				}
				PlayerSide psFrom = board.getPlayerSideFromPoint(m.getFromPoint());
				PlayerSide psTo = board.getPlayerSideFromPoint(m.getToPoint());
				if (psFrom != psTo) {
					if (board.getNumPips(m.getToPoint()) == 1) {
						hitOtherPip = m;
					}
				} 
				if (psFrom == psTo) {
					if (board.getNumPips(m.getToPoint()) == 1) {
						coverOwnPip = m;
					}
					if (board.getNumPips(m.getFromPoint()) == 1 && board.getNumPips(m.getToPoint()) > 0) {
						singleToMultiplePip = m;
					}
				}
				if (board.getNumPips(m.getFromPoint()) > 2) {
					safePip = m;
				}
				if (m.getToPoint() == Move.TO_BEAR) {
					bearOffMove = m;
				}
			}
			if (coverOwnPip != null) {
				bestMove = coverOwnPip;
			} else if (hitOtherPip != null) {
				bestMove = hitOtherPip;
			} else if (singleToMultiplePip != null) {
				bestMove = singleToMultiplePip;
			} else if (safePip != null) {
				bestMove = safePip;
			} else if (bearOffMove != null) {
				bestMove = bearOffMove;
			} else {
				bestMove = randomMove;
			}
		}
		
		if (bestMove == null) {
			return Optional.empty();
		}
		return Optional.of(bestMove);
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



