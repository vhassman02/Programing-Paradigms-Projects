//NAME: Vincent Hassman
//DATE: 10 March 2023 
/*DESCRIPTION: 
 * Keeps track of all instances of tiles
 * Manages marshaling and unmarshaling of Json objects
 * Includes functionality to add and remove tiles
 * Determines if a collision with a sprite is occuring
*/
import java.util.ArrayList;
import java.util.Iterator;

class Model
{
    Link link; //instance of the Link object 

    //Declare a variable "tiles" of ArrayList type
    ArrayList<Tile> tiles; 

    //constructor 
    Model()
    {
        tiles = new ArrayList<Tile>(); //initialize the tiles ArrayList
    }

    void setLink(Link l)
	{
		link = l;
	}

    //unmarshaling function for model 
    public void unmarshal(Json ob)
    {
        Json tmpList = ob.get("tiles"); //create a temporary Json list
        tiles.clear(); //clear the existing ArrayList 
        for (int i = 0; i<tmpList.size(); i++)
        {
            tiles.add(new Tile(tmpList.get(i))); //add all objects in Json list to Model list 
        } 
    }

    //Marshaling function for model 
    public Json marshal()
    {
        Json ob = Json.newObject(); //create a new Json object 
        Json tmpList = Json.newList(); //create a new Json temporary list 
        ob.add("tiles", tmpList); //add the list to the Json object and call it "tiles"
        
        //iterator over arraylist to marshal tiles into Json file
        Iterator<Tile> tile = tiles.iterator(); 
        while(tile.hasNext())
        {
            tmpList.add(tile.next().marshal()); //retrieve tile information from Tile marshal method
        }
        ob.save("map.json"); //save the object to a file 
        System.out.println("Map was saved to file map.json"); 
        return ob;
    }

    //change tiles that appear on the screen when mouse clicked
    public void modifyTile(int x_pos, int y_pos)
    {
        //calculate a tile location 
        int tile_x = x_pos-x_pos % Tile.TILE_WIDTH; 
        int tile_y = y_pos-y_pos % Tile.TILE_HEIGHT;
        
        //add a tile if there is no tile at location
        if (!tileHere(x_pos, y_pos))
        {     
            Tile t = new Tile(tile_x,tile_y); //create a new tile object called "t"
            tiles.add(t); //add a tile "t" to the "tiles" ArrayList 
        }
        //otherwise remove the tile 
        else
        {
            for (int i = 0; i<tiles.size(); i++)
            {
                if (tiles.get(i).x==tile_x && tiles.get(i).y==tile_y)
                    tiles.remove(i);
            }
        }
    }

    //check if tile exists where mouse was clicked
    private boolean tileHere(int x_pos, int y_pos)
    {
        //search ArrayList for index of tile @ location (x,y)
        for (int i = 0; i<tiles.size(); i++)
        {
            if (tiles.get(i).tileIsHere(x_pos, y_pos))
                return true; 
        }
        return false; //returns false if no tile is found at (x,y)
    }

    public void update()
    {
        for (int i = 0; i<tiles.size(); i++)
        {
            if (isTileCollisionOccurring(tiles.get(i)) == true)
            {
                //System.out.println("A collision was detected with tile at: (" + tiles.get(i).x + "," + tiles.get(i).y + ")"); 
                link.avoidTile(tiles.get(i).x, tiles.get(i).y); //call avoid tile in Link.java
            }
        }
        link.update(); 
    }

    //determine if Link is colliding with a tile on the map 
    public boolean isTileCollisionOccurring(Tile t)
    {
        //clarify the dimensions of link character
        int linkLeft = link.x;
        int linkRight = link.x + Link.WIDTH; 
        int linkTop = link.y;
        int linkBottom = link.y + Link.HEIGHT; 
        
        //detect right collision
        if (linkRight < t.x)
        {
            return false; 
        }
        //detect left collision
        if (linkLeft > t.x+Tile.TILE_HEIGHT)
        {
            return false; 
        }
        //detect bottom collision
        if (linkBottom < t.y)
        {
            return false; 
        }
        //detect top collision 
        if (linkTop > t.y+Tile.TILE_HEIGHT)
        {
            return false; 
        }
        return true; //return true if none of the conditions are met, so a collision is occurring 
    }

}
