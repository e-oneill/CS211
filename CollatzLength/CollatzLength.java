package CollatzLength;

public class CollatzLength {
	static int[] values = {1, 2, 7, 9, 11, 12, 13, 14, 15};
	static int[] collatzArray = new int [values.length];
	
	public static void main(String[] args) {
		for (int i = 0; i < values.length; i++)
		{
			collatzArray[i] = getCollatzLength(values[i]);
			System.out.print(values[i] + " (" + collatzArray[i] + ")");
			if (i < values.length -1)
				System.out.print(", ");
			else
				System.out.println();
		}
		
		
		quickSort(0, collatzArray.length-1);
		for (int i=0; i < values.length; i++)
		{
			System.out.print(values[i] + " (" + collatzArray[i] + ")");
			if (i < values.length -1)
				System.out.print(", ");
			else
				System.out.println();
		}
		int target = findTarget(2);
		System.out.println(values[target]);
		
	}

	public static int findTarget(int num) {
		int target = collatzArray[num];
		int tracker = num;
		for (int i = num; i < collatzArray.length; i++)
		{
			if (collatzArray[i] > collatzArray[num])
			{
				break;
			}
			if (values[i] < values[num])
			{
				tracker = i;
			}
		}

		return tracker;
	}
	
	public static int getCollatzLength(int num) {
		int steps = 0;
		while (num != 1)
		{
			if (num % 2 == 0)
			{
				num /= 2;
			}
			else
			{
				num = (num*3) + 1;
			}
			steps++;
		}
		return steps;
	}
	
	public static int shortcutCollatzLength(int num) {
		int steps = 0;
		if (num == 1)
		{
			return steps;
		}
		
		if (num % 2 != 0)
		{
			num = (num*3) + 1;
//			System.out.println(num);
			steps++;
		}
		
		//get nearest power of two to number, and add that number of steps to the number
		double nearestPower = Math.floor(Math.log(num) / Math.log(2));
		steps += nearestPower; //add the power of two to the number
		//get the value of nearest power and check if that is less than the num from the odd step
		int raised = (int) Math.pow(2, nearestPower);
		boolean multipleRemainders = false;
		if (num - raised > 0)
		{
			int remainder = (int) (num - raised);
			//keep repeating the nearest power of two method until the remainder is gone
			while (remainder > 0)
			{
				nearestPower = Math.floor(Math.log(remainder) / Math.log(2));
				steps += nearestPower; //add the power of two to the number
				raised = (int) Math.pow(2, nearestPower);
				remainder -= raised;
				if (remainder > 0 && remainder % 2 != 0)
					remainder = (remainder * 3) + 1;
			}
		}
		return steps;
	}

	
	public static void quickSort(int left, int right) {
		if (right-left <= 0)
			return;
		else
			{
				int middle = (left + right) / 2;
				if (collatzArray[left] > collatzArray[middle])
					swap(left, middle);
				if (collatzArray[left] > collatzArray[right])
					swap(left, right);
				if (collatzArray[middle] > collatzArray[right])
					swap(middle, right);
				long pivot = collatzArray[right];
				// System.out.println(right + ":" + pivot);
				int partition = partitionIt(left,right,pivot);
				quickSort(left, partition -1);
				quickSort(partition+1, right);
			}
	}
	
	public static int partitionIt(int left, int right, long pivot)
	{
		int leftPtr = left-1;
		int rightPtr = right;
		
		while (true)
		{
			while (collatzArray[++leftPtr] < pivot)
			{
				
			}
			while (collatzArray[--rightPtr] > pivot)
			{
				
			}
			if (leftPtr >= rightPtr)
			{
				break;
			}
			else
				swap(leftPtr, rightPtr);
		}
		swap(leftPtr, right);
		return leftPtr;
	}
	
	public static void swap(int a, int b)
	{
		int temp = collatzArray[a];
		int temp2 = values[a];
		collatzArray[a] = collatzArray[b];
		values[a] = values[b];
		collatzArray[b] = temp;
		values[b] = temp2;
	}
}
