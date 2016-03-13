import javax.swing.table.*;


public abstract class AbstractModel extends AbstractTableModel
{
	//Add public to all
	public abstract String getTitle();
	
	public abstract void clear();
	
	public abstract boolean isWinner();
	
	public abstract boolean isValidSelection(int column, int row);
	
	public abstract void addTo(int x, int y);
	
	public abstract int getNumFilled();
	
	public abstract String getInProgressText();

	public abstract int setBackgroundColor(Tile x);
	
	public abstract String getStartText();
	
	public abstract void Solve();
}
