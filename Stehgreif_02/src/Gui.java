import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;

import javax.swing.JFrame;


/**
 * JFrame class for displaying the maze in a window.
 * 
 * @author Felix Wuest
 *
 */
public class Gui extends JFrame{

	static int counter = 0;
	static int margin = 0;
	static int paddingY = 25; 
	static int paddingX = 5;
	static int fieldSize = 0;
	static boolean done = false;
	
	/**
	 *This method paints the maze array using rectangles.
	 *It directly gets the array from the RightHandSolver class.
	 *Depending on the char, the fields will be colorized.
	 *Method repaint needs to be called, to update the graphics. 
	 */
	@Override
	public void paint(Graphics g) {
		
		for(int i = 0; i<RightHandSolver.selectedMaze.length;i++) {
			
			for(int j =0; j<RightHandSolver.selectedMaze[i].length;j++) {
				
				
				if(RightHandSolver.selectedMaze[i][j]==RightHandSolver.WALL) {
					g.setColor(Color.black);
				}
				if(RightHandSolver.selectedMaze[i][j]==RightHandSolver.CORRIDOR) {
					g.setColor(Color.white);
				}
				if(i==RightHandSolver.startingPoint.y &&j==RightHandSolver.startingPoint.x) {
					g.setColor(Color.orange);
				}
				if(RightHandSolver.selectedMaze[i][j]==RightHandSolver.R2D2) {
					g.setColor(Color.blue);
				}
				
				if(done&&RightHandSolver.selectedMaze[i][j]==RightHandSolver.BB8) {
					g.setColor(Color.green);
				}

				g.fillRect(paddingX+j*fieldSize+j*margin, paddingY+i*fieldSize+i*margin, fieldSize, fieldSize);
				
			}
		}
	}
	
	/**
	 * Initializes a JFrame window with contentPane.
	 * 
	 * @param xWidth		width of the array to be displayed
	 * @param yHeight		height of the array to be displayed
	 * @param fieldSize		size of the individual tiles in px
	 * @param margin		space between the tiles
	 */
	public void open(int xWidth, int yHeight, int fieldSize, int margin) {
		this.fieldSize = fieldSize;
		this.margin = margin;
		
		//Jframe init
		setTitle("MazeRunner");
		setSize(xWidth*fieldSize+xWidth*margin+paddingX, yHeight*fieldSize+yHeight*margin+paddingY);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setAlwaysOnTop(true);
		
		//Layout
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
	
		setVisible(true);
			
	}

}
