package PersonalWork;

public class MaximumSubarray {
    public static void main(String[] args) {
    	//Given an array of numbers, compute the contiguous sub array with the largest sum.
    	// Example { -2, 1, -3, 4, -1, 2, 1, -5, 4 } - Result: {4, -1, 2, 1} = 6
        int[] arr = { -2 , 1, -3, 4, -1, 2, 1, -5, 4};
        int max = 0, runningTotal = 0, currBest = 0, currentIndex = 0, startIndex = 0, endIndex = 0;

        while (currentIndex < arr.length)
        {
            runningTotal += arr[currentIndex++];
            if (runningTotal < 0)
            {
                startIndex = currentIndex;
                runningTotal = 0;
            }
            if (runningTotal > currBest)
            {
                currBest = runningTotal;
                endIndex = currentIndex;
            }
        }
        System.out.println("Largest Subarray found was from " + startIndex + " to " + endIndex + " for a total of: " + currBest);
        System.out.print("{");
        for (int i = startIndex; i < endIndex; i++)
        {
        	System.out.print(arr[i]);
        	if (i < endIndex - 1)
        	{
        		System.out.print(", ");
        	}
        }
        System.out.println("}");
    }
}
