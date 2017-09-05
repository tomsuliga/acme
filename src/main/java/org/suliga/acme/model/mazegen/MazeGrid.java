package org.suliga.acme.model.mazegen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MazeGrid {
	private static final Logger logger = LoggerFactory.getLogger(MazeGrid.class);
	
	private MazeCell[][] mazeCells;
	private int numCols;
	private int numRows;

	public MazeGrid() {
		this(32, 22);
	}

	public MazeGrid(int numCols, int numRows) {
		this.numCols = numCols;
		this.numRows = numRows;
		init();
	}

	public MazeCell[][] getMazeCells() {
		return mazeCells;
	}

	public void setMazeCells(MazeCell[][] mazeCells) {
		this.mazeCells = mazeCells;
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

	private void init() {
		mazeCells = new MazeCell[numCols][numRows];

		for (int col = 0; col < numCols; col++) {
			for (int row = 0; row < numRows; row++) {
				mazeCells[col][row] = new MazeCell(col, row);
			}
		}

		recursiveBacktrack(mazeCells[5][1]);

		mazeCells[3][0].setViewText("S"); mazeCells[3][1].setViewClass(mazeCells[3][1].getViewClass().replace("north", ""));
		mazeCells[28][21].setViewText("E"); mazeCells[28][20].setViewClass(mazeCells[28][20].getViewClass().replace("south", ""));

		verifyDoubleEdge();
	}

	private void verifyDoubleEdge() {
		for (int col = 0; col < numCols; col++) {
			for (int row = 0; row < numRows; row++) {
				MazeCell cell = mazeCells[col][row];
				if (row > 0 && mazeCells[col][row - 1].getViewClass().contains("south")
						&& !cell.getViewClass().contains("north")) {
					cell.setViewClass(cell.getViewClass() + " north");
				}
				if (row < 21 && mazeCells[col][row + 1].getViewClass().contains("north")
						&& !cell.getViewClass().contains("south")) {
					cell.setViewClass(cell.getViewClass() + " south");
				}
				if (col > 0 && mazeCells[col - 1][row].getViewClass().contains("east")
						&& !cell.getViewClass().contains("west")) {
					cell.setViewClass(cell.getViewClass() + " west");
				}
				if (col < 31 && mazeCells[col + 1][row].getViewClass().contains("west")
						&& !cell.getViewClass().contains("east")) {
					cell.setViewClass(cell.getViewClass() + " east");
				}
			}
		}
	}

	private void recursiveBacktrack(MazeCell cell) {
		cell.setVisited(true);

		int col = cell.getCol();
		int row = cell.getRow();
		
		List<Integer> numsToCheck = new ArrayList<>();
		numsToCheck.add(0);
		numsToCheck.add(1);
		numsToCheck.add(2);
		numsToCheck.add(3);

		// Do while not blocked
		while (!mazeCells[col - 1][row].isVisited() 
			|| !mazeCells[col + 1][row].isVisited()
		    || !mazeCells[col][row - 1].isVisited() 
		    || !mazeCells[col][row + 1].isVisited()) {

			MazeCell nextCell = null;
			// pick random direction
			Collections.shuffle(numsToCheck);
			
			for (int i=0;i<4;i++) {
				switch (numsToCheck.get(i)) {
				case 0:
					if (!mazeCells[col][row - 1].isVisited()) {
						nextCell = mazeCells[col][row - 1];
						cell.setViewClass(cell.getViewClass().replace("north", ""));
						nextCell.setViewClass(nextCell.getViewClass().replace("south", ""));
					}
					break;
				case 1:
					if (!mazeCells[col][row + 1].isVisited()) {
						nextCell = mazeCells[col][row + 1];
						cell.setViewClass(cell.getViewClass().replace("south", ""));
						nextCell.setViewClass(nextCell.getViewClass().replace("north", ""));
					}
					break;
				case 2:
					if (!mazeCells[col - 1][row].isVisited()) {
						nextCell = mazeCells[col - 1][row];
						cell.setViewClass(cell.getViewClass().replace("west", ""));
						nextCell.setViewClass(nextCell.getViewClass().replace("east", ""));
					}
					break;
				case 3:
					if (!mazeCells[col + 1][row].isVisited()) {
						nextCell = mazeCells[col + 1][row];
						cell.setViewClass(cell.getViewClass().replace("east", ""));
						nextCell.setViewClass(nextCell.getViewClass().replace("west", ""));
					}
					break;
				}
				if (nextCell != null)
					break;
			}
			recursiveBacktrack(nextCell);
		}
	}
}
