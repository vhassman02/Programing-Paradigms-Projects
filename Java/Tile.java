//NAME: Vincent Hassman
//DATE: 31 March 2023
/*DESCRIPTION: 
 * Manages properties of tile objects
 * Keeps track of coordinates of each tile object
 * Responsible for marshaling an individual tile
*/
import java.awt.Graphics;
//import java.awt.image.BufferedImage; 

public class Tile extends Sprite
{
    //constructor with parameters 
    Tile(int x, int y)
    {
        this.x = x; 
        this.y = y; 
        this.width = 50;
        this.height = 50; 
        this.image = View.loadImage("img/tile.png");      
    }

    //Json constructor
    public Tile(Json ob)
    {
        this.x = (int)ob.getLong("tileX"); 
        this.y = (int)ob.getLong("tileY"); 
        width = 50;
        height = 50; 
        image = View.loadImage("img/tile.png"); 
    }

    //marshal method for a tile object 
    public Json marshal()
    {
        Json ob = Json.newObject(); 
        ob.add("tileX", this.x);
        ob.add("tileY", this.y); 
        ob.add("tileW", this.width);
        ob.add("tileH", this.height);
        return ob; //return a Json object 
    }

    //a blank update method required for interface set up by Sprite superclass
    public boolean update()
    {
        return true; 
    }

    public void draw(Graphics g, int scrollPosX, int scrollPosY)
    {
        g.drawImage(image, this.x-scrollPosX, this.y-scrollPosY, null); //draw a tile on the screen
    }

    //toString method for tile 
    @Override
    public String toString()
    {
        return "Tile coordinates (x,y) = (" + x + ", " + y + "), w = " + width + ", h = " + height; 
    }

    @Override
    boolean isTile()
    {
        return true;
    }
}
