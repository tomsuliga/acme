package org.suliga.acme.model.mazegen;

import java.util.concurrent.ThreadLocalRandom;

public class MazeCell {
	private boolean upWall;
	private boolean downWall;
	private boolean rightWall;
	private boolean leftWall;
	private String viewText = "";
	private String viewClass = "";
	private int col;
	private int row;
	private boolean visited;
	
	public MazeCell(int col, int row) {
		this.col = col;
		this.row = row;
		
		// temp
		initViewClass();
	}
	public boolean isUpWall() {
		return upWall;
	}
	public void setUpWall(boolean upWall) {
		this.upWall = upWall;
	}
	public boolean isDownWall() {
		return downWall;
	}
	public void setDownWall(boolean downWall) {
		this.downWall = downWall;
	}
	public boolean isRightWall() {
		return rightWall;
	}
	public void setRightWall(boolean rightWall) {
		this.rightWall = rightWall;
	}
	public boolean isLeftWall() {
		return leftWall;
	}
	public void setLeftWall(boolean leftWall) {
		this.leftWall = leftWall;
	}
	public boolean isVisited() {
		return visited;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	
	// temp ?
	private void initViewClass() {
		if (col == 0 || row == 0 || col == 31 || row == 21) {
			viewClass = "edge";
			visited = true;
			return;
		}
		
		viewClass = "north south east west";
/*		
		ThreadLocalRandom r = ThreadLocalRandom.current(); 
		switch (r.nextInt(18)) {
		case 0: viewClass = "north"; break;
		case 1: viewClass = "north south"; break;
		case 2: viewClass = "north south east"; break;
		case 3: viewClass = "north south east west"; break;
		case 4: viewClass = "north east"; break;
		case 5: viewClass = "north east west"; break;
		case 6: viewClass = "north west"; break;
		case 7: viewClass = "north south west"; break;
		case 8: viewClass = "south"; break;
		case 9: viewClass = "south east"; break;
		case 10: viewClass = "south west"; break;
		case 11: viewClass = "south east west"; break;
		case 12: viewClass = "east"; break;
		case 13: viewClass = "east west"; break;
		case 14: viewClass = "west"; break;
		default: viewClass = "none";
		}
		if (row == 1 && !viewClass.contains("north")) {
			viewClass += " north";
		}
		if (row == 20 && !viewClass.contains("south")) {
			viewClass += " south";
		}
		if (col == 1 && !viewClass.contains("west")) {
			viewClass += " west";
		}
		if (col == 30 && !viewClass.contains("east")) {
			viewClass += " east";
		}
		viewClass = "north south east west";
*/		
	}
	
	public String getViewClass() {
		return viewClass;
	}
	
	public void setViewClass(String viewClass) {
		this.viewClass = viewClass;
	}
	
	public String getViewText() {
		return viewText;
	}
	
	public void setViewText(String viewText) {
		this.viewText = viewText;
	}
	
	@Override
	public String toString() {
		return "MazeCell: " + col + "," + row + " " + viewClass;
	}
}






