package Week4Lab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.*;

import WordleHelper.Dictionary;

public class Brain {
	static String validWords = "C://CS210//wordleValids.txt";
	static Dictionary valids = new Dictionary(validWords);
	public static ArrayList<String> dictionary = new ArrayList<String>(Arrays.asList(valids.input));
	static String attempt = "soare";
	static ArrayList<Character> allKnownList = new ArrayList<Character>(); //moving this inside the method causes a bug
	
	
	public static void main(String[] args) {
		Scanner sc  = new Scanner(System.in);
		playWordle();
		while (true)
		{
			System.out.println("Do you want to play again?");
			String command = sc.nextLine();
			if (command.toLowerCase().equals("restart"))
			{
				dictionary = new ArrayList<String>(Arrays.asList(valids.input));
				playWordle();
			}
			else if (command.toLowerCase().equals("exit"))
			{
				System.exit(0);
			}
		}
	}
	
	public static void playWordle() {
		Pattern pattern = Pattern.compile("^[120]{5}$", Pattern.CASE_INSENSITIVE);
		allKnownList = new ArrayList<Character>();
		Scanner sc  = new Scanner(System.in);
		attempt = guessWord("00000");
		System.out.println(attempt);
		
		for (int i = 0; i < 6; i++)
		{
			System.out.println("What was the feedback?");
			String feedback = sc.nextLine();
//			System.out.println(feedback);
			Matcher matcher = pattern.matcher(feedback);
			boolean correctFeedback = matcher.find();
			while (!correctFeedback) {
				System.out.println("Please enter feedback in the format: 00000");
				feedback = sc.nextLine();
				matcher = pattern.matcher(feedback);
				correctFeedback = matcher.find();
			}
			if (feedback.equals("22222"))
			{
				break;
			}
			
			attempt = guessWord(feedback);
			System.out.println(attempt);
		}
	}
	
	public static String guessWord(String feedback) {
		String guess = "placeholder";
		int[] result = new int[5];
		char[] knowns = new char[5];
		char[] unknowns = new char[5];
		char[] knownNot = new char[5];
		int[] targetChars = new int[26];
		int[] knownQuantities = new int[26];
		
		ArrayList<Character> unknownsList = new ArrayList<Character>();
		ArrayList<Character> notInWord = new ArrayList<Character>();
		
		//not sure how Phil is going to provide the previous guess, will need to update this stage when that is known;
		if (feedback == "00000") {
			return "soare";
		}
		else {
			for (int i = 0; i < 5; i++)
			{
				String tempHolder = "";
				tempHolder += feedback.charAt(i);
				result[i] = Integer.parseInt(tempHolder);
//				System.out.print(result[i] + ", ");
			}
		}
		
		boolean attemptHasChanged = false; //boolean used to track that we've updated temp, a brute force solution to a situation where we keep getting all 00000 and it would guess the same word
		//region consume the result array to produce our data structures we will use to check the list of words
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
//		System.out.println();
		//endregion;
		
		Iterator<String> iter = dictionary.iterator();
		int bestWord = 0; //int variable for tracking which word in the list has the highest score;
		while (iter.hasNext())
		{
			ArrayList<Character> wordCharList = new ArrayList<Character>();
//			float wordScore = 0;
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
				
				if (c == knowns[j])
					thisWord += 2;
				else if (knowns[j] != '\u0000' && c != knowns[j]  )
				{
					iter.remove();
					removed = true;
					break;
				}
			}
			for (int i = 0; i < 26; i++) //iterating through two alphabet-size arrays to remove when a character is present too many times, e.g. we know e is only in the word once but this word contains it twice
			{
				if ((targetChars[i] > 0 && wordChars[i] < targetChars[i]) || (knownQuantities[i] > 0 && wordChars[i] > knownQuantities[i]))
				{
					if (!removed)
					{
						removed = true; 
						iter.remove(); //this removal is causing a bug right now
						/* Worked out why this was causing a bug:
						 * in the case where we are removing a letter that appears too often, if a 0 letter is before the 2, it causes all words to be removed */
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
				guess = word.substring(0, 5);
				attemptHasChanged = true;
			}
		}
		if (!attemptHasChanged)
		{
			Random rand = new Random();
			guess = dictionary.get(rand.nextInt(dictionary.size()));
		}
		
		return guess;
	}
}
