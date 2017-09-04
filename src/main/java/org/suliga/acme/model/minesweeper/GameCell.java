package org.suliga.acme.model.minesweeper;

public class GameCell {
	private String cellValue;
	private boolean visible;
	private boolean possibleMine;
	
	public GameCell() {
		cellValue = GameGrid.EMPTY_VALUE;
		visible = false;
	}
	public GameCell(String cellValue, boolean visible) {
		this.cellValue = cellValue;
		this.visible = visible;
	}
	public String getCellValue() {
		return cellValue;
	}
	public void setCellValue(String cellValue) {
		this.cellValue = cellValue;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public String getViewData() {
		return visible ? cellValue : "";
	}
	public String getViewClass() {
		return (isVisible() ? "cellKnown" : "cellUnknown") + " " + ((visible && cellValue.equals("*")) ? "visibleMine" : "") + (possibleMine ? "possibleMine" : "");
	}
	public boolean isPossibleMine() {
		return possibleMine;
	}
	public void setPossibleMine(boolean possibleMine) {
		this.possibleMine = possibleMine;
	}
}
