/*
 * public class lolfirstgenerator {
 * 
 * }
 * 
 * public static char[][] generateMaze(int size) {
 * 
 * char[][] maze = new char[size][size];
 * 
 * int startingPoint = rnd.nextInt(maze.length);
 * 
 * int x = 0; int y = startingPoint; System.out.println("StartY: "+y);
 * 
 * 
 * 
 * 
 * printMaze(maze);
 * 
 * while (x<maze[0].length-1) { int z = rnd.nextInt(4); if(z==0) { x++; }else
 * if(z==1&&y<maze.length-1){ y++; }else if(z==2&&y>0){ y--; }else
 * if(z==3&&x>0){ x--; }
 * 
 * maze[y][x] = ' ';
 * 
 * }
 * 
 * for(int i=0;i<maze.length;i++) { for(int j=0;j<maze.length;j++) {
 * 
 * if(maze[i][j]==' ' && j==maze.length-1) {
 * 
 * if(i==0) { maze[i+1][j] = 'A'; }else if(i==maze.length-1) { maze[i-1][j] =
 * 'A'; }else { maze[i][j] = 'A'; }
 * 
 * }else if(maze[i][j]==' ') { maze[i][j] = ' '; }else { maze[i][j] = '.'; } } }
 * 
 * printMaze(maze);
 * 
 * for(int i=0;i<maze.length;i++) { for(int j=0;j<maze[0].length;j++) {
 * 
 * if(maze[0][j]!='A') { maze[0][j]='.'; } if(maze[maze.length-1][j]!='A') {
 * maze[maze.length-1][j]='.'; } if(maze[i][0]!='A') { maze[i][0]='.'; }
 * if(maze[i][maze[i].length-1]!='A') { maze[i][maze[i].length-1]='.'; }
 * 
 * } } maze[startingPoint][0]=BB8; maze[startingPoint][1]=' '; printMaze(maze);
 * return maze; }
 */