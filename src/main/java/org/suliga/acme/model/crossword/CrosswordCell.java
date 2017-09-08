package org.suliga.acme.model.crossword;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class CrosswordCell {
	private boolean block;
	private String userLetter;
	private String realLetter;
	private String acrossClue;
	private String downClue;
	private int clueNumber;
	
	public CrosswordCell() {
		block = false;
		realLetter = "";
		userLetter = "";
	}
	
	public boolean hasEitherClue() {
		return acrossClue != null || downClue != null;
	}
	public boolean isBlock() {
		return block;
	}

	public void setBlock(boolean block) {
		this.block = block;		
	}

	public String getUserLetter() {
		return userLetter;
	}

	public void setUserLetter(String userLetter) {
		this.userLetter = userLetter.toUpperCase();
	}

	public String getRealLetter() {
		return realLetter;
	}

	public void setRealLetter(String realLetter) {
		this.realLetter = realLetter.toUpperCase();
	}

	public String getAcrossClue() {
		return acrossClue;
	}

	public void setAcrossClue(String acrossClue) {
		this.acrossClue = acrossClue;
	}

	public String getDownClue() {
		return downClue;
	}

	public void setDownClue(String downClue) {
		this.downClue = downClue;
	}
	public int getClueNumber() {
		return clueNumber;
	}
	public void setClueNumber(int clueNumber) {
		this.clueNumber = clueNumber;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
