//NAME: Vincent Hassman
//DATE: 31 March 2023
/*DESCRIPTION: 
 * Manages attributes regarding the on-screen character
 * Loads images for the character into an array
 * Holds all 40 images for character
 * Offers functionality for avoiding tiles when a collision occurs
*/
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Link extends Sprite
{
    //Local previous coordinates for Link character 
    int prev_x;
    int prev_y;  

    static final double speed = 10; //Link's speed
    final int NUM_IMAGES = 40; //total number of images
    final int MAX_IMAGES_PER_DIRECTION = 10; //max images per direction 
    static BufferedImage[] images = null; //array of link images
    int currentImage; //index of current image
    /* Directions:
     * 0 = down
     * 1 = left
     * 2 = right
     * 3 = up     */
    int direction; 

    //default constructor 
    public Link(int x, int y)
    {
        this.x = x;
        this.y = y; 
        this.width = 55;
        this.height = 55;

        //load all character images
        image = View.loadImage("img/link01.png"); //a default image for Link
        images = new BufferedImage[NUM_IMAGES]; 
        images[0] = null; 
        for (int i = 1; i<NUM_IMAGES; i++)
        {   
            String j = String.format("%02d", (i));
            String path = "img/link" + j + ".png"; 
            images[i] = View.loadImage(path);
        }
        currentImage = 1; //start at image 1 in the array
        image = View.loadImage("img/link01.png");
        prev_x = x;
        prev_y = y; 
    }

    //a blank update method required by the interface of Sprite superclass
    public boolean update()
    {
        return true; 
    }

    //update the image number in the array
    public void updateImageNum(int direction)
    {
        this.direction = direction; 
        currentImage++; 
        if(currentImage >= MAX_IMAGES_PER_DIRECTION)
            currentImage = 1; //reset to image 1
    }

    //set previous coordinates for collision detection
    public void setPrevCoordinate()
    {
        this.prev_x = this.x;
        this.prev_y = this.y;
    }

    //marshal function
    public Json marshal() 
    {
        Json ob = Json.newObject(); 
        ob.add("linkX", this.x);
        ob.add("linkY", this.y); 
        ob.add("linkW", this.width);
        ob.add("linkH", this.height);
        return ob; //return a Json object 
    }

    //character can draw themselves
    public void draw(Graphics g, int scrollPosX, int scrollPosY)
    { 
        g.drawImage(images[currentImage + direction * MAX_IMAGES_PER_DIRECTION], this.x-scrollPosX, this.y-scrollPosY, width, height, null); 
    }

    //method to move Link back to previous coordinate when a collision occurs
    public void avoidObstacle(Sprite obstruction, int obstruction_x, int obstruction_y)
    {
        //fix collision with left of another sprite
        if (this.x + this.width >= obstruction_x && this.prev_x + this.width <= obstruction_x)
        {
            this.x = obstruction_x - this.width;  
        }
        //fix collision with right of another sprite
        if (this.x <= obstruction_x + obstruction.width && this.prev_x >= obstruction_x + obstruction.width)
        {
            this.x = obstruction_x + obstruction.width; 
        }
        //fix collision with top of another sprite
        if (this.y + this.height >= obstruction_y && this.prev_y + this.height <= obstruction_y)
        {
            this.y = obstruction_y - this.height; 
        }
        //fix collision with bottom of another sprite
        if (this.y <= obstruction_y + obstruction.height && this.prev_y >= obstruction_y + obstruction.height)
        {
            this.y = obstruction_y + obstruction.height;
        }
    }

    //special toString method for Link character
    @Override
    public String toString()
    {
        return "Link coordinates (x,y): " + this.x + "," + this.y + ")"; 
    }

    @Override
    boolean isLink()
    {
        return true; 
    }
}
