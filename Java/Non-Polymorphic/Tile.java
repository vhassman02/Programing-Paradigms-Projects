//NAME: Vincent Hassman
//DATE: 10 March 2023
/*DESCRIPTION: 
 * Manages properties of tile objects
 * Keeps track of coordinates of each tile object
 * Responsible for sending/recieving data for the Model class' Json methods
 * Responsible for sending coordinate data for a desired tile 
 * Holds tile image
*/
import java.awt.Graphics;
import java.awt.image.BufferedImage; 

public class Tile 
{
    //position of a tile
    int x;
    int y; 
    //dimensions of a tile
    static final int TILE_WIDTH = 50;
    static final int TILE_HEIGHT = 50; 

    static BufferedImage tile_image = null; 

    //constructor with parameters 
    Tile(int x, int y)
    {
        this.x = x; 
        this.y = y; 
        //load an image if it is not already loaded
        if (tile_image == null)
        {
            tile_image = View.loadImage("img/tile.png"); 
        }     
    }

    //Json constructor
    public Tile(Json ob)
    {
        this.x = (int)ob.getLong("positionX"); 
        this.y = (int)ob.getLong("positionY"); 
        if (tile_image == null)
        {
            tile_image = View.loadImage("img/tile.png"); 
        }
    }

    //marshal method for a tile object 
    Json marshal()
    {
        Json ob = Json.newObject(); 
        ob.add("positionX", this.x);
        ob.add("positionY", this.y); 
        return ob; //return a Json object 
    }

    //tells the model class if a tile exists here 
    boolean tileIsHere(int x_pos, int y_pos)
    {
        //if the mouse coordinate corresponds to a tile location, tileIsHere returns true
        if(x_pos>=this.x && x_pos<=(int)(this.x+TILE_WIDTH) && y_pos>=(this.y) && y_pos <=(int)(this.y+TILE_HEIGHT))
            return true;
        //otherwise it returns false
        else 
            return false; 
    }

    void drawTile(Graphics g, int scrollPosX, int scrollPosY)
    {
        g.drawImage(tile_image, this.x-scrollPosX, this.y-scrollPosY, null); //put the tile on the screen
    }

    //toString method for tile 
    @Override
    public String toString()
    {
        return "Tile coordinates (x,y) = (" + x + ", " + y + "), w = " + TILE_WIDTH + ", h = " + TILE_HEIGHT; 
    }
}
