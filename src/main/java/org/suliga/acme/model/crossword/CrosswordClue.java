package org.suliga.acme.model.crossword;

public class CrosswordClue {
	private String id;
	private String text;
	private String direction;
	private int col;
	private int row;
	
	public CrosswordClue() {}

	public CrosswordClue(String id, String text, String direction, int col, int row) {
		super();
		this.id = id;
		this.text = text;
		this.direction = direction;
		this.col = col;
		this.row = row;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
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
}
