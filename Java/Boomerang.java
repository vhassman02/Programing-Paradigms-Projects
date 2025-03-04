//NAME: Vincent Hassman
//DATE: 31 March 2023
/*DESCRIPTION: 
 * Manages attributes regarding the boomerang
 * Loads images for the boomerang
 * Holds all 4 images for booemrang in an array
 * Determines the direction that a boomerang will fly in
 * Manages cycling through boomerang images for spinning animation
*/

import java.awt.Graphics;
import java.awt.image.BufferedImage; 

public class Boomerang extends Sprite
{
    int speed;
    int x_direction;
    int y_direction; 
    int imageIndex = 0; 
    boolean isActive = true; 
    BufferedImage[] images; 

    Boomerang(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.width = 30;
        this.height = 30;
        this.speed = 15; 
        this.image = View.loadImage("img/boomerang1.png"); 
        images = new BufferedImage[4]; 
        isActive = true; 
        for (int i = 1; i<4; i++)
        {
            String path = "img/boomerang" + i + ".png"; 
            images[i] = View.loadImage(path);
        }
    }

    public void draw(Graphics g, int scrollPosX, int scrollPosY)
    {
        g.drawImage(images[imageIndex], this.x-scrollPosX, this.y-scrollPosY, null); //draw a boomerang on the screen
    }

    public void collided()
    {
        isActive = false; //changes isActive to false after collision occurs
    }

    public boolean update()
    {
        x += x_direction*speed;
        y += y_direction*speed;
        imageIndex++;
        if (imageIndex == 4)
        {
            imageIndex = 0; 
        }
        return isActive; 
    }

    public Json marshal()
    {
        Json ob = Json.newObject(); 
        ob.add("bmgX", this.x);
        ob.add("bmgY", this.y); 
        ob.add("bmgW", this.width);
        ob.add("bmgH", this.height);
        return ob; //return a Json object 
    }

    @Override 
    boolean isBoomerang()
    {
        return true; 
    }

    /*@Override 
    string toString()
    {
        return "Boomerang coordinates: (" + this.x + "," + this.y + ")";
    }*/
}
