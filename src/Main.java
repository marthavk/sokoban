import java.io.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Vector;

public class Main {

	public static Board board;
	public static PriorityQueue<Board> frontier;
	public static Board deadlocks;
	public static HashSet<String> visited;
	public static boolean[][] tunnels;
	public static final int mDepth = 100;
	
	public static void main(String[] args) throws IOException {
	//	Deadline deadline = new Deadline(new Date(new Date().getTime()));    
		Vector<String> b = new Vector<String>();
		frontier = new PriorityQueue<Board>();
		visited = new HashSet<String>();
	
		BufferedReader br = new BufferedReader(
				new InputStreamReader(System.in));
		
		/*BufferedReader br = new BufferedReader(
				new FileReader("test059"
						+ ".in"));*/
		
		String line;
		int maxLength = 0;
		while(br.ready()) {
			line = br.readLine();
			b.add(line);
			if (line.length()>maxLength) maxLength = line.length();
		} // End while
		
		/*convert to array*/
		board = new Board(b.size(), maxLength);
		int numberOfBoxes = 0;
		for (int row=0; row<b.size(); row++) {
			for (int col=0; col<b.get(row).length(); col++){
				switch (b.get(row).charAt(col)) {
				case ' ': 	board.setCell(row, col, Constants.SPACE);
							break;
				case '#':	board.setCell(row, col, Constants.WALL);
							break;
				case '.': 	board.setCell(row, col, Constants.GOAL);
							break;
				case '@':	board.setCell(row, col, Constants.SOKO);
							break;
				case '+':	board.setCell(row, col, Constants.SOKO_ON_GOAL);
							break;
				case '$':	board.setCell(row, col, Constants.BOX);
							numberOfBoxes++;
							break;
				case '*':	board.setCell(row, col, Constants.BOX_ON_GOAL);
							numberOfBoxes++;
							break;
				default:	board.setCell(row, col, Constants.NOT_USED);
							break;				
				}						
			}
		}
		br.close();
		
		/*Mark Tunnels*/
		tunnels = Functions.markTunnels(board);
		//Check Tunnels
	/*	String tunnelBoard = new String();
		for (int i=0; i<board.rowsN; i++) {
			for (int j=0; j<board.columnsN; j++) {
				if (tunnels[i][j]) tunnelBoard += "1 ";				
				else tunnelBoard += "0 ";				
			}
			tunnelBoard += '\n';
		}
		System.out.println(tunnelBoard);*/
		
		/*Mark Static Deadlocks*/
		deadlocks = Functions.markStaticDeadlocks(board);		

		frontier.add(board);
		Main.visited.add(board.hashIt());
		Board solution = Main.bestFirstSearch();			
		try {
			System.out.println(solution.getPath());
		}
		catch (NullPointerException e) {
			System.out.println("L");			
		}
		
	//	System.out.println("Time for whole program:" + deadline.timeUntil());
 			
		
	} // main

/***************************FUNCTIONS**************************************/
	
	/**
	 * 	
	 * @return
	 */
	public static Board bestFirstSearch() {
		while (!frontier.isEmpty()) {	
			Board rootBoard = frontier.remove();		
			//check if it is a solution
			if (rootBoard.isSolved()) return rootBoard;
		
		//	System.out.println("BOARD TO BE EXPANDED: \n" + rootBoard.toString());			
			
			Vector<Board> expanded = rootBoard.possiblePushes();
					
			
	//		System.out.println("Expanded # = " + expanded.size());
	//		System.out.println("hash code = " + rootBoard.hashIt());
	//		System.out.println("-----expands in");		
			 
			for (Board temp: expanded) {	
				if (!Main.visited.contains(temp.hashIt())) {
					if (temp.isDeadlock()) {	
						Main.visited.add(temp.hashIt());
					}
					else {
						Main.visited.add(temp.hashIt());
						temp.evaluateBoard();						
						frontier.add(temp);
					}
				}				
			}//end for			
		}//end while		
		return null;		
	}
	
	/**
	 * 
	 * @param elements
	 * @return
	 */
	/*public static Board getMin(Vector<Board> elements) {
		Iterator<Board> iter = elements.iterator();
		Board bestB = iter.next();
		double min = bestB.getValue();
		while (iter.hasNext()) {
			Board temp = iter.next();
			if (temp.getValue()<min) {
				bestB = temp;
				min = temp.getValue();
			}
		}			
		return bestB;
	}*/
	
	/**
	 * 
	 * @param pos
	 * @return
	 */
	public static boolean isStaticDeadlock(Position pos) {
		if (deadlocks.getCell(pos)==Constants.DEADLOCK) return true;
		return false;
	}
	
	
	/**
	 * expands the map - 1 step
	 * used for debugging purposes
	 */
	public static void miniSearch() {
		//Board rootBoard = frontier.pop();
		Board rootBoard = frontier.remove();
		Vector<Board> expanded = rootBoard.possiblePushes();
		
//		System.out.println("BOARD TO BE EXPANDED: \n" + rootBoard.toString());	
	//	System.out.println("Expanded # = " + expanded.size());
	//	System.out.println("hash code = " + rootBoard.hashIt());
	//	System.out.println("-----expands in");
						
		for (Board temp: expanded) {						
				if (!Main.visited.contains(temp.hashIt())) {
					if (temp.isDeadlock()) {						
						Main.visited.add(temp.hashIt());
						System.out.println(temp.toString());
					}
					else {
						Main.visited.add(temp.hashIt());
						temp.evaluateBoard();						
						frontier.add(temp);
						System.out.println(temp.toString());
					}//end if-else
				}//end if 				
		}//end for						
	}//end miniSearch
			
		
}//end Main
