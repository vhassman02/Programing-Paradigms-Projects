//NAME: Vincent Hassman
//DATE: 10 March 2023
/*DESCRIPTION: 
 * Manages attributes regarding the on-screen character
 * Loads images for the character
 * Holds all 40 images for character
 * Offers functionality for avoiding tiles when a collision occurs
*/
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Link
{
    int x; 
    int y;
    int prev_x;
    int prev_y; 

    static final double speed = 10; //Link's speed
    static final int HEIGHT = 55; //Link height
    static final int WIDTH  = 55; //Link width
    final int NUM_IMAGES = 40; //total number of images
    final int MAX_IMAGES = 10; //max images per direction 
    static BufferedImage[] images=  null; //array of link images
    int currentImage; //index of current image
    int direction; //0 = down, 1 = left, 2 = right, 3 = up

    //default constructor 
    public Link(int x, int y)
    {
        this.x = x;
        this.y = y; 

        //load all character images
        if (images == null)
        {
            images = new BufferedImage[NUM_IMAGES]; 
            images[0] = null; 
            for (int i = 1; i<NUM_IMAGES; i++)
            {   
                String j = String.format("%02d", (i));
                String path = "img/link" + j + ".png"; 
                images[i] = View.loadImage(path);
            }
        }
        currentImage = 1; //start at image 1
        prev_x = x;
        prev_y = y; 
    }

    public void update()
    {
        
    }

    public void updateImageNum(int direction)
    {
        this.direction = direction; 
        currentImage++; 
        if(currentImage >= MAX_IMAGES)
            currentImage = 1; //reset to image 1
    }

    //set previous coordinates for collision detection
    public void setPrevCoordinate()
    {
        this.prev_x = this.x;
        this.prev_y = this.y;
    }

    //character can draw themselves
    void drawLink(Graphics g, int scrollPosX, int scrollPosY)
    { 
        g.drawImage(images[currentImage + direction * MAX_IMAGES], this.x-scrollPosX, this.y-scrollPosY, WIDTH, HEIGHT, null); 
    }

    //method to move Link back to previous coordinate when a collision occurs
    public void avoidTile(int tile_x, int tile_y)
    {
        //System.out.println("Collision fixed"); 
        //fix collision with left of tile
        if (this.x + Link.WIDTH >= tile_x && this.prev_x + Link.WIDTH <= tile_x)
        {
            this.x = tile_x - Link.WIDTH;  
        }
        //fix collision with right of tile
        if (this.x <= tile_x + Tile.TILE_WIDTH && this.prev_x >= tile_x + Tile.TILE_WIDTH)
        {
            this.x = tile_x + Tile.TILE_WIDTH; 
        }
        //fix collision with top of tile
        if (this.y + Link.HEIGHT >= tile_y && this.prev_y + Link.HEIGHT <= tile_y)
        {
            this.y = tile_y - Link.HEIGHT; 
        }
        //fix collision with bottom of tile
        if (this.y <= tile_y + Tile.TILE_HEIGHT && this.prev_y >= tile_y + Tile.TILE_HEIGHT)
        {
            this.y = tile_y + Tile.TILE_HEIGHT; 
        }
    }

    //toStirng method for Link character
    @Override
    public String toString()
    {
        return "Link coordinates (x,y): " + this.x + "," + this.y + ")"; 
    }
}
