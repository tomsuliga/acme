package org.suliga.acme.model.test;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZGameServiceImpl implements ZGameService {
	private static final Logger logger = LoggerFactory.getLogger(ZGameServiceImpl.class);
	
	@Autowired 
	private ZGameDao gameDao;
	
	@Override
	@Transactional
	public void testSaveAndLoad1() {
		logger.info("in test");
		
		ZPlayer p1 = new ZPlayer("Tom");
		ZPlayer p2 = new ZPlayer("HAL 9000");
		ZGame game = new ZGame(p1,p2);
		
		for (int i=0;i<7;i++) {
			ZTurn turn = new ZTurn();
			game.addTurn(turn);
			for (int j=0;j<4;j++) {
				ZMove move = new ZMove(j, i);
				turn.addMove(move);
			}
		}
		
		gameDao.save(game);
		logger.info("Found ZGame A: " + game.toString());
	}
	
	@Override
	@Transactional
	public void testSaveAndLoad2(long index) {
		ZGame zgame = gameDao.findOne(index);
		logger.info("Found ZGame B: " + zgame.toString());
	}
}



