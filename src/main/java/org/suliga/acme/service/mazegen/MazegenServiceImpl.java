package org.suliga.acme.service.mazegen;

import org.springframework.stereotype.Service;
import org.suliga.acme.model.mazegen.MazeGrid;

@Service
public class MazegenServiceImpl implements MazegenService {

	private MazeGrid mazeGrid;
	
	@Override
	public MazeGrid getMazeGrid() {
		if (mazeGrid == null) {
			mazeGrid = new MazeGrid();
		}
		return mazeGrid;
	}
	
	@Override
	public void generateNewMaze() {
		mazeGrid = new MazeGrid();
	}
}
