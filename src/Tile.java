
public class Tile 
{
	private int inShape; //What direction is the wire coming into the tile from
	private int outShape; //What direction is the wire going out of the tile to
	private int row; //The row of the tile on the board
	private int column; //The column of the tile on the board
	private int color; //An int representing the color of the wire the tile belongs to
	private boolean isEmpty; //Does the tile have a section of wire that is not a terminal in it?
	private boolean isTerminal; //Is the tile a terminal?
	
	/**
	 * My constructor for my tile object
	 * @param is inShape
	 * @param os outShape
	 * @param x column
	 * @param y row
	 * @param col color
	 * @param ter isTerminal
	 */
	public Tile(int is, int os, int x, int y, int col, boolean ter)
	{
		inShape = is;
		outShape = os;
		row = y;
		column = x;
		color = col;
		isEmpty = true;
		isTerminal = ter;
	}
	
	/**
	 * Getter for inShape
	 * @return
	 */
	public int getInShape()
	{
		return inShape;
	}
	
	/**
	 * Setter for inShape
	 * @param val new value
	 */
	public void setInShape(int val)
	{
		inShape = val;
	}
	
	/**
	 * getter for outShape
	 * @return
	 */
	public int getOutShape()
	{
		return outShape;
	}
	
	/**
	 * Setter for outShape
	 * @param val new value
	 */
	public void setOutShape(int val)
	{
		outShape = val;
	}
	
	/**
	 * Getter for row
	 * @return
	 */
	public int getRow()
	{
		return row;
	}
	
	/**
	 * Getter for column
	 * @return
	 */
	public int getColumn()
	{
		return column;
	}

	/**
	 * Getter for color
	 * @return
	 */
	public int getColor()
	{
		return color;
	}
	
	/**
	 * Setter for color
	 * @param c new value
	 */
	public void setColor(int c)
	{
		color = c;
	}
	
	/**
	 * Getter for isEmpty
	 * @return
	 */
	public boolean getIsEmpty()
	{
		return isEmpty;
	}
	
	/**
	 * Setter for isEmpty
	 * @param tf
	 */
	public void setIsEmpty(boolean tf)
	{
		isEmpty = tf;
	}
	
	/**
	 * Getter for isTerminal
	 * @return
	 */
	public boolean getIsTerminal()
	{
		return isTerminal;
	}
	
	/**
	 * removes any wire segment on the tile
	 * @param isTerminal
	 */
	public void resetTile(boolean isTerminal)
	{
		if(isTerminal)
		{
			isEmpty = true;
			inShape = 0;
			outShape = 0;
		}
		else
		{
			isEmpty = true;
			inShape = -1;
			outShape = -1;
			color = -1;
		}
	}
}