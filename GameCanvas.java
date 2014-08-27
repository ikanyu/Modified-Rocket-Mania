import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Stack;

/** 
 * Game canvas class inherits from JPanel and is 
 *  used to display the game board where the game would be played
 * 
 * @author Ruizhe
 * 
 * including 
 * 1. a visible game board which is a 2D array of JButtons
 * 2. a game board object handling the tile connection
 */

class GameCanvas extends JPanel {

	private int rows, cols;
	Board b;
	private JButton[][] jbts;
	private int noOfRocketsFired = 0;
	private boolean connected = false;
	private boolean AIplaying = false, AIpause = false;
	private boolean animation = false;

	/**
	 * Game canvas constructor initialises the board
	 * @param rows
	 * @param cols
	 */
	public GameCanvas(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;

		setLayout(new GridLayout(rows, cols, 0, 0));		
		setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(
				148, 145, 140)));

		b = new Board(rows, cols);
		jbts = new JButton[rows][cols];		
		
		// initialise the all the tiles in the game board
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				// first column where the fuels are
				if (j == 0) {
					b.tiles[i][j] = new FuelTile();
					JButton jbtFuel = new JButton();
					jbtFuel.setIcon(new ImageIcon("tiles/FuelTile.png"));
					jbtFuel.setBackground(Color.WHITE);
					add(jbtFuel);
					jbts[i][j] = jbtFuel;
					b.tiles[i][j].setX(j);
					b.tiles[i][j].setY(i);
					
				// last column where the rockets are
				} else if (j == 7) {
					b.tiles[i][j] = new RocketTile();
					JButton jbtRocket = new JButton();
					jbtRocket.setIcon(new ImageIcon("tiles/RocketTile.png"));
					jbtRocket.setBackground(Color.WHITE);
					add(jbtRocket);
					jbts[i][j] = jbtRocket;
					b.tiles[i][j].setX(j);
					b.tiles[i][j].setY(i);
				} 
				
				// other places for tiles
				else {
					randomImage(i, j);
				}
			}
		}

		checkFuel();
		//starts a new thread in case a connection has already been made by the 
		//random distribution of tiles
		Thread thread = new Thread(new Game());
		thread.start();
	}
	
	/**
	 * get method
	 * @return AIPlaying boolean flag
	 */
	public boolean getIsAIPlaying(){
		return AIplaying;
	}
	
	/**
	 * set method 
	 * @param isItPlaying sets the AIplaying boolean flag
	 */
	public void setAIPlaying(boolean isItPlaying){
		AIplaying  = isItPlaying;
	}

	/**
	 * get method 
	 * @return the number of rockets fired
	 */
	public int getRockets(){
		return noOfRocketsFired;
	}
	
	/**
	 * Calculates the number of rockets fired.
	 */
	private void rocketsFired() {
		for (int i = 0; i < rows; i++) {
			if(b.tiles[i][6].connectedToRocket && b.tiles[i][6].fuel && b.tiles[i][6].right){
				noOfRocketsFired++;
			}	
		}
	}


	/**
	 * reset method which sets the number of rockets fired to 0
	 */
	public void reset() {
		noOfRocketsFired = 0;
	}
	
	/**
	 * Fills a particular tile on the board with a random image/Tile
	 * @param i
	 * @param j
	 */
	private void randomImage(int i, int j) {
		// randomly generate a position for Ttile and coner tile
		int randomTilePosition = (int) (Math.random() * 4 + 1);
		// randomly generate a position for line tile
		int randomLineTilePosition = (int) (Math.random() * 2 + 1);

		// a random number to set the ratio of different types of tiles
		int randomTile = (int) (Math.random() * 17);
		
		// 1 for plus tile and two for Ttile
		// 2- 9 for line tile and 10 to 17 for coner tile
		if (randomTile >= 2 && randomTile <= 9)
			randomTile = 2;
		else if (randomTile >= 10 && randomTile <= 17)
			randomTile = 3;

		switch (randomTile) {
		case 0:
			JButton jbtTile0 = new JButton();
			jbtTile0.setIcon(new ImageIcon("tiles/PlusTileFalse.png"));
			jbtTile0.setBackground(Color.WHITE);
			b.tiles[i][j] = new PlusTile();
			jbts[i][j] = jbtTile0;
			add(jbtTile0);
			jbtTile0.addMouseListener(new MouseListener());
			b.tiles[i][j].setX(j);
			b.tiles[i][j].setY(i);
			break;

		case 1:
			JButton jbtTile1 = new JButton();
			jbtTile1.setIcon(new ImageIcon("tiles/TTileFalse"
					+ randomTilePosition + ".png"));
			jbtTile1.setBackground(Color.WHITE);
		
			b.tiles[i][j] = new TTile();
			jbts[i][j] = jbtTile1;
			add(jbtTile1);
			rotateTile(b.tiles[i][j], randomTilePosition);
			jbtTile1.addMouseListener(new MouseListener());
			b.tiles[i][j].setX(j);
			b.tiles[i][j].setY(i);
			break;

		case 2:
			JButton jbtTile2 = new JButton();
			jbtTile2.setIcon(new ImageIcon("tiles/LineTileFalse"
					+ randomLineTilePosition + ".png"));
			jbtTile2.setBackground(Color.WHITE);
			b.tiles[i][j] = new LineTile();
			jbts[i][j] = jbtTile2;
			add(jbtTile2);
			rotateTile(b.tiles[i][j], randomLineTilePosition);
			jbtTile2.addMouseListener(new MouseListener());
			b.tiles[i][j].setX(j);
			b.tiles[i][j].setY(i);
			break;

		case 3:
			JButton jbtTile3 = new JButton();
			jbtTile3.setIcon(new ImageIcon("tiles/CornerTileFalse"
					+ randomTilePosition + ".png"));
			jbtTile3.setBackground(Color.WHITE);
			b.tiles[i][j] = new CornerTile();
			jbts[i][j] = jbtTile3;
			add(jbtTile3);
			rotateTile(b.tiles[i][j], randomTilePosition);
			jbtTile3.addMouseListener(new MouseListener());
			b.tiles[i][j].setX(j);
			b.tiles[i][j].setY(i);
			break;
		}		
	}
	
	/**
	 * method for setting the tile boolean for the random tile
	 * @param tile
	 * @param times
	 */
	private void rotateTile(Tile tile, int times) {
		for (int i = 1; i < times; i++) {
			tile.rotate();
		}
	}
	
	/**
	 * Listens for mouse clicks on the tiles.
	 */
	private class MouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {			
			//if no animation is happening and the AI is not activated
			if(!animation && !AIplaying){
				doMouseClicked(e);
			}
		}
	}
	
	/**
	 * Rotate the tile that has been clicked
	 * Checks the fuel after the tile has been clicked for a new connection
	 * Launches the animation thread in case a connection has been made
	 * @param e
	 */
	private void doMouseClicked(MouseEvent e){
		rotateImage((JButton) e.getSource());
		Point p = getObject((JButton) e.getSource());
		b.tiles[p.x][p.y].rotate();

		checkFuel();
		Thread thread = new Thread(new Game());
		thread.start();
	}
	
	
	/**
	 * Utility function. Pass in a JButton and returns the coordinates of it.
	 * Used when we get a JButton from an actionEvent but we don't know its location.
	 * @param o
	 * @return Point object containing the coordinates of the JButton.
	 */
	private Point getObject(JButton o) {
		for (int i = 0; i < jbts.length; i++) {
			for (int j = 0; j < jbts[i].length; j++) {
				if (jbts[i][j].equals(o)) {
					return new Point(i, j);
				}
			}
		}
		return null;
	}
	
	/**
	 * rotates the actual image on the JButton.
	 * @param jbtTile the JButton which the image should be rotated on
	 * @return the new JButton with rotated image
	 */
	private JButton rotateImage(JButton jbtTile) {
		String url = jbtTile.getIcon().toString();

		switch (url) {
		case "tiles/TTileFalse1.png":
			jbtTile.setIcon(new ImageIcon("tiles/TTileFalse2.png"));
			return jbtTile;
		case "tiles/TTileFalse2.png":
			jbtTile.setIcon(new ImageIcon("tiles/TTileFalse3.png"));
			return jbtTile;
		case "tiles/TTileFalse3.png":
			jbtTile.setIcon(new ImageIcon("tiles/TTileFalse4.png"));
			return jbtTile;
		case "tiles/TTileFalse4.png":
			jbtTile.setIcon(new ImageIcon("tiles/TTileFalse1.png"));
			return jbtTile;
			//
		case "tiles/TTileFuel1.png":
			jbtTile.setIcon(new ImageIcon("tiles/TTileFuel2.png"));
			return jbtTile;
		case "tiles/TTileFuel2.png":
			jbtTile.setIcon(new ImageIcon("tiles/TTileFuel3.png"));
			return jbtTile;
		case "tiles/TTileFuel3.png":
			jbtTile.setIcon(new ImageIcon("tiles/TTileFuel4.png"));
			return jbtTile;
		case "tiles/TTileFuel4.png":
			jbtTile.setIcon(new ImageIcon("tiles/TTileFuel1.png"));
			return jbtTile;

		case "tiles/TTileRocket1.png":
			jbtTile.setIcon(new ImageIcon("tiles/TTileRocket2.png"));
			return jbtTile;
		case "tiles/TTileRocket2.png":
			jbtTile.setIcon(new ImageIcon("tiles/TTileRocket3.png"));
			return jbtTile;
		case "tiles/TTileRocket3.png":
			jbtTile.setIcon(new ImageIcon("tiles/TTileRocket4.png"));
			return jbtTile;
		case "tiles/TTileRocket4.png":
			jbtTile.setIcon(new ImageIcon("tiles/TTileRocket1.png"));
			return jbtTile;
			//
		case "tiles/CornerTileFalse1.png":
			jbtTile.setIcon(new ImageIcon("tiles/CornerTileFalse2.png"));
			return jbtTile;
		case "tiles/CornerTileFalse2.png":
			jbtTile.setIcon(new ImageIcon("tiles/CornerTileFalse3.png"));
			return jbtTile;
		case "tiles/CornerTileFalse3.png":
			jbtTile.setIcon(new ImageIcon("tiles/CornerTileFalse4.png"));
			return jbtTile;
		case "tiles/CornerTileFalse4.png":
			jbtTile.setIcon(new ImageIcon("tiles/CornerTileFalse1.png"));
			return jbtTile;

		case "tiles/CornerTileFuel1.png":
			jbtTile.setIcon(new ImageIcon("tiles/CornerTileFuel2.png"));
			return jbtTile;
		case "tiles/CornerTileFuel2.png":
			jbtTile.setIcon(new ImageIcon("tiles/CornerTileFuel3.png"));
			return jbtTile;
		case "tiles/CornerTileFuel3.png":
			jbtTile.setIcon(new ImageIcon("tiles/CornerTileFuel4.png"));
			return jbtTile;
		case "tiles/CornerTileFuel4.png":
			jbtTile.setIcon(new ImageIcon("tiles/CornerTileFuel1.png"));
			return jbtTile;

		case "tiles/CornerTileRocket1.png":
			jbtTile.setIcon(new ImageIcon("tiles/CornerTileRocket2.png"));
			return jbtTile;
		case "tiles/CornerTileRocket2.png":
			jbtTile.setIcon(new ImageIcon("tiles/CornerTileRocket3.png"));
			return jbtTile;
		case "tiles/CornerTileRocket3.png":
			jbtTile.setIcon(new ImageIcon("tiles/CornerTileRocket4.png"));
			return jbtTile;
		case "tiles/CornerTileRocket4.png":
			jbtTile.setIcon(new ImageIcon("tiles/CornerTileRocket1.png"));
			return jbtTile;

		case "tiles/LineTileFalse1.png":
			jbtTile.setIcon(new ImageIcon("tiles/LineTileFalse2.png"));
			return jbtTile;
		case "tiles/LineTileFalse2.png":
			jbtTile.setIcon(new ImageIcon("tiles/LineTileFalse1.png"));
			return jbtTile;

		case "tiles/LineTileFuel1.png":
			jbtTile.setIcon(new ImageIcon("tiles/LineTileFuel2.png"));
			return jbtTile;
		case "tiles/LineTileFuel2.png":
			jbtTile.setIcon(new ImageIcon("tiles/LineTileFuel1.png"));
			return jbtTile;

		case "tiles/LineTileRocket1.png":
			jbtTile.setIcon(new ImageIcon("tiles/LineTileRocket2.png"));
			return jbtTile;
		case "tiles/LineTileRocket2.png":
			jbtTile.setIcon(new ImageIcon("tiles/LineTileRocket1.png"));
			return jbtTile;

		case "tiles/TTileTrue1.png":
			jbtTile.setIcon(new ImageIcon("tiles/TTileTrue2.png"));
			return jbtTile;
		case "tiles/TTileTrue2.png":
			jbtTile.setIcon(new ImageIcon("tiles/TTileTrue3.png"));
			return jbtTile;
		case "tiles/TTileTrue3.png":
			jbtTile.setIcon(new ImageIcon("tiles/TTileTrue4.png"));
			return jbtTile;
		case "tiles/TTileTrue4.png":
			jbtTile.setIcon(new ImageIcon("tiles/TTileTrue1.png"));
			return jbtTile;
		case "tiles/CornerTileTrue1.png":
			jbtTile.setIcon(new ImageIcon("tiles/CornerTileTrue2.png"));
			return jbtTile;
		case "tiles/CornerTileTrue2.png":
			jbtTile.setIcon(new ImageIcon("tiles/CornerTileTrue3.png"));
			return jbtTile;
		case "tiles/CornerTileTrue3.png":
			jbtTile.setIcon(new ImageIcon("tiles/CornerTileTrue4.png"));
			return jbtTile;
		case "tiles/CornerTileTrue4.png":
			jbtTile.setIcon(new ImageIcon("tiles/CornerTileTrue1.png"));
			return jbtTile;
		case "tiles/LineTileTrue1.png":
			jbtTile.setIcon(new ImageIcon("tiles/LineTileTrue2.png"));
			return jbtTile;
		case "tiles/LineTileTrue2.png":
			jbtTile.setIcon(new ImageIcon("tiles/LineTileTrue1.png"));
			return jbtTile;
		}

		return null;
	}
	
	/**
	 * helper function for rotating image by the coordinates of tile
	 * @param the coordinate of the tile in the board
	 */
	public void rotateImages(Point p) {
		rotateImage(jbts[p.y][p.x]);
	}
	
	/**
	 * Updates the fuel boolean on a tile at Point p.
	 * @param p
	 * @param fuel
	 */
	public void updateFuel(Point p, boolean fuel) {
		b.tiles[p.y][p.x].fuel = fuel;
	}
	
	/**
	 * Updates the images during the animation.
	 */
	public void updateImages() {
		animation=true;
		for (int i = 0; i < rows; i++) {
			for (int j = 1; j < cols ; j++) {
				String url = jbts[i][j].getIcon().toString();
				String newUrl;

				if (b.tiles[i][j].fuel && b.tiles[i][j].connectedToRocket) {
					connected = true;
					AIpause = true;
					
					if (url.contains("False")) {
						newUrl = url.replace("False", "True");
					} else if (url.contains("Rocket")) {
						newUrl = url.replace("Rocket", "True");
					} else {
						newUrl = url.replace("Fuel", "True");
					}

				} else if (b.tiles[i][j].connectedToRocket) {
					if (url.contains("False")) {
						newUrl = url.replace("False", "Rocket");
					} else if (url.contains("Fuel")) {
						newUrl = url.replace("Fuel", "Rocket");
					} else {
						newUrl = url.replace("True", "Rocket");
					}

				} else if (b.tiles[i][j].fuel) {
					if (url.contains("False")) {
						newUrl = url.replace("False", "Fuel");
					} else if (url.contains("Rocket")) {
						newUrl = url.replace("Rocket", "Fuel");
					} else {
						newUrl = url.replace("True", "Fuel");
					}
				}

				else {
					if (url.contains("True")) {
						newUrl = url.replace("True", "False");
					} else if (url.contains("Rocket")) {
						newUrl = url.replace("Rocket", "False");
					} else {
						newUrl = url.replace("Fuel", "False");
					}
				}

				jbts[i][j].setIcon(new ImageIcon(newUrl));
			}
		}
		animation = false;
	}
	
	/**
	 * set method
	 * @param the boolean for if a fuel is connected to a rocket through some tiles
	 */
	public void setConnected(boolean conn){
		connected  = conn;
	}
	
	/**
	 * check if the animation is playing
	 * @return animation
	 */
	public boolean checkAnimation(){
		return animation;
	}	
	
	/**
	 * Method to check connection. Sets the correct fuel values in each Tile and
	 * checks to see if game is solved
	 *
	 * @return true if game is solved, else false
	 */
	public void checkFuel() {

		if(AIplaying && !AIpause && !connected){			
			AI.solve(b, this);			
		} else {
			b = AI.setFuel(b);
			b = AI.setRocketConnections(b);
		}

		for (int i = 0; i < rows; i++) {
			for (int j = 1; j < cols ; j++) {
				String url = jbts[i][j].getIcon().toString();
				String newUrl;

				if (b.tiles[i][j].fuel && b.tiles[i][j].connectedToRocket) {
					connected = true;
					AIpause = true;
					
					if (url.contains("False")) {
						newUrl = url.replace("False", "True");
					} else if (url.contains("Rocket")) {
						newUrl = url.replace("Rocket", "True");
					} else {
						newUrl = url.replace("Fuel", "True");
					}

				} else if (b.tiles[i][j].connectedToRocket) {
					if (url.contains("False")) {
						newUrl = url.replace("False", "Rocket");
					} else if (url.contains("Fuel")) {
						newUrl = url.replace("Fuel", "Rocket");
					} else {
						newUrl = url.replace("True", "Rocket");
					}

				} else if (b.tiles[i][j].fuel) {
					if (url.contains("False")) {
						newUrl = url.replace("False", "Fuel");
					} else if (url.contains("Rocket")) {
						newUrl = url.replace("Rocket", "Fuel");
					} else {
						newUrl = url.replace("True", "Fuel");
					}
				}

				else {
					if (url.contains("True")) {
						newUrl = url.replace("True", "False");
					} else if (url.contains("Rocket")) {
						newUrl = url.replace("Rocket", "False");
					} else {
						newUrl = url.replace("Fuel", "False");
					}
				}

				jbts[i][j].setIcon(new ImageIcon(newUrl));
			}
		}		
		
		if (AIplaying) {
			Thread thread = new Thread(new Game());
			thread.start();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
		}		
	}

	/**
	 * Resets the JButtons with random JButtons.
	 * Used in animation.
	 * @param jbt
	 * @param i
	 * @param j
	 */
	private void resetJbt(JButton jbt, int i, int j) {
		int randomTile = (int) (Math.random() * 17);
		int randomTilePosition = (int) (Math.random() * 4 + 1);
		int randomLineTilePosition = (int) (Math.random() * 2 + 1);

		if (randomTile >= 2 && randomTile <= 9)
			randomTile = 2;
		else if (randomTile >= 10 && randomTile <= 17)
			randomTile = 3;

		switch (randomTile) {
		case 0:
			jbt.setIcon(new ImageIcon("tiles/PlusTileFalse.png"));
			b.tiles[i][j] = new PlusTile();
			b.tiles[i][j].setX(j);
			b.tiles[i][j].setY(i);
			break;

		case 1:
			jbt.setIcon(new ImageIcon("tiles/TTileFalse" + randomTilePosition
					+ ".png"));
			b.tiles[i][j] = new TTile();
			b.tiles[i][j].setX(j);
			b.tiles[i][j].setY(i);
			rotateTile(b.tiles[i][j], randomTilePosition);
			break;

		case 2:
			jbt.setIcon(new ImageIcon("tiles/LineTileFalse"
					+ randomLineTilePosition + ".png"));
			b.tiles[i][j] = new LineTile();
			b.tiles[i][j].setX(j);
			b.tiles[i][j].setY(i);
			rotateTile(b.tiles[i][j], randomLineTilePosition);
			break;

		case 3:
			jbt.setIcon(new ImageIcon("tiles/CornerTileFalse"
					+ randomTilePosition + ".png"));
			b.tiles[i][j] = new CornerTile();
			b.tiles[i][j].setX(j);
			b.tiles[i][j].setY(i);
			rotateTile(b.tiles[i][j], randomTilePosition);
			break;
		}
	}

	
	/**
	 * Returns the JButton at Point p
	 * @param p
	 * @return JButton
	 */
	public JButton getJButton(Point p){
		return jbts[p.y][p.x];
	}

	
	/**
	 * Starting thread for the falling tile and flying rocket animations
	 *
	 */
	private class Game implements Runnable {
		public void run() {
			// start animation if some fuels and rockets are connected
			if (connected) {
				animation = true;
				// new thread for eliminating the true tiles
				Thread thread1 = new Thread(new Disappear());
				try {
					while(connected) {
						Thread.sleep(300);
						thread1.start();
						thread1.join();
						
						// after the elimination is finished start rocket fired animation
						for(int i = 0; i < rows; i++) {
							if(jbts[i][7].getIcon().toString().contains("True")) {
								Thread thread2 = new Thread(new Rocket(i));
								thread2.start();
								thread2.join();
							}
						}
						rocketsFired();
						
						//start thread for falling tile column by column
						for (int i = 1; i < cols - 1; i++) {
							Thread thread3 = new Thread(new FallingCol(i));
							thread3.start();
							thread3.join();
						}
						connected = false;
						//sets new pictures
						checkFuel();
						
						//starts a new thread, in case a connection has been made randomly after the animation
						Thread thread = new Thread(new Game());
						thread.start();	
					}
					
					animation = false;
				} catch(InterruptedException  | IllegalThreadStateException ex) {
				}
			}
		}
	}

	/**
	 * Makes the old tiles disappear
	 * search the whole 2D array and make the tile disappear if it is a true tile
	 */
	private class Disappear implements Runnable {
		public void run() {
			try {
				for(int i = 0; i < rows; i++) {
					for(int j = 1; j < cols - 1;j++) {
						String url = jbts[i][j].getIcon().toString();
						
						// eliminate one true tile and sleep the thread for 100 milliseconds
						if(url.contains("True")) {
							jbts[i][j].setIcon(new ImageIcon("tiles/True.png"));
							Thread.sleep(100);
						}
					}
				}
			} catch (InterruptedException e) {
			}
			
			AIpause = false;
		}
	}

	/**
	 * Makes the rockets blast off
	 *
	 */
	private class Rocket implements Runnable {
		private int row;
		
		/**
		 * Constructor
		 * @param the row of the rocket to fire
		 */
		public Rocket(int row) {
			this.row = row;
		}
		
		public void run() {
			try {
				//change the image of rocket to make the animation
				for(int i = 0; i < 5; i++) {
					jbts[row][7].setIcon(new ImageIcon("tiles/True" + i +"Tile.png"));
					Thread.sleep(80);
				}
				
				//reset the image to normal after fired
				jbts[row][7].setIcon(new ImageIcon("tiles/RocketTile.png"));
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Makes the new tiles fall into place
	 *
	 */
    private class FallingCol implements Runnable {
		private int col;

		/**
		 * Constructor
		 * @param the column of tiles to fall
		 */
		public FallingCol(int col) {
			this.col = col;
		}
		
		public void run() {
			try {
				// create a new array for storing the column of JButtons
				for (int j = 1; j < cols - 1; j++) {
					JButton[] colOfJbt = new JButton[9];
					
					// copy the JButtons to the array
					for (int i = 0; i < 9; i++)
						colOfJbt[i] = jbts[i][col];

					int numOfTrue = 0;
					
					// set the true tile to null in the array
					for (int i = 0; i < colOfJbt.length; i++) {
						if (colOfJbt[i].getIcon().toString().contains("True")) {
							colOfJbt[i] = null;
							numOfTrue++;
						}
					}
					
					// new arraylist for storing the JButtons after updating
					ArrayList<JButton> colOfJbt1 = new ArrayList<JButton>();
					
					// add the JButtons from array to arraylist so that true tiles are removed
					for (int i = 0; i < colOfJbt.length; i++) {
						if (colOfJbt[i] != null)
							colOfJbt1.add(colOfJbt[i]);
					}

					// set the tiles to the game board
					if (numOfTrue > 0) {
						int k=0;
						while(colOfJbt[k] != null){
							colOfJbt[k] = null;
							k++;
							numOfTrue++;
						}

						for (int i = 0; i < numOfTrue; i++) {
							resetJbt(jbts[i][col], i, col);
						}
					}
				}
				
				// sleep for 80 milliseconds for next column
				Thread.sleep(80);
			} catch (InterruptedException e) {
			}
		}
	}
}
