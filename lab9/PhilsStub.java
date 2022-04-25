package lab9;

import java.util.*;

public class PhilsStub{
    
    public static void main(String[] args){
        int lives = 200;
        Scanner myScanner = new Scanner(System.in);
        String[] input = new String[20];
        for(int i=0;i<20;i++){
            input[i]=myScanner.nextLine();
        }
        
        int posX=Integer.parseInt(myScanner.nextLine());
        int posY=Integer.parseInt(myScanner.nextLine());
        
        boolean[][] maze = new boolean[20][20];
        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++){
                if(input[i].charAt(j)=='X'){
                    maze[i][j]=false;
                }else{
                    maze[i][j]=true;
                }
            }
        }
        Brain myBrain = new Brain();
        
        
        while(lives>0){
            String move =myBrain.getMove(maze[posX+1][posY],maze[posX-1][posY],maze[posX][posY-1],maze[posX][posY+1]);
            if(move=="north"&&maze[posX+1][posY]){
                posX++;
            }else if(move=="south"&&maze[posX-1][posY]){
                posX--;
            }else if(move=="east"&&maze[posX][posY-1]){
                posY--;
            }else if(move=="west"&&maze[posX][posY+1]){
                posY++;
            }
            lives--;
            if(posY%19==0||posX%19==0){
                System.out.println(posX+","+posY);
                System.exit(0);
            }
        }
        System.out.println("You died in the maze!");
    }
}

class Brain{

    static Tile last = null;
	static Tile start = null;
	static Tile currTile = null;
	static String lastMove = null;
    
    public String getMove(boolean north, boolean south, boolean east, boolean west){
        // if last is null, we are starting the maze
		if (last == null)
		{
			currTile = new Tile();
			start = currTile;
		}
		/*if last is not null, we check if the tile we are on already exists
			if not, we create it. if it does, we set currTile to the already created tile
		*/
		else {
			switch (lastMove)
			{
				case ("north"):
					if (last.north == null)
					{
						currTile = new Tile();
						last.north = currTile;
					}
					else
						currTile = last.north;
				
					currTile.south = last;
				break;
				case ("south"):
					if (last.south == null)
					{
						currTile = new Tile();
						last.south = currTile;
					}
					else
						currTile = last.south;
					currTile.north = last;
				break;
				case ("west"):
					if (last.west == null)
					{
						currTile = new Tile();
						last.west = currTile;
					}
					else
						currTile = last.west;
					currTile.east = last;
				break;
				case ("east"):
					if (last.east == null)
					{
						currTile = new Tile();
						last.east = currTile;
					}
					else
						currTile = last.east;
					currTile.west = last;
				break;
			}
		}	
//		if (start == null)
//		{
//			start = currTile;
//		}
		
		
		
		//start by eliminating case where there is only one direction to move in.
		if (((north && (!south && !east && !west)) || (north && lastMove != "south"))  && (currTile.north == null || !currTile.north.deadEnd))
		{
			if (north && 
					((!south || (currTile.south != null && currTile.south.deadEnd)) 
					&& (!east || (currTile.east != null && currTile.east.deadEnd)) 
					&& (!west ||( currTile.west != null && currTile.west.deadEnd))) 
//					&& lastMove == "south"
					)
			{
				currTile.deadEnd = true;
			}
			lastMove = "north";
//			return lastMove;
		}
		else if (((east && (!north && !south && !west)) || (east && lastMove != "west")) && (currTile.east == null || !currTile.east.deadEnd))
		{
			if (east && 
					((!north || (currTile.north != null && currTile.north.deadEnd)) 
					&& (!south || (currTile.south != null && currTile.south.deadEnd)) 
					&& (!west || (currTile.west != null && currTile.west.deadEnd ))) 
//					&& lastMove == "east"
					)
			{
				currTile.deadEnd = true;
			}
			lastMove = "east";
//			return lastMove;
		}
		else if (((west && (!north && !south && !east)) || (west && lastMove != "east") ) && (currTile.west == null || !currTile.west.deadEnd))
		{
			if (west && 
					((!north || (currTile.north != null && currTile.north.deadEnd)) 
					&& (!south || (currTile.south != null && currTile.south.deadEnd)) 
					&& (!east || (currTile.east != null && currTile.east.deadEnd))) 
//					&& lastMove == "west"
					)
			{
				currTile.deadEnd = true;
			}
			lastMove = "west";
//			return lastMove;
		}
		else if (((south && (!north && !east && !west)) || (south && lastMove != "north")) && (currTile.south == null || !currTile.south.deadEnd))
		{
			if (south && 
					((!north || (currTile.north != null && currTile.north.deadEnd)) 
					&& (!east || (currTile.east != null && currTile.east.deadEnd)) 
					&& (!west || (currTile.west != null && currTile.west.deadEnd))) 
//					&& lastMove == "north"
					)
			{
				currTile.deadEnd = true;
			}
			lastMove = "south";
//			return lastMove;
		}
		
		
		last = currTile;
		return lastMove;
		//we have reached a dead-end;
//		System.out.println("Tile is a dead end");
//		currTile.deadEnd = true;
////		switch (lastMove)
////		{
////			cast
////		}
//		//should be unreachable
//		return null;
    }
}

class Tile {
	//adjacency matrix 0 = north, 1 = south, 2 = east, 3 = west
//	boolean[] adjacency = new boolean[4];
	boolean deadEnd = false;
	Tile west;
	Tile north;
	Tile east;
	Tile south;
	
	public Tile() {
//		adjacency[0] = north;
//		adjacency[1] = south;
//		adjacency[2] = east;
//		adjacency[3] = west;
	}
	
//	public Tile(boolean north, boolean south, boolean east, boolean west) {
//		adjacency[0] = north;
//		adjacency[1] = south;
//		adjacency[2] = east;
//		adjacency[3] = west;
//	}
	
}