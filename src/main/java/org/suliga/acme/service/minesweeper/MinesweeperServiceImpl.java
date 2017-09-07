package org.suliga.acme.service.minesweeper;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.suliga.acme.model.minesweeper.GameColRow;
import org.suliga.acme.model.minesweeper.GameGrid;

@Service
public class MinesweeperServiceImpl implements MinesweeperService {
	private static final Logger logger = LoggerFactory.getLogger(MinesweeperServiceImpl.class);
	private static final Map<String, GameGrid> gameGrid = new HashMap<>();
	
	@Override
	public GameGrid getGameGrid(String sessionId) {
		if (!gameGrid.containsKey(sessionId)) {
			synchronized(this) {
				if (!gameGrid.containsKey(sessionId)) {
					gameGrid.put(sessionId, new GameGrid(15, 15));
				}
			}
		}
		return gameGrid.get(sessionId);
	}

	@Override
	public GameGrid newGame(String sessionId, int numCols, int numRows) {
		logger.debug("newGame called");
		gameGrid.put(sessionId,  new GameGrid(numCols, numRows));
		return gameGrid.get(sessionId);
	}

	@Override
	public void setCellVisible(GameColRow gameColRow) {
		//logger.info("setCellVisible: " + gameColRow);
		getGameGrid(gameColRow.getSessionId()).getCells()[gameColRow.getCol()][gameColRow.getRow()].setVisible(true);
	}

	@Override
	public void setPossibleMine(GameColRow gameColRow) {
		getGameGrid(gameColRow.getSessionId()).getCells()[gameColRow.getCol()][gameColRow.getRow()].setPossibleMine(gameColRow.isPossibleMine());
	}

	@Override
	public void clearGameGrid(String sessionId) {
		gameGrid.remove(sessionId);
	}
}
