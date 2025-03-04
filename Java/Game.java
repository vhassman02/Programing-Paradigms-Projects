//NAME: Vincent Hassman
//DATE: 31 March 2023
/*DESCRIPTION: 
 * Establishes a window instance; creates model, view, and controller objects
 * Calls various update methods in view, controller, and model
 * Sets up mouse and keyboard listeners
*/
import javax.swing.JFrame;
import javax.swing.ImageIcon; 
import java.awt.Toolkit;

public class Game extends JFrame
{
	//Create class instance variables for model, controller, and view
	Model model;
	Controller controller;
	View view; 
	static final int windowWidth = 700; 
	static final int windowHeight = 500; 
	
	public Game() //Constructor for a "Game" object
	{
		model = new Model(); //create a model object 
		controller = new Controller(model); //create a controller object
		view = new View(controller, model); //create a view object
		view.addMouseListener(controller); //make controller in charge of mouse listener in window 
		this.addKeyListener(controller); //make controller in charge of key listener in window

		this.setTitle("Assignment5"); //set window title
		this.setSize(windowWidth, windowHeight); //set window size 
		this.setFocusable(true); 
		this.setResizable(false); //make window not resizable 
		this.getContentPane().add(view); //make the view object the contents of the game window
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true); 
		this.setIconImage(new ImageIcon("img/appicon.png").getImage()); //make the app icon a tile
	}

	public void run() //method to run the program 
	{
		while(true)
		{
			controller.update(); //update window contents call
			model.update(); 
			view.repaint(); // This will indirectly call View.paintComponent
			Toolkit.getDefaultToolkit().sync(); // Updates screen

			//go to sleep for 25 milliseconds to set 40 FPS
			try
			{
				Thread.sleep(40);
			} 
			catch(Exception e) 
			{
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	public static void main(String[] args) //Main method to initialize game object and run app window
	{
		Game g = new Game(); //create a new instance of game object 
		g.run(); //run the game
	}
}
