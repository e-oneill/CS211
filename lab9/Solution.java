package lab9;

public class Solution {
	//variables that my getMove uses to track
	static Tile last = null;
	static Tile start = null;
	static Tile currTile = null;
	static String lastMove = null;
	
	
	public static String[] maze1 = {
	                               "XXXXXXXX X",
	                               "X XXXXXX X",
	                               "X  XXXXX X",
	                               "XX       X",
	                               "XX XXX XXX",
	                               "XX XXX XXX",
	                               "XX  XX XXX",
	                               "XXX XX    ",
	                               "XXX XXXXXX",
	                               "XXXXXXXXXX",
	};
	
	public static String[] maze2 = {
			"XXXXXXXXXXXXXXXXXXXX",
            "X XXXXXXXXXXXXX XXXX",
            "   XXXXX           X",
            "XX X      XXXXXXXX X",
            "XX XXXXXX XXXXXXXX X",
            "XX XXXXXX XXXXXXXX X",
            "XX  XXX   XXXXXXXX X",
            "XXX XXXXXXXXXXXXXX X",
            "XXX XXXXXXXXXXXXXX X",
            "XXX XXXXXXXXXXXXXX X",
            "XXX XXXXXXXXXX     X",
            "XXX XXXXXXXXXX XXXXX",
            "XXX XXXXXXXXXX     X",
            "XX  X    XXXXXXXXX X",
            "XX XXX XXXXXXXXXXX X",
            "XX XXX XXXXXXXXXXX X",
            "XX  XX XXXXXXXXXXX X",
            "XXX XX             X",
            "XXX XXXXXXXXXXXXXX X",
            "XXX XXXXXXXXXXXXXXXX",
	};
	
	public static String[] maze3 = {
			"XXXXXXXXXXXXXXXXXX X",
            "X XXXXXXXXXXXXX XX X",
            "X  XXXXX           X",
            "XX        XXXXXXXX X",
            "XX XXXXXX XXXXXXXX X",
            "XX XXXXXX XXXXXXXX X",
            "XX  XX    XXXXXXXX X",
            "XXX XX XXXXXXXXXXX X",
            "XXX    XXXXXXXXXXX X",
            "XXX XXXXXXXXXXXXXX X",
            "XXX XXXXXXXXXX     X",
            "XXX XXXXXXXXXX XXXXX",
            "XXX XXXXXXXXXX     X",
            "XX       XXXXXXXXX X",
            "XX XXX XXXXXXXXXXX X",
            "XX XXX XXXXXXXXXXX X",
            "XX  XX XXXXXXXXXXX X",
            "XXX XX             X",
            "XXX XXXXXXXXXXXXXX X",
            "XXX XXXXXXXXXXXXXXXX",
	};
	
	public static String[] maze4 = {
			"XXXXXXXXXXXXXXXXXX X",
            "X XXXXXXXXXXXXX XX X",
            "X  XXXXX           X",
            "XX X      XXXXXXXX X",
            "XX XXXXXX XXXXXXXX X",
            "XX XXXXXX XXXXXXXX X",
            "XX  XX    XXXXXXXXXX",
            "XXX XX XX          X",
            "XXX X  XXXXXXXXXXX X",
            "XXX XXXXXXXXXXXXXX X",
            "XXX XXXXXXXXXX     X",
            "XXX XXXXXXXXXX XXXXX",
            "XXX XXXXXXXXXX     X",
            "XX       XXXXXXXXX X",
            "XX XXX XXXXXXXXXXX X",
            "XX XXX XXXXXXXXXXX X",
            "XX  XX XXXXXXXXXXX X",
            "XXX XX             X",
            "XXX XXXXXXXXXXXXXX X",
            "XXX XXXXXXXXXXXXXXXX",
	};

	public static String[] maze5 = {
		"XXXXXXXXXXXXXXXXXX X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "XXX XXXXXXXXXXXXXXXX",
	};

	public static String[] maze6 = {
		"X XXXXXXXXXXXXXXXXXX",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "X                  X",
        "XXXXXXXXXXXXXXXXXX X",
	};
	
	
	// //maze 1 settings
	// public static String[] maze = maze1;
	// static int[] curr = {0, 8}; //starting position
	// static int[] target = {7, 9};
	
	//maze 2 settings
	// public static String[] maze = maze2;
	// static int[] curr = {19, 3};
	// static int[] target = {2, 0};
	
//	//maze 3 settings 
	// public static String[] maze = maze3;
	// static int[] curr = {19, 3};
	// static int[] target = {0, 18};
	
	// //maze 4 settings 
	// 	public static String[] maze = maze4;
	// 	static int[] curr = {19, 3};
	// 	static int[] target = {0, 18};

	// //maze 5 settings 
		// public static String[] maze = maze5;
		// static int[] curr = {19, 3};
		// static int[] target = {0, 18};

	// //maze 6 settings 
		public static String[] maze = maze6;
		static int[] curr = {19, 18};
		static int[] target = {0, 1};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int steps = 0;
		System.out.println(maze.length);
		System.out.println(maze[0].length());
		boolean hasNextMove = true;
		while (hasNextMove)
		{
			steps++;
			boolean[] adjacents = getCanMove(curr[0], curr[1]);
//			System.out.println();
//			for (boolean canMove : adjacents)
//			{
//				System.out.print(canMove + " ");
//			}
//			System.out.println();
			String nextMove = (getMove(adjacents[0], adjacents[1], adjacents[2], adjacents[3]));
			System.out.println(nextMove);
			switch (nextMove)
			{
				case ("north"):
					curr[0]--;
				break;
				case ("south"):
					curr[0]++;
				break;
				case ("west"):
					curr[1]--;
				break;
				case ("east"):
					curr[1]++;
				break;
				default:
					System.out.println("Dead End");
//					hasNextMove = false;
					break;
			}
			if (curr[0] == target[0] && curr[1] == target[1])
			{
				System.out.println("Finished maze");
				hasNextMove = false;
			}
		}
		System.out.println(steps);
		
		
		
	}
	
	public static boolean[] getCanMove(int row, int column)
	{
//		System.out.print(row + "," + column + " ");
		boolean[] adjacents = new boolean[4];
		//NORTH
		if (row > 0 && maze[row-1].charAt(column) != 'X')
		{
			adjacents[0] = true;
		}
		else
			adjacents[0] = false;
		
		//SOUTH
		if (row < (maze.length - 2) && maze[row+1].charAt(column) != 'X')
		{
			adjacents[1] = true;
		}
		else
			adjacents[1] = false;
		
		//EAST 
		if (column < (maze[row].length() - 2) && maze[row].charAt(column+1) != 'X')
		{
			adjacents[2] = true;
		}
		else
			adjacents[2] = false;
		
		//WEST
		if (column > 0 && maze[row].charAt(column-1) != 'X')
		{
			adjacents[3] = true;
		}
		else
			adjacents[3] = false;
		return adjacents;
	}
	
	public static String getMove(boolean north, boolean south, boolean east, boolean west) {
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

