import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

public class Gui extends JFrame{

	static int counter = 0;
	static int margin = 15;
	static int padding = 50; 
	static boolean update=true;
	
	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
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

				g.fillRect(padding/2+j*50+j*margin, padding+i*50+i*margin, 50, 50);
				
			}
		}
		if(update) {
		RightHandSolver.wait(RightHandSolver.delay);
		repaint();	
		}
		
		
	}



	public void open(int xWidth, int yHeight) {
		
	
		Gui app = new Gui();
		app.setSize(xWidth*50+xWidth*margin+padding, yHeight*50+yHeight*margin+padding);
		app.setVisible(true);
			
	}

}
