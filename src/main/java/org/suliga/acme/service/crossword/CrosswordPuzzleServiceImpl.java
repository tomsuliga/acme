package org.suliga.acme.service.crossword;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.suliga.acme.model.crossword.CrosswordGrid;

@Service
public class CrosswordPuzzleServiceImpl implements CrosswordPuzzleService {

	private Map<String, CrosswordGrid> crosswordGrids = new HashMap<>();
	
	@Override
	public CrosswordGrid getCrosswordGrid(String sessionId) {
		if (!crosswordGrids.containsKey(sessionId)) {
			crosswordGrids.put(sessionId,  new CrosswordGrid());
		}
		return crosswordGrids.get(sessionId);
	}
}
