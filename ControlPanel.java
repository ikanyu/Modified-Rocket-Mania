import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Control Panel class inherits from JPanel and is
 *  used to control the process of the game
 *  and displaying game information
 *  
 * @author Ruizhe 
 *  
 *  including:
 *  1. two labels which show the name of the game
 *  2. four textfields for score, level, rockets and time
 *  3. a ratiobutton for swiching AI
 *  4. two buttons for pause and quit the game 
 */
public class ControlPanel  extends JPanel implements ActionListener{	
	
	private int remainingTime=RocketManiaGame.DEFAULT_TIMER;
	private int rockets = RocketManiaGame.STARTING_ROCKETS;
	private int score = RocketManiaGame.DEFAULT_SCORE;
	private boolean AIplaying;
	private int level = RocketManiaGame.DEFAULT_LEVEL;
	private JFrame frame = new JFrame();
	
	private JLabel
		lblRocket = new JLabel("Rocket"),
		lblMania = new JLabel("Mania");
	
	private JTextField
		tfScore = new JTextField("Score = " + score),
    	tfLevel = new JTextField("Level " + level),
		tfRockets = new JTextField("Rockets : " + rockets),
		tfTimer = new JTextField("Time = " + remainingTime);

	private final JRadioButton rbAI = new JRadioButton("AI");
	
	private JButton
    	btPause = new JButton("Pause"),
    	btQuit = new JButton("Quit");

	private JPanel plInfo = new JPanel();
	private JPanel plButton = new JPanel();
	
	private Border border = new EtchedBorder(
	        EtchedBorder.RAISED, Color.white, new Color(148, 145, 140));
	
	/**
	 * constructor without arguments
	 * place every component to its position
	 */
	public ControlPanel() {		
		AIplaying = false;
		setBackground(Color.ORANGE);
		
		// set two labels that display the name of the game
		lblRocket.setHorizontalAlignment(SwingConstants.CENTER);
		lblRocket.setFont(new Font("Aharoni", Font.PLAIN, 32));
		lblRocket.setBounds(54, 10, 105, 61);
		add(lblRocket);
		lblMania.setHorizontalAlignment(SwingConstants.CENTER);
		lblMania.setFont(new Font("Aharoni", Font.PLAIN, 30));
		lblMania.setBounds(54, 43, 105, 61);
		add(lblMania);		
		
		// set panel of information
		plInfo.setBackground(Color.ORANGE);
		plInfo.setBounds(23, 99, 166, 216);
		plInfo.setLayout(null);		
		plInfo.setBorder(null);	
		add(plInfo);		
		
		// within the panel set four text field of score, level, rockets, time
		tfScore.setFont(new Font("Aharoni", Font.BOLD, 25));
		tfScore.setEditable(false);
		tfScore.setBackground(Color.WHITE);
		tfScore.setHorizontalAlignment(SwingConstants.CENTER);
		tfScore.setBounds(19, 10, 127, 32);
		plInfo.add(tfScore);
		
		tfLevel.setFont(new Font("Aharoni", Font.BOLD, 25));
		tfLevel.setEditable(false);
		tfLevel.setBackground(Color.WHITE);
		tfLevel.setHorizontalAlignment(SwingConstants.CENTER);
		tfLevel.setBounds(29, 43, 108, 32);
		plInfo.add(tfLevel);		

		tfRockets.setFont(new Font("Aharoni", Font.BOLD, 25));
		tfRockets.setEditable(false);
		tfRockets.setBackground(Color.WHITE);
		tfRockets.setHorizontalAlignment(SwingConstants.CENTER);
		tfRockets.setBounds(10, 91, 146, 32);
		plInfo.add(tfRockets);
		
		tfTimer.setFont(new Font("Aharoni", Font.BOLD, 25));
		tfTimer.setEditable(false);
		tfTimer.setBackground(Color.WHITE);
		tfTimer.setHorizontalAlignment(SwingConstants.CENTER);
		tfTimer.setBounds(15, 124, 136, 32);
		plInfo.add(tfTimer);
		
		//set the ratiobutton of AI switching
		rbAI.setHorizontalAlignment(SwingConstants.CENTER);
		rbAI.setBackground(Color.WHITE);
		rbAI.setFont(new Font("Aharoni", Font.BOLD, 25));
		rbAI.setBounds(43, 172, 79, 32);
		
		// action listener for AI swiching
		rbAI.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		    	// if the radiobutton is pressed, reverse the value
		    	// of the boolean flag for AI playing
		       if(!AIplaying)		    	
		    	   AIplaying = true;
		       else		    	   
		    	   AIplaying = false;
		       
		    }
		});		
		plInfo.add(rbAI);
		
		// set the panel of buttons
		plButton.setBackground(Color.ORANGE);
		plButton.setBounds(23, 325, 166, 141);
		plButton.setLayout(null);
		plButton.setBorder(null);
		setLayout(null);
		add(plButton);		
		
		// set and add action listenner for two buttons which are pause and quit
		btPause.setFont(new Font("Aharoni", Font.BOLD, 22));
		btPause.setBounds(15, 31, 135, 32);
		plButton.add(btPause);		
		btPause.addActionListener(this);
		
		btQuit.setFont(new Font("Aharoni", Font.BOLD, 22));
		btQuit.setBounds(15, 73, 135, 32);
		plButton.add(btQuit);
		btQuit.addActionListener(this);
	}
	
	/**
	 * Checks to see if the AI playing boolean flag
	 * is set
	 * @return true if AI playing is true, else false
	 */
	public boolean isAIPlaying(){
		if(AIplaying){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Sets the AIplaying boolean
	 * @param b
	 */
	public void setAIPlaying(boolean b){
		AIplaying = b;
	}
	/**
	 * method for updating the current score
	 * @param score
	 */
	public void updateScore(int score) {
		this.score = score;
		tfScore.setText("Score: " +score);
		
	}
	
	/**
	 * method for pop up a JOptionPane when level up
	 * @param level
	 */
	public void newlevel(int level) {
		JOptionPane.showMessageDialog(frame,
		    "Conratulations! Level completed.");
		setLevel(level);
	}
	
	/**
	 * set method for set the current level
	 * @param level  
	 */
	public void setLevel(int level) {
		tfLevel.setText("Level " + level);		
	}
	
	/**
	 * method for updating the number of the rockets to fire
	 * @param rockets
	 */
	public void updateRockets (int rockets){
		this.rockets = rockets;
		tfRockets.setText("Rockets: " +rockets);
	}
	
	/**
	 * method for updating remaining time
	 * @param time
	 */
	public void updateRemainingTime(int time){
		remainingTime = time;
		tfTimer.setText("Time = " + remainingTime);
	}
		
	/**
	 * Sets the radio button for AI to pressed or not pressed
	 * @param b true if pressed, false if not
	 */
	public void setRBAIstate(boolean b){
		rbAI.setSelected(b);
	}		
	
	/**
	 * action listener for the buttons
	 * while pause is clicked, pop up a JOptionPane and disable the game play
	 * while quit is clicked, end the program.
	 */
    public void actionPerformed(ActionEvent e) {    	
        String action = ((JButton) e.getSource()).getText();
        
        switch (action) {        
	        case "Pause" : 	        	
	        	RocketManiaGame.pauseGame();	        	
	        	RocketManiaGame.AIOn = false;
	        		        	
	        	JOptionPane.showMessageDialog(frame,
	        		    "Paused.");	        	
	        	RocketManiaGame.pauseGame();
	        	break;
	        	
	        case "Quit" :
	        	System.exit(0);  
        }    
    }
}
