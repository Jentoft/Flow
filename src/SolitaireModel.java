 
public class SolitaireModel extends AbstractModel 
{
	Tile[][] board = new Tile[7][7]; //Matrix corresponding to the board
	Tile selected = null; //Which "peg" is selected?
	int numGone; //How many pegs have been removed
	
	/**
	 * My constructor, sets up the board in the English fashion
	 */
	public SolitaireModel()
	{
		for(int i = 0; i < 7; i++)
		{
			for(int j = 0; j < 7; j++)
			{
				//Initializing those tiles with pegs to their proper values
				if(((i > 1 && i < 5) || (j > 1 && j < 5)) &&(i != 3 || j != 3))
				{
					board[i][j] = new Tile(0, 0, i, j, 0, false);
					board[i][j].setIsEmpty(false);
				}
				
				//Initializing the one initially empty peg
				else if(i == 3 && j == 3)
				{
					board[i][j] = new Tile(0, 0, i, j, 6, false);
					board[i][j].setIsEmpty(true);
				}
				
				//Initializing every other tile
				else
				{
					board[i][j] = new Tile(-1, -1, i, j, 0, false);
				}
			}
		}
		fireTableStructureChanged();
	}
	
	@Override
	public int getColumnCount()
	{
		return 7;
	}

	@Override
	public int getRowCount()
	{
		return 7;
	}

	@Override
	public Object getValueAt(int column, int row)
	{
		return board[column][row];
	}

	@Override
	public String getTitle()
	{
		// TODO Auto-generated method stub
		return "Peg Solitaire";
	}

	/**
	 * A method that resets the board
	 */
	@Override
	public void clear()
	{
		for(int i = 0; i < 7; i++)
		{
			for(int j = 0; j < 7; j++)
			{
				//Initializing those tiles with pegs to their proper values
				if(((i > 1 && i < 5) || (j > 1 && j < 5)) &&(i != 3 || j != 3))
				{
					board[i][j] = new Tile(0, 0, i, j, 0, false);
					board[i][j].setIsEmpty(false);
				}
				
				//Initializing the one initially empty peg
				else if(i == 3 && j == 3)
				{
					board[i][j] = new Tile(0, 0, i, j, 6, false);
					board[i][j].setIsEmpty(true);
				}
				
				//Initializing every other tile
				else
				{
					board[i][j] = new Tile(-1, -1, i, j, 0, false);
				}
			}
		}
		numGone = 0;
		selected = null;
		fireTableStructureChanged();
	}

	@Override
	public boolean isWinner()
	{
		return numGone == 31;
	}

	/**
	 * Method that returns whether a given tile is part of the board
	 */
	@Override
	public boolean isValidSelection(int column, int row)
	{
		if((column < 2 || column > 4) && (row < 2 || row > 4))
			return false;
		return true;
	}

	/**
	 * A function that determines whether the user is selecting a different peg, making an invalid move, or making a valid move, and acting accordingly
	 */
	@Override
	public void addTo(int y, int x)
	{
		//If the click is not on the board, do nothing
		if((x < 0 || y < 0) || (x > 6 || y > 6))
		{
			return;
		}
		
		//If no tile has been selected yet if the user clicks on an occupied tile, set that tile as the "peg" to be moved...
		if(selected == null)
		{
			selected = board[x][y];
		}
		
		else
		{
			int move = isValidMove(board[x][y]);
			//If the move is invalid...
			if(move == -1)
			{
				//If the selected tile has no peg, do nothing
				if(board[x][y].getIsEmpty())
				{
					return;
				}
				
				//Otherwise select that peg
				selected = board[x][y];
				fireTableStructureChanged();
				return;
			}
			
			else
			{
				addPeg(board[x][y]);
				if(move == 0)
				{
					removePeg(board[x][y + 1]);
					removePeg(board[x][y + 2]);
					selected = board[x][y];
				}
				
				else if(move == 1)
				{
					removePeg(board[x][y - 1]);
					removePeg(board[x][y - 2]);
					selected = board[x][y];
				}
				
				else if(move == 2)
				{
					removePeg(board[x - 1][y]);
					removePeg(board[x - 2][y]);
					selected = board[x ][y];
				}
				
				else
				{
					removePeg(board[x + 1][y]);
					removePeg(board[x + 2][y]);
					selected = board[x][y];
				}
			}
		}
		fireTableStructureChanged();
	}

	/**
	 * Getter for numGone
	 */
	@Override
	public int getNumFilled()
	{
		// TODO Auto-generated method stub
		return numGone;
	}

	@Override
	public String getInProgressText()
	{
		// TODO Auto-generated method stub
		return "Game in Progress";
	}

	@Override
	public int setBackgroundColor(Tile x)
	{
		if(x.equals(selected))
			return -2;
		return -1;
	}
	
	/**
	 * Returns an int corresponding to whether a tile is a valid tile to "move" the selected "peg" to, and if so, in which direction
	 * @param x The tile that the peg is trying to move to
	 * @return -1 if the move is invalid
	 * 		   0 if the peg moves up
	 * 		   1 if the peg moves down
	 * 		   2 if the peg moves left
	 * 		   3 if the peg moves right
	 */
	private int isValidMove(Tile x)
	{
		int sCol = selected.getColumn();
		int sRow = selected.getRow();
		int toCol = x.getColumn();
		int toRow = x.getRow();

		//If you're tryin to move to a tile already occupied, it ain't gonna work
		if(!x.getIsEmpty())
		{
			return -1;
		}
		
		//If the tile trying to be moved to is 2 above the currently selected tile, and the intervening tile has a peg in it...
		else if(sCol == toCol && sRow == (toRow + 2) && toRow != 6 && !board[sCol][toRow + 1].getIsEmpty())
		{
			return 0;
		}
		
		//If the tile trying to be moved to is 2 below the currently selected tile, and the intervening tile has a peg in it...
		else if(sCol == toCol && sRow == (toRow - 2) && toRow != 0 && !board[sCol][toRow - 1].getIsEmpty())
		{
			return 1;
		}
		
		//If the tile trying to be moved to is 2 to the left of the currently selected tile, and the intervening tile has a peg in it...
		else if(sRow == toRow && sCol == (toCol - 2) && toCol != 0 && !board[toCol - 1][toRow].getIsEmpty())
		{
			return 2;
		}

		//If the tile trying to be moved to is 2 to the right of the currently selected tile, and the intervening tile has a peg in it...
		else if(sRow == toRow && sCol == (toCol + 2) && toCol != 6 && !board[toCol + 1][sRow].getIsEmpty())
		{
			return 3;
		}

		return -1;
	}
	
	/**
	 * A function that handles the mechanics of removing a peg from the board
	 * @param x which peg is being removed?
	 */
	private void removePeg(Tile x)
	{
		x.setColor(6);
		x.setIsEmpty(true);
		numGone++;
	}
	
	/**
	 * A function that adds a peg to the board
	 * @param x where is the peg being added?
	 */
	private void addPeg(Tile x)
	{
		x.setColor(0);
		x.setIsEmpty(false);
		numGone--;
	}

	@Override
	public Class<?> getColumnClass(int x)
	{
		return Tile.class;
	}

	@Override
	public String getStartText() 
	{
		return "Begin a Game";
	}
	
	/**
	 * Solve function should do nothing
	 */
	public void Solve()
	{
		return;
	}
}
