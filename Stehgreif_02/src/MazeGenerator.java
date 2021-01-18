import java.util.Random;

/**
* MazeGenerator
* This class is used to generate a maze using the recursive division algorithm.
*
* @author Felix Wuest
*/
public class MazeGenerator {

	static Random rnd = new Random();
	static int delay = 150;
	static String points = "";
	static int pointCounter = 0;

	/**
	 * This Method has to be initially called to generate a random Maze.
	 * 
	 * @param size					the size of the generated maze (x=y)
	 * @param corridor				the symbol used for Corridors
	 * @param wall					the symbol used for walls
	 * @param start					the symbol used for the start
	 * @param end					the symbol used for the end
	 * @return						returns a two dimensional Char Array with the random generated Maze
	 */
	public static char[][] generateMaze(int size, char corridor, char wall, char start, char end) {
		
		//Initialize the Maze Array with individual size
		char[][] maze = new char[size][size];

		//Fill Array with Corridor symbols
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				maze[i][j] = corridor;
			}
		}

		//Draw the Border around the maze
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (i == 0 || i == maze.length - 1 || j == 0 || j == maze[i].length - 1) {
					maze[i][j] = wall;
				}
			}
		}

		//the algorithm always generates a path from the same in to the same out point
		//initialize start symbol
		maze[1][0] = start;
		//initialize end Char
		maze[maze.length - 2][maze[0].length - 1] = end;
		
		//Call for the actual random maze generation
		divide(maze, 0, 0, maze.length - 1, maze[0].length - 1);
		
		System.out.println("Labyrinth mit der groesse "+size+" erfolgreich generiert!");
		
		return maze;
	}

	/**
	 * This Method generates a random maze with a chooseable size
	 * It uses the recursive division algorithm, so there is always a solution
	 * @param maze			the two Dimensional char array to write the maze to
	 * @param x				current X Position (0 at initial call)
	 * @param y				current Y Position (0 at initial call)
	 * @param width			current width of the maze (length of array[] at initial call)
	 * @param height		current height of the maze (length of array at initial call)
	 */
	public static void divide(char[][] maze, int x, int y, int width, int height) {
		
		/**
		 * RECURSIVE DIVISION MAZE GENERATION
		 * 
		 * The recursive division algorithm starts with a predefined "room", an array.
		 * It then divides the room either horizontally or vertically, 
		 * and calls itself recursively for both sides of the wall again.
		 * at the end this will generate a random maze which always has a solution.
		 * 
		 * 
		 * IF either width or height gets to small
		 * 		THEN the recursion stops at that point
		 * 
		 * IF the subroom is narrower than it is high OR if width==height its a random decision
		 * 		THEN generate a HORIZONTAL wall at a random height
		 * 			 and a Door at a random position on that wall 
		 * 			 to prevent walls blocking doors, Horizontal Walls will only be generated at an odd index
		 * 			 horizontal doors will be generated at an even index	
		 * 			 then recursively call the method again twice	
		 * 			 one for the subroom above the wall (parameters define the new smaller room)
		 *			 and one for the subroom below the wall (parameters define the new smaller room) 
		 * 
		 * ELSE 
		 *			 generate a VERTICAL wall at a random colummn
		 *			 and a Door at a random Height on that wall
		 *			 to prevent walls blocking doors, Vertical Walls will only be generated at an odd index
		 *			 vertical doors will be generated at an even index
		 * 			 then recursively call the method again twice
		 * 			 one for the subroom on left side of the wall (parameters define the new smaller room)
		 * 			 and one for the subroom on the right side of the wall (parameters define the new smaller room)
		 */
		
		
		//Print the maze in each step for a builing "animation"
		RightHandSolver.clearConsole();
		
		if(pointCounter<3) {
			points = points +".";
			pointCounter++;
		}else {
			pointCounter=0;
			points="";
		}
		
		
		System.out.println("Labyrinth wird generiert"+points);
		RightHandSolver.printMaze(maze);
		
		RightHandSolver.wait(delay);
			
		if (width <= 3 || height <= 3) {
			return;
		}

		if (width < height || (width == height && rnd.nextBoolean())) {
			//Generate Horizontal wall and door
			int randBarPosY = randomBetween(2, height - 2);
			do {
				randBarPosY = randomBetween(2, height - 2);
			} while (randBarPosY % 2 == 1);

			int doorPos = randomBetween(1, width - 2);
			do {
				doorPos = randomBetween(1, width - 2);
			} while (doorPos % 2 == 0);

			//Draw Horizontal Line	
			for (int i = x; i < x + width; i++) {

				if (i != doorPos + x) {
					maze[y + randBarPosY][i] = '.';
				} else {

				}
			}

			//Recursive generate Part above the wall
			divide(maze, x, y, width, randBarPosY);

			//Recursive generate Part below the wall
			divide(maze, x, y + randBarPosY, width, height - randBarPosY);

		} else {
			//Generate Vertical Wall and door
			int randBarPosX = randomBetween(2, width - 2);
			do {
				randBarPosX = randomBetween(2, width - 2);
			} while (randBarPosX % 2 == 1);

			int doorPos = randomBetween(1, height - 2);
			do {
				doorPos = randomBetween(1, height - 2);
			} while (doorPos % 2 == 0);

			//Draw Vertical Lines
			for (int i = y; i < y + height; i++) {

				if (i != doorPos + y) {
					maze[i][x + randBarPosX] = '.';
				} else {

				}
			}

			//Recursive generate left Part of the wall
			divide(maze, x, y, randBarPosX, height);

			//Recursive generate right Part of the wall
			divide(maze, x + randBarPosX, y, width - randBarPosX, height);

		}

	}

	/**
	 * Generates a random integer between (inclusive) a min and max value
	 * @param min		minimum value (inclusive)
	 * @param max		maximum value (inclusive)
	 * @return
	 */
	public static int randomBetween(int min, int max) {
		// inclusive Min/Max
		return rnd.nextInt((max - min) + 1) + min;
	}

}
