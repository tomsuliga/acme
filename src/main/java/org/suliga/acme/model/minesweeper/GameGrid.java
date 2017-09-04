package org.suliga.acme.model.minesweeper;

import java.util.concurrent.ThreadLocalRandom;

public class GameGrid {
	public static String MINE_VALUE = "*";
	public static String EMPTY_VALUE = " ";
	private GameCell[][] cells;
	private int numCols;
	private int numRows;
	private int numMines;
	private String sessionId;
	
	public GameGrid() {}
	
	public GameGrid(int numCols, int numRows) {
		this.numCols = numCols;
		this.numRows = numRows;
		init();
	}
	private void init() {
		cells = new GameCell[numCols][numRows];
		
		for (int i=0;i<numCols;i++) {
			for (int j=0;j<numRows;j++) {
				cells[i][j] = new GameCell();
			}
		}
		ThreadLocalRandom random = ThreadLocalRandom.current();
		numMines = (int) Math.floor(numCols*numRows*0.09);
		int numPlaced = 0;
		while (numPlaced < numMines) {
			int col = random.nextInt(numCols);
			int row = random.nextInt(numRows);
			if (cells[col][row].getCellValue() == EMPTY_VALUE) {
				cells[col][row].setCellValue(MINE_VALUE);
				numPlaced++;
			}
		}
		for (int i=0;i<numCols;i++) {
			for (int j=0;j<numRows;j++) {
				if (cells[i][j].getCellValue() != MINE_VALUE) {
					int foundMines = 0;
					for (int deltaX = -1; deltaX < 2; deltaX++) {
						for (int deltaY = -1; deltaY < 2; deltaY++) {
							if ((deltaX == 0 && deltaY == 0)
							  || (i + deltaX < 0)
							  || (j + deltaY < 0)
							  || (i + deltaX >= numCols)
							  || (j + deltaY >= numRows))  {
								continue;
							}
							
							if (cells[i+deltaX][j+deltaY].getCellValue() == MINE_VALUE) {
								foundMines++;
							}
						}
					}
					if (foundMines > 0) {
						cells[i][j].setCellValue(Integer.toString(foundMines));
					}
				}
			}
		}
	}
	public GameCell[][] getCells() {
		return cells;
	}
	public void setCells(GameCell[][] cells) {
		this.cells = cells;
	}
	public int getNumCols() {
		return numCols;
	}
	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}
	public int getNumRows() {
		return numRows;
	}
	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}
	public int getNumMines() {
		return numMines;
	}
	public void setNumMines(int numMines) {
		this.numMines = numMines;
	}
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		return "GameGrid: sid=" + sessionId + ", col=" + numCols + ", rows=" + numRows;
	}
}
