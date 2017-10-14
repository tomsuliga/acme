package org.suliga.acme.service.backgammon;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.suliga.acme.model.backgammon.Game;

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
}
