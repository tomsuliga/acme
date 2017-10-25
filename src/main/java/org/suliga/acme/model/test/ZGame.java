package org.suliga.acme.model.test;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;

@Entity
public class ZGame {
	private static final Logger logger = LoggerFactory.getLogger(ZGame.class);
	
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private ZPlayer player1;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private ZPlayer player2;
	
	@JoinColumn(name="FK_ZGAME") // col is in ZTurn table
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.PERSIST, orphanRemoval = true)
	private List<ZTurn> zturns;
	
	public ZGame() {
		zturns = new ArrayList<>();
	}
	
	public ZGame(ZPlayer player1, ZPlayer player2) {
		this();
		this.player1 = player1;
		this.player2 = player2;
	}

	public void addTurn(ZTurn turn) {
		zturns.add(turn);
	}
	
	@Override
	public String toString() {
		return "ZGame " + player1 + ", " + player2 + ", num turns = " + zturns.size();
	}
	
	@Transactional
	public static void testSaveAndLoad2(ZGameDao gameDao) {
		ZGame loadedGame = gameDao.findOne(1L);
	
		logger.info("Loaded Game:");
		logger.info(loadedGame.toString());
	}
}
