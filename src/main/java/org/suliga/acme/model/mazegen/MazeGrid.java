package org.suliga.acme.model.mazegen;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MazeGrid {
	private static final Logger logger = LoggerFactory.getLogger(MazeGrid.class);
	
	private MazeCell[][] mazeCells;
	private int numCols;
	private int numRows;
	private List<Integer> numsToCheck;

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
		
		numsToCheck = new ArrayList<>();
		numsToCheck.add(0);
		numsToCheck.add(1);
		numsToCheck.add(2);
		numsToCheck.add(3);

		recursiveBacktrack(mazeCells[5][1]);

		mazeCells[3][0].setViewText("S"); 
		mazeCells[3][1].removeClass("north");
		
		mazeCells[28][21].setViewText("E"); 
		mazeCells[28][20].removeClass("south");

		verifyDoubleEdge();
		
		// temp
		//solve();
	}

	private void verifyDoubleEdge() {
		for (int col = 0; col < numCols; col++) {
			for (int row = 0; row < numRows; row++) {
				MazeCell cell = mazeCells[col][row];
				if (row > 0 && mazeCells[col][row - 1].getViewClass().contains("south")
						&& !cell.getViewClass().contains("north")) {
					cell.addClass("north");
				}
				if (row < 21 && mazeCells[col][row + 1].getViewClass().contains("north")
						&& !cell.getViewClass().contains("south")) {
					cell.addClass("south");
				}
				if (col > 0 && mazeCells[col - 1][row].getViewClass().contains("east")
						&& !cell.getViewClass().contains("west")) {
					cell.addClass("west");
				}
				if (col < 31 && mazeCells[col + 1][row].getViewClass().contains("west")
						&& !cell.getViewClass().contains("east")) {
					cell.addClass("east");
				}
			}
		}
	}

	private void recursiveBacktrack(MazeCell cell) {
		cell.setVisited(true);

		int col = cell.getCol();
		int row = cell.getRow();
		
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
						cell.removeClass("north");
						nextCell.removeClass("south");
					}
					break;
				case 1:
					if (!mazeCells[col][row + 1].isVisited()) {
						nextCell = mazeCells[col][row + 1];
						cell.removeClass("south");
						nextCell.removeClass("north");
					}
					break;
				case 2:
					if (!mazeCells[col - 1][row].isVisited()) {
						nextCell = mazeCells[col - 1][row];
						cell.removeClass("west");
						nextCell.removeClass("east");
					}
					break;
				case 3:
					if (!mazeCells[col + 1][row].isVisited()) {
						nextCell = mazeCells[col + 1][row];
						cell.removeClass("east");
						nextCell.removeClass("west");
					}
					break;
				}
				if (nextCell != null)
					break;
			}
			recursiveBacktrack(nextCell);
		}
	}
	
	public void solve() {
		// clean old solution
		for (int col = 0; col < numCols; col++) {
			for (int row = 0; row < numRows; row++) {
				MazeCell cell = mazeCells[col][row];
				if (cell.getViewClass().contains("path")) {
					cell.removeClass("path");
					cell.setViewText("");
					cell.setSolveVisited(false);
				}
			}
		}

		Deque<MazeCell> stack = new ArrayDeque<>();
		stack.push(mazeCells[3][1]);
		solve(stack);
				
		stack.forEach(e -> {
			e.addClass("path");
			e.setViewText("&#9642;");
		});
	}
	
	private void solve(Deque<MazeCell> stack) {
		// any moves left?
		MazeCell cell = stack.peek();
		cell.setSolveVisited(true);
		
		if (cell.getCol() == 28 && cell.getRow() == 20) {
			// end found
			return;
		}
		
		MazeCell nextCell = null;
		int col = cell.getCol();
		int row = cell.getRow();
		
 		// pick random direction
		Collections.shuffle(numsToCheck);
		boolean found = false;
		
		for (int i=0;i<4;i++) {
			switch (numsToCheck.get(i)) {
			case 0:
				if (!cell.getViewClass().contains("south") && !mazeCells[col][row+1].isSolveVisited()) {
					nextCell = mazeCells[col][row+1];
					stack.push(nextCell);
					found = true;
				}
				break;
			case 1:
				if (!cell.getViewClass().contains("north") && !mazeCells[col][row-1].isSolveVisited()) {
					nextCell = mazeCells[col][row-1];
					stack.push(nextCell);
					found = true;
				}				
				break;
			case 2:
				if (!cell.getViewClass().contains("east") && !mazeCells[col+1][row].isSolveVisited()) {
					nextCell = mazeCells[col+1][row];
					stack.push(nextCell);
					found = true;
				}
				break;
			case 3:
				if (!cell.getViewClass().contains("west") && !mazeCells[col-1][row].isSolveVisited()) {
					nextCell = mazeCells[col-1][row];
					stack.push(nextCell);
					found = true;
				}
				break;
			}
			if (found)
				break;
		}
		if (!found) {
			stack.pop();
		}

		solve(stack);
	}
}





