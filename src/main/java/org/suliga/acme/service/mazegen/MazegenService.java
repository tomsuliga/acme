package org.suliga.acme.service.mazegen;

import org.suliga.acme.model.mazegen.MazeGrid;

public interface MazegenService {
	MazeGrid getMazeGrid();
	void generateNewMaze();
}
