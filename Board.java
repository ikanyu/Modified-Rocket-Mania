import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Board implements BoardInterface{

	private int ROWS;
	private int COLS;
	
	public Tile[][] tiles;
	
	public Board(int ROWS, int COLS){
		this.ROWS = ROWS;
		this.COLS = COLS;
		
		tiles = new Tile[ROWS][COLS];
		
	}
	
	public Board(Board a) {
		this.ROWS = a.ROWS;
		this.COLS = a.COLS;
		this.tiles = a.tiles;
	}
	
	/**
	 * Init board
	 */
	public void initialiseBoard(){
		for (int row = 0; row < ROWS; row++){
			tiles[row][0] = new FuelTile();
			tiles[row][0].setX(0);
			tiles[row][0].setY(row);
		}
		
		for (int row = 0; row < ROWS; row++){
			tiles[row][COLS - 1] = new RocketTile();
			tiles[row][COLS - 1].setX(COLS - 1);
			tiles[row][COLS - 1].setY(row);
		}
		
	}
	
	
	/**
	 * Resets all of the fuel booleans to their default value
	 */
	public void resetFuel() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {// j starts at 1 to prevent matches from being extinguished.
				if (j == 0) {
					tiles[i][j].fuel = true;
				} else {
					tiles[i][j].fuel = false;
				}
			}
		}
	}
	
	
	/**
	 * Resets all of the rocket booleans to their default values
	 */
	public void resetRockets() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {// j starts at 1 to prevent matches from being extinguished.
				if (j == COLS - 1) {
					tiles[i][j].connectedToRocket = true;
				}
				else {
					tiles[i][j].connectedToRocket = false;
				}
			}
		}
	}


	@Override
	public Tile[] getMatches() {
		
		Tile[] match = new Tile[9];
		
		for(int i=0;i<9;i++){
			match[i] = tiles[i][0];
		}
		return match;
	}

	@Override
	public void setTiles(Tile[] tiles) {
		for (int i = 0; i < tiles.length; i++) {
			Point p = tiles[i].getCoord();
			this.tiles[p.x][p.y] = tiles[i];
		}
	}
	
	public void setTile(int row, int col, Tile t) {
		tiles[row][col] = t;
	}
	
	public Tile getTile(int row, int col) {
		return tiles[row][col];
	}
	
	public Tile getTile(Tile tile) {
		Point p = tile.getCoord();
		return tiles[p.y][p.x];
	}
	
	public void rotateTile(int row, int col) {
		tiles[row][col].rotate();
	}
	
	/**
	 * Rotates the tile
	 * @param tile
	 */
	public void rotateTile(Tile tile) {
		Point p = tile.getCoord();
		tiles[p.y][p.x].rotate();
	}

	@Override
	public Tile[] getRockets() {
		Tile[] rockets = new Tile[9];
		
		for(int i=0;i<9;i++){
			rockets[i] = tiles[i][COLS - 1];
		}
		return rockets;
	}

	@Override
	public void setFuel(Tile tile, boolean fuelled) {
		Point p = tile.getCoord();
		tiles[p.y][p.x].fuel = fuelled;
	}
	
	@Override
	public void setRocketConnections(Tile tile, boolean fuelled) {
		Point p = tile.getCoord();
		tiles[p.y][p.x].connectedToRocket = fuelled;
	}
	
	@Override
	public int noOfFuelledTiles() {
		int counter = 0;
		
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				if (tiles[row][col].fuel) {
					counter++;
				}
			}
		}
		
		return counter;
	}

	
	
	public Tile[] getAdjacentTiles(Tile tile) {
		
		return tile.getAdjacent(tiles);
	}
	
	public boolean getFuel(Tile tile) {
		Point p = tile.getCoord();
		return tiles[p.y][p.x].fuel;
	}

	/**
	 * Returns true if the 2 tiles are connected, else false
	 * @param 2 tiles for connection to be checked
	 * @return boolean connected
	 */
	public boolean connected(Tile tile1, Tile tile2) {
		if (tile1 == null || tile2 == null)
			return false;
		
		Point p1 = tile1.getCoord();
		Point p2 = tile2.getCoord();
		
		if(p1.getX()>p2.getX()){
			//tile2 is to the left of tile1
			if(tile1.left && tile2.right){
				return true;
			}
			
		}else if(p1.getX()<p2.getX()){
			//tile2 is to the right of tile1
			if(tile1.right && tile2.left){
				return true;
			}
			
		}else if(p1.getY()<p2.getY()){
			//tile2 is below tile1
			if(tile1.down && tile2.up){
				return true;
			}
		}else if(p1.getY()>p2.getY()){
			//t2 above t1
			if(tile1.up && tile2.down){
				return true;
			}
		}
		return false;
	}
	
}
