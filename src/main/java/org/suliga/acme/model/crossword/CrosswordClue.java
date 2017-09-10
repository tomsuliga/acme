package org.suliga.acme.model.crossword;

public class CrosswordClue {
	private String id;
	private String text;
	private String direction;
	
	public CrosswordClue() {}

	public CrosswordClue(String id, String text, String direction) {
		super();
		this.id = id;
		this.text = text;
		this.direction = direction;
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
}
