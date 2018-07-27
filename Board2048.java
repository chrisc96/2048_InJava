// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP 103 Assignment 1
 * Name:
 * Usercode:
 * ID:
 */

import ecs100.*;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

import javax.swing.JComponent;

public class Board2048 {
	
	// Stops board from moving when using arrow keys
	// Source: Sanjay Govind.
	{
		((JComponent)UI.theUI.canvas).addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			e.consume();
			}
		});
	}

    private static final double LIMIT = 0.7; // to determine the ratio of 2s over 4s (the closer to 10, the more 2s)
    private static final int TARGET = 2048; // number value the player needs to reach

    private static final int ROWS = 4;
    private static final int COLUMNS = 4;
    private int [][] board;
    int rows;
    int cols;

    public Board2048 () {
    	((JComponent)UI.theUI.canvas).requestFocus();
        board = new int [ROWS][COLUMNS];
        rows = board.length;
        cols = board[0].length;
        setColor();
    }

    /** Return whether (at least) the magic target number has been achieved.  
     */
    public boolean hasReachedTarget() {
    	for (int row = 0; row < rows; row++) {
    		for (int col = 0; col < cols; col++) {
    			if (board[row][col] == TARGET) {
    				return true;
    			}
    		}
    	}
    	return false;
    }

    /** Return whether the game is over (true) or not (false). 
     *  If there is some space on the board left, the game is not over.
     *  If there is no space left, the game is still not over, if adjacent tiles hold the same value, 
     *  as they could be compressed to fewer tiles by a player move.
     */
    public boolean isGameOver() {
    	// If all tiles are full with numbers
    	if (this.numEmptyTiles() == 0) {
    		int count = 0;
    		// Check for merging
    		
    		
    		// Check horizontally for pairs that can merge
    		for (int row = 0; row < rows; row++) {
    			// checking for pairs in columns
    			for (int col = 0; col < cols - 2; col+=2 ) {
    				// Looking horizontally for merges
    				int pair1 = board[row][col];
    				int pair2 = board[row][col+1];
		    		if (pair1 == pair2) {
		    			count++;
		    		}
    			}
    		}
    		
    		// Check vertically for pairs that can merge
    		for (int row = 0; row < rows - 2; row+=2) {
    			// checking for pairs in rows
    			for (int col = 0; col < cols; col++ ) {
    				// Looking horizontally for merges
    				int pair1 = board[row][col];
    				int pair2 = board[row+1][col];
		    		if (pair1 == pair2) {
		    			count++;
		    		}
    			}
    		}
    		
    		// count == 0 if no pairs are found, thus gameover
    		if (count == 0) {
    			return true;
    		}
    	}
    	return false;
    }

    /** Return the number of empty tiles. 
     *  An empty tile is one which holds the value 0.
     */
    private int numEmptyTiles() {
        int count = 0;
        for (int row = 0; row < rows; row++) {
        	for (int col = 0; col < cols; col++) {
        		if (board[row][col] == 0) {
        			count++;
        		}
        	}
        }
        return count;
    }

    /** Insert a random number (either 2 or 4) at a random empty tile.
     *  Note that 7 out of 10 times the number should be 2.
     *  An empty tile is one which holds the value 0.
     */
    public void insertRandomTile() {
    	// Variables/Arraylists
    	int val;
    	ArrayList<Integer> emptyRowVals = new ArrayList<Integer>();
    	ArrayList<Integer> emptyColVals = new ArrayList<Integer>();
    	
    	
    	if (this.numEmptyTiles() != 0) {
    		// Assign a 2 7/10 times otherwise a 4 to random tile
        	double random = Math.random();
        	if (random <= LIMIT) {
        		val = 2;
        	}
        	else {
        		val = 4;
        	}
        	
	    	for (int row = 0; row < rows; row++) {
	    		for (int col = 0; col < cols; col++) {
		    		if (board[row][col] == 0) { // if index is empty, store this index in alternate array
		    			emptyRowVals.add(row); //
		    			emptyColVals.add(col); // 
		    		}
	    		}
	    	}
	    	
	    	int size = emptyRowVals.size();
	    	int randVal = (int) (Math.random()*size);
	    	int indexRow = emptyRowVals.get(randVal);
	    	int indexCol = emptyColVals.get(randVal);
	    	
	    	board[indexRow][indexCol] = val;
    	}

    	
    	
    }

    /** Move the tiles left. 
      Each time two tiles with the same number touch, the numbers are added and the two tiles merge on 
      the left side. An empty tile is then added on the right hand side of the board.
    
      Examples: 
        2 2 4 2 will give 4 4 2 0 (the first 2s merge into a 4. Then the remaining
          4 and 2 follow, and the board is completed on the right with a 0)
          
       4 4 2 2 will give 8 4 0 0 (4 and 4 merge into a 8, 2 and 2 merge into a 4, 
          completing with zeros on the right)
          
       4 4 4 4 will give 8 8 0 0 (First two 4s merge together, the last two 4s merge together)
    
      For each row, do the following:
      1. Shift all non-empty tiles to the left as far as possible, making sure that all empty tiles are on the right.
      2. From left to right, merge any two tiles with the same number by adding them, discarding
         the second one, and adding an empty tile on the right of the board.
     */
    public void left() {
    	// shifting non zero values to left
    	// only need to shift the 2nd, 3rd and 4th column
    	// hence why col = 1;
    	for (int row = 0; row < rows; row++) {
    		for (int col = 1; col < cols; col++) {
    			int current = board[row][col];
    			if (current != 0) {
    				for (int i = 0; i < col; i++) {
    					int nextTile = board[row][i];
    					if (nextTile == 0) {
    						board[row][i] = board[row][col];
    						board[row][col] = 0;
    					}
    				}
    			}
    		}
    	}

        // merging values together
        for (int row = 0; row < rows; row++) {
        	for (int col = 1; col < cols; col++) {
            	int firstVal = board[row][col];
            	int secVal = board[row][col-1];
            	if (firstVal == secVal) {
            		int added = firstVal + secVal;
            		board[row][col-1] = added;
            		board[row][col] = 0;
            	}
        	}
        }        
    }

    /** Move the tiles right. 
     * Each time 2 tiles with the same number touch, the numbers are added and the two tiles merge on 
     * the right side. An empty tile is then added on the left hand side of the board.
    
     * Examples:  
     *   2 2 4 2 will give 0 4 4 2 (2 and 4 remain unchanged, then the last leftmost 2s merge 
     *     into a 4, completing with a zero on the left.)
     *     
     *   4 4 2 2 will give 0 0 8 4 (2 and 2 merge into a 4, 4 and 4 merge into a 8)
     *   4 4 4 4 will give 0 0 8 8 (First two 4s merge together, the last two 4s merge together)
     *   
     * For each row, do the following:
     *   1. Shift all non-empty tiles to the right, making sure that all the empty tiles are on the left.
     *   2. From right to left, merge any two tiles with the same number by adding them, discarding 
     *      the second one, and adding an empty tile on the left of the board.
     */
    public void right() {
    	// shifting non zero values to right
    	for (int row = 0; row < rows; row++) {
    		for (int col = cols - 2 ; col >= 0; col--) {
    			int current = board[row][col];
    			if (current != 0) {
    				for (int i = cols; i > 0; i--) {
    					int nextTile = board[row][i - 1];
    					if (nextTile == 0) {
    						board[row][i-1] = board[row][col];
    						board[row][col] = 0;
    					}
    				}
    			}
    		}	
    	}

        // merging values together
        for (int row = 0; row < rows; row++) {
        	for (int col = cols - 1; col > 0; col--) {
            	int firstVal = board[row][col];
            	int secVal = board[row][col-1];
            	if (firstVal == secVal) {
            		int added = firstVal + secVal;
            		board[row][col] = added;
            		board[row][col-1] = 0;
            	}
        	}
        }
    }

    /** Move the tiles up. 
      Each time 2 tiles with the same number touch, the numbers are added and the two tiles merge on 
      the up side. An empty tile is then added at the bottom of the board.
      
      For each column, do the following:
      1. Move all non-empty tiles up, making sure that all empty tiles are at the bottom.
      2. From top to down, merge any two adjacent tiles with the same number by adding them, discarding
         the second one, and adding an empty tile at the bottom of the board.
     */
    public void up() {
    	for (int col = 0; col < cols; col++) {
    		for (int row = 1; row < rows; row++) {
    			int current = board[row][col];
    			if (current != 0) {
    				for (int i = 0; i < row; i++) {
    					int nextTile = board[i][col];
    					if (nextTile == 0) {
    						board[i][col] = board[row][col];
    						board[row][col] = 0;
    					}
    				}
    			}
    		}
    	}
    
        // merging values together
        for (int row = 1; row < rows; row++) {
        	for (int col = 0; col < cols; col++) {
            	int firstVal = board[row - 1][col];
            	int secVal = board[row][col];
            	if (firstVal == secVal) {
            		int added = firstVal + secVal;
            		board[row-1][col] = added;
            		board[row][col] = 0;
            	}
        	}
        }
	}
    
    
    
    /** Move the tiles down. 
      Each time 2 tiles with the same number touch, the numbers are added and the two tiles merge at
      the bottom. An empty tile is then added at the top of the board.
    
      For each column, do the following:
      1. Move all non-empty tiles down, making sure that all empty tiles are at the top.
      2. From bottom to top, merge any two adjacent tiles with the same number by adding them, discarding
         the second one, and adding an empty tile at the top of the board.
     */
    public void down() {
    	// shifting non zero values to bottom
    	for (int col = 0; col < cols; col++) {
    		for (int row = rows - 2; row >= 0; row--) {
    			int current = board[row][col];
    			if (current != 0) {
    				for (int i = rows; i > 0; i--) {
    					int nextTile = board[i - 1][col];
    					if (nextTile == 0) {
    						board[i-1][col] = board[row][col];
    						board[row][col] = 0;
    					}
    				}
    			}
    		}	
    	}
    	
        // merging values together
        for (int row = rows - 1; row > 0; row--) {
        	for (int col = 0; col < cols; col++) {
            	int firstVal = board[row - 1][col];
            	int secVal = board[row][col];
            	if (firstVal == secVal) {
            		int added = firstVal + secVal;
            		board[row][col] = added;
            		board[row-1][col] = 0;
            	}
        	}
        }
    }

    public String toString() {
        String tiles = "";
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) 
                tiles += "  " + board[row][col];
           
            tiles += "\n"; // row ends here
        }
        return tiles;
    }

    // layout of the board
    private static final int boardLeft = 80;    // left edge of the board
    private static final int boardTop = 40;     // top edge of the board
    private static final int tileSize = 80;     // width of tiles in the board
    private static final int padding = 5;       // size of the padding;
    private static final Color colorPad = new Color (187,173,160);

    public static int getHeight() {
        return boardTop*2 + ROWS*tileSize+(ROWS+1)*padding + 100;
    }

    public static int getWidth() {
        return boardLeft*2 + COLUMNS*tileSize+(COLUMNS+1)*padding;
    }

    public void displayMessage(String txt){
        // Clear up any eventual previous message first
        UI.setColor(Color.white);
        UI.fillRect(boardLeft, boardTop + 50 + tileSize * ROWS, tileSize * ROWS, 200);

        // Display the message
        UI.setFontSize(40);
        UI.setColor(Color.red);
        UI.drawString(txt, boardLeft, boardTop + 50 + tileSize * (ROWS + 1 ));
    }

    public void redraw() {
        // draw the padding
        UI.setColor(colorPad);

        double width = COLUMNS*tileSize+(COLUMNS+1)*padding;
        double height  = ROWS*tileSize+(ROWS+1)*padding;
        UI.fillRect(boardLeft, boardTop, width, height);

        // Draw the tiles
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) 
                drawTile(row, col);
            
        }
        
        displayScore();
    }

    private void drawTile(int row, int col) {
        double left = boardLeft+padding+col*(tileSize+padding);
        double top = boardTop+padding+row*(tileSize+padding);

        // Fill the rectangle with a colour matching the value of the tile
        UI.setColor(getColor(board[row][col]));
        UI.fillRect(left,top,tileSize,tileSize);

        // Display the number
        if (board[row][col] == 0) 
          return;
          
        if (board[row][col] >= 8) 
            UI.setColor(Color.white);
        else 
            UI.setColor(new Color (119,110,101));

        int fontSize = getFontSize(board[row][col]);
        UI.setFontSize(fontSize);

        double x = left + getXPos(board[row][col]);;
        double y = top + tileSize * 0.55;
        UI.drawString(""+board[row][col], x, y);
    }

    // mapping from tile values to colours
    Map <Integer, Color> colors = new HashMap <Integer, Color> ();
    private void setColor() {
        colors = new HashMap <Integer, Color> ();
        colors.put(new Integer(0), new Color (205,192,180));
        colors.put(new Integer(2), new Color (238,228,218));
        colors.put(new Integer(4), new Color (241,225,202));
        colors.put(new Integer(8), new Color (242,177,121));
        colors.put(new Integer(16), new Color (245,149,99));
        colors.put(new Integer(64), new Color (246,94,59));
        colors.put(new Integer(32), new Color (246,124,95));
        colors.put(new Integer(128), new Color (237,207,114));
        colors.put(new Integer(256), new Color (237,204,97));
        colors.put(new Integer(512), new Color (237,200,80));
        colors.put(new Integer(1024), new Color (237,197,63));
        colors.put(new Integer(2048), new Color (237,194,46));
        colors.put(new Integer(4096), new Color (243,102,107));
        colors.put(new Integer(8192), new Color (241,76,86));
        colors.put(new Integer(16384), new Color (247,63,62));
        colors.put(new Integer(32768), new Color (113,180,216));
        colors.put(new Integer(65536), new Color (92,160,227));
        colors.put(new Integer(131072), new Color (20,131,208));

    }

    private Color getColor(int value) {
        Color c = colors.get(new Integer(value));

        if (c == null) // has an unknown value been supplied?
          return Color.black;  // return "black" as a default
        
        return c;
    }

    // compute font size depending on number value
    private int getFontSize(int value) {
        if (value < 100) 
          return 28;
          
        if (value < 1000) 
          return 24;
          
        return 22;
    }

    private double getXPos(int value) {
        if (value < 10)       return (tileSize * 0.45);
        if (value < 100)      return (tileSize * 0.35);
        if (value < 1000)     return (tileSize * 0.3);
        if (value < 10000)    return (tileSize * 0.2);
        if (value < 100000)   return (tileSize * 0.14);
        else                  return (tileSize * 0.05);
    }

    private void displayScore() {
        double x = boardLeft + tileSize * COLUMNS /2.;
        double y = boardTop/2;

        // Clear up previous score first
        UI.setColor(Color.white);
        UI.fillRect(x-5, 0, 150, 30);

        int score = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) 
                score += board[row][col];
            
        }
        
        UI.setFontSize(20);
        UI.setColor(Color.blue);
        UI.drawString("" + score, x, y);
    }
}