package Lab7;

public class Solution {
	private static String[] test1 = {"sea", "sun", "moon", "solidify", "mediocre","cats","hello", "world", "snide", "remark", "settle", "landmark", "linquard"};
	private static String[] test2 = {"marius", "marcus", "sasquatch"};
	
	public static void main(String[] args)
	{
		//setup the test data to use
		String[] test;
		test = test2;
		//get the nearest prime number to double the size
		int size = getNearestPrime(test.length *2);
		
		//fill the hashTable
		String[] hashTable = fill(size, test);
		HashTable myTable = new HashTable(hashTable);
		
		//search for every item in the hashtable
		for (int i = 0; i < test.length; i++)
		{
			find(size, myTable, test[i]);
//			System.out.println(test[i]);
		}
		
		System.out.println(myTable.getTotal());
	}
	
	public static int find(int size, HashTable mytable, String word) 
	{
		int hashIndex = primHashFunction(word, size);
		int j = 0;
		while (!mytable.check(hashIndex, word))
		{
			hashIndex = probing(hashIndex, j, size);
			j++;
		}
		return hashIndex;
	}
	
	
	
	public static String[] fill(int size, String[] array)
	{
		String[] hashTable = new String[size];
		for (int i = 0; i < size; i++) {
			hashTable[i] = "";
			
		}
		
		for (int i = 0; i < array.length; i++)
		{
			System.out.print("Word " + array[i] + " hash index: (");
			int hashIndex = primHashFunction(array[i], size);
			System.out.print(hashIndex);
			
			//do quadratic probing until we hit the right point
			int j = 0;
			while (hashTable[hashIndex] != "")
			{
				hashIndex = probing(hashIndex, j, size);
//						(hashIndex + (int) Math.pow(2, j)) % size;
				System.out.print(", collision, jumping to next: " + hashIndex);
				j++;
			}
			hashTable[hashIndex] = array[i];
			System.out.println(") = " + hashIndex );
		}
		return hashTable;
	}
	
	public static int getNearestPrime(int size)
	{
		while (true)
		{
			for (int i = 2; i < (size/2); i++)
			{
				if (size % i == 0)
				{
					size++;
					continue;
				}
			}
			return size;
		}
	}
	
	//the hashing function that gets a hash value
	public static int primHashFunction(String word, int modulo)
	{
		double hash = 0;
		//calculate the hash for this word
		//hash function is the character value X 128 ^ j
//		
		for (int j = 0; j < word.length(); j++)
		{
			double c = word.charAt(j) * Math.pow(128, (double)j);
			hash += c;
//			System.out.print(c);
			if (j < word.length()-1)
			{
//				System.out.print(",");
			}
		}
		int hashIndex = (int) (hash % modulo);
		
		return hashIndex;
	}
	
	//the probing function that probes for an open slot
	public static int probing (int hashIndex, int tracker, int size)
	{
		//linear probing
		return (hashIndex + 1) % size;
		
		//quadratic probing
//		return (hashIndex + (int) Math.pow(2, tracker)) % size;
	}
	
}

class HashTable {
	private String[] hashTable;
	private int total =0;
	
	public HashTable (String[] input) {
		hashTable = input;
	}
	
	public boolean check(int slot, String check) {
		if (hashTable[slot].equals(check)) {
			return true;
		}
		else {
			total++;
			return false;
		}
	}
	
	public int getTotal() {
		return total;
	}
}