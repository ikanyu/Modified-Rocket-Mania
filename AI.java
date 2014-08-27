import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Craig
 * 
 * This class contains all of the algorithms used in the game.
 *
 */

public class AI {
	private static List<Tile> finalRoute = new ArrayList<Tile>();
	
	/**
	 * Sets the fuel property to true in every tile linked to a match
	 * 
	 * @return the board passed in with the fuel values set correctly
	 */
	public static Board setFuel(Board b) {
		if (b == null) { // Makes sure the board is initialised
			return null;
		}
		
		b.resetFuel(); // resets all fuel properties to false
		
		List<Tile> checked = new ArrayList<Tile>(); // used to avoid checking tiles multiple times
		List<Tile> nextToCheck = new ArrayList<Tile>(); // a queue containing the order to check tiles
		
		Tile matches[] = b.getMatches(); // returns an array of the matches as Tile objects

		for (int i = 0; i < matches.length; i++) {
			nextToCheck.add(matches[i]); // Adds all the matches to the queue
		}
		
		while (nextToCheck.size() > 0) { // Keep looping while there are unchecked tiles
		        Tile currentTile = nextToCheck.get(nextToCheck.size() - 1); // Take the tile from the end of the queue
		        															// Last in, first out ####
		        
		        checked.add(currentTile); // Makes sure the tile is only checked once
		        nextToCheck.remove(currentTile); // no longer in the queue
		       
		        Tile adjacentTiles[] = b.getAdjacentTiles(currentTile); // returns an array of all adjacent tiles
		 
		        for (int i = 0; i < adjacentTiles.length; i++) { // loop through each adjacent tile
		        	if(adjacentTiles[i] != null){
		                if (b.connected(currentTile, adjacentTiles[i]) && !checked.contains(adjacentTiles[i])) {
		                											// If it's connected and hasn't already been checked
		                        b.setFuel(adjacentTiles[i], true);  // set its fuel property to true
		                        nextToCheck.add(adjacentTiles[i]);  // Add it to the queue since it is connected
		                }
		        	}
		        }
		}
		
		return b; // return the updated board
	}
	
	/**
	 * Sets the fuel property to true in every tile linked to a rocket
	 * 
	 * @return the board passed in with the fuel values set correctly
	 */
	public static Board setRocketConnections(Board b) {		// This method is identical to the setFuel method
															// However it sets the connections to the rockets instead
		if (b == null) {
			return null;
		}
		
		b.resetRockets();
		
		List<Tile> checked = new ArrayList<Tile>();
		List<Tile> nextToCheck = new ArrayList<Tile>();
		
		Tile rockets[] = b.getRockets();

		for (int i = 0; i < rockets.length; i++) {
			nextToCheck.add(rockets[i]);
		}
		
		while (nextToCheck.size() > 0) {
		        Tile currentTile = nextToCheck.get(nextToCheck.size() - 1);
		        
		        checked.add(currentTile);
		        nextToCheck.remove(currentTile);
		       
		        Tile adjacentTiles[] = b.getAdjacentTiles(currentTile);
		 
		        for (int i = 0; i < adjacentTiles.length; i++) {
		        	if(adjacentTiles[i] != null){
		                if (b.connected(currentTile, adjacentTiles[i]) && !checked.contains(adjacentTiles[i])) {
		                        b.setRocketConnections(adjacentTiles[i], true);
		                        nextToCheck.add(adjacentTiles[i]);
		                }
		        	}
		        }
		}
		
		return b;
	}
	
	/**
	 * Game is solved if returns > 0
	 * 
	 * @return number of rockets that have fuel
	 */
	public static int gameSolved(Board b) {
		Tile[] rockets = b.getRockets();
		int numRockets = 0;
		
		for (int i = 0; i < rockets.length; i++) {
			if (rockets[i].fuel) {
				numRockets++;
			}
		}
		
		return numRockets;
	}
	
	
	/**
	 * Sleeps for 90ms before each rotation while running the AI
	 */
	private static void sleep() {
		try {
			Thread.sleep(90);
		} catch (InterruptedException e) {
			System.out.println("Thread fail");
		}
	}
	
	
	
	/**
	 * If no route has been found, return an empty array
	 * @return solved route from fuel to rocket
	 */
	public static Tile[] getRoute() {
		int amount = finalRoute.size();
		Tile[] routeArray = new Tile[amount];
		
		for (int i = 0; i < amount; i++) {
			routeArray[i] = finalRoute.get(i);
		}
		if (routeArray.length > 0) {
			finalRoute.clear();
		}
		return routeArray;
	}
	

	/**
	 * finds a route by rotating the tiles using depth first search
	 * 
	 * @param b is the board to solve
	 * @param gc is the GameCanvas object that displays the board
	 * @return an instance of the solved board
	 */
	public static Board solve(Board b, GameCanvas gc) {
		
		if (b == null || gc == null)
			return b; // return the board if it is null or the GameCanvas passed in is null
		
		if (finalRoute.size() > 0) {
			return b; // return the board if a route has already been found
		}
		
		b = setFuel(b);
		b = setRocketConnections(b); // set the correct fuel and rocket connections to start with
		
		
		List<Tile> route = new ArrayList<Tile>(); // used for the current route we're working on. allows backtracking
		List<Tile> checked = new ArrayList<Tile>(); // used to keep track of the checked tiles in the working route
		List<Tile> queue = new ArrayList<Tile>(); // the queue of tiles to check next
		
		Tile[] matches = b.getMatches();
		
		for (int i = 0; i < matches.length; i++) {
			queue.add(matches[i]); // the queue starts with the matches in
		}
		
		while (gameSolved(b) <= 0) { // keep looping till the board is solved
			boolean restart = false; // used to break out when the next tile in the route has been found
			
			if (route.size() == 0) { // if there is no current route to check
				if (queue.size() == 0) { // if there is nothing left to check, return the board as it is
					
					return b;
				}
				route.add(queue.get(queue.size() - 1)); // add the next queue item to the new working route
				queue.remove(queue.size() - 1); // remove the tile from the queue
				checked.clear(); // new checked list for the new route
			}
			
			Tile currentTile = route.get(route.size() - 1); // take the last tile in the route
			
			
			Tile[] adjacentTiles = b.getAdjacentTiles(currentTile); // find the adjacent tiles to it
			
			boolean foundNext = false;
			
			for (int i = 0; i < adjacentTiles.length; i++) { // loop through the adjacent tiles
				if (adjacentTiles[i] != null) {
					for (int j = 0; j < 4; j++) { // rotate the adjacent tiles 4 times to check for a route
						
						
						if(!RocketManiaGame.AIOn){
							return b; // return the board as it is if the AI is turned off
						}
						
						if(RocketManiaGame.getRemainingRockets()==0 || RocketManiaGame.getInterval() <= 1){
							return b; // return the board as it is if it's game over
						}

						//gets the jbutton for the current tile and the adjacent tile
						Point point1 = adjacentTiles[i].getCoord();
						Point point2 = currentTile.getCoord();
						String aUrl = gc.getJButton(point1).getIcon().toString();
						String aUrl2 = gc.getJButton(point2).getIcon().toString();
						
						// checks to see if any of the tiles are currently green (which means there is a route)
						if (aUrl.contains("True") || aUrl2.contains("True")){
				
							//sets the connected boolean to true
							gc.setConnected(true);

							return b;
						}
						
						//if the current tile and adjacent tile are connected and the adjacent tile hasn't been checked
						if (b.connected(currentTile, adjacentTiles[i]) && !checked.contains(adjacentTiles[i])) {
							
							route.add(adjacentTiles[i]); // add the connected tile to the route
							checked.add(adjacentTiles[i]); // add it to the checked pile
							
							foundNext = true;
							restart = true; // signal to find next tile in the route
							
							Point p = adjacentTiles[i].getCoord();
							Point p2 = currentTile.getCoord();
							String url = gc.getJButton(p).getIcon().toString();
							String url2 = gc.getJButton(p2).getIcon().toString();
							
							
							
							if (adjacentTiles[i].connectedToRocket || adjacentTiles[i].rocket || url.contains("True") || url2.contains("True")){
						
								gc.setConnected(true);
								return b; // if route has been found return the board
							}
							if (adjacentTiles[i].rocket) {
								for (int k = 0; k < route.size(); k++) {
									finalRoute.add(route.get(k));
								}
								return b; // if route has been found return the board
							}
							
							break;
						}
						
						
						
						b.rotateTile(adjacentTiles[i]); // if adjacent tile can't be connected, rotate it
					
						b = setFuel(b);
						b = setRocketConnections(b); // set the correct fuel and rocket connections
						
						adjacentTiles[i].fuel = b.getTile(adjacentTiles[i]).fuel;
					
						
						// update the graphical components
						gc.rotateImages(adjacentTiles[i].getCoord()); 
						gc.updateImages();
						
						while(gc.checkAnimation()){}
						
						sleep();
						
					}
					
					if (restart) {
						break;
					}
				}
				
			}
			
			if (!foundNext) {
				route.remove(route.size() - 1); // if the route can't be taken further, step backwards one
			}
			
			b = setFuel(b);
			b = setRocketConnections(b); // correct the fuel and rocket connections
		}
		return b; // return the board as it is
	}
	
	
	
	
	
	/**
	 * Finds a route from the fuel to the rockets by randomly rotating tiles. Very inefficient, used to test
	 * @param b the board to solve
	 * @param gc the GameCanvas object drawing the board
	 * @return a solved instance of Board
	 */
	public static Board randomSolve(Board b, GameCanvas gc) {
		if (b == null || gc == null) {
			return b;
		}
		
		while (gameSolved(b) <= 0) {
			Random r = new Random();
			Tile randomTile = b.tiles[r.nextInt(8) + 1][r.nextInt(6) + 1];
			
			b.rotateTile(randomTile);
			
			b = setFuel(b);
			b = setRocketConnections(b);
			
    		gc.rotateImages(randomTile.getCoord());
    		gc.updateFuel(randomTile.getCoord(), randomTile.fuel);
    		gc.updateImages();
    		
    		sleep();
		}
		
		return b;
	}
	
}