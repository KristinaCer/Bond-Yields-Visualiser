import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.geom.Point2D;
import java.io.File;

import java.util.function.Function;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
 
 



public class ProgramFrame  extends JFrame {
	
		private JButton openButton, closeButton;
		private JTextField fileNameField, tradeDetailsField; 
		private JComboBox xField,yField; 
		private Scatterplot chartPanel;
		private final BondTradesService service = new BondTradesService();
 
		private JPanel mainPanel; 
	
		/**
		 * Function was implemented in order to connect the scatter plot points and with the mouse listener.
		 */
		
		private Function <Point2D,Object> onPointClick = new Function<Point2D, Object>(){
			
			public Object apply (Point2D point) {
				BondPoint bondPoint = (BondPoint)point;
				BondTrade bondTrade = bondPoint.bondTrade;//retrieving bondTrade object since it is connected to the Point2D object in the BondPoint class.

				tradeDetailsField.setText(bondTrade.toString()); //Converting bondTrade object into the string. 
				return null;
			}
			
		};
		
		/** 
		 * Listeners were implemented using anonymous classes.
		 */
		
		private ActionListener axisChangeListener = new ActionListener() {
 
			public void actionPerformed(ActionEvent e) { 

				drawChart((String)xField.getSelectedItem(),(String)yField.getSelectedItem());
				
				
			}
			
		};
		
		private ActionListener openButtonListener = new ActionListener() {
 
			public void actionPerformed(ActionEvent e) { 
				
				showFileChooserDialog(); 
			}
			
		};

		private ActionListener closeButtonListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) { 
				dispose(); 
			}
			
		};
		
		public static void main(String[] args) {
			ProgramFrame frame = new ProgramFrame(); 
			frame.show(); 
		}
		
		
		public ProgramFrame() {
			this.setSize(500,500);  
			setup();
			this.setVisible(true); 
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		}
		
		
	private void setup() {

		mainPanel = new JPanel(new BorderLayout()); 
		this.add(mainPanel);

		openButton = new JButton("Open");

		openButton.addActionListener(openButtonListener);

		fileNameField = new JTextField("     <Name of file>     ");

		fileNameField.setEditable(false);

		closeButton = new JButton("Close");

		closeButton.addActionListener(closeButtonListener);

		JPanel thePanel2 = new JPanel();
		
		thePanel2.setLayout(new BoxLayout(thePanel2, BoxLayout.X_AXIS));
		
		thePanel2.add(openButton);
		
		thePanel2.add(fileNameField);
		
		thePanel2.add(closeButton);
		
		mainPanel.add(thePanel2, BorderLayout.NORTH);
		
		
		xField = new JComboBox(BondTrade.AXIS );
		xField.setSelectedIndex(1); //setting default axis using values from the String [] axis array.
		
		xField.addActionListener(axisChangeListener);

		yField = new JComboBox(BondTrade.AXIS);
		yField.setSelectedIndex(0); //setting default axis using values from the String [] axis array.
		
		yField.addActionListener(axisChangeListener);

		JPanel thePanel3 = new JPanel(); 
		thePanel3.setLayout(new BoxLayout(thePanel3, BoxLayout.X_AXIS));
		
		thePanel3.add(xField);
		thePanel3.add(yField);
		

		tradeDetailsField = new JTextField("<Detail of a selected trade>");
		tradeDetailsField.setEnabled(false); 

		JPanel thePanel5 = new JPanel(); 
		thePanel5.setLayout(new BoxLayout(thePanel5, BoxLayout.X_AXIS));
		thePanel5.add(tradeDetailsField);
		thePanel3.add(thePanel5, BorderLayout.SOUTH);
		 
		chartPanel = new Scatterplot(onPointClick); //passing on pointClickClick function to the Scatterplot.

		JPanel thePanel4 = new JPanel(); 
		thePanel4.setLayout(new BoxLayout(thePanel4, BoxLayout.X_AXIS));

		thePanel4.add(chartPanel); 

		mainPanel.add(thePanel4, BorderLayout.CENTER);
		

		mainPanel.add(thePanel3, BorderLayout.SOUTH);
	
			
		}
		
	/**
	 * DrawChart method is used to draw axes, remove all the previously drawn series
	 * before loading the new ones. The method loops through the data (yield,
	 * amount, days to maturity) retrieved from the BondTradeService class and saves
	 * them in the BondTrade object. The data is then redefined based on the xAxis
	 * and yAxis passed to the object from the inner BondPoint class implemented
	 * bellow. BondPoint objects are then added to point array list and to the
	 * chartPanel.
	 * 
	 */
		
		private void drawChart(String xAxis, String yAxis) {
			
		    chartPanel.xAxis = xAxis;
		    chartPanel.yAxis = yAxis;

			chartPanel.points.clear();
			
		    for (BondTrade bond : service.getBondTrades()) {
		    	Point2D point = new BondPoint(bond.getByAxisLabel(xAxis), bond.getByAxisLabel(yAxis), bond);
		    	chartPanel.points.add(point); 
		    } 
		    
		    chartPanel.repaint();
		}
		
		
		
	/**
	 * The method showFileChooserDialog is used to show the JFileChooser dialog and
	 * obtain the file from the user; it also draws the chart using the obtained
	 * data.
	 **/
		
		
		private void showFileChooserDialog() {
			JFileChooser chooser = new JFileChooser(); 
			
			FileFilter filter = new FileNameExtensionFilter("CSV files only","csv"); //explicitly choosing the csv format files.
			
			chooser.setFileFilter(filter);
			
			//chooser.addActionListener(fileDialogListener);
			
  			int value = chooser.showOpenDialog(null); 
			
			if (value == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				fileNameField.setText(file.getAbsolutePath());
				try {
				    service.readFile(file);
				} catch (Exception e) { 
					JOptionPane.showMessageDialog(this, "Failed to read data \n" + e.getMessage()); //Printing out the message of the error if .
				}
				
				drawChart((String)xField.getSelectedItem(), (String)yField.getSelectedItem()); 
			} 
			
		}       
		
	/**
	 * The inner BondPoint class includes the new constructor of the Point2D class, with the added
	 * reference to the BondTrade object in order to associate the bondTrade object and its
	 * coordinates when it is needed. 
	 */
		
		
		private  class BondPoint extends Point2D.Double {
			
			final BondTrade bondTrade;
			
			public BondPoint (double x, double y, BondTrade bondTrade) { 
				super(x,y);
				this.bondTrade = bondTrade;
			}
			
		}
			       
}


