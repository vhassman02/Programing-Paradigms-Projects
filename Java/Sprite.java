//NAME: Vincent Hassman
//DATE: 31 March 2023
/* DESCRIPTION: Generalized abstract class representing all tile, link, pot, boomerang objects
 * Requires all child classes to have draw, update, and marshal methods
 * Provides a default toString method
 * Holds images and coordinates/dimensions of all sprite-type objects
*/

import java.awt.Graphics;
import java.awt.image.BufferedImage; 

public abstract class Sprite
{
    //variables for dimensions and location of any sprite object
    int x;
    int y;
    int width;
    int height;
    //a generic image for all sprite objects
    BufferedImage image; 

    boolean isActive;
    
    //generic drawing imgae method (implemented within subclasses)
    public abstract void draw(Graphics g, int scrollPosX, int scrollPosY);
    //generic update method (implemented within subcalsses)
    public abstract boolean update();
    //generic marshal method 
    public abstract Json marshal(); 

    //determine if a sprite exists at some desired coordinates 
    public boolean spriteIsHere(int coordinate_x, int coordinate_y)
    {
        //if the mouse coordinate corresponds to a tile location, tileIsHere returns true
        if(coordinate_x>=this.x && coordinate_x<=(int)(this.x+width) && coordinate_y>=(this.y) && coordinate_y <=(int)(this.y+height))
            return true;
        //otherwise it returns false
        else 
            return false; 
    }
    
    public String toString()
    {
        return "Sprite: (" + x + "," + y + ")";
    }

    //Sprite type checking methods
    boolean isTile()
    {
        return false;
    }

    boolean isLink()
    {
        return false; 
    }

    boolean isPot()
    {
        return false; 
    }

    boolean isBoomerang()
    {
        return false; 
    }
}
