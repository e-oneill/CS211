package Lab8;

import java.util.*;

public class Maze{
    
    public static void main(String[] args){
        int lives = 100;
        String[] input = new String[20];
        input[ 0]="XXX XXXXXXX XXXXXX X";
        input[ 1]="XXX XXXXXXX XXXXXX X";
        input[ 2]="XXX      XX XXXX   X";
        input[ 3]="XXXXXXX XXXXXXXXXXXX";
        input[ 4]="XXXXXXX XX      XXXX";
        input[ 5]="XXX  XXXXX XXXX XXXX";
        input[ 6]="XX  X XXXX   XX XXXX";
        input[ 7]="XXX XXXXXXXX XX XXXX";
        input[ 8]="XX  X  XXXXX XX XXXX";
        input[ 9]="XXXXXX       XX XXXX";
        input[10]="X XXXX XX  XXXX XXXX";
        input[11]="     XXXX  XXXX XXXX";
        input[12]="XXXXXXXXXXXXXXX XXXX";
        input[13]="XXXXXX  XXXX    XXXX";
        input[14]="XX XX XXXXXX XX XXXX";
        input[15]="X  XX XXXXXX XX XXXX";
        input[16]="XX XX X  X   XX XX  ";
        input[17]="X  XXXXXXX XXXX XX X";
        input[18]="XX XXXXXXX XXXXXXX X";
        input[19]="XX XXXXXXX XXXXXXX X";
        int posX=10;
        int posY=10;
        
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
        System.out.println(posX+" "+posY);
        printboard(maze,posX,posY);
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
            System.out.println(posX+" "+posY+" "+lives);
            printboard(maze,posX,posY);
            lives--;
            if(posY%19==0||posX%19==0){
                System.out.println(posX+","+posY);
                System.exit(0);
            }
        }
        System.out.println("You died in the maze!");
    }

    
    public static void printboard(boolean[][] board, int posX, int posY){
        for(int y=0;y<20;y++){
            for(int x=0;x<20;x++){
                if(x==posX&&y==posY){
                    System.out.print(":)");
                }else{
                    if(board[x][y]==true){
                        System.out.print("  ");
                    }else{
                        System.out.print("■ ");
                    }
                }
            }
            System.out.println();
        }
        try{
            Thread.sleep(100);
        }catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }
    }
}

class Brain{

    static Tile last = null;
	static Tile start = null;
	static Tile currTile = null;
	static String lastMove = null;
    static LinkedList<Tile> path = new LinkedList<Tile>();
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
					if (last.west != null && last.west.north != null && last.west.north.east != null)
					{
						currTile = last.west.north.east;
					}
					else if (last.east != null && last.east.north != null && last.east.north.west != null)
					{
						currTile = last.east.north.west;
					}
					else if (last.north == null)
					{
						currTile = new Tile();
						last.north = currTile;
					}
					else
						currTile = last.north;
				
					currTile.south = last;
				break;
				case ("south"):
					if (last.east != null && last.east.south != null && last.east.south.west != null)
					{
						currTile = last.east.south.west;
					}
					else if (last.west != null && last.west.south != null && last.west.south.east != null)
					{
						currTile = last.west.south.east;
					}
					else if (last.south == null)
					{
						currTile = new Tile();
						last.south = currTile;
					}
					else
						currTile = last.south;
					currTile.north = last;
				break;
				case ("west"):
					if (last.south != null && last.south.west != null && last.south.west.north != null)
					{
						currTile = last.south.west.north;
					}
					else if (last.north != null && last.north.west !=null && last.north.west.south != null)
					{
						currTile = last.north.west.south;
					}
					else if (last.west == null)
					{
						currTile = new Tile();
						last.west = currTile;
					}
					else
						currTile = last.west;
					currTile.east = last;
				break;
				case ("east"):
					if (last.south != null && last.south.west != null && last.south.west.north != null)
					{
						currTile = last.south.west.north;
					}
					if (last.north != null && last.north.west != null && last.north.west.south != null)
					{
						currTile = last.north.west.south;
					}
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
		
		if (((currTile.north != null || !north)) && ((currTile.west != null || !west)) && ((currTile.south != null || !south)) && (currTile.east != null || !east))
		{
			currTile.deadEnd = true;
		}
//		if (start == null)
//		{
//			start = currTile;
//		}

		
		
		//start by eliminating case where there is only one direction to move in.
		if ((((north && (!south && !east && !west)) || (north && lastMove != "south"))  && (currTile.north == null || !currTile.north.deadEnd)) && (currTile.north == null || currTile.deadEnd))
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
		else if ((((east && (!north && !south && !west)) || (east && lastMove != "west")) && (currTile.east == null || !currTile.east.deadEnd)) && (currTile.east == null || currTile.deadEnd))
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
		else if ((((west && (!north && !south && !east)) || (west && lastMove != "east") ) && (currTile.west == null || !currTile.west.deadEnd)) && (currTile.west == null || currTile.deadEnd))
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
		else if ((((south && (!north && !east && !west)) || (south && lastMove != "north")) && (currTile.south == null || !currTile.south.deadEnd)) && (currTile.deadEnd ||currTile.south == null))
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
		
		path.push(currTile);
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
	int x;
	int y;
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

	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
//	public Tile(boolean north, boolean south, boolean east, boolean west) {
//		adjacency[0] = north;
//		adjacency[1] = south;
//		adjacency[2] = east;
//		adjacency[3] = west;
//	}
	
}

class Position {
	int x;
	int y;


}

