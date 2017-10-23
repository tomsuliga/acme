package org.suliga.acme.dao.backgammon;

import org.springframework.data.repository.CrudRepository;
import org.suliga.acme.model.backgammon.Move;

public interface MoveDao extends CrudRepository<Move, Long> {

}
