import java.util.*;
public class Node<T>
{
	private List<Node<T>> children = new ArrayList<Node<T>>();
	private Node<T> parent = null;
	private T data = null;
	
	public Node(T dat)
	{
		this.data = data;
	}
	
	public Node(Node par, T dat)
	{
		this.parent = par;
		this.data = dat;
	}
	
	public List<Node<T>> getChildren() 
	{
		return children;
	}
	
	public void addChild(T data)
	{
		Node<T> child = new Node(data);
		child.setParent(this);
		this.children.add(child);
	}
	
	public void addChild(Node<T> child)
	{
		child.setParent(this);
		this.children.add(child);
	}
	
	public void setChildren(List<Node<T>> children) 
	{
		this.children = children;
	}
	
	public Node<T> getParent() 
	{
		return parent;
	}
	
	public void setParent(Node<T> parent) 
	{
		parent.addChild(this);
		this.parent = parent;
	}
	
	public T getData() 
	{
		return data;
	}
	
	public void setData(T data) 
	{
		this.data = data;
	}
	
	public boolean isRoot()
	{
		return (this.parent == null);
	}
	
	public boolean isLeaf()
	{
		return equals(this.children.size() == 0);
	}
	
	public void removeParent()
	{
		this.parent = null;
	}
}
