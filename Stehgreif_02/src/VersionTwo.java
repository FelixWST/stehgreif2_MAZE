import java.awt.Point;
import java.util.Random;

public class VersionTwo {
	
	static MazeGenerator mazeGen = new MazeGenerator();

	static char NORTH='N';
	static char EAST='E';
	static char SOUTH='S';
	static char WEST='W';
	
	static char[] direction = {NORTH, EAST, SOUTH, WEST};
	static int directionIndex;
	static char forwardDirection;
	
	static char BB8 = 'B';
	static char R2D2 = 'A';
	static char WALL = '.';
	static char CORRIDOR =' ';
	
	static int stepCounter =0;
	static int selectedMap =0;
	static int delay=200;
	
	static char[][]selectedMaze;
	static char[][]traceMap;
	
    private static long startTime;
    private static long endTime;
    private static long algorithmTime;
    
	


	public static void main (String[] args) {
		

		//Zeitmessung
		directionIndex = 1;
		forwardDirection = direction[directionIndex];
	
		selectedMaze = deepCopyArray(mazeGen.generateMaze(25, CORRIDOR, WALL, BB8, R2D2));
		traceMap = deepCopyArray(selectedMaze);


		Point startingPoint = new Point(findStartingPoint(selectedMaze)[0],findStartingPoint(selectedMaze)[1]);
		
		startTime = System.currentTimeMillis();
		solveMaze(startingPoint);
		
		System.out.println(algorithmTime+" ms");
		
		directionIndex = 1;
		stepCounter = 0;
		startTime=0;
		endTime=0;
		
		
		//Debug mode with position print etc -> Boolean debug
		
	}
		
	
	
	public static void solveMaze(Point p) {
		
		stepCounter++;
		endTime = System.currentTimeMillis(); 

		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(selectedMaze[p.y][p.x] == R2D2) {
			
			System.out.println("Solved it!");
			
			for(char [] row : traceMap) {
				for(char element : row) {
					System.out.print(element+" ");
				}
				System.out.println();
			}
			
		}else {
			selectedMaze[p.y][p.x] = BB8; 
			 
			//Clean Console
			for(int i = 0; i<100; i++) {
				System.out.println();
			}
			
			printMaze(selectedMaze);
	
			algorithmTime = (endTime-startTime)-(stepCounter*delay); 
			
			System.out.println("Point(x|y): ("+p.x+" | "+p.y+")");
			System.out.println("Currently Facing "+forwardDirection);
			System.out.println("Iterations: "+stepCounter);
			
			if(lookRight(p)!=WALL) {
				turnRight();
				moveForward(p);
				solveMaze(p);
			}else if(lookForward(p)!=WALL) {
				moveForward(p);
				solveMaze(p);
			}else if(lookLeft(p)!=WALL) {
				turnLeft();
				moveForward(p);
				solveMaze(p);
			}else { //DEAD END
				turnRight();
				turnRight();
				moveForward(p);
				solveMaze(p);
			}
	
		}
	
	}
	
	
	public static char[][] selectMap(int level){
		
		char [][] map1 = {
				{'.','.','.','.','.','.','.','.','.','.','.','.','.'},
				{'.',' ',' ',' ',' ',' ','.','.',' ',' ',' ',' ','.'},
				{'.',' ','.','.','.',' ',' ','.',' ',' ','.',' ','.'},
				{'.',' ',' ',' ','.','.',' ','.','.',' ','.',' ','A'},
				{'.','.','.',' ','.',' ',' ',' ',' ',' ','.',' ','.'},
				{'.',' ',' ',' ','.','.',' ','.','.',' ','.',' ','.'},
				{'.',' ','.','.',' ',' ',' ','.',' ',' ','.',' ','.'},
				{'B',' ','.',' ',' ','.',' ','.',' ','.','.',' ','.'},
				{'.','.','.','.','.','.','.','.','.','.','.','.','.'}
			};
		
		char [][] map2 = {
				{'.','.','.','.','.','.','.','.','.','.','.','.','.'},
				{'.',' ',' ',' ',' ',' ','.','.',' ',' ',' ',' ','.'},
				{'.',' ','.','.','.',' ',' ','.',' ',' ','.',' ','.'},
				{'.',' ',' ',' ','.','.',' ','.','.',' ','.',' ','A'},
				{'.','.','.',' ','.',' ',' ',' ',' ',' ','.',' ','.'},
				{'.',' ',' ',' ','.','.',' ','.','.',' ','.',' ','.'},
				{'.',' ','.','.',' ',' ',' ','.',' ',' ','.',' ','.'},
				{'B',' ','.',' ',' ','.',' ','.',' ','.','.',' ','.'},
				{'.','.','.','.','.','.','.','.','.','.','.','.','.'}
			};
		
		char [][] map3 = {
				{'.','.','.','.','.','.','.','.','.','.','.','.','.'},
				{'.',' ',' ',' ',' ',' ','.','.',' ',' ',' ',' ','.'},
				{'.',' ','.','.','.',' ',' ','.',' ',' ','.',' ','.'},
				{'.',' ',' ',' ','.','.',' ','.','.',' ','.',' ','A'},
				{'.','.','.',' ','.',' ',' ',' ',' ',' ','.',' ','.'},
				{'.',' ',' ',' ','.','.',' ','.','.',' ','.',' ','.'},
				{'.',' ','.','.',' ',' ',' ','.',' ',' ','.',' ','.'},
				{'B',' ','.',' ',' ','.',' ','.',' ','.','.',' ','.'},
				{'.','.','.','.','.','.','.','.','.','.','.','.','.'}
			};
		
		
		char [][][] maps = {map1, map2, map3};
		//Invalid???
		return maps[level];
	}
	
	public static void printMaze(char[][] maze) {

		for (char[] row : maze) {
			for(char element : row) {
				System.out.print(element+" ");
			}
			System.out.println();
		}
	}
	
	public static int[] findStartingPoint(char[][] selectedMap) {
		
		int row = 0;
		int colummn = 0;
		
		
		for(int i=0; i<selectedMap.length;i++) {
			
			for(int j=0; j<selectedMap[i].length;j++) {
				if(selectedMap[i][j]==BB8) {
					row = i;
					colummn = j;	
				}
			}
		}
		return new int []{colummn, row} ;
	}
	
	public static void moveForward(Point p) {
		selectedMaze[p.y][p.x] = 'b';
		
		if(forwardDirection==NORTH) {
			traceMap[p.y][p.x]='^';
			p.y--;	
		}else if(forwardDirection==EAST) {
			traceMap[p.y][p.x]='>';
			p.x++;
		}else if(forwardDirection==SOUTH) {
			traceMap[p.y][p.x]='v';
			p.y++;
		}else{ //West
			traceMap[p.y][p.x]='<';
			p.x--;
		}	
	}
	
	public static char lookRight(Point p) {
		
		if (forwardDirection==NORTH) {
			return selectedMaze[p.y][p.x+1];
		}else if (forwardDirection==EAST) {
			return selectedMaze[p.y+1][p.x];
		}else if (forwardDirection==SOUTH) {
			return selectedMaze[p.y][p.x-1];
		}else { //WEST
			return selectedMaze[p.y-1][p.x];
		}
	}
	
	public static char lookLeft(Point p) {
		
		if (forwardDirection==NORTH) {
			return selectedMaze[p.y][p.x-1];
		}else if (forwardDirection==EAST) {
			return selectedMaze[p.y-1][p.x];
		}else if (forwardDirection==SOUTH) {
			return selectedMaze[p.y][p.x+1];
		}else { //WEST
			return selectedMaze[p.y+1][p.x];
		}	
	}
	
	public static char lookForward(Point p) {
		
		if (forwardDirection==NORTH) {
			return selectedMaze[p.y-1][p.x];
		}else if (forwardDirection==EAST) {
			return selectedMaze[p.y][p.x+1];
		}else if (forwardDirection==SOUTH) {
			return selectedMaze[p.y+1][p.x];
		}else { //WEST
			return selectedMaze[p.y][p.x-1];
		}
	}
		
	public static void turnRight() {
		directionIndex = (directionIndex+1)%4;
		forwardDirection = direction[directionIndex];
	}
	
	public static void turnLeft() {
		directionIndex = (directionIndex+3)%4;
		forwardDirection = direction[directionIndex];
	}
	
	public static char[][] deepCopyArray(char[][]master) {
		
	int length = master.length;
	char[][] copy = new char[length][master[0].length];
		for(int i = 0; i < master.length;i++) {
			for(int j = 0; j<master[i].length;j++) {
				copy[i][j] = master[i][j];
			}
		}
		return copy;
	}
}
