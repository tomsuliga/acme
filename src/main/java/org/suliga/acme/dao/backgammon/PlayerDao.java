package org.suliga.acme.dao.backgammon;

import org.springframework.data.repository.CrudRepository;
import org.suliga.acme.model.backgammon.Player;

public interface PlayerDao extends CrudRepository<Player, Long> {

}
