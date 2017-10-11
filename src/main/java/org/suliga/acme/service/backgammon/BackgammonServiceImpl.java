package org.suliga.acme.service.backgammon;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.suliga.acme.model.backgammon.Game;

@Service
public class BackgammonServiceImpl implements BackgammonService {
	private Map<String, Game> games;
	
	public BackgammonServiceImpl() {
		games = new HashMap<>();
	}
	
	@Override
	public Game getGame(String sessionId) {
		if (!games.containsKey(sessionId)) {
			synchronized(BackgammonServiceImpl.class) {
				if (!games.containsKey(sessionId)) {
					boolean init = true;
					games.put(sessionId, new Game(init));
				}
			}
		}
		return games.get(sessionId);
	}
}
