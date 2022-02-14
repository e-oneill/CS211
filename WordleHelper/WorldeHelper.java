package WordleHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

public class WorldeHelper {
	static String targetsFile = "C://CS210//wordleTargets.txt";
	public static Dictionary targets = new Dictionary(targetsFile);
	static String validWords = "C://CS210//wordleValids.txt";
	static Dictionary valids = new Dictionary(validWords);
	static String attempt = "";
	static int[] result = new int[5];
	static char[] knowns = new char[5];
	static char[] unknowns = new char[5];
	static char[] knownNot = new char[5];
	static int[] knownQuantities = new int[26];
	static ArrayList<Character> unknownsList = new ArrayList<Character>();
	static ArrayList<Character> allKnownList;
	static String[] unchangedPossibilities;
	static boolean attemptHasChanged = false;
	static ArrayList<Character> notInWord = new ArrayList<Character>();
	static ArrayList<String> possibilities;
	static String opener = "soare";
	static Scanner sc = new Scanner(System.in);
	public static void main (String[] args) {
		allKnownList = new ArrayList<Character>();
		possibilities = new ArrayList<String>(Arrays.asList(targets.input));
		System.out.println("For your first guess, try " + opener);
		attempt = opener;
		checkAttempt();
		
		for (int i = 1; i < 6; i++)
		{
			System.out.println("What did you guess?");
			
			attempt = sc.nextLine();
			if (attempt.length() < 5 || attempt.length() > 5)
			{
				System.out.println("Invalid word provided, please retry");
				attempt = sc.nextLine();
			}
			checkAttempt();
			
		}
	}
	
	public static void checkAttempt() {
		System.out.println("What was the feedback?");
		for (int i = 0; i < 5; i++)
		{
			result[i] = sc.nextInt();
		}
		sc.nextLine();
		
		attemptHasChanged = false;
		for (int i = 0; i < result.length; i++)
		{
			char c = attempt.charAt(i);
			System.out.print(result[i] + " ");
			if (result[i] == 2)
			{
				knowns[i] = c;
				if (!allKnownList.contains(knowns[i]))
				allKnownList.add(knowns[i]);
			}
			if (result[i] == 1)
			{
				unknowns[i] = c;
				unknownsList.add(unknowns[i]);
				if (!allKnownList.contains(unknowns[i]))
				allKnownList.add(unknowns[i]);
			}
			if (result[i] == 0)
			{
				knownNot[i] = c;
				if (!allKnownList.contains(c))
				notInWord.add(c);
			}
		}
		Iterator<String> iter = possibilities.iterator();
		int bestWord = 0; //int variable for tracking which word in the list has the highest score;
		while (iter.hasNext())
		{
			String word = iter.next();
			int thisWord = 0;
			int matches = 0;
			boolean containsUnknown = false;
			boolean removed = false;
			for (int j = 0; j < 5; j++)
			{
				char c = word.charAt(j);
				if (notInWord.contains(c) || word.charAt(j) == unknowns[j] || knownNot[j] == c)
				{
//					possibilities.remove(s);
//					System.out.println("Word excluded");
					iter.remove();
					removed = true;
					break;
				}
//				if (word.charAt(j) == unknowns[j])
//				{
//					iter.remove();
//					removed = true;
//					break;
//				}
				
				if (unknownsList.size() > 0)
				{
					if (unknownsList.contains(c))
						containsUnknown = true;
				}
				if (allKnownList.contains(c))
				{
					matches++;
					thisWord++;
				}
				
//				for (int i = 0; i < 5; i++)
//				{
//					if (word.charAt(j) == unknowns[i])
//						containsUnknown=true;
//						
//				}
				
				if (word.charAt(j) == knowns[j])
					thisWord += 2;
				
				
			}
			if ((!containsUnknown && unknownsList.size() > 0) || matches < allKnownList.size())
			{
				if (!removed)
				iter.remove();
//				break;
				continue;
			}
			if (thisWord > bestWord && !removed)
			{
				bestWord = thisWord;
				attempt = word.substring(0, 5);
				attemptHasChanged = true;
			}
		}
		
//		System.out.println();
	
		
		System.out.println("My best guess is now: " + attempt);
		System.out.println("This has a " + (float) 1 / possibilities.size() * 100 + "% certainty of being the answer");
		System.out.println();
	}
}
