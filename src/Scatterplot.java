import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import java.util.List;


import java.util.function.Function;


import javax.swing.JPanel;

 


public class Scatterplot extends  JPanel {
  
    public final List<Point2D > points = new ArrayList<>(); //the ArrayList of point objects retrieved from the user input. 
    
    private final List<Point> graphicPoints = new ArrayList<>(); //the ArrayList of the graphical representation of the points.
    
    private double pointRadius = 10.0; 
    private double padding = 40.0; //value used to generate the borders on the GUI, selected based on the experimentation. 
    private double dotPadding;
    public String xAxis = "X axis";
    public String yAxis = "Y axis";   
    private final Function<Point2D, Object> onPointClicked; //saving the reference passed to the Scatterplot constructor.
    
    
    public Scatterplot(Function<Point2D, Object> onPointClick ) { 
 
    	this.onPointClicked = onPointClick;
    	
        addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) { 
			    repaint();
				// TODO Auto-generated method stub
				for (Point point : graphicPoints) { //going through all the saved graphical representations of the points to check if any of objects extended from the Point2D class contains the same coordinates.
					if (point.contains(e .getX(), e .getY())) {  
						onPointClick.apply(point.pointReference); 
						return; //If point.contains(e .getX(), e .getY() is true, then the point objects that has these coordinates is converted to string by using apply method.
					}
				} 
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}});
         

        setVisible(true);
    }
     
    
    	double xMin;
    	double xMax;
    	double yMin;
    	double yMax;
    	double xRatio;
    	double yRatio;
    	
        @Override
        public void paintComponent(Graphics g) { 

            double r = pointRadius / 2;
        	dotPadding = padding + r;

            findMinMax(); 
        	
            super.paintComponent(g); 
             
            //Creating border graphics:
            
            Graphics2D borderGraphics = (Graphics2D) g.create();   
            
            borderGraphics.draw(new Rectangle2D.Double(padding, padding, getWidth() - padding * 2, getHeight() - padding * 2)); 
  
            g.setColor(Color.BLACK);  
            
           // Creating and setting the font for labels:
            
            Font font = new Font("Arial", Font.BOLD, 20); 
            
            g.setFont(font); 
            
            // Creating text graphics:
            
            Graphics2D axisTextGraphics = (Graphics2D) g.create(); 
            
            textRotate(axisTextGraphics, padding - 5, getHeight()/2, 270, yAxis); //adjusting the label's text to the y axis.
            
            textRotate(axisTextGraphics, getWidth()/2, getHeight() - 5, 0, xAxis); //adjusting the label's text to the x axis.
            
            
           //Drawing the minimum and maximum values that define x and y axes: 
            
            g.setColor(Color.GRAY);   
            g.setFont(new Font("Arial", Font.BOLD, 12));
            Graphics2D rangeTextGraphics = (Graphics2D) g.create();
            
            textRotate(rangeTextGraphics, padding - 5, getHeight() - padding, 270,  yMin + ""); //The values - 5 and 12 are "the magic numbers" used to adjust text to the x and y axis, selected based on experimentation. 
            textRotate(rangeTextGraphics, padding, getHeight() - padding + 12 , 0,  xMin + ""); 

            textRotate(rangeTextGraphics, padding - 5,  padding + 12 , 270,  yMax + ""); 
            textRotate(rangeTextGraphics, getWidth() - padding, getHeight() - padding + 12 , 0,  xMax + ""); 
            
           //Drawing data points on the graph:
            
            g.setColor(Color.BLUE);  
            Graphics2D pointGraphics = (Graphics2D) g.create(); 
     

            graphicPoints.clear(); // Removing any remaining points before drawing the new ones.
            
    
		/**
		 * Getting data from the List <Point2D> points, storing values in Point2D
		 * point, then adjusting those values to fit in the graph and adding them to the
		 * graph in the for of graphic representation of Point2D points.
		 */
            
            for (Point2D  point : points) {  
            	double x = (point.getX() - xMin ) * xRatio - r  + dotPadding; 
            	double y =  getHeight() - pointRadius - ((point.getY() - yMin ) * yRatio - r + dotPadding); 
                Point dot = new Point(x , y, pointRadius, point);  
                graphicPoints.add(dot);
                pointGraphics.fill(dot);
            } 
  
            pointGraphics.dispose(); 
            borderGraphics.dispose(); 
            axisTextGraphics.dispose(); 
        }  
        
        /**
         * TextRotate method functions to rotate the x and y axes labels.
         */

	private void textRotate(Graphics2D gg, double x, double y, int angle, String text) {
		gg.translate((float) x, (float) y);
		gg.rotate(Math.toRadians(angle));
		gg.drawString(text, 0, 0);
		gg.rotate(-Math.toRadians(angle));
		gg.translate(-(float) x, -(float) y);
	}
		
	/**
	 * The findMinMax method finds all the "extreme" values and adjust the values to
	 * fit in the graph.
	 */
		
        private void findMinMax() {  
        	
            for (Point2D  point : points) {   
            	if (point.getX() < xMin ) {
            		xMin = point.getX();
            	} else if (point.getX() > xMax) {
            		xMax = point.getX();
            	}

            	if (point.getY() < yMin ) {
            		yMin = point.getY();
            	} else if (point.getY() > yMax) {
            		yMax = point.getY();
            	}
            } 
            
            xRatio =  (this.getWidth() - dotPadding*2 )/(xMax - xMin );
            yRatio =  (this.getHeight()- dotPadding*2 )/(yMax - yMin );
        }
         
        
        
 
	/**
	 * The inner Point class includes the new constructor of the Ellipse2D.Double
	 * class, with the added Point2D reference.
	 */

	private class Point extends Ellipse2D.Double {

		public Point(double x, double y, double radius, Point2D reference) {
			super(x, y, radius, radius);
			pointReference = reference;

		}

		final Point2D pointReference;

	}

     
    
}