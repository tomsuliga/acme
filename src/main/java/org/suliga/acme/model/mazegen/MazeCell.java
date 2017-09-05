package org.suliga.acme.model.mazegen;

public class MazeCell {
	private String viewText = "";
	private String viewClass = "";
	private int col;
	private int row;
	private boolean visited;
	private boolean solveVisited;
	
	public MazeCell(int col, int row) {
		this.col = col;
		this.row = row;
		initViewClass();
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
	public boolean isSolveVisited() {
		return solveVisited;
	}
	public void setSolveVisited(boolean solveVisited) {
		this.solveVisited = solveVisited;
	}
	
	private void initViewClass() {
		if (col == 0 || row == 0 || col == 31 || row == 21) {
			viewClass = "edge";
			visited = true;
			solveVisited = true;
			return;
		}
		
		viewClass = "north south east west";
	}
	
	public String getViewClass() {
		return viewClass;
	}
	
	public void setViewClass(String viewClass) {
		this.viewClass = viewClass;
	}
	
	public void removeClass(String classToRemove) {
		this.viewClass = this.viewClass.replaceAll(classToRemove, "");
	}
	
	public void addClass(String classToAdd) {
		this.viewClass = this.viewClass + " " + classToAdd;
	}
	
	public String getViewText() {
		return viewText;
	}
	
	public void setViewText(String viewText) {
		this.viewText = viewText;
	}
	
	@Override
	public String toString() {
		return "MazeCell: " + col + "," + row + " " + viewClass + ", visited:" + visited;
	}
}






