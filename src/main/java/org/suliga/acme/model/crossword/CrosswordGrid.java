package org.suliga.acme.model.crossword;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrosswordGrid {
	private static final Logger logger = LoggerFactory.getLogger(CrosswordGrid.class);
	
	private CrosswordCell[][] crosswordCells;
	private int numCols;
	private int numRows;
	private List<String> acrossClues;
	private List<String> downClues;
	
	public CrosswordGrid() {
		numCols = 15;
		numRows = 15;
		init();
	}
	
	private void init() {
		createCells();
		fillInBlocks();		
		fillInWordsAndClues();
	}
	
	private void createCells() {
		crosswordCells = new CrosswordCell[numCols][numRows];
		for (int i=0;i<numCols;i++) {
			for (int j=0;j<numRows;j++) {
				crosswordCells[i][j] = new CrosswordCell();
			}
		}
	}
	
	private void fillInBlocks() {
		String[] block = new String[numRows];
		
		block[0] = ". . . . . . . . X . . . . . .";
		block[1] = ". X . X . X X . X . X . X X .";
		block[2] = ". X . X . X . . . . X . . . .";
		block[3] = ". . . . . X X . X X X . X X .";
		block[4] = ". X X X . . . . X . . . . X .";
		block[5] = ". . . . . X X . . . . X . X .";
		block[6] = "X X . X . X . X X . X . . . .";
		block[7] = ". . . . X . . . . . . X . X .";
		block[8] = ". X . X . X . X X . X X . X X";
		block[9] = ". . . . . . . X . . . . . . .";
		block[10]= ". X X X . X . . . . X . X X .";
		block[11]= ". . . . . X . X . X . . . . .";
		block[12]= ". X X . X . . . . X . X . X .";
		block[13]= ". X X . . . X X . X . X . X .";
		block[14]= ". . . . X . . . X . . . . . .";

		// Fill in Block squares
		for (int col = 0;col < numCols;col++) {
			for (int row = 0; row < numRows;row++) {
				if (block[row].substring(col*2, col*2 + 1).equalsIgnoreCase("X"))
					crosswordCells[col][row].setBlock(true);
				else
					crosswordCells[col][row].setBlock(false);
			}
		}
		logger.info("block[0] = " + block[0]);
	}
	
	private void fillInWordsAndClues() {
		String dataArray[][] = {
				// col - row - across/down - word - clue
				{ "0", "0",  "A", "MOBYDICK","Ahab's obsession" },
				{ "0", "7",  "D", "STARBUCK","Chief mate of the Pequod"  },
				{ "5", "12", "A", "AHAB",    "Monomaniacal captain"  },
				{ "9", "4",  "D", "ISHMAEL", "Call me ____"  },
				{ "9", "14", "A", "RACHEL",  "Ship that finds an orphan"  },
				{ "14", "0", "D", "MELVILLE","Famous author"  },
				
				{ "14", "9", "D", "DORSAL", "Of or on the back or upper surface of an animal"  },
				{ "0", "0",  "D", "MORTAL", "Liable or subject to death"  },
				{ "0", "5",  "A", "LEAPS",  "Springs or jumps"  },
				{ "0", "11", "A", "BONES",  "Components of skeletons"  },
				{ "10", "11","A", "SPEAR",  "A shaft with a sharp point and barbs"  },
				{ "6", "2",  "A", "ECHO",   "Repetition of a sound by reflection"  },
				{ "9", "0", "A",  "WIGWAM", "Native American dwelling having a conical framework"  },
				{ "11", "0", "D", "GHOST",  "The spirit of a dead person"  },
				{ "5", "7", "A",  "STORMS", "Thunder and lightning producers"  },
				{ "3", "11", "D", "EVIL",   "Morally bad or wrong"  },
				{ "0", "14", "A", "KEEL",   "The principal structural member of a boat or ship"  },
				{ "3", "13", "A", "ILL",    "Not healthy"  },
				{ "5", "14", "A", "EBB",    "The receding or outgoing tide"  },
				{ "8", "9", "A",  "DELUDED", "Deceived, fooled"  },
				{ "8", "9", "D",  "DOUBT",  "A feeling of uncertainty or lack of conviction"  },
				{ "6", "6", "D",  "STRETCH",  "Made longer or wider without tearing"  },
				{ "6", "10", "A", "TOOL",   "A device used to carry out a particular function"  },
				{ "0", "7", "A",  "SPOT",   "A mark or pip on a playing card"  },
				{ "2", "5", "D",  "ALONE",  "Being apart from others"  },
				{ "2", "0", "D",  "BORN",   "Brought into life"  },
				{ "4", "4", "A",  "URGE",   "Try to persuade someone to do something"  },
				{ "11", "6", "A", "VEIL",   "A length of cloth worn over the head"  },
				{ "4", "8", "D",  "JARS",   "Cylindrical glass or earthenware vessels"  },
				{ "11", "2", "A", "OPAL",   "A mineral of hydrated silica"  },
				{ "12", "4", "D", "OBEYED", "Carried out or fulfilled a command"  },
				{ "11", "9", "D", "URP",    "To vomit"  },
				
				{ "9", "4", "A", "INTO",    "To the inside or interior of"  },
				{ "0", "9", "A", "AVERAGE",  "Middle point between extremes"  },
				{ "7", "0", "D", "KICKED",  "Strike out with the foot or feet"  },
				{ "0", "3", "A", "TANGO",   "A Latin-American dance"  },
				{ "7", "5", "A", "DOSE",     "Amount of a medicine that is taken at one time"  },
				{ "10", "4", "D", "NE",     "Compass heading"  },
				{ "4", "0", "D", "DELOUSE",   "To remove lice from"  },
				{ "9", "0", "D", "WHO",     "The person or persons that"  },
				{ "10", "11", "D", "SOFA",  "A long upholstered seat"  },
				{ "12", "11", "D", "EACH",  "One of two or more"  },
				{ "5", "12", "D", "ALE",    "Full-bodied beer"  }	
		};
		
		for (int daRow=0;daRow<dataArray.length;daRow++) {
			String[] line = dataArray[daRow];
			
			int col = Integer.parseInt(line[0]);
			int row = Integer.parseInt(line[1]);
			String acrossDown = line[2];
			String word = line[3];
			String clue = line[4];
			
			switch (acrossDown) {
			case "A":
				for (int j=0;j<word.length();j++) {
					crosswordCells[col+j][row].setRealLetter(word.substring(j,j+1));
				}
				crosswordCells[col][row].setAcrossClue(clue);
				break;
			case "D":
				for (int j=0;j<word.length();j++) {
					crosswordCells[col][row+j].setRealLetter(word.substring(j,j+1));
				}
				crosswordCells[col][row].setDownClue(clue);
				break;
			}
		}
		// now assign clue numbers 
		int clueNumber = 1;
		for (int row=0;row<numRows;row++) {
			for (int col=0;col<numCols;col++) {
				if (crosswordCells[col][row].hasEitherClue()) {
					crosswordCells[col][row].setClueNumber(clueNumber);
					clueNumber++;
				}
			}
		}
		
		// store clue numbers and clues together so they can be sorted
		acrossClues = new ArrayList<>();
		downClues = new ArrayList<>();
		
		for (int row=0;row<numRows;row++) {
			for (int col=0;col<numCols;col++) {
				if (crosswordCells[col][row].getAcrossClue() != null) {
					acrossClues.add(crosswordCells[col][row].getClueNumber() + " " + crosswordCells[col][row].getAcrossClue());
					
				}
				if (crosswordCells[col][row].getDownClue() != null) {
					downClues.add(crosswordCells[col][row].getClueNumber() + " " + crosswordCells[col][row].getDownClue());
				}
			}
		}
	}
	
	public CrosswordCell[][] getCrosswordCells() {
		return crosswordCells;
	}
	public String getAutoCheck() {
		return null;
	}
	
	public void setAutoCheck(String value) {
		
	}

	public int getNumCols() {
		return numCols;
	}

	public void setNumCols(int cols) {
		this.numCols = cols;
	}

	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int rows) {
		this.numRows = rows;
	}

	public List<String> getAcrossClues() {
		return acrossClues;
	}

	public void setAcrossClues(List<String> acrossClues) {
		this.acrossClues = acrossClues;
	}

	public List<String> getDownClues() {
		return downClues;
	}

	public void setDownClues(List<String> downClues) {
		this.downClues = downClues;
	}
}
