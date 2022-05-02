package project;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
		
		SchoolRouteVisualiser.setVariables(mapBounds, screenBounds, bestPath);
		
		System.out.println("\n" + bestDistance + "km");
		//the window is run here for thread safety
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						SchoolRouteVisualiser.createVisualiserWindow();
					}
				});
	}
	
	public static void nearestFragment(int connector, int fragmentStart, int fragmentEnd, ArrayList<School> path) {
		
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
//				System.out.println(newDistance);
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
//		PriorityQueue<School> pq = new PriorityQueue<School>();
		
		//we start at index 0 in the schools array and plot the shortest route. 
		School currSchool = schools[0];
		currSchool.distance = 0;
		
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


