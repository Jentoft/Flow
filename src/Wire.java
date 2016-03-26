import java.util.*;


public class Wire
{
	private int color;
	private Tile t1;
	private Tile t2;
	private Tile last;
	private boolean isComplete;
	private LinkedList<Tile> list;
	private ArrayList<path> viablePaths = new ArrayList<path>();
	
	/**
	 * Defines a wire given the coordinates of the two terminals and the color associated with the wire
	 * @param x1 Column of the first terminal
	 * @param y1 Row of the first terminal
	 * @param x2 Column of the second terminal
	 * @param y2 Row of the second terminal
	 * @param col color associated with the wire
	 */
	public Wire(int x1, int y1, int x2, int y2, int col)
	{
		t1 = new Tile(0,0,x1,y1,col,true);
		t2 = new Tile(0,0,x2,y2,col,true);
		last = null;
		isComplete = false;
		color = col;
		list = new LinkedList<Tile>();
	}
	
	/**
	 * Setter for isComplete
	 * @param val
	 */
	public void setIsComplete(boolean val)
	{
		isComplete = val;
	}
	
	/**
	 * Getter for isComplete
	 * @return
	 */
	public boolean getIsComplete()
	{
		return isComplete;
	}
	
	/**
	 * Returns the first element in the doubly linked list of tiles
	 * @return
	 */
	public Tile getFirst()
	{
		return t1;
	}
	
	public Tile getT2()
	{
		return t2;
	}

	/**
	 * Returns the first terminal
	 * @return
	 */
	public Tile getTer1()
	{
		return t1;
	}
	
	/**
	 * Returns the second terminal
	 * @return
	 */
	public Tile getTer2()
	{
		return t2;
	}
	
	/**
	 * Allows elements to be added on to the end of the linked list
	 * @param x
	 */
	public void addElement(Tile x)
	{
		list.add(x);
	}
	
	/**
	 * A function that "rewinds" the linked list until the last element in spot
	 * @param spot
	 * @return how many elements have been deleted?
	 */
	public int rewindTo(Tile spot)
	{
		int counter = 0;
		while(list.getLast() != spot)
		{
			list.getLast().resetTile(list.getLast().getIsTerminal());
			list.removeLast();
			list.getLast().setOutShape(-1);
			counter++;
		}
		return counter;
	}
	
	/**
	 * Clears the linked list
	 * @return how many sections of wire have been deleted?
	 */
	public int delete()
	{
		int temp = list.size();
		if(!list.isEmpty())
		{
			rewindTo(list.getFirst());
			list.getFirst().resetTile(true);
			list.clear();
		}
		return temp;
	}
	
	/**
	 * Is the tile x in the linked list?
	 * @param x
	 * @return
	 */
	public boolean contains(Tile x)
	{
		if(x.getIsTerminal() && !isComplete)
		{
			return list.getFirst().equals(x);
		}
		return (x.getColor() == color);
	}
	
	/**
	 * Returns the length of the linked list
	 * @return
	 */
	public int getSize()
	{
		return list.size();
	}
	
	/**
	 * Returns the last element in the linked list
	 * @return
	 */
	public Tile getLast()
	{
		return list.getLast();
	}
	
	/**
	 * What is the color associated with the wire?
	 * @return
	 */
	public int getColor()
	{
		return color;
	}
	
	public ArrayList<path> getPaths()
	{
		return viablePaths;
	}
	
	public void setPaths(ArrayList<path> value)
	{
		viablePaths = value;
	}
}
