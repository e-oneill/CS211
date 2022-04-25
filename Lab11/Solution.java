package Lab11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class Solution {
	static int score = 0;
	public static void main(String[] args)
	{
		for (int i = 0; i < 100; i++)
		{
			hangman();
		}
		System.out.println(score);
	}
	
	public static void hangman()
	{
		Random r = new Random();
		Dictionary dictionary = new Dictionary();
		int num = dictionary.getSize();
		int lives = 8;
		String target =	dictionary.input[r.nextInt(num)].toLowerCase();
//		target = target.substring(0, target.length() - 1);
//		target = "awake";
//		target = target.t
//		System.out.println(target.toLowerCase());
		
		String blackout = "";
		for (int i = 0; i < target.length(); i++)
		{
			blackout += "_";
		}
		
		Brain mybrain = new Brain(Arrays.copyOf(dictionary.input, dictionary.input.length), blackout);
		boolean running = true;
		while (running) {
			char guess = mybrain.guessLetter();
//			System.out.println(guess);
			String original = mybrain.hiddenWord;
			char[] arrayform = original.toCharArray();
			for (int i = 0; i < target.length(); i++)
			{
				if (target.charAt(i)==guess) {
					arrayform[i] = guess;
				}
			}
			String newform = "";
			for (int i = 0; i < target.length(); i++)
			{
				newform += arrayform[i];
			}
			mybrain.hiddenWord = newform;
//			System.out.println(newform);
			if (newform.equals(original)) {
				lives--;
			}
			if (lives == 0)
			{
				running = false;
			}
			if (mybrain.hiddenWord.equals(target)) {
				running = false;
//				System.out.println("Hurray!");
				score++;
			}
			
		}
	
	}
}

class Brain {
	boolean trailingCarriageReturn = true;
	public String[] dictionary;
	public String hiddenWord;
	public char[] knowns;
	public int length;
	public ArrayList<String> possibles = new ArrayList<String>();
	public ArrayList<Character> guesses = new ArrayList<Character>();
	boolean firstGuess = true;
	
	public Brain(String[] wordlist, String target) {
		dictionary = wordlist;
		hiddenWord = target;
		length = target.length();
		knowns = new char[length];
		for (int i = 0; i < wordlist.length; i++)
		{
			if (wordlist[i].length() == length)
			{
				possibles.add(wordlist[i]);
			}
		}
	}
	
	public char guessLetter() {
		knowns = hiddenWord.toCharArray();
		int[] alphaCounter = new int[26];
		Iterator<String> iter = possibles.iterator();
//		if (firstGuess)
//		{
//			firstGuess = false;
//			guesses.add('e');
//			return 'e';
//		}
		iterator: while (iter.hasNext())
		{
			String word = iter.next().toLowerCase();
			for (int i = 0; i < word.length(); i++)
			{
				//start by checking if the word should be removed
				if (knowns[i] != '_' && word.charAt(i) != knowns[i])
				{
//					System.out.println(knowns[i]);
					//we know a character is here but this word doesn't have it;
					iter.remove();
					continue iterator;
				}
				if (!guesses.contains(word.charAt(i)))
				{
//					System.out.println(word.charAt(i));
					alphaCounter[(int) word.charAt(i) - 97]++;
				}
//					
			}
			
		}
		
		int currMax = -1;
		int currIndex = -1;
		for (int i = 0; i < alphaCounter.length; i++)
		{
			if (alphaCounter[i] > currMax)
			{
				currIndex = i;
				currMax = alphaCounter[i];
			}
		}
		char guess = (char) (currIndex + 97);
		guesses.add(guess);
//		System.out.println(guess);
		return guess;
	}
}
