package org.suliga.acme.dao.backgammon;

import org.springframework.data.repository.CrudRepository;
import org.suliga.acme.model.backgammon.Dice;

public interface DiceDao extends CrudRepository<Dice, Long> {

}
