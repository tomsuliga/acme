package org.suliga.acme.service.minesweeper;

import org.suliga.acme.model.minesweeper.GameColRow;
import org.suliga.acme.model.minesweeper.GameGrid;

public interface MinesweeperService {
	public GameGrid getGameGrid(String sessionId);
	public GameGrid newGame(String sessionId, int numCols, int numRows);
	public void setCellVisible(GameColRow gameColRow);
	public void setPossibleMine(GameColRow gameColRow);
	public void clearGameGrid(String sessionId);
}
