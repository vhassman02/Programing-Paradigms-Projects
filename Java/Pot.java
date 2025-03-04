//NAME: Vincent Hassman
//DATE: 31 March 2023
/*DESCRIPTION: 
 * Manages attributes regarding the a pot object
 * Loads images for the pot
 * Holds image for pot
 * Offers functionality for updating, sliding pots, and shattering pots
*/

import java.awt.Graphics; 

public class Pot extends Sprite
{
    int timer = 20; //timer to display broken pots for 10 iterations
    boolean shattered = false; 
    boolean isActive = true; 

    int x_direction = 0;
    int y_direction = 0;
    int speed = 10; 

    //Constructor 
    Pot(int x, int y)
    {
        this.x = x; 
        this.y = y;
        width = 30;
        height = 30; 
        //load default pot image 
        image = View.loadImage("img/pot_normal.png"); 
    }

    //Json constructor
    public Pot(Json ob)
    {
        this.x = (int)ob.getLong("potX"); 
        this.y = (int)ob.getLong("potY"); 
        this.width = (int)ob.getLong("potW"); 
        this.height = (int)ob.getLong("potH"); 
        image = View.loadImage("img/pot_normal.png"); 
    }

    public void draw(Graphics g, int scrollPosX, int scrollPosY)
    {
        if (!shattered)
            g.drawImage(this.image, this.x-scrollPosX, this.y-scrollPosY, this.width, this.height, null); //draw a pot on the screen
        else if (shattered)
        {
            this.image = View.loadImage("img/pot_broken.png"); 
            g.drawImage(this.image, this.x-scrollPosX, this.y-scrollPosY, this.width, this.height, null); //draw a pot on the screen
        }
    }

    public boolean update()
    {
        if (!shattered)
        {
            this.x += x_direction*speed;
            this.y += y_direction*speed;
        }
        if (shattered)
            timer--; 

        if (shattered && timer == 0)
            return false;
        
        return true; 
    }

    public void slide(int direction)
    {
        if (direction == 0)
		//slide down
		{
			y_direction = 1;
			x_direction = 0;
		}
		if (direction == 1)
		//slide left
		{
			y_direction = 0;
			x_direction = -1; 
		}
		if (direction == 2)
		//slide right
		{
			y_direction = 0;
			x_direction = 1;
		}
		if (direction == 3)
		//slide up
		{
			y_direction = -1;
			x_direction = 0; 
		}
    }

    public void shattered()
    {
        shattered = true; 
    }

    public Json marshal()
    {
        Json ob = Json.newObject(); 
        ob.add("potX", this.x);
        ob.add("potY", this.y); 
        ob.add("potW", this.width);
        ob.add("potH", this.width); 
        return ob; //return a Json object 
    }
    
    @Override 
    boolean isPot()
    {
        return true; 
    }

    /*@Override 
    string toString()
    {
        return "Pot coordinates: (" + this.x + "," + this.y + ")." + "Pot broken = " + pot_broken;
    }*/
}
