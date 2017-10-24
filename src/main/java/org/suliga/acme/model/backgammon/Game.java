package org.suliga.acme.model.backgammon;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name="BG_GAME")
public class Game {
	private static final Logger logger = LoggerFactory.getLogger(Game.class);

	@Id
	@GeneratedValue
	private long id;
	
	@OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
	private Player player1;
	
	@OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
	private Player player2;
	
	private boolean gameOver;
	
	@OneToMany(mappedBy="game", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Turn> turns;

	@Transient
	private Board board;
			
	@Transient
	private Turn currentTurn;
		
	public Game() {
		board = new Board();
	}
	
	public Game(boolean init) {
		if (init) {
			init();
		}
	}
	
	public void init() {
		board = new Board();
		turns = new ArrayList<>();
		gameOver = false;
		currentTurn = null;
		
		player1 = new Player("Tom", TuringType.HUMAN);
		player2 = new Player("Hal 9000", TuringType.COMPUTER);
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public int getNumTurns() {
		return turns.size();
	}
	
	public void debugMoves() {
		turns.forEach(t -> logger.info(t.toString()));
	}
	
	public Turn getCurrentTurn() {
		if (currentTurn == null) {
			currentTurn = turns.get(turns.size()-1);
		}
		return currentTurn;
	}
	
	public Dice roll() {
		// automatically switch sides with each dice roll
		PlayerSide ps = null;
		if (getCurrentTurn().getPlayerSide() == PlayerSide.PLAYER_1) {
			ps = PlayerSide.PLAYER_2;
		} else {
			ps = PlayerSide.PLAYER_1;
		}
		currentTurn = new Turn(this, ps, new Dice());
		board.setCurrentPlayerSide(ps);
		turns.add(currentTurn);
		return currentTurn.getDice();
	}
	
	// Only done one time per game
	public Dice startOfGameFirstRoll() {
		Dice dice = new Dice();
		while (dice.isDouble()) {
			dice = new Dice();
		}
		if (dice.getDie1() > dice.getDie2()) {
			currentTurn = new Turn(this, PlayerSide.PLAYER_1, dice);
		} else {
			currentTurn = new Turn(this, PlayerSide.PLAYER_2, dice);
		}
		board.setCurrentPlayerSide(currentTurn.getPlayerSide());
		turns.add(currentTurn);
		return dice;
	}
	
	public Dice getDice() {
		return getCurrentTurn().getDice();
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Turn> getTurns() {
		return turns;
	}

	public void setTurns(List<Turn> turns) {
		this.turns = turns;
	}
}


