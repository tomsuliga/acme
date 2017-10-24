package org.suliga.acme.dao.backgammon;

import org.springframework.data.repository.CrudRepository;
import org.suliga.acme.model.backgammon.Turn;

public interface TurnDao extends CrudRepository<Turn, Long> {

}
