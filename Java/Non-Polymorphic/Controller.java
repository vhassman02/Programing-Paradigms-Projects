//NAME: Vincent Hassman
//DATE: 10 March 2023
/*DESCRIPTION: 
 * Sends controls from user to the model, view, etc. classes
 * Keeps track of when the mouse is pushed or when specific keyboard keys are pushed
 * Responsible for calling saving/loading/moving map methods 
*/

//Add listeners for mouse input, button click, and keyboard input
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent; 

//ActionListener waits for an instance of an action to occur
class Controller implements ActionListener, MouseListener, KeyListener 
{
	View view; //Declare a View object called "view"
	Model model; //Declare a Model object called "model" 
	Link link; //Declare a link object called "link"
	
	static boolean editMode = false; //tells if edit mode is enabled
	
	//boolean mapLoaded; 

	//Boolean variables for keyboard inputs
	boolean keyLeft; //left arrow 
	boolean keyRight; //right arrow 
	boolean keyUp; //up arrow 
	boolean keyDown; //down arrow 
	boolean keyEsc; //escape/quit
	boolean keyQ; //quit
	
	boolean keyW; //up
	boolean keyA; //left 
	boolean keyX; //down
	boolean keyD; //right
	
	boolean keyS; //save map
	boolean keyL; //load map

	Controller(Model m) //Constructor for "Controller" object
	{
		model = m; //set the Controller's model object
		loadMap();
	}

	void setView(View v)
	{
		view = v; //set the Controller's view object
	}

	void setModel(Model m)
	{
		model = m; 
	}

	void setLink(Link l)
	{
		link = l;
	}


	//Required method for an "ActionListener" class
	public void actionPerformed(ActionEvent e)
	{
		//view.removeButton(); //remove button when it is pressed 
	}

	//MOUSE EVENT METHODS
	//mouse pressed event
	public void mousePressed(MouseEvent e)
	{
		//modify to take into account changes in the view 
		if (editMode == true)
			model.modifyTile(e.getX()+view.scrollPosX, e.getY()+view.scrollPosY);
	}
	//other mouse event methods (required even if no implementation)
	public void mouseReleased(MouseEvent e) {    }
	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }
	public void mouseClicked(MouseEvent e) 
	{    
		/*if (e.getY() < 100)
		{
			System.out.println("break here"); 
		}*/

	}

	//KEYBOARD EVENT METHODS
	//keyboard press event
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: 
				keyRight = true; 
				break;
			case KeyEvent.VK_LEFT: 
				keyLeft = true; 
				break;
			case KeyEvent.VK_UP: 
				keyUp = true; 
				break;
			case KeyEvent.VK_DOWN: 
				keyDown = true; 
				break;
			
			case KeyEvent.VK_ESCAPE: 
				exitListener();
				break;
			case KeyEvent.VK_Q: 
				exitListener();
				break; 
			//char c = Character.toLowerCase(e.getKeyChar()); can also be used to listen for a key event 
				//use the value of character c to determine action

			//when S is pressed, save map
			case KeyEvent.VK_S:
				model.marshal(); 
				break; 
			//when L is pressed, load map
			case KeyEvent.VK_L:
				keyL = true; 
				loadMap();
				break; 
		}
	}
	//keyboard released event
	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: keyRight = false; break;
			case KeyEvent.VK_LEFT: keyLeft = false; break;
			case KeyEvent.VK_UP: keyUp = false; break;
			case KeyEvent.VK_DOWN: keyDown = false; break; 
			
			case KeyEvent.VK_W: 
				keyW = true; 
				break; //up 
			case KeyEvent.VK_A: 
				keyA = true; 
				break; //left
			case KeyEvent.VK_D: 
				keyD = true; 
				break; //right
			case KeyEvent.VK_X: 
				keyX = true; 
				break; //down

			case KeyEvent.VK_E: //toggle edit mode
				if (editMode == true)	
				{
					editMode = false;
					System.out.println("Status: Edit mode OFF"); 
				}		
				else if (editMode == false)
				{
					editMode = true;
					System.out.println("Status: Edit mode ON"); 
				}
				break; 
		}
	}
	//other keyboard event method (required even if no implementation)
	public void keyTyped(KeyEvent e) {	  }
	
	//update moving contents in window
	void update()
	{
		//set previous coordinates
		link.setPrevCoordinate();
		//change location coordinates of character
		//0 = down, 1 = left, 2 = right, 3 = up
		if(keyRight) 
		{
			//link.setPrevCoordinate(link.x, link.y);
			link.x+=Link.speed;
			link.updateImageNum(2); 
		}

		if(keyLeft) 
		{
			//link.setPrevCoordinate(link.x, link.y);
			link.x-=Link.speed;
			link.updateImageNum(1); 
		}	
				
		if(keyDown) 
		{
			///link.setPrevCoordinate(link.x, link.y);
			link.y+=Link.speed;
			link.updateImageNum(0); 
		}
		
		if(keyUp) 
		{
			//link.setPrevCoordinate(link.x, link.y);
			link.y-=Link.speed;
			link.updateImageNum(3); 
		}
	}

	void moveMap()
	{
		if (keyW) 
		{
			view.scrollPosY = 0; 
			keyW = false; //reset the key boolean after it is used 
		}
		if (keyX) 
		{
			view.scrollPosY = 500; 
			keyX = false; //reset the key boolean after it is used 
		}
		if (keyA) 
		{
			view.scrollPosX = 0; 
			keyA = false; //reset the key boolean after it is used 
		}
		if (keyD) 
		{
			view.scrollPosX = 700; 
			keyD = false; //reset the key boolean after it is used 
		}
	}

	public void loadMap()
	{
			Json loadedFile = Json.load("map.json");
			model.unmarshal(loadedFile); 
			//mapLoaded = true; 
	}

	//exit when ESC/Q/q pressed
	public void exitListener()
	{
		System.exit(0); 	
	}

	//determine if edit mode is on or not
	public static boolean editing()
	{
		return editMode; 
	}
}
