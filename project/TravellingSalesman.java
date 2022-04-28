package project;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
//import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.*;
public class TravellingSalesman {
	public static JFrame windowFrame = null;
	static ArrayList<School> pathTaken = new ArrayList<School>();
	static ArrayList<School> bestPath = new ArrayList<School>();
	static School[] schools;
	static double bestDistance = Integer.MAX_VALUE;
	public static double[] mapBounds = {55.42568667088451, -10.817886799465555, 51.386428234540446, -5.147800097919487};
	public static double[] screenBounds = {-40, 10, 772, 936};
//	public static double[] screenBounds = {-40, 10, 1544, 1872};
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//use rewritten dictionary to read the 121 schools from the file
		//dictionary has been modified so that the input array is a 2D array of doubles containing the school coordinates
		Dictionary dictionary = new Dictionary();
//		System.out.println(dictionary.getSize());
		
		//take the dictionary array and build the school objects. This will create an array of 121 objects, each of which has a school's adjacency array in it
		schools = buildSchoolMatrix(dictionary.input);
		int t = 1;
		for (int i = 0; i < t; i++)
		{
			pathTaken = new ArrayList<School>();
			double newDistance = travellingUniversitySalesman();
			if (newDistance < bestDistance)
			{
				bestDistance = newDistance;
				bestPath = pathTaken;
			}
		}
		
//		System.out.println(schools.length);
		int x = 0;
		for (School s : bestPath)
		{
			System.out.print(s.index);
			if (x < pathTaken.size() - 1)
			{
				System.out.print(",");
			}
			x++;
		}
		System.out.println("\n" + bestDistance + "km");
		
		//the window is run here for thread safety
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						createVisualiserWindow();
					}
				});
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
				Image img = icon.getImage().getScaledInstance(((int) screenBounds[2]), ((int) screenBounds[3]), Image.SCALE_DEFAULT);
				{setOpaque(false);}
				public void paintComponent(Graphics graphics)
				{
					super.paintComponent(graphics);
					
					graphics.drawImage(img, 0, 0, this);
					int radius = 7;
					School maynoothUni = bestPath.get(0);
					//Maynooth 53.3842125238019,-6.60088967821164
					// if 53.3 = 491 and -6 = 370, how do I convert?
					// lat = 9.1974757477425109904881996274741
					// longt = -56.053050124637597748929034413172
//					double[] screenXY = getXYFromSchoolGPS(maynoothUni);
//					int x = (int) Math.round(screenXY[0]);
//					int y = (int) Math.round(screenXY[1]);
//					graphics.fillOval(x, y, radius, radius);
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
		System.out.println(windowResolution);
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
		System.out.println(screenPos[0] + ", " + screenPos[1]);
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
	
	public static double travellingUniversitySalesman()
	{
		//plot the shortest route between all points in our array of schools
		plotRoute(schools);
		
		//the distance of the shortest route found
		double distance = calculateDistance(pathTaken);
//		System.out.println(distance);
		
		//2-opt, advance through the entire route and try to swap each block of four. If new route is shorter, overwrite route.
		int i = 1; int k = 4;
		ArrayList<School> newRoute = pathTaken;
		double newDistance = distance;
		while (k < pathTaken.size() - 1) {
			newRoute = twoOptSwap(newRoute, i, k);
			i++;
			k++;
			newDistance = calculateDistance(newRoute);
			
			if (newDistance < distance)
			{
				System.out.println(newDistance);
				pathTaken = newRoute;
				distance = newDistance;
			}
		}
		return distance;
	}
	
	public static double calculateDistance(ArrayList<School> schools)
	{
		double distance = 0;
		for (School school : schools)
		{
			int index = schools.indexOf(school);
			if (index > 0)
			{
				School lastSchool = schools.get(index - 1);
				distance += lastSchool.adjacenciesHashMap.get(school);
			}
		}
		return distance;
	}
	
	public static ArrayList<School> twoOptSwap(ArrayList<School> path, int i, int k)
	{
		ArrayList<School> tempPath = new ArrayList<School>();
		for (int j = 0; j < i; j++)
		{
			tempPath.add(path.get(j));
		}
		for (int j = k; j >= i; j--)
		{
			tempPath.add(path.get(j));
		}
		for (int j = k+1; j < pathTaken.size();j++)
		{
			tempPath.add(path.get(j));
		}
		return tempPath;
	}
	
	//greedy solution that just takes the shortest next node each time
	public static int[] plotRoute(School[] schools)
	{
		
		int[] route = new int[schools.length];
		PriorityQueue<School> pq = new PriorityQueue<School>();
		
		//we start at index 0 in the schools array and plot the shortest route. 
		School currSchool = schools[0];
		
		
		while(pathTaken.size() < schools.length)
		{
			School nextSchool = null;
			double currShortest = Double.MAX_VALUE;
			for (Map.Entry<School, Double> entry : currSchool.adjacenciesHashMap.entrySet()) {
				double distance = entry.getValue();
//				Random r = new Random();
//				boolean randomBool = r.nextBoolean();
//				double random = Math.floor(Math.random()*(20-1+1)+ 1);
//				if (randomBool)
//					random *= -1;
//				distance += random;
				if (distance < currShortest && entry.getValue() > 0 && !pathTaken.contains(entry.getKey()))
				{
					currShortest = entry.getValue();
					nextSchool = entry.getKey();
				}
				
			}
			pathTaken.add(currSchool);
			currSchool = nextSchool;
		}
		
		//2-opt algorithm
		
		pathTaken.add(schools[0]);
		
		
		//
		
		return route;
	}
	
	public static School[] buildSchoolMatrix(double[][] arr)
	{
		School[] schools = new School[arr.length];
		for (int i = 0; i < arr.length; i++)
		{
			schools[i] = new School(i, arr[i]);
		}
		
		for (int i = 0; i < schools.length; i++)
		{
			schools[i].fillAdjacencyMatrix(schools);
		}
		
		return schools;
	}

}

class School {
	int index;
	ArrayList<Double> adjacencies;
	HashMap<School, Double> adjacenciesHashMap = new HashMap<School, Double>();
	School prevSchool = null;
	double lat;
	double longt;
	
	School(int i, double[] coords)
	{
		index = i;
		this.lat = coords[0];
		this.longt = coords[1];
	}
	
	public void fillAdjacencyMatrix(School[] arr)
	{
		this.adjacencies = new ArrayList<Double>();
		for (int i = 0; i < arr.length; i++)
		{
			double distance = getGPSDistance(lat, arr[i].lat, longt, arr[i].longt);
			this.adjacenciesHashMap.put(arr[i], distance);
			this.adjacencies.add(getGPSDistance(lat, arr[i].lat, longt, arr[i].longt));
		}
		
		Collections.sort(this.adjacencies);
	}
	
	public static double getGPSDistance(double lat1, double lat2, double long1, double long2)
	{
		int earth = 6371;
		//distance between two points on a sphere: radius * central angle
		//we must solve for central angle, which involves the haversine function
		//haversine formula: calculate the haversine of 0 (hav(0)) from the lat and long
		//hav(0) = hav(lat2 - lat1) + cos(lat1) * cos(lat2) * hav(long2 - long1)
		Double latDist = Math.toRadians(lat2 - lat1);
		Double latSum = Math.toRadians(lat1 + lat2);
		Double longDist = Math.toRadians(long2 - long1);
		
		//haversine function = sin^2(theta / 2) = (1 - cos(theta))/2
		Double havLat = haversineFunction(latDist);
		Double havLong = haversineFunction(longDist);
		Double havLatSum = haversineFunction(latSum);
		
		//solve for central angle, 
		Double centralAngle = 2 * Math.asin(Math.sqrt(
				(havLat + (1 - havLat - havLatSum)*havLong)
				));
		Double distance = earth * centralAngle;
		
		return distance;
	}
	
	public static double haversineFunction(double theta)
	{
//		return Math.sin(theta / 2) * Math.sin(theta / 2);
		return Math.pow(Math.sin(theta/2), 2);
	}
}
