//NAME: Vincent Hassman
//DATE: 31 March 2023
/*DESCRIPTION: 
 * Controls the view area of the map
 * Provides an image loading method
 * Draws graphics on the window
*/
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Color; 
import java.awt.Font; 

import java.util.Iterator; 

class View extends JPanel
{
	//class instance objects/variables
	Model model; //declare a motion model object for window contents

	//variables keep track of coordinates relative to room the view is in 
	public int scrollPosX; 
	public int scrollPosY;

	//variables to keep track of view area width and height
	public final int viewWidth = 700;
	public final int viewHeight = 500; 
	
	//Constructor for window contents object
	View(Controller c, Model m)
	{
		c.setView(this); //tell the controller object which window view it must work with
		model = m; //make the model object within View.java = the model passed in from Game.java
				
		//Load tile image
		this.scrollPosX = 0;
		this.scrollPosY = 0; 
	}

	void setModel(Model m)
	{
		model = m; 
	}

	//image loader method
	static BufferedImage loadImage(String imagePath)
	{
		BufferedImage image = null; 
		try
		{
			image = ImageIO.read(new File(imagePath));
		}
		catch(IOException e)
		{
			e.printStackTrace(System.err);
			System.out.println("File " + imagePath + " could not be loaded."); 
			System.exit(1);
		}
		return image; 
	}

	//Draws objects on the window area
	@Override 
	public void paintComponent(Graphics g)
	{
		g.setColor(new Color(26,64,159)); //set background color
		g.fillRect(0,0,this.getWidth(), this.getHeight()); //fill rectangular window area with color

		Iterator<Sprite> sprite_iterator = (model.sprites).iterator(); 
		while (sprite_iterator.hasNext())
		{
			Sprite nextSprite = sprite_iterator.next(); //define the current index of the sprite
			nextSprite.draw(g, scrollPosX, scrollPosY); //draw the sprite

			//update the view if it is a Link sprite
			if (nextSprite.isLink())
			{
				if (nextSprite.x>=Game.windowWidth)
					this.scrollPosX = Game.windowWidth; 
			 	if (nextSprite.y>=Game.windowHeight)
					this.scrollPosY = Game.windowHeight; 
			 	if (nextSprite.x<Game.windowWidth)
					this.scrollPosX = 0; 
			 	if (nextSprite.y<viewHeight)
					this.scrollPosY = 0;
			} 
		}

		//enable graphics for edit mode display
		if (Controller.editing()==true)
		{ 
			g.setColor(new Color(255, 255,102));
			g.setFont(new Font("Helvetica", Font.BOLD, 20));
			g.drawString("Edit Mode enabled", 500, 460);
		}
	}
}
