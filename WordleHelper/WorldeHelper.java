//WordleHelper (typo used due to having another class with the name from last week (bot for playing game)
//In 50 tests, gets it right in 99% of cases, but takes over 4 guesses on average. Earlier solution was faster, but only when the target set of words was used as the possibilities.
//Need to improve the algorithm for selecting a good answer, as now it is too stupid. 

package WordleHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class WorldeHelper {
	static String targetsFile = "C://CS210//wordleTargets.txt";
	public static Dictionary targets = new Dictionary(targetsFile);
	static String validWords = "C://CS210//wordleValids.txt";
	static Dictionary valids = new Dictionary(validWords);
	static String attempt = "";
	static int[] result = new int[5]; //the feedback array of type 0 0 0 1 1 1, etc.
	static char[] knowns = new char[5]; //array of all characters whose position is known
	static char[] unknowns = new char[5]; //array of the position of all unknown characters in this test - used for removing words with chars in wrong pos
	static char[] knownNot = new char[5]; //array of characters where we know its not in this location bus we know its location elsewhere (e.g. a 0 for a character that is a 2 elsewhere)
	static int[] knownQuantities = new int[26]; //alphabetical list of where we know the number of duplicates of this character in the word (if you find a 0 of a character that is known to be in the word, we know the current frequency of that char is its count in word)
	static ArrayList<Character> unknownsList = new ArrayList<Character>(); //list of all unknowns
	static ArrayList<Character> allKnownList; //list of allknowns
	static String[] unchangedPossibilities; //original array of possibilities, used if we need to reset the list for a new game
	static boolean attemptHasChanged = false; //used to track if attempt was not changed during the last run, then we'll just use a random one
	static ArrayList<Character> notInWord = new ArrayList<Character>(); //list of characters not in the word, so we can just call .contains instead of iterating through an entire array
	static ArrayList<String> possibilities; //list of possible options
	static String opener = "soare"; //opener
	static Scanner sc = new Scanner(System.in);
	public static void main (String[] args) {
		allKnownList = new ArrayList<Character>();
		possibilities = new ArrayList<String>(Arrays.asList(targets.input));
		System.out.println("For your first guess, try " + opener);
		attempt = opener;
		result = getResultFromScanner();
		checkAttempt(result);
		
		for (int i = 1; i < 6; i++)
		{
			System.out.println("What did you guess?");
			
			attempt = sc.nextLine();
			if (attempt.length() < 5 || attempt.length() > 5)
			{
				System.out.println("Invalid word provided, please retry");
				attempt = sc.nextLine();
			}
			
			result = getResultFromScanner();
			checkAttempt(result);
			
		}
	}
	
	public static int[] getResultFromScanner() { //breaking this into methods with arguments and returns to make it easier to use with hackerrank next week
		System.out.println("What was the feedback?");
		for (int i = 0; i < 5; i++)
		{
			result[i] = sc.nextInt();
		}
		sc.nextLine();
		return result;
	}
	
	public static void checkAttempt(int[] result) {
		
		int[] targetChars = new int[26];
		
		attemptHasChanged = false;
		for (int i = 0; i < result.length; i++)
		{
			char c = attempt.charAt(i);
			int cIndex = (int) Character.toLowerCase(c) - 97;
			System.out.print(result[i] + " ");
			if (result[i] == 2)
			{
				knowns[i] = c;
				targetChars[cIndex]++;
				if (!allKnownList.contains(knowns[i]))
				allKnownList.add(knowns[i]);
			}
			if (result[i] == 1)
			{
				unknowns[i] = c;
				unknownsList.add(unknowns[i]);
				targetChars[cIndex]++;
				if (!allKnownList.contains(unknowns[i]))
				allKnownList.add(unknowns[i]);
			}
			if (result[i] == 0)
			{
				knownNot[i] = c;
				if (!allKnownList.contains(c))
				notInWord.add(c);
				else
				{
					knownQuantities[cIndex] = Collections.frequency(allKnownList, c);
				}
			}
		}
		Iterator<String> iter = possibilities.iterator();
		int bestWord = 0; //int variable for tracking which word in the list has the highest score;
		while (iter.hasNext())
		{
			ArrayList<Character> wordCharList = new ArrayList<Character>();
			float wordScore = 0;
			String word = iter.next();
			int thisWord = 0;
			int matches = 0;
			boolean containsUnknown = false;
			boolean removed = false;
			int[] wordChars = new int[26];
			for (int j = 0; j < 5; j++)
			{
				char c = word.charAt(j);
				wordCharList.add(c);
				if (Collections.frequency(wordCharList, c) > Collections.frequency(allKnownList, c))
				{
					thisWord--; //deducting score because of repetition of character beyond known repetition in target - used to avoid wasted tries
				}
				int cIndex = (int) Character.toLowerCase(c) - 97;
				wordChars[cIndex]++;
				if (notInWord.contains(c) || word.charAt(j) == unknowns[j] || knownNot[j] == c) //removing all words that contain characters we know are not in target, we know are in wrong place and we know are not in this play (e.g. got a 2 elsewhere but a 0 here)
				{
					iter.remove();
					removed = true;
					break;
				}
				
				if (unknownsList.size() > 0) //adding to our list of chars we know are in the word but are not sure where
				{
					if (unknownsList.contains(c))
						containsUnknown = true;
				}
				if (allKnownList.contains(c)) // adding a score to the word if it contains a character we know is in the word, to increase its likelihood of suggestion
				{
					matches++;
					thisWord++;
				}
				
				if (word.charAt(j) == knowns[j])
					thisWord += 2;
			}
			for (int i = 0; i < 26; i++) //iterating through two alphabet-size arrays to remove when a character is present too many times, e.g. we know e is only in the word once but this word contains it twice
			{
				if ((targetChars[i] > 0 && wordChars[i] < targetChars[i]) || (knownQuantities[i] > 0 && wordChars[i] > knownQuantities[i]))
				{
					if (!removed)
					{
						removed = true;
						iter.remove();
					}
					
					break;
				}
			}
			if ((!containsUnknown && unknownsList.size() > 0) || matches < allKnownList.size())
			{
				if (!removed)
				iter.remove();
				continue;
			}
			if (thisWord > bestWord && !removed)
			{
				bestWord = thisWord;
				attempt = word.substring(0, 5);
				attemptHasChanged = true;
			}
		}
		if (!attemptHasChanged)
		{
			Random rand = new Random();
			attempt = possibilities.get(rand.nextInt(possibilities.size()));
		}
		
		
		
//		System.out.println();
	
		
		System.out.println("My best guess is now: " + attempt);
		System.out.println("This word has a score of: " + bestWord);
		System.out.println("This has a " + (float) 1 / possibilities.size() * 100 + "% certainty of being the answer");
		System.out.println();
	}
}
