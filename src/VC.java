import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableCellRenderer;

public class VC implements ActionListener, ListSelectionListener
{
	AbstractModel model;
	JFrame mainframe;
	JComboBox gameSelect; //Allowing you to choose the game
	JPanel buttons;//To hold the buttons
	JPanel overall; //To set the overall format
	JTable board; //To hold the board
	JButton load; //To load a board
	JButton solve; //To solve a board
	JButton clear; //To reset the board
	JLabel status; //To say the status of the board
	JLabel data; //To say how many tiles have been changed
	
	/**
	 * My constructor
	 */
	public VC()
	{
		JPanel stat = new JPanel();
		JPanel dat = new JPanel();
		model = new RouterModel(5, 2, new int[4]);
		String[] selection = new String[2];
		selection[0] = "Router";
		selection[1] = "Solitaire";
		gameSelect = new JComboBox(selection);
		if(model != null)
		{
			mainframe = new JFrame(model.getTitle());
		}
		else
		{
			mainframe = new JFrame();
		}
		board = new JTable(model);
		board.getSelectionModel().addListSelectionListener(this);
		data = new JLabel();
		data.setText("No Tiles Changed");
		
		//Formatting the board
		for(int i = 0; i < board.getColumnCount(); i++)
		{
			board.getColumnModel().getColumn(i).setPreferredWidth(50);
		}
		board.setRowHeight(50);
		
		//Setting up my graphics
		board.setDefaultRenderer(model.getColumnClass(0), new TableCellRenderer()
		{

			@Override
			public Component getTableCellRendererComponent(JTable brd,
					Object t, boolean isSelected, boolean hasfocus, int row, int column)
			{
				Tile tile = (Tile)t;
				BufferedImage bi = new BufferedImage(50,50,BufferedImage.TYPE_INT_RGB);
				Graphics2D graph = (Graphics2D)bi.getGraphics();
				
				//Setting the background color
				if(model.setBackgroundColor(tile) == -1)
				{
					graph.setBackground(Color.white);
				}
				else
				{
					graph.setBackground(Color.black);
				}
				graph.clearRect(0, 0, 50, 50);
				graph.setColor(findColor(tile));
				
				//Drawing the shapes
				if(tile.getInShape() == 0 || tile.getOutShape() == 0)
				{
					graph.fillOval(15,15,20,20);
				}
				if(tile.getInShape() == 1 || tile.getOutShape() == 1)
				{
					graph.fillRect(25,20,25,10);
				}
				if(tile.getInShape() == 2 || tile.getOutShape() == 2)
				{
					graph.fillRect(0,20,25,10);
				}
				if(tile.getInShape() == 3 || tile.getOutShape() == 3)
				{
					graph.fillRect(20,20,10,30);
				}
				if(tile.getInShape() == 4 || tile.getOutShape() == 4)
				{
					graph.fillRect(20,0,10,30);
				}
				ImageIcon ii = new ImageIcon(bi);
				JLabel temp = new JLabel(ii);
				temp.setPreferredSize(new Dimension(50,50));
				return temp;
			}
		});
		
		load = new JButton("Load");
		solve = new JButton("Solve");
		clear = new JButton("Reset");
		gameSelect.addActionListener(this);
		buttons = new JPanel();
		buttons.setLayout(new GridLayout(1,3));
		status = new JLabel("Load a Board");
		status.setHorizontalAlignment(SwingConstants.LEFT);
		status.setBackground(Color.red);
		stat.add(status);

		overall = new JPanel();
		overall.setLayout(new BoxLayout(overall,BoxLayout.Y_AXIS));
		if(gameSelect.getSelectedIndex() == 0)
		{
			load.addActionListener(this);
			solve.addActionListener(this);
		}
		clear.addActionListener(this);
		
		overall.add(gameSelect);
		overall.add(stat);
		overall.add(board);
		buttons.add(load);
		buttons.add(solve);
		buttons.add(clear);
		overall.add(buttons);
		
		dat.add(data);
		overall.add(dat);
		
		mainframe.add(overall);
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainframe.pack();
		mainframe.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		VC gui = new VC();
		return;
	}
	
	private boolean loadBoard()
	{
        //pop up a file dialog for the user and collect a file descriptor
        FileDialog fd = new FileDialog(mainframe, "Choose a file", FileDialog.LOAD);
        fd.setVisible(true);
        String directory = fd.getDirectory();
        String filename = fd.getFile();
        Scanner in = null;
        try
        {
            //Create a Scanner for this file
            in = new Scanner(new File(directory+filename));
            //board size
            int size = in.nextInt();
            //allocate enough space to hold all coordinates based on second number
            int[] starters = new int[in.nextInt()*4];
            //collect specified number of coordinates
            for(int i = 0; i < starters.length; i++)
            {
                starters[i] = in.nextInt();
            }
            System.out.println("]");
            //Calls my model constructor for a Router model since solitaire doesn't need to import a board
            model = new RouterModel(size, starters.length/4, starters);
            
            board.setModel(model);

            //variables size and starters now contain all relevant info, so make your board however you do that
            //clean up after ourselves
            in.close();
            return true;
        } 
        catch (FileNotFoundException ex) 
        {
            JOptionPane.showMessageDialog(mainframe, ex.getMessage(),"Invalid File",JOptionPane.ERROR_MESSAGE );
        }
        catch(IllegalArgumentException ex)
        {
            JOptionPane.showMessageDialog(mainframe, ex.getMessage(),"File does not contain loadable board",JOptionPane.ERROR_MESSAGE );
        }
        catch (Exception ex) 
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainframe, ex.getMessage(),"Unexpected Error",JOptionPane.ERROR_MESSAGE );
        }
        finally{//if things have gone south, clean up the Scanner
            if(in != null){
                in.close();
            }
        }
        return false;
    }

	@Override
	public void actionPerformed(ActionEvent event)
	{
		//Selecting a game
		if(event.getSource().equals(gameSelect))
		{
			int choice = gameSelect.getSelectedIndex();
			//If you select Router...
			if(choice == 0)
			{
				model = new RouterModel(5, 2, new int[4]);
				board.setModel(model);
				load.addActionListener(this);
			}
			//If you select Solitaire
			else
			{
				model = new SolitaireModel();
				board.setModel(model);
				status.setText("Begin Game");
				data.setText("No Tile Changed");
				load.removeActionListener(this);
			}
			
			//Making sure the board remains the right size
			mainframe.setVisible(false);
			for(int i = 0; i < board.getColumnCount(); i++)
			{
				board.getColumnModel().getColumn(i).setPreferredWidth(50);
			}
			board.setRowHeight(50);
			mainframe.pack();
			mainframe.setVisible(true);
			status.setText(model.getStartText());
		}
		
		//Reseting the board
		if(event.getSource().equals(clear))
		{
			model.clear();
			status.setText(model.getStartText());
			data.setText("No Tiles Changed");
		}
		
		//Loading a board
		if(event.getSource().equals(load))
		{
			loadBoard();
		}
		
		//Solving the puzzle
		if (event.getSource().equals(solve))
		{
			model.Solve();
		}
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if(!e.getValueIsAdjusting())
		{
			//Defining the text in the two JLabels
			model.addTo(board.getSelectedColumn(), board.getSelectedRow());
			if(model.isWinner())
			{
				status.setText("Game Over");
			}
			else
			{
				status.setText(model.getInProgressText());
			}
			
			if(model.getNumFilled() == model.getColumnCount()*model.getColumnCount())
				status.setText("Perfect Game");
			if(model.getNumFilled() == 0)
			{
				data.setText("No Tiles Changed");
			}
			else
			{
				data.setText("Number changed: " + model.getNumFilled());
			}
		}
	}
	
	/**
	 * Converts from the integer colors of the model to actual colors used by graphics2D
	 * @param x
	 * @return
	 */
	private Color findColor(Tile x)
	{
		int c = x.getColor();
		if(c == 0)
			return Color.RED;
		if(c == 1)
			return Color.blue;
		if(c == 2)
			return Color.green;
		if(c == 3)
			return Color.yellow;
		if(c == 4)
			return Color.cyan;
		if(c == 5)
			return Color.gray;
		if(c == 6)
			return Color.darkGray;
		if(c == 7)
			return Color.orange;
		if(c == 8)
			return Color.magenta;
		return Color.pink;
	}
}
