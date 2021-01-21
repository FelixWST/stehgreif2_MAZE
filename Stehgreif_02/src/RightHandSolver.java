import java.awt.Point;

/**
* Stegreifprojekt 2: Das Labyrinth
* This class guides the user through the process of BB8 solving a Maze by using the right-hand-rule.
* The Maze additionally can be generated randomly with the class MazeGenerator.
*
* @author Felix Wuest, Max Muthler, Marvin Wernli
*/

public class RightHandSolver {
	
	//Directions for TraceMap
	static final char NORTH='^';
	static final char EAST='>';
	static final char SOUTH='v';
	static final char WEST='<';
	
	//Variables for keeping up with the Direction
	static final char[] direction = {NORTH, EAST, SOUTH, WEST};
	static int directionIndex=1;
	static char forwardDirection = direction [directionIndex];
	
	//Variables for printing the maze
	static final char BB8 = 'B';
	static final char R2D2 = 'A';
	static final char WALL = '.';
	static final char CORRIDOR =' ';
	
	//User Input
	static final char INPUT_YES = 'y';
	static final char INPUT_NO = 'n';
	static int userInputInt;
	static char userInputChar;
	
	//GameModes
	static final int PREDEFINED_MODE = 1;
	static final int RANDOM_MODE = 2; 
	
	//Chosing one of predefined Mazes
	static final int MAZE_COUNT = 3;
	static int selectedMap =0;
	
	//Settings and Debugging
	static int stepCounter =0;
	static int delay=200;
	static boolean debug = false;
	private static long startTime;
    private static long algorithmTime;
	
	//Arrays for the Maze and TraceMap
	static char[][]selectedMaze;
	static char[][]traceMap;
	
	//GUI
	static Gui window = new Gui();
	static Point startingPoint = new Point();
	static boolean windowMode = false;

	public static void main (String[] args) {
		
		System.out.println("DAS LABYRINTH");
		System.out.println("======================================================================");
		System.out.println("Willkommen in der Welt von Star Wars!");
		System.out.println("======================================================================");
		System.out.println("BB8 hat sich auf den Weg gemacht, um seinen Freund R2D2 zu suchen.");
		System.out.println("Dummerweise hat er sich verlaufen und sucht nun nach dem Ausgang...");
		System.out.println("======================================================================");
		System.out.println("Du entscheidest, durch welches Labyrinth BB8 herausfinden muss!");
		System.out.println("(1) Waehle entweder eines von "+MAZE_COUNT+" vorgefertigen Labyrinthen aus...");
		System.out.println("(2) ...oder lasse eins zufaellig generieren!\n");
		System.out.print("Was ist deine Wahl?: ");
		
		userInputInt = StaticScanner.nextInt();
		
		while(userInputInt != PREDEFINED_MODE && userInputInt != RANDOM_MODE) {
			System.out.println("Bitte gebe eine valide Zahl ein!");
			System.out.println("(1) Waehle entweder eines von 	rei vorgefertigen Labyrinthen aus...");
			System.out.println("(2) ...oder lasse eins zufaellig generieren!\n");
			System.out.print("Was ist deine Wahl?: ");
			userInputInt = StaticScanner.nextInt();
		}
		
		
		if(userInputInt==PREDEFINED_MODE) {
			//User can pick one of the predefined mazes
			for(int i=0; i<MAZE_COUNT; i++) {
				System.out.println("Gebe "+i+" ein, fuer dieses Labyrinth: ");
				printMaze(selectMap(i));
			}
			
			System.out.print("Welches Labyrinth moechtest du auswaehlen?: ");
			userInputInt = StaticScanner.nextInt();
			
			while(userInputInt>=MAZE_COUNT || userInputInt<0) {
				System.out.println("\nDu kannst nur Labyrinthe zwischen 0 und "+MAZE_COUNT+" waehlen!");
				System.out.print("Welches Labyrinth moechtest du auswaehlen?: ");
				userInputInt = StaticScanner.nextInt();
			}
			
			selectedMaze = deepCopyArray(selectMap(userInputInt));
			System.out.println("Du hast Labyrinth "+userInputInt+" ausgewaehlt!");
			
		}else {
			//Random Maze Generation with size given by User
		System.out.println("\n### HINWEIS: am besten sehen Labyrinthe mit ungerade groessen aus ###");
		System.out.print("Welche groesse soll das zufaellig generierte Labyrinth haben? (5-50): ");
		userInputInt = StaticScanner.nextInt();
		
		while(userInputInt<5 || userInputInt>50) {
			System.out.println("\nZu grosse Labyrinthe koennen je nach Leistung zu problemen fuehren.");
			System.out.println("Zu kleine Labyrinthe koennen nicht generiert werden.");
			System.out.print("Waehle eine groesse zwischen 5 und 50: ");
			userInputInt = StaticScanner.nextInt();
		}
			selectedMaze = deepCopyArray(MazeGenerator.generateMaze(userInputInt, CORRIDOR, WALL, BB8, R2D2));
		}
		
		createTraceMap();
		startingPoint = new Point(findStartingPoint(selectedMaze)[0],findStartingPoint(selectedMaze)[1]);
		
		
		System.out.print("\nMoechtest Du das Programm im Debug Modus laufen lassen? (Y/N): ");
		userInputChar = Character.toLowerCase(StaticScanner.nextChar());
		
		
		while(userInputChar != INPUT_YES && userInputChar != INPUT_NO) {
			System.out.println("Bitte gebe nur Y fuer Ja und N fuer Nein ein!");
			System.out.println("Moechtest Du das Programm im Debug-Modus laufen lassen? (Y/N): ");
			userInputChar = Character.toLowerCase(StaticScanner.nextChar());
		}
		
		if(userInputChar == INPUT_YES) {
			debug = true;
			System.out.println("Okay, der Debug-Modus ist aktiviert!");
			delay = delay*2;
		}
		
		
		System.out.print("\nMoechtest Du das Labyrinth zusätzlich im Fenster sehen (Y/N): ");
		userInputChar = Character.toLowerCase(StaticScanner.nextChar());
		
		
		while(userInputChar != INPUT_YES && userInputChar != INPUT_NO) {
			System.out.println("Bitte gebe nur Y fuer Ja und N fuer Nein ein!");
			System.out.println("Moechtest Du das Labyrinth zusätzlich im Fenster sehen (Y/N): ");
			userInputChar = Character.toLowerCase(StaticScanner.nextChar());
		}
		
		if(userInputChar == INPUT_YES) {
			windowMode = true;
			System.out.println("Okay, der Fenster-Modus ist aktiviert!");
		}
		
		startTime = System.currentTimeMillis();

		
		if(windowMode) {
			window.open(selectedMaze[0].length, selectedMaze.length);
		}
		
		solveMaze(startingPoint);
		
	}

	/**
	 * This method is the main method guiding through the maze solving process
	 * solveMaze will call itself after each step to make the next step  
	 * 
	 * @param p			Point with the current X and Y Coordinate of BB8
	 */
	public static void solveMaze(Point p) {
		stepCounter++;

		wait(delay);
		if(windowMode) {
			//repaint the Window Graphics
			window.repaint();	
		}
		
		if(selectedMaze[p.y][p.x] == R2D2) {
			selectedMaze[p.y][p.x] = BB8;
			traceMap[p.y][p.x] = forwardDirection; 
			clearConsole();
			printMaze(selectedMaze);
			if(debug) {
				printStats(p);
			}
			wait(delay);
			clearConsole();
			Gui.done=true;
			window.repaint();
			escaped();
		}else {
			selectedMaze[p.y][p.x] = BB8;  
			clearConsole();
			printMaze(selectedMaze);
			if(debug) {
				printStats(p);
			}
			
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
	
	/**
	 * This method contains all the pre-generated levels from which the user can choose
	 * and returns the selected one.
	 * 
	 * @param level			indicates the level chosen by the user to return
	 * @return				returns the chosen level as an 2D char array
	 */
	public static char[][] selectMap(int level){
		char [][] map1 = {
				{WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL},
				{WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL},
				{WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,WALL,CORRIDOR,WALL,WALL,CORRIDOR,WALL,CORRIDOR,R2D2},
				{WALL,WALL,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL},
				{WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,WALL,CORRIDOR,WALL,WALL,CORRIDOR,WALL,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL},
				{BB8,CORRIDOR,WALL,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,WALL,CORRIDOR,WALL},
				{WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL}
			};
		
		char [][] map2 = {
				{WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL},
				{BB8,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL},
				{WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL},
				{WALL,WALL,WALL,WALL,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL,WALL,WALL,WALL,WALL},
				{WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,R2D2},
				{WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL}
			};
		
		char [][] map3 = {
				{WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL},
				{BB8,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,WALL,WALL,WALL,WALL,CORRIDOR,WALL},
				{WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL},
				{WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL},
				{WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL},
				{WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL,WALL,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL},
				{WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL},
				{WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,CORRIDOR,WALL,WALL,WALL,WALL,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,WALL,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,WALL,WALL},
				{WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,WALL,WALL,WALL,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,WALL,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,WALL,WALL},
				{WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,WALL,CORRIDOR,CORRIDOR,CORRIDOR,R2D2},
				{WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL}
			};
		char [][][] maps = {map1, map2, map3};
		
		return maps[level];
	}
	
	/**
	 * This method fills the array with Corridors (e.g. spaces).
	 * Just for output purposes to make the trace look better.
	 */
	public static void createTraceMap() {
		traceMap = new char[selectedMaze.length][selectedMaze[0].length];
		
		for(int i =0; i<traceMap.length;i++) {
			for(int j = 0; j<traceMap[i].length; j++) {
				traceMap[i][j] = CORRIDOR;
			}
			
		}
		
	}
	
	/**This method prints a given 2D char array (the maze);
	 * @param maze			the 2D char array maze to print
	 */
	public static void printMaze(char[][] maze) {

		for (char[] row : maze) {
			for(char element : row) {
				
				System.out.print(element+" ");
				
			}System.out.println();		
		}
	}
	
	/**
	 * This method scans through the array to find the starting point of a maze and returns the position.
	 * 
	 * @param selectedMap			the maze to check for the starting point
	 * @return						returns the position (X,Y) of the starting point 
	 */
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
	
	/**
	 * The main method for moving through the Maze.
	 * depending on the current Direction, moving forward means either: up, right, down or left.
	 * Thats why the current direction is always updated in a variable.
	 * Then either X or Y needs to be incremented or decremented.
	 * 
	 * North = move upwards = y--
	 * East = move right = x++
	 * South = move down = y++
	 * West ? move left = x--
	 * 
	 * At the same time it writes the Direction to the traceMap
	 * 
	 * @param p			the current Position of BB8
	 */
	public static void moveForward(Point p) {
		selectedMaze[p.y][p.x] = ' ';
		traceMap[p.y][p.x]=forwardDirection;
		
		if(forwardDirection==NORTH) {	
			p.y--;	
		}else if(forwardDirection==EAST) {
			p.x++;
		}else if(forwardDirection==SOUTH) {
			p.y++;
		}else{ //West
			p.x--;
		}	
	}
	
	/**
	 * This method checks the field right to BB8.
	 * Depending on the current Direction, right means either: up, right, down or left.
	 * Thats why the current direction is always updated in a variable.
	 *
	 *
	 * @param p			current Position of BB8
	 * @return			char at field on BB8s right
	 */
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
	
	/**
	 * This method checks the field left to BB8.
	 * Depending on the current Direction, left means either: up, right, down or left.
	 * Thats why the current direction is always updated in a variable.
	 *
	 *
	 * @param p			current Position of BB8
	 * @return			char at field on BB8s left
	 */
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
	
	/**
	 * This method checks the field in front of BB8.
	 * Depending on the current Direction, forward means either: up, right, down or left.
	 * Thats why the current direction is always updated in a variable.
	 *
	 *
	 * @param p			current Position of BB8
	 * @return			char at field in front of BB8
	 */
	public static char lookForward(Point p) {
		if (forwardDirection==NORTH) {
			return selectedMaze[p.y-1][p.x];
		}else if (forwardDirection==EAST) {
			return selectedMaze[p.y][p.x+1];
		}else if (forwardDirection==SOUTH) {
			return selectedMaze[p.y+1][p.x];
		}else { //Must be WEST
			return selectedMaze[p.y][p.x-1];
		}
	}
		
	
	/**
	 * This method turns BB8 Clockwise.
	 * Therefore the direction Index gets incremented.
	 * To prevent the index from getting over the boundaries of the directions,
	 * the modulo operator is used.
	 * Then the forwardDirection is updated.
	 * 
	 */
	public static void turnRight() {
		directionIndex = (directionIndex+1)%direction.length;
		forwardDirection = direction[directionIndex];
	}
	
	/**
	 * This method turns BB8 Counter-Clockwise.
	 * Therefore the direction Index gets incremented by one less then there are directions.
	 * To prevent the index from getting over the boundaries of the directions,
	 * the modulo operator is used.
	 * Then the forwardDirection is updated.
	 * 
	 */
	public static void turnLeft() {
		directionIndex = (directionIndex+(direction.length-1))%direction.length;
		forwardDirection = direction[directionIndex];
	}
	
	/**
	 * This method deep copys a two-dimensional array without just linking them.
	 *  
	 * @param master			the master array to be copied.
	 * @return					returns the cloned array.
	 */
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
	
	/**
	 * This method gets called as BB8 found the exit of the maze.
	 */
	public static void escaped() {
		System.out.println("\n===================================================");
		System.out.println("\nBB8 hat zu R2D2 gefunden!!!");
		System.out.println("Dafuer hat er "+stepCounter+" Schritte benötigt!");
		System.out.println("\nDas hier war sein Weg:\n");
		printMaze(traceMap);
		wait(500);
		Gui.traceMap=true;
		window.repaint();
		System.out.println("\n===================================================");
		System.out.println("Danke fuer´s spielen!");
		System.out.println("Dieses Spiel wurde programmiert von Felix Wuest, Max Muthler und Marvin Wernli.");
	}
	
	/**
	 * To make the maze animation more fluent, the console can be cleared using this method.
	 * It will just print 100 empty lines, so the console seems empty.
	 */
	public static void clearConsole() {
		for(int i = 0; i<100; i++){
			System.out.println();
		}
	}
	
	/**
	 * This method is used for printing the current stats when debugging is enabled. 
	 * @param p				Current position of BB8
	 */
	public static void printStats(Point p) {
		algorithmTime = (System.currentTimeMillis()-startTime)-(stepCounter*delay);
		
		System.out.println("\nPosition (x|y): ( "+p.x+" | "+p.y+" )");
		System.out.println("Currently Facing "+forwardDirection);
		System.out.println("Iterations: "+stepCounter);
		System.out.println("Current Time elapsed: "+algorithmTime+" ms");
	}
	
	/**
	 * This method can be called to make to thread sleep for a given time (in ms).
	 * 
	 * @param timeInMillis		time (in ms) to wait
	 */
	public static void wait(int timeInMillis) {
		try {
			Thread.sleep(timeInMillis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
