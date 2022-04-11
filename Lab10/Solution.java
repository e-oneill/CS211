package Lab10;

import java.util.ArrayList;
//import java.lang.Math.*;
import java.util.Arrays;
import java.util.Collections;
public class Solution {
	public static boolean debugString = false;
	static int earth = 6371;
	static double[][] schools = {
			{51.62497513,	-8.887028344},
			{51.94026835,	-10.24088642},
			{52.178285,	-8.911617},
			{52.268,	-7.0945},
			{52.33356304, -6.463963364},
			{52.4736608,	-8.4346524},
			{52.86618127,	-6.942840109},
			{52.909722,	-6.839167},
			{53.1699938,	-6.914885591},
			{53.17901778,	-7.719844209},
			{53.18436564,	-6.792525445},
			{53.19490793,	-6.670261667},
			{53.1953312,	-6.670313902},
			{53.21575053,	-6.661529073},
			{53.27663886,	-6.48044077},
			{53.29179685,	-6.698044529},
			{53.31827917,	-6.389738373},
			{53.32421,	-6.33366},
			{53.33655618,	-6.462126917},
			{53.3579,	-6.441},
			{53.36407274,	-6.498749584},
			{53.36838522,	-6.274938564},
			{53.37006,	-6.58208},
			{53.3717,	-6.3905},
			{53.37484,	-6.40909},
			{53.3888,	-6.39608},
			{53.389153,	-6.169552409},
			{53.3945937,	-6.5985973},
			{53.3977573,	-6.4348173},
			{53.50413322,	-6.387552494},
			{53.52695407,	-7.346691823},
			{53.552384,	-6.790148},
			{53.64314,	-6.64577},
			{53.80115134,	-9.512749602},
			{54.1795236,	-7.225463119},
			{54.26405697,	-6.956248402},
			{54.648672,	-8.112425},
			{54.655499,	-8.632717},
			{55.09671038,	-8.278657422}
	};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub	
		int index = getEpicentre(schools);
		System.out.println("The epicentre is school " + (index + 1) + ", at coordinates " + schools[index][0] + ", " + schools[index][1]);
	}
	
	public static int getEpicentre(double[][] arr)
	{
		double bestDistance = Double.MAX_VALUE;
		int bestIndex = -1;
		for (int i = 0; i < arr.length; i++)
		{
			ArrayList<Double> schoolArr = new ArrayList<Double>();
			int x = 0;
			for (int j = 0; j < arr.length; j++)
			{
				if (j != i)
				{
					schoolArr.add((Double) getGPSDistance(arr[i][0], arr[j][0], arr[i][1], arr[j][1]));
				}
			}
			if (debugString)
			{
				System.out.println("School " + i + ":");
				for (int y = 0; y < schoolArr.size(); y++)
				{
					System.out.print(Math.round(schoolArr.get(y)) + " ");
				}
				System.out.println();
			}
			
			Collections.sort(schoolArr, Collections.reverseOrder());
			if (debugString)
			{
				for (int y = 0; y < schoolArr.size(); y++)
				{
					System.out.print(Math.round(schoolArr.get(y)) + " ");
				}
				System.out.println();
			}
			
			
			if (schoolArr.get(9) < bestDistance)
			{
				if (debugString)
					System.out.println("New best distance: " + schoolArr.get(9));
				bestDistance = schoolArr.get(9);
				bestIndex = i;
			}
			if (debugString)
				System.out.println();
		}
		return bestIndex;
	}
	
	public static double getGPSDistance(double lat1, double lat2, double long1, double long2)
	{
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
