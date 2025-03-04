//NAME: Vincent Hassman
//DATE: 31 March 2023 
/*DESCRIPTION: 
 * Keeps track of all instances of sprite objects
 * Manages marshaling and unmarshaling of pots and tiles
 * Includes functionality to add and remove tiles and pots
 * Determines if sprite collisions are occuring
*/
import java.util.ArrayList;

class Model
{
    Link link; //redundant Link object

    //Arraylist of sprites
    ArrayList<Sprite> sprites; 

    //constructor 
    Model()
    {
        sprites = new ArrayList<Sprite>(); //initialize the ArrayList of sprites 
        link = new Link(52, 50); 
        sprites.add(link); //add a link object to the ArrayList   
    }

    //allow other classes to view Link's direction
    public int getLinkDirection()
    {
        return link.direction; 
    }

    void setLink(Link l)
	{
		link = l;
	}

    //add or remove tiles on the screen when mouse is clicked
    public void modifyTiles(int x_pos, int y_pos)
    {   
        //add a tile if there is no tile at location
        if (!spriteHere(x_pos, y_pos))
        {     
            Tile t = new Tile(x_pos-x_pos % 50, y_pos-y_pos % 50); //create a new tile object called "t"
            sprites.add(t); //add a tile "t" to the "tiles" ArrayList 
        }
        //otherwise remove the tile 
        else
        {
            for (int i = 0; i<sprites.size(); i++)
            {
                if (sprites.get(i).isTile())
                {
                    //obtain attributes of the current tile being looked at
                    Tile t = (Tile)sprites.get(i); 
                    int tile_x = x_pos-x_pos % t.width; 
                    int tile_y = y_pos-y_pos % t.height;
                    //find the tile and remove it
                    if (sprites.get(i).x==tile_x && sprites.get(i).y==tile_y)
                        sprites.remove(i);
                }            
            }
        }
    }

    //add pots on the screen when mouse is clicked
    public void modifyPots(int x_pos, int y_pos)
    {
        //add a pot if there is no pot at location
        if (!spriteHere(x_pos, y_pos))
        {
            Pot p = new Pot(x_pos, y_pos); //create a new pot when desired
            sprites.add(p); //add a pot "p" to the ArrayList 
        }
    }

    //makes Link release a boomerang when Ctrl is pressed
    public void throwBoomerang(int x_throwDirection, int y_throwDirection)
    {
        //determine coordinates for a new boomerang 
        int boomerangX = link.x+(link.width/2);
        int boomerangY = link.y+(link.height/2); 
        Boomerang b = new Boomerang(boomerangX, boomerangY); //create a new boomerang object
        b.x_direction = x_throwDirection;
        b.y_direction = y_throwDirection; 
        sprites.add(b); //add the boomerang to the ArrayList
    }

    //check if a sprite exists where mouse was clicked
    private boolean spriteHere(int x_pos, int y_pos)
    {
        //search ArrayList for index of sprite @ location (x_pos,y_pos)
        for (int i = 0; i<sprites.size(); i++)
        {
            if (sprites.get(i).spriteIsHere(x_pos, y_pos))
                return true; 
        }
        return false; //returns false if no sprite is found at (x_pos,y_pos)
    }

    public void update()
    {
        for (int i = 0; i<sprites.size(); i++)
        {
            for (int j = 0; j<sprites.size(); j++)
            {
                //determine if a collision is occuring with any sprite
                if (isColliding(sprites.get(i), sprites.get(j)))
                {
                    //if Link collides with a tile, avoid it
                    if (sprites.get(i).isLink() && sprites.get(j).isTile())
                    {
                        //update the link sprite location
                        link.avoidObstacle(sprites.get(j), sprites.get(j).x, sprites.get(j).y); 
                    }
                    //remove boomerang if it collides with a tile
                    if (sprites.get(i).isBoomerang() && sprites.get(j).isTile())
                    {
                        //remove the boomerang
                        ((Boomerang)sprites.get(i)).collided();
                    }

                    //remove boomerang if it collides with a pot
                    if (sprites.get(i).isBoomerang() && sprites.get(j).isPot())
                    {
                        //remove the boomerang
                        ((Boomerang)sprites.get(i)).collided();
                    }

                    //shatter the pot if boomerang collides with pot
                    if (sprites.get(i).isBoomerang() && sprites.get(j).isPot())
                    {
                        //make the pot become shattered
                        ((Pot)sprites.get(j)).shattered(); 
                    }
                    
                    //if Link walks into a pot, make the pot slide
                    if (sprites.get(i).isLink() && sprites.get(j).isPot())
                    {
                        //send Link's direction to the pot to make it slide in that direction
                        ((Pot)sprites.get(j)).slide(getLinkDirection());
                    }
                    
                    //if pot collides with tile, shatter and remove the pot
                    if (sprites.get(i).isTile() && sprites.get(j).isPot())
                    {
                        ((Pot)sprites.get(j)).shattered(); 
                    }

                    /*//if a pot collides with another pot, shatter both pots
                    if (sprites.get(i).isPot() && sprites.get(j).isPot() && (sprites.get(i)!=sprites.get(j)))
                    {
                        ((Pot)sprites.get(i)).shattered(); 
                    }*/
                }
            }
            //call an update method on the sprite object
            //if the update method returns false, remove the object
            if (sprites.get(i).update() == false)
                sprites.remove(i); 
        }
    }

    //determine if a general collision is occuring
    public boolean isColliding(Sprite a, Sprite b)
    {
        //clarify the dimensions of an object
        int a_left = a.x;
        int a_right = a.x + a.width; 
        int a_top = a.y;
        int a_bottom = a.y + a.height; 
        
        //detect right collision
        if (a_right < b.x)
            return false; 
        //detect left collision
        if (a_left > b.x+b.height)
            return false; 
        //detect bottom collision
        if (a_bottom < b.y)
            return false; 
        //detect top collision 
        if (a_top > b.y+b.height)
            return false; 
        return true; //return true if none of the conditions are met, so a collision is occurring 
    }

    //Marshaling function for tiles and pots
    public Json marshal()
    {
        System.out.println("marshaling"); 
        Json ob = Json.newObject(); //create a new Json object 
        Json tmpTileList = Json.newList(); //create a new Json temporary list 
        Json tmpPotList = Json.newList(); 
        ob.add("tiles", tmpTileList); //add a list of tiles to the Json object
        ob.add("pots", tmpPotList); //add a list of pots to the Json object

        for (int i = 0; i<sprites.size(); i++)
        {
            //marshal a tile
            if (sprites.get(i).isTile())
                tmpTileList.add(sprites.get(i).marshal()); 
            //marshal a pot
            if (sprites.get(i).isPot())
                tmpPotList.add(sprites.get(i).marshal()); 
        }

        ob.save("map.json"); //save the object to a file 
        System.out.println("Map was saved to file map.json"); 
        return ob;
    }
    //unmarshaling function for model 
    public void unmarshal(Json ob)
    {
        Json tmpList = ob.get("tiles"); //create a temporary Json list
        Json tmpListPots = ob.get("pots"); 

        //load tiles to screen
        for (int i = 0; i<tmpList.size(); i++)
        {
            //System.out.println(tmpList.get(i).toString()); 
            sprites.add(new Tile(tmpList.get(i))); //add all objects in Json list to Model list 
        }

        //load pots to screen
        for (int i = 0; i<tmpListPots.size(); i++)
        {
            sprites.add(new Pot(tmpListPots.get(i))); 
        }
		
		for (int i = 0; i<sprites.size(); i++)
		{
			if (sprites.get(i).isTile())
				//System.out.println("this.sprites.push(new Sprite(" + sprites.get(i).x + "," + sprites.get(i).y + ", img/tile.png, Sprite.prototype.linkUpdate);");
                System.out.println("self.sprites.append(Tile("+sprites.get(i).x +","+sprites.get(i).y+",50,50,img/tile.png))");
			if (sprites.get(i).isPot())
				//System.out.println("this.sprites.push(new Sprite(" + sprites.get(i).x + "," + sprites.get(i).y + ", img/pot_normal.png, Sprite.prototype.potUpdate);");
                System.out.println("self.sprites.append(Pot("+sprites.get(i).x +","+sprites.get(i).y+",50,50,img/pot_normal.png))");
		}
    }
}
