package org.suliga.acme.service.backgammon;

import org.suliga.acme.model.backgammon.Game;

public interface BackgammonService {
	Game getGame(String sessionId);
}

