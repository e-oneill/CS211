package project;

import java.util.Comparator;

class SchoolComparator implements Comparator<School> {
	public int compare (School school1, School school2) {
		if (school1.distance < school2.distance)
		{
			return -1;
		}
		else if (school2.distance < school1.distance)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
}
