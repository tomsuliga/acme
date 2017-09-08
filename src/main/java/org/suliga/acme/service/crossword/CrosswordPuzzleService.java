package org.suliga.acme.service.crossword;

import org.suliga.acme.model.crossword.CrosswordGrid;

public interface CrosswordPuzzleService {
	CrosswordGrid getCrosswordGrid(String sessionId);
}
