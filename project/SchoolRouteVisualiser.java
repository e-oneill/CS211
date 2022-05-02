package project;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SchoolRouteVisualiser {
	static double[] mapBounds;
	static double[] screenBounds;
	static ArrayList<School> bestPath;
	public static JFrame windowFrame = null;
	
	public static void setVariables(double[] mapBounds, double[]screenBounds, ArrayList<School> bestPath)
	{
		SchoolRouteVisualiser.mapBounds = mapBounds;
		SchoolRouteVisualiser.screenBounds = screenBounds;
		SchoolRouteVisualiser.bestPath = bestPath;
	}
	
	public static void createVisualiserWindow() {
		//Element Size variables;
		Dimension windowResolution = new Dimension(716, 754);
		//Create the window
		windowFrame = new JFrame("Travelling Salesman");
		windowFrame.setTitle("Travelling Salesman");
		windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		;
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("E:\\Gits\\CS211\\project\\ireland3.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final ImageIcon icon = new ImageIcon(img);
		JPanel text = new JPanel()
		{
			private static final long serialVersionUID = 1L;
				Image img = icon.getImage().getScaledInstance(((int) screenBounds[2]), ((int) screenBounds[3]), Image.SCALE_DEFAULT);
				{setOpaque(false);}
				public void paintComponent(Graphics graphics)
				{
					super.paintComponent(graphics);
					
					graphics.drawImage(img, 0, 0, this);
					int radius = 7;
					int lastX = -1, lastY = -1;
					for (School s : bestPath)
					{
						double[] screenXY = getXYFromSchoolGPS(s);
						int x = (int) Math.round(screenXY[0]);
						int y = (int) Math.round(screenXY[1]);
						graphics.fillOval(x - (radius/2), y - (radius/2), radius, radius);
						if (lastX != -1)
						{
							graphics.drawLine(x, y, lastX, lastY);
						}
						lastX = x;
						lastY = y;
					}
				}
		};
		JScrollPane pane = new JScrollPane(text);
		Container content = windowFrame.getContentPane();
		content.add(pane, BorderLayout.CENTER);
//		windowFrame.setSize(400, 300);
//		windowFrame.setUndecorated(true);
		
//		windowFrame.setPreferredSize(windowResolution);
//		windowFrame.setMinimumSize(windowResolution);
//		windowFrame.setMaximumSize(windowResolution);
		windowFrame.pack();
		Insets ins = windowFrame.getInsets();
		windowFrame.revalidate();
		windowResolution = new Dimension((int) 1920, ((int) 1080 - ins.top));
//		System.out.println(windowResolution);
		windowFrame.setPreferredSize(windowResolution);
		windowFrame.setMinimumSize(windowResolution);
		windowFrame.setMaximumSize(windowResolution);
		windowFrame.setVisible(true);
	}
	
	public static double[] getXYFromSchoolGPS(School school)
	{
		//upper left corner: 55.42568667088451, -10.817886799465555
		//bottom right corner: 51.386428234540446, -5.147800097919487
		//the double array contains:
		//0: X value, 1: Y value, 2: GPS Lat, 3: GPS Long
		int r = 6371;
		double[] globalStart = GPStoGlobalXY(mapBounds[0], mapBounds[1]);
		double[] globalEnd = GPStoGlobalXY(mapBounds[2], mapBounds[3]);
//		System.out.println("Starts at: " + globalStart[0] + "," + globalStart[1] + "\nEnds at: " + globalEnd[0] + "," + globalEnd[1]);
		
		double[] pos = GPStoGlobalXY(school.lat, school.longt);
//		System.out.println(pos[0] + ", " + pos[1]);
		double[] relativePos = new double[2];
		relativePos[0] = ((pos[0] - globalStart[0])/(globalEnd[0] - globalStart[0]));
		relativePos[1] = ((pos[1] - globalStart[1])/(globalEnd[1] - globalStart[1]));
//		System.out.println(relativePos[0] + ", " + relativePos[1]);
		
		double[] screenPos = new double[2];
		screenPos[0] = screenBounds[0] + (screenBounds[2] - screenBounds[0])*relativePos[0];
		screenPos[1] = screenBounds[1] + (screenBounds[3] - screenBounds[1])*relativePos[1];
//		System.out.println(screenPos[0] + ", " + screenPos[1]);
		return screenPos;
	}
	
	public static double[] GPStoGlobalXY(double lat, double longt)
	{
		double[] xy = new double[2];
		double x = (6371*longt*Math.cos((mapBounds[0] + mapBounds[2])/2));
		double y = (6371*lat);
		xy[0] = x;
		xy[1] = y;
		
		return xy;
	}
}
