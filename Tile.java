import java.awt.Point;

/**
 * @author Liam
 * 
 * Tile super class from which the various tiles inherit.
 *
 */
public abstract class Tile {
	
	protected boolean up, down, left, right, fuel, rocket, connectedToFuel, connectedToRocket;
	protected int x, y;
	
	public Tile(){
		connectedToRocket = false;
		connectedToFuel = false;
	}
	
	/**
	 * Sets the boolean flags to represent that the tile has
	 * been rotated.
	 */
	public abstract void rotate();
	
	/**
	 * 
	 * @return a Point object containing the Tile's x & y coordinates.
	 */
	public Point getCoord(){
		Point p = new Point(x,y);
		return p;
	}
	
	/**
	 * 
	 * @param gameBoard - a 2D Tile array
	 * @return a 1D Tile array consisting of all of the tiles
	 * adjacent to this tile.
	 */
	public abstract Tile[] getAdjacent(Tile[][] gameBoard);
	
	
	/**
	 * Sets the x coordinate of this tile.
	 * @param x
	 */
	public abstract void setX(int x);
	
	/**
	 * Sets the y coordinate of this tile.
	 * @param y
	 */
	public abstract void setY(int y);

}
	
