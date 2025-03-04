//NAME: Vincent Hassman
//DATE: 10 March 2023
/*DESCRIPTION: 
 * Controls what is being viewed in the window
 * Keeps track of what portion of the map is being displayed
 * Provides an image loading method
 * Paints graphics on the window
*/
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Color; 
import java.awt.Font; 

class View extends JPanel
{
	//class instance objects/variables
	Model model; //declare a motion model object for window contents

	//variables keep track of coordinates relative to room the view is in 
	public int scrollPosX; 
	public int scrollPosY; 

	BufferedImage[] link_images; //an array to include character images
	Link link; 
	
	//Constructor for window contents object
	View(Controller c, Model m)
	{
		c.setView(this); //tell the controller object which window view it must work with
		model = m; //make the model object within View.java = the model passed in from Game.java
				
		//Load tile image
		this.scrollPosX = 700;
		this.scrollPosY = 0; 
		link = new Link(52, 50); 
		c.setLink(link); 
		model.setLink(link); 
	}

	void setModel(Model m)
	{
		model = m; 
	}

	//image loader
	static BufferedImage loadImage(String imagePath)
	{
		BufferedImage image = null; 
		try
		{
			image = ImageIO.read(new File(imagePath));
			//System.out.println("File " + imagePath + " was successfully loaded."); 
		}
		catch(IOException e)
		{
			e.printStackTrace(System.err);
			System.out.println("File " + imagePath + " could not be loaded."); 
			System.exit(1);
		}
		return image; 
	}

	//Puts objects on the window area
	@Override //overrides paintComponent method in JPanel class
	public void paintComponent(Graphics g)
	{
		g.setColor(new Color(26,64,159)); //set background color
		g.fillRect(0,0,this.getWidth(), this.getHeight()); //fill rectangular window area with color
		for(int i = 0; i< model.tiles.size(); i++)
		{
			Tile t = model.tiles.get(i); //retrieve a tile object from ArrayList tiles
			t.drawTile(g, scrollPosX, scrollPosY); //put the tile on the screen
		}

		//enable graphics for edit mode display
		if (Controller.editing())
		{ 
			g.setColor(new Color(255, 255,102));
			g.setFont(new Font("Helvetica", Font.BOLD, 20));
			g.drawString("Edit Mode enabled", 500, 460);
		}

		//Draw the chartacter
		link.drawLink(g, scrollPosX, scrollPosY);
		//update the view region when necessary
		if (link.x>=700)
		{
			this.scrollPosX = 700; 
		}
		if (link.y>=500)
		{
			this.scrollPosY = 500; 
		}
		if (link.x<700)
		{
			this.scrollPosX = 0; 
		}
		if (link.y<500)
		{
			this.scrollPosY = 0; 
		}
	}
}
