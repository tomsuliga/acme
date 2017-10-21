package org.suliga.acme.service.backgammon;

import org.suliga.acme.model.backgammon.ClientServerMessage;
import org.suliga.acme.model.backgammon.Game;

public interface BackgammonService {
	Game getGame(String sessionId);
	ClientServerMessage movePip(ClientServerMessage messageIn);
	ClientServerMessage pipDeselected(ClientServerMessage messageIn);
	ClientServerMessage pipSelectedToMove(ClientServerMessage messageIn);
	ClientServerMessage continueComputerTurn(ClientServerMessage messageIn);
	ClientServerMessage switchToComputerSide(ClientServerMessage messageIn);
	ClientServerMessage continuePlayerTurn(ClientServerMessage messageIn);
	ClientServerMessage startPlayerTurn(ClientServerMessage messageIn);
	ClientServerMessage startOfGameFirstRoll(ClientServerMessage messageIn);

	void debugPoints(ClientServerMessage messageIn);
}

