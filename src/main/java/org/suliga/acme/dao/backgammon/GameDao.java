package org.suliga.acme.dao.backgammon;

import org.springframework.data.repository.CrudRepository;
import org.suliga.acme.model.backgammon.Game;

// public interface GameDao extends PagingAndSortingRepository<Game, Long> {
public interface GameDao extends CrudRepository<Game, Long> {

}
