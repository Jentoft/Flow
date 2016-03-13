import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import javax.swing.JOptionPane;


public class RouterModel extends AbstractModel
{
	//Add visibility, initialize in constructor
	private Tile[][] board;  //A 2-D array of tiles representing the board
	private Wire current = null; //which wire is in progress, null if no inprogress wire
	private Wire[] wArr; //Array of all wires
	private int numComp = 0; //How many wires have been completed?
	private int numFilled = 0; //How many tiles have a section of wire in them?
	private boolean hasBoard = true; //Has a board been loaded?
	
	/**
	 * My constructor for my router model
	 * @param bSize what size is my board?
	 * @param wSize how many wires
	 * @param starters an array of the coordinates of the terminals
	 */
	RouterModel(int bSize, int wSize, int[] starters)
	{
		board = new Tile[bSize][bSize];
		wArr = new Wire[wSize];
		
		//Initializing the board
		for(int i = 0; i < bSize; i++)
		{
			for(int j = 0; j < bSize; j++)
			{
				board[i][j] = new Tile(-1, -1, i, j, 0, false);
			}
		}
		
		//If starters is all zeroes, do nothing.  Just a dummy constructor
		int count = 0;
		for(int i = 0; i < starters.length; i++)
		{
			if(starters[i] == 0)
				count++;
		}
		if(count == starters.length)
		{
			hasBoard = false;
			return;
		}
		
		//Initializing wArr
		for(int i = 0; i < starters.length/4; i++)
        {
			wArr[i] = new Wire(starters[4*i], starters[4*i + 1], starters[4*i + 2], starters[4*i + 3], i);
        }
		
		//Setting up my terminals
        for(int i = 0; i < starters.length/2; i++)
        {
        	board[starters[2*i]][starters[2*i + 1]] = new Tile(0, 0, starters[2*i], starters[2*i + 1], i/2, true);
        }
        fireTableStructureChanged();
	}
	
	/**
	 * A function that return how many tiles have been changed
	 */
	public int getNumFilled()
	{
		return numFilled;
	}
	
	/**
	 * How many wires in the board
	 * @return
	 */
	public int getWArrSize()
	{
		return wArr.length;
	}

	
	@Override
	public int getColumnCount()
	{
		// TODO Auto-generated method stub
		return board[0].length;
	}

	@Override
	public int getRowCount()
	{
		// TODO Auto-generated method stub
		return board[0].length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		// TODO Auto-generated method stub
		return board[columnIndex][rowIndex];
	}
	
	@Override
	public Class<?> getColumnClass(int x)
	{
		return Tile.class;
	}
	
	public void setValueAt(int rowIndex, int columnIndex, Tile obj)
	{
		board[columnIndex][rowIndex] = obj;
	}

	@Override
	public String getTitle()
	{
		return "Router (Flow)";
	}

	/**
	 * My reset function
	 */
	@Override
	public void clear()
	{
		if(current != null || numComp != 0)
		{
			for(int i = 0; i < wArr.length; i++)
			{
				wArr[i].delete();
				numComp = 0;
				current = null;
			}
			numFilled = 0;
		}
		fireTableStructureChanged();
	}

	/**
	 * Are we there yet?
	 */
	@Override
	public boolean isWinner()
	{
		return numComp == wArr.length;
	}

	/**
	 * Returns false if either the index is not on the board or if no wire is in progress and the selection is not a terminal
	 */
	@Override
	public boolean isValidSelection(int column, int row)
	{
		Tile inQuestion = board[column][row];
		if(current.equals(null))
		{
			if(inQuestion.getIsTerminal())
				return true;
			return false;
		}
		if(inQuestion.getIsEmpty() && (isAdjacent(inQuestion) != -1))
			return true;
		return false;
	}
	
	/**
	 * A function that returns an integer representing the adjacency of a tile to the last tile in the current wire
	 * @param x tile in question
	 * @return -1 if a tile is not adjacent to the last tile in the current wire
	 * 		   1 if the tile is directly to the right of the last tile
	 * 		   2 if the tile is directly to the left of the last tile
	 * 		   3 if the tile is directly below the last tile
	 * 		   4 if the tile is directly above the last tile
	 * 		   0 if the tile begins a new wire
	 */
	private int isAdjacent(Tile x)
	{
		int tRow = x.getRow();
		int tCol = x.getColumn();
		
		//If no wire is in progress or the wire in progress has no wires yet...
		if(current == null || current.getSize() == 0)
		{
			if(x.getIsTerminal())//If a terminal is selected return 0
				return 0;
			else
				return -1;
		}
		
		//Otherwise, a wire is in progress...
		else
		{
			int wRow = current.getLast().getRow();
			int wCol = current.getLast().getColumn();
			
			//If the selected tile is on the same row...
			if(tRow == wRow)
			{
				//If it's directly to the right, return 1
				if(tCol == (wCol + 1))
					return 1;
				
				//If it's directly to the left, return 2
				else if(tCol == (wCol -1))
					return 2;
				
				//If it is in the same row, but not adjacent, return -1
				return -1;
			}
			
			//If it's in the same column...
			else if (tCol == wCol)
			{
				//If it's directly below return 3
				if(tRow == (wRow + 1))
					return 3;
				
				//If it's directly above, return 4
				else if (tRow == (wRow - 1))
					return 4;
				
				//If it's in the same column but not adjacent, return -1
				return -1;
			}
			
			//If it's neither in the same column nor row, return -1
			return -1;
		}
	}

	//Setter fo wArr
	public void addWArrElement(int index, Wire element)
	{
		wArr[index] = element;
	}
	
	/**
	 * The workhorse of the program that actually adds a section of wire to board[xCoor][yCoor]
	 */
	public void addTo(int xCoor, int yCoor)
	{
		//If invalid coordinates are passed in, do nothing
		if(xCoor <0 || yCoor < 0 || xCoor > board[0].length || yCoor > board[0].length)
		{
			return;
		}
		
		//If the game is over, do nothing
		if(isWinner())
		{
			return;
		}
		
		//If no board is loaded, do nothing
		if(!hasBoard)
		{
			return;
		}
		
		Tile toAdd = board[xCoor][yCoor]; //The tile the wire will be moving to
		int place = isAdjacent(toAdd); //An int representing which direction the wire will go used to determine shapes
		
		//If no wire is in progress, only do something if a terminal is clicked on
		if(current == null)
		{
			if(toAdd.getIsTerminal())
			{
				selectWire(toAdd);
			}
		}
		
		//If the tile is a terminal that is adjacent to the last section of wire...
		else if((isAdjacent(toAdd) != -1 && isAdjacent(toAdd) != 0) && toAdd.getIsTerminal())
		{
			//If it's a terminal with the same color...
			if(toAdd.getColor() == current.getColor())
			{
				//If it's empty, it completes the wire
				if(toAdd.getIsEmpty())
				{
					addSection(toAdd);
					current.setIsComplete(true);
					current = null;
					numComp++;
				}
				
				//Otherwise it's the beginning, so rewind
				else
				{
					numFilled -= current.rewindTo(toAdd);
				}
			}
			
			//Otherwise, delete the wire
			else
			{
				numFilled -= current.delete();
				numComp --;
				selectWire(toAdd);
			}
		}
		
		else if(toAdd.getIsEmpty()) //If the tile doesn't have a wire section on it yet...
		{
			//If no wire is in progress, only do something if a terminal is clicked on
			if(current == null)
			{
				if(toAdd.getIsTerminal())
				{
					selectWire(toAdd);
				}
			}
			//If the tile clicked on is a terminal that doesn't complete the wire or a tile that is not adjacent to the last wire section, delete the partial wire
			if((toAdd.getIsTerminal() && current.getSize() != 0 && !current.contains(toAdd)))
			{
				current.delete();
				current = null;
			}
			
			//If the selection is empty and non-adjacent to the current wire, delete the current wire
			if((place == -1))
			{
				if(current != null)
				{
					current.delete();
					current = null;
				}
			}
				 
			//If none of the above, it is a valid tile to add a wire section to without further bother
			else
			{
				addSection(toAdd);
			}
		}
		
		//If the wire is occupied...
		else
		{
			//If the tile is a part of the wire in progress, rewind to the node that was clicked on
			if(current.contains(toAdd))
			{
				numFilled -= current.rewindTo(toAdd);
			}
			
			//Otherwise the tile is part of another wire
			else
			{
				//If the tile isn't adjacent to the last section...
				if(isAdjacent(toAdd) == -1)
				{
					numFilled -= current.delete(); //delete the partial wire
					//If it is a terminal of another wire, just delete partial wire
					if(toAdd.getIsTerminal())
						current = null;
					else
					{
						current = wArr[toAdd.getColor()];
						numFilled -= current.rewindTo(toAdd);
					}
				}
				//If you are clicking on an adjacent terminal
				else if (toAdd.getIsTerminal())
				{
					numFilled -= current.delete();
					current = null;
				}
				
				//Otherwise, delete the wire that you ran into and add another section
				else
				{
					numFilled -= wArr[toAdd.getColor()].delete();
					addSection(toAdd);
				}
			}
		}
		fireTableStructureChanged();
	}
	
	/**
	 * Code for the begninnings of a wire that was repeated, so I put into a function
	 * @param x which terminal begins
	 */
	private void selectWire(Tile x)
	{
		current = wArr[x.getColor()];
		current.addElement(x);
		numFilled++;
	}
	
	/**
	 * Code that actually changes the board to add a section of wire
	 * @param x
	 */
	private void addSection(Tile x)
	{
		if(wArr != null && wArr.length != 0 && x.getColor() != -1)
		{
			Wire wire = wArr[x.getColor()];
			if(x.getIsTerminal())
			{
				if(wire.getSize() == 0)
				{
					x.setInShape(0);
					x.setOutShape(0);
				}
				else if(wire.getIsComplete())
				{
					if(x.equals(wire.getLast()))
						x.setOutShape(0);
					else x.setInShape(0);
				}
				else if(x.equals(wire.getFirst()))
				{
					x.setInShape(0);
				}
				else
				{
					x.setInShape(0);
					x.setOutShape(0);
				}
			}
		}
		int place = isAdjacent(x);
		if(current.getSize() != 0)
		{
			current.getLast().setOutShape(place);
		}
		if(place == 1)
		{
			x.setInShape(2);
		}
		else if(place == 2)
		{
			x.setInShape(1);
		}
		else if(place == 3)
		{
			x.setInShape(4);
		}
		else if (place == 4)
		{
			x.setInShape(3);
		}
		
		x.setColor(current.getColor());
		
		current.addElement(x);
		x.setIsEmpty(false);
		numFilled++;
	}

	/**
	 * What should the header of the GUI say during the game?
	 */
	@Override
	public String getInProgressText()
	{
		if(!hasBoard)
			return "Load a Board";
		if(current == null)
			return "Select a Wire";
		return "Wire in Progress";
	}

	/**
//	 * What should the background color for the tile be?
	 */
	@Override
	public int setBackgroundColor(Tile x)
	{
		if(isPerfect())
			return -1;
		if(current == null)
			return -2;
		else if(x.equals(current.getLast()))
		{
			return -1;
		}
		return -2;
	}
	
	//Is it a perfect game?
	private boolean isPerfect()
	{
		return (isWinner() && numFilled == board[0].length*board[0].length);
	}

	@Override
	public String getStartText()
	{
		return "Load a board";
	}
	
	/**
	 * My attempt at a solve method that should handle any situation where the wire doesn't travel away from the other terminal
	 */
	public void Solve()
	{
		//Start with a fresh board
		clear();
		int numDone = 0;
		
		//For all wires in the board...
		for(int i = 0; i < wArr.length; i++)
		{
			//Select the next wire
			Wire wire = wArr[i];
			current = wire;
			Tile t1 = wire.getTer1();
			Tile t2 = wire.getTer2();
			int t1Col = t1.getColumn();
			int t1Row = t1.getRow();
			int t2Col = t2.getColumn();
			int t2Row = t2.getRow();
			
			//Add a wire to the first terminal
			addTo(t1Col, t1Row);
			
			int x = t1Col;
			int y = t1Row;
			int xTemp = x;
			int yTemp = y;
			
			//Move as far as possible the first time...
			x = moveHorizontal(t1Col, t2Col, t1Row);
			y = moveVertical(t1Row, t2Row, x);

			//Until one reaches the goal or no change has happened in the last update, keep going
			while((x != t2Col && y != t2Row) || (xTemp == x && yTemp == y))
			{
				//Move horizontally until either reach the x-coordinate of the other terminal, or until hit another wire
				x = moveHorizontal(t1Col, t2Col, t1Row);
				//Move vertically until either hit the y-coordinate of the other terminal, or hit another wire
				y = moveVertical(t1Row, t2Row, x);
			}
			
			//Add the last terminal to the wire 
			addTo(t2Col,t2Row);
		}
		fireTableStructureChanged();
	}
	
	/**
	 * Adds wire sections on a horizontal line from (col, x1) to (col, x2) but stops if a tile is occupied
	 * @param x1
	 * @param x2
	 * @param col
	 * @return where the function stops
	 */
	private int moveHorizontal(int x1, int x2, int col)
	{
		int temp = x1;
		while(temp != x2)
		{
			//If moving to the right...
			if (temp < x2)
			{
				//If the tile moving to is occupied, stop
				if(!board[temp + 1][col].getIsEmpty())
					return temp;
				
				//Otherwise, move
				addTo(temp + 1,col);
				temp++;
			}
			
			//If moving to the left...
			else
			{
				//If the tile being moved to is occupied, stop
				if(!board[temp - 1][col].getIsEmpty())
					return temp;
				
				//Otherwise, move
				addTo(temp,col);
				temp--;
			}
		}
		return temp;
	}
	
	/**
	 * A function that adds wire in a vertical line from (y1, row) to (y2, row) but stops if a tile is occupied
	 * @param y1
	 * @param y2
	 * @param row
	 * @return  where the function stops
	 */
	private int moveVertical(int y1, int y2, int row)
	{
		int temp = y1;
		while(temp != y2)
		{
			//If moving down...
			if (temp < y2)
			{
				//If the tile moving to is occupied, stop
				if(!board[row][temp + 1].getIsEmpty())
				{
					return temp;
				}
				
				//Otherwise, move
				addTo(row,temp);
				temp++;
			}
			
			//If moving up...
			else
			{
				//If the tile moving to is occupied, stop
				if(!board[row][temp - 1].getIsEmpty())
				{
					return temp;
				}
				
				//Otherwise, move
				addTo(row,temp);
				temp--;
			}
		}

		return temp;
	}
}
