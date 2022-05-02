package project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class School {
		int index;
		ArrayList<Double> adjacencies;
		double distance = Double.MAX_VALUE;
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
//			return Math.sin(theta / 2) * Math.sin(theta / 2);
			return Math.pow(Math.sin(theta/2), 2);
		}
}
