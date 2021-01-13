import java.util.Random;

public class MazeGenerator {

	static Random rnd = new Random();

	public static void main(String[] args) {

			
		

	}
	
	public static char[][] generateMaze(int size, char corridor, char wall, char start, char end){
		
		char[][] maze = new char[size][size];
		
		// Fill array
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				maze[i][j] = ' ';
			}
		}
		
		//Maze durchgehen und Rahmen ziehen
		for(int i =0;i<maze.length;i++) {
			for(int j =0;j<maze[i].length;j++) {		
				if(i==0 || i==maze.length-1 || j==0 || j==maze[i].length-1) {
					maze[i][j]='.';
				}
			}
		}
		
		
		
		//initialize start Char
		maze[1][0]='B';
		
		//initialize end Char
		maze[maze.length-2][maze[0].length-1]='A';
		
		
		divide(maze, 0, 0, maze.length - 1, maze[0].length - 1);
		
		return maze;
		
	}
	

	public static void divide(char[][] maze, int x, int y, int width, int height) {



		if (width <= 4 || height <= 4) {
			return;
		}

		if (width < height || (width == height && rnd.nextBoolean())) {

			//Horizontal

			// Höhe für Schnitt zufällig, jede Zeile ausser die ersten und letzten BEIDEN
			
			//Wall even, Door Odd
			int randBarPosY = randomBetween(2, height - 3);
			do {
				randBarPosY = randomBetween(2, height - 3);
			}while(randBarPosY%2==1);
			
			

			int doorPos = randomBetween(1, width - 2);
			
			do {
				doorPos = randomBetween(1, width - 2);
			}while(doorPos%2==0);

//Draw Horizontal Line	
			for (int i = x; i < x+width; i++) {
				
				if (i != doorPos+x) {
					maze[y + randBarPosY][i] = '.';
				}else {
					
				}
			}

			

			// Ueber der Linie
			divide(maze, x, y, width, randBarPosY);

			// Unter der Linie
			divide(maze, x, y + randBarPosY, width, height - randBarPosY);

		} else {
			
			
			// Höhe für Schnitt zufällig, jede Zeile ausser die ersten und letzten BEIDEN

						int randBarPosX = randomBetween(2, width - 3);

						do {
							randBarPosX = randomBetween(2, width - 3);
						}while(randBarPosX%2==1);

						int doorPos = randomBetween(1, height - 2);
						do {
							doorPos = randomBetween(1, height - 2);
						}while(doorPos%2==0);
						//System.out.println("DoorPos: "+doorPos);

			// Draw Vertical Lines	
						for (int i = y; i < y+height; i++) {
							
							if (i != doorPos+y) {
								maze[i][x + randBarPosX] = '.';
							}else {
								
							}
						}

						
						// Links der Linie
						divide(maze, x, y, randBarPosX, height);

						// Rechts der Linie
						divide(maze, x+randBarPosX, y, width-randBarPosX, height);

			
			

		}

	}

	public static int randomBetween(int min, int max) {
		// inclusive Min/Max
		return rnd.nextInt((max - min) + 1) + min;
	}


}
