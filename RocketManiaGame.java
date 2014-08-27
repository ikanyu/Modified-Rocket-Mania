import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * mMin class of the game, inherits from JFrame class in charge for the overall
 * control of the game
 * 
 * @author Ruizhe 
 * including 
 * 1.an Object of gameCanvas (the board) 
 * 2.an Object of ControlPanel
 */
public class RocketManiaGame extends JFrame {

	public final static int DEFAULT_LEVEL = 1, DEFAULT_TIMER =120, STARTING_ROCKETS = 10, MAX_LEVEL = 10,DEFAULT_SCORE =0;
	
	public static boolean AIOn;

	private GameCanvas canvas; 
	private ControlPanel ctrlPanel;
	
	private boolean playing = true; // check if the game is playing

	private static boolean isPaused = false; 
	
	private Timer t;
	private static int interval = DEFAULT_TIMER;
	private int score = DEFAULT_SCORE;

	private static int rocketsRemaining = STARTING_ROCKETS;

	private int rocketstoFire = rocketsRemaining;
	private int level = DEFAULT_LEVEL; 

	public RocketManiaGame() {
		
		getContentPane().setBackground(Color.WHITE);
		loadCanvas();
		gameLoop();
	}

	/**
	 * initialises the 2 JPanels
	 */
	
	/**
	 * Loads the GUI and displays it.
	 */
	public void loadCanvas(){
		canvas = new GameCanvas(9, 8);
		canvas.setBorder(null);
		canvas.setBackground(new Color(255, 255, 255));
		canvas.setBounds(334, 28, 458, 462);
		ctrlPanel = new ControlPanel();
		ctrlPanel.setBackground(Color.ORANGE);
		ctrlPanel.setBounds(34, 28, 206, 462);
		getContentPane().setLayout(null);
		getContentPane().add(canvas);
		getContentPane().add(ctrlPanel);

		setTitle("Rocket Mania");
		setSize(851, 553);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((scrSize.width - getSize().width) / 2,
				(scrSize.height - getSize().height) / 2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	
	public int setInterval() {
		if (interval == 1) {
			t.cancel();
		}
		ctrlPanel.updateRemainingTime(interval);
		
		
		
		return --interval;

	}
	
	
	/**
	 * 
	 * @return the number of rockets remaining to be fired
	 */
	public static int getRemainingRockets(){
		return rocketsRemaining;
	}


	/**
	 * get current canvas
	 * 
	 * @return GameCanvas
	 */
	public GameCanvas getCanvas() {
		return canvas;
	}

	/**
	 * start the game
	 */
	public void playGame() {
		play();
		ctrlPanel.requestFocus();
	}

	/**
	 * game start
	 */
	private void play() {
		playing = true;
		Thread thread = new Thread(new Game());
		thread.start();
	}

	/**
	 * pause the game
	 */
	public static void pauseGame() {
		isPaused = !isPaused;
	}
	
	public static boolean isPaused(){
		return isPaused;
	}
	
	/**
	 * Stops the game
	 */
	public void stopGame() {
		playing = false;
	}
	

	
	
	/**
	 * 
	 * @param level number to be set
	 */
	public void setLevel(int level) {
		if (level < MAX_LEVEL+1 && level > 0)
			ctrlPanel.setLevel(level); 
	}
	
	/**
	 * 
	 * @return the current time remaining
	 */
	public static int getInterval(){
		return interval;
	}

	
	/**
	 * sets the value of the timer back to 120
	 */
	private void resetTimer(){
		interval = DEFAULT_TIMER;
	}
	
	/**
	 * Updates the level
	 * @return true if successful, else false (if there are no levels left)
	 */
	public boolean levelUpdate() {


		if (level < MAX_LEVEL) {
			
			canvas.reset();
			
			rocketsRemaining = MAX_LEVEL + 2 * (level);

			rocketstoFire = rocketsRemaining;

			level++;

			ctrlPanel.newlevel(level);
			
			AIOn = false;
			ctrlPanel.setAIPlaying(false);
			ctrlPanel.setRBAIstate(false);

			resetTimer();
	
			return true;
		}
		return false;

	}
	
	/**
	 * Dialogue message to show game over, then resets the game
	 */
	private void reportGameOver() {
		JOptionPane.showMessageDialog(this, "Game Over! \nScore = " + score + " \n Press OK to start new game.");
		resetTheGame();
		
	}
	
	/**
	 * Dialogue message to show won message, then resets game
	 */
	private void reportWon() {
		JOptionPane.showMessageDialog(this, "You won! \nScore = " + score + " \nPress OK to close.");
		closeGame();
	}
	
	/**
	 * Reset's the game, sets up the GUI for a new game.
	 */
	private void resetTheGame(){
		//removes the current JPanels
		canvas.removeAll();
		ctrlPanel.removeAll();
		
		//resetting the score, level and timer
		resetTimer();
		score = DEFAULT_SCORE;
		level = DEFAULT_LEVEL;
		rocketstoFire = STARTING_ROCKETS;
		
		
		//starting the game again
		playGame();
		loadCanvas();
		canvas.revalidate();
		canvas.repaint();
		ctrlPanel.revalidate();
		ctrlPanel.repaint();
		ctrlPanel.setAIPlaying(false);
		AIOn = false;
		gameLoop();
	}
	
	/**
	 * exits the game
	 */
	private void closeGame(){
		System.exit(0);
	}
	
	/**
	 * The main loop of the game. 
	 */
	private void gameLoop() {
		int temp = 0;
		t = new Timer();
		//timer counts down to zero
		t.scheduleAtFixedRate(new TimerTask() {
			public void run() {

				if (!isPaused) {
					setInterval(); //takes 1 away from the time displayed every second
					
					//checks to see if the AI playing boolean flag is set and sets other flags
					//depending on the result.
					if (ctrlPanel.isAIPlaying()) {
						AIOn = true;
						canvas.setAIPlaying(true);
					} else {
						AIOn = false;
						canvas.setAIPlaying(false);
					}
				}
			}
		}, 1000, 1000); //intervals of 1000ms == 1 second
		
		//game loop
		while (playing) {
		
			if (AIOn || ctrlPanel.isAIPlaying()) {
				// if either of the AI flags are set, start the AI algorithm
				canvas.checkFuel();
			} 
			
			//if the game is still ongoing
			if (rocketsRemaining > 0 && interval > 0) {

				rocketsRemaining = rocketstoFire - canvas.getRockets();
				
				//if the user fires 2 rockets on the last connection it will == -1
				//this fixes that
				if(rocketsRemaining<0){
					rocketsRemaining = 0;
				}
								
				ctrlPanel.updateRockets(rocketsRemaining);
				
				
				//updates score
				if(canvas.getRockets() != 0){	
					score =  temp + canvas.getRockets() * 10;
				}
		
				ctrlPanel.updateScore(score);
		
			} else {
				
				playing = false;
				
				if(rocketsRemaining<0){
					rocketsRemaining = 0;
				}
				
				//if user has completed level
				if (rocketsRemaining == 0) {
					temp = score;
					
					//update the level
					boolean gameNotWon = levelUpdate();	
					
					if(!gameNotWon){
						reportWon();
					}
					playing = true;
				}
				else {
					reportGameOver();
				}
			}
		}
	}

	private class Game implements Runnable {

		@Override
		public void run() {

		}

	}
	
	/**
	 * The main method
	 * @param args
	 */
	public static void main(String[] args) {
		new RocketManiaGame();
	}
}
