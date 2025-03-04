//NAME: Vincent Hassman
//DATE: 10 March 2023
/*DESCRIPTION: Establishes a window instance; creates model, view, and controller objects
 * Calls various update methods in view, controller, and model
 * Sets up mouse and keyboard listeners
 * Continues running the game while Esc or q are not pressed
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
	final int windowWidth = 700; 
	final int windowHeight = 500; 
	
	public Game() //Constructor for a "Game" object
	{
		model = new Model(); //create a model object 
		controller = new Controller(model); //create a controller object
		view = new View(controller, model); //create a view object
		view.addMouseListener(controller); //make controller in charge of mouse listener in window 
		this.addKeyListener(controller); //make controller in charge of key listener in window

		this.setTitle("Assignment4"); //set window title
		this.setSize(windowWidth, windowHeight); //set window size 
		this.setFocusable(true); 
		this.getContentPane().add(view); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true); 
		this.setIconImage(new ImageIcon("img/appicon.png").getImage()); //make the app icon a tile
		this.setResizable(false);
	}

	public void run() //method to run the program 
	{
		while(true)
		{
			controller.update(); //update window contents call
			model.update(); 
			view.repaint(); // This will indirectly call View.paintComponent
			Toolkit.getDefaultToolkit().sync(); // Updates screen

			// Go to sleep for 40 milliseconds to control frame rate
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
