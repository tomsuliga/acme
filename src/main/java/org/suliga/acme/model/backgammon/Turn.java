package org.suliga.acme.model.backgammon;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="BG_TURN")
public class Turn {
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	private Game game;
	
	@Enumerated(EnumType.ORDINAL) // should use 0,1,2
	private PlayerSide playerSide; // 1 or 2
	
	@OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
	private Dice dice;
	
	@OneToMany(mappedBy = "turn", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch=FetchType.EAGER)
	private List<Move> moves;
	
	public Turn() {}
	
	public Turn(Game game, PlayerSide playerSide, Dice dice) {
		this.game = game;
		this.playerSide = playerSide;
		this.dice = dice;
		this.moves = new ArrayList<>();
	}

	public PlayerSide getPlayerSide() {
		return playerSide;
	}

	public void setPlayerSide(PlayerSide playerSide) {
		this.playerSide = playerSide;
	}

	public Dice getDice() {
		return dice;
	}

	public void pushMove(Move move) {
		moves.add(move);
	}
	
	@Override
	public String toString() {
		return "[" + id + "] Turn for " + playerSide + " " + dice.toString() + " " + moves;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public List<Move> getMoves() {
		return moves;
	}

	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}

	public void setDice(Dice dice) {
		this.dice = dice;
	}
}
