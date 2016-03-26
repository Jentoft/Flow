import java.util.*;

public class path 
{
	private LinkedList<Coordinate> pathway = new LinkedList();
	
	public path(Coordinate svalue)
	{
		pathway.add(svalue);
	}
	
	public Coordinate getStart()
	{
		return pathway.get(0);
	}
	
	public Coordinate getEnd()
	{
		return pathway.getLast();
	}
	
	public LinkedList<Coordinate> getPath()
	{
		return pathway;
	}
	
	public void setPath(LinkedList<Coordinate> value)
	{
		pathway = value;
	}
	
	public void updatePath(Coordinate value)
	{
		pathway.add(value);
	}
	
	public boolean intersects(path other)
	{
		for (int i = 0; i < pathway.size(); i++)
		{
			for (int j = 0; j < other.pathway.size(); j++)
			{
				if (pathway.get(i).equals(other.pathway.get(j)))
						return true;
			}
		}
		return false;
	}
	
	public path combine(path other)
	{
		path temp = new path(getStart());
		temp.setPath(pathway);
		LinkedList<Coordinate> otherP = other.getPath();
		for(int i = 0; i < other.getPath().size(); i++)
		{
			updatePath(otherP.get(i));
		}
		return temp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pathway == null) ? 0 : pathway.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		path other = (path) obj;
		if (pathway == null) {
			if (other.pathway != null)
				return false;
		} else if (!pathway.equals(other.pathway))
			return false;
		return true;
	}

	public String toString()
	{
		String result = "{";
		for(int i = 0; i < (pathway.size() - 1); i++)
		{
			result += "[" + pathway.get(i).getColumn() + ", " + pathway.get(i).getRow() + "], ";
		}
		result += "[" + pathway.getLast().getColumn() + ", " + pathway.getLast().getRow() + "]}";
		return result;
	}
}
