//NAME: Vincent Hassman
//DATE: 31 March 2023
/*DESCRIPTION: 
 * Sends controls from user to the model and view
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
	Link link; 
	
	static boolean editMode = false; //tells if edit mode is enabled
	static boolean potMode = false; //tells if editing pot mode is enabled

	//Boolean variables for keyboard inputs
	boolean keyLeft; boolean keyRight; boolean keyUp; boolean keyDown; 
	boolean keyEsc; 
	boolean keyQ; boolean keyW; boolean keyA; boolean keyX; boolean keyD; boolean keyS; boolean keyL; 

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

	//Required method for a action listener interface
	public void actionPerformed(ActionEvent e){ 	}

	//MOUSE EVENT METHODS
	//mouse pressed event
	public void mousePressed(MouseEvent e)
	{
		//modify tiles if edit tiles mode activated
		if (editMode == true && potMode == false)
			model.modifyTiles(e.getX()+view.scrollPosX, e.getY()+view.scrollPosY);
		
		//modify pots if edit pot mode activated
		if (potMode == true)
		{
			model.modifyPots(e.getX()+view.scrollPosX, e.getY()+view.scrollPosY); 
		}
	}
	//other mouse event methods required by interface
	public void mouseReleased(MouseEvent e) {    }
	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }
	public void mouseClicked(MouseEvent e) {    }

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
			case KeyEvent.VK_CONTROL:
				moveBoomerang();
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
			
			//toggle edit mode
			case KeyEvent.VK_E:
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

			//toggle edit pots mode
			case KeyEvent.VK_P:
				if (potMode == true)
				{
					potMode = false;
					System.out.println("Status: Edit Pots mode OFF");
				}
				else if (editMode == true && potMode == false)
				{
					potMode = true;
					System.out.println("Status: Edit Pots mode ON"); 
				}
				break; 
			//when S is pressed, save map
			case KeyEvent.VK_S:
				keyS = true;
				saveMap();  
				break; 
			//when L is pressed, load map
			case KeyEvent.VK_L:
				keyL = true; 
				loadMap();
				break;
		}
	}
	//other keyboard event method required by interface
	public void keyTyped(KeyEvent e) {	  }
	
	//update moving contents in window
	void update()
	{
		//find the Sprite in the array that is a Link object
		for (int i = 0; i<model.sprites.size(); i++)
		{
			if (model.sprites.get(i).isLink())
				link = ((Link)(model.sprites.get(i)));
		}
		//set previous coordinates for character
		link.setPrevCoordinate();
		
		//change location coordinates of character
		//0 = down, 1 = left, 2 = right, 3 = up
		if(keyRight) 
		{
			link.x+=Link.speed; 
			link.updateImageNum(2); 
		}

		if(keyLeft) 
		{
			link.x-=Link.speed;
			link.updateImageNum(1); 
		}	
				
		if(keyDown) 
		{
			link.y+=Link.speed;
			link.updateImageNum(0); 
		}
		
		if(keyUp) 
		{
			link.y-=Link.speed;
			link.updateImageNum(3); 
		}
	}

	public void loadMap()
	{
		Json loadedFile = Json.load("map.json");
		model.unmarshal(loadedFile);
	}

	public void saveMap()
	{
		model.marshal(); 
	}

	public void moveBoomerang()
	{
		/* Link directions:
     	* 0 = down
     	* 1 = left
     	* 2 = right
     	* 3 = up */
		int direction = model.getLinkDirection(); 
		int x_throwDirection;
		int y_throwDirection; 
		if (direction == 0)
		//throw down
		{
			y_throwDirection = 1;
			x_throwDirection = 0;
			model.throwBoomerang(x_throwDirection, y_throwDirection); //throw the boomerang
		}
		if (direction == 1)
		//throw left
		{
			y_throwDirection = 0;
			x_throwDirection = -1; 
			model.throwBoomerang(x_throwDirection, y_throwDirection); //throw the boomerang
		}
		if (direction == 2)
		//throw right
		{
			y_throwDirection = 0;
			x_throwDirection = 1;
			model.throwBoomerang(x_throwDirection, y_throwDirection); //throw the boomerang
		}
		if (direction == 3)
		//throw up
		{
			y_throwDirection = -1;
			x_throwDirection = 0; 
			model.throwBoomerang(x_throwDirection, y_throwDirection); //throw the boomerang
		}
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
