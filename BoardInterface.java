
public interface BoardInterface {
	/**
	 * Randomly assigns tiles to the board and sets the matches and rockets
	 */
	public void initialiseBoard();
	
	/**
	 * Sets all of the tiles other than matches to unlit
	 */
	public void resetFuel();
	
	/**
	 * Sets the value of fuel for the passed in tile
	 * @param tile
	 * @param fuelled true if fuelled
	 */
	public void setFuel(Tile tile, boolean fuelled);
	
	/**
	 * 
	 * @return number of tiles fuelled (including matches and rockets)
	 */
	public int noOfFuelledTiles();
	
	/**
	 * 
	 * @return an array containing the match tiles
	 */
	public Tile[] getMatches();
	
	
	/**
	 * updates the tiles in the board using the passed in tiles coordinates
	 * @param tiles is an array of tiles that need updating
	 */
	public void setTiles(Tile[] tiles);
	
	/**
	 * set a tile at a specific coordinate
	 * 
	 * @param row
	 * @param col
	 * @param t is the new tile
	 */
	public void setTile(int row, int col, Tile t);
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @return the tile at a specific coordinate
	 */
	public Tile getTile(int row, int col);
	
	/**
	 * 
	 * @return an array containing the rocket tiles
	 */
	public Tile[] getRockets();
	
	/**
	 * Rotates the tile at the coordinates in the parameters
	 * @param row
	 * @param col
	 */
	public void rotateTile(int row, int col);
	
	/**
	 * 
	 * @param tile to get the adjacent tiles from
	 * @return an array containing all tiles adjacent to the passed in tile
	 */
	public Tile[] getAdjacentTiles(Tile tile);
	
	/**
	 * 
	 * @param tile1
	 * @param tile2
	 * @return true if the two submitted tiles are connected
	 */
	public boolean connected(Tile tile1, Tile tile2);
	
	
	/**
	 * Sets the connection of the passed in tile with the boolean fuelled
	 * @param tile
	 * @param fuelled
	 */
	void setRocketConnections(Tile tile, boolean fuelled);
	
	/**
	 * Resets the connections of the rocket boolean flags to their default values.
	 */
	void resetRockets();
}
