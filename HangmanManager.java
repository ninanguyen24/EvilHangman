/*
 *Nina Nguyen
 *7/7/2018
 *EvilHangman Project
 *HangmanManager.java 
 *
 */

import java.util.*;

public class HangmanManager {
	private int numOfGuess;
	private int wordLength;
	private Set<String> totalWords;
	private SortedSet<Character> guessedLetters;
	private String pattern;
	
	// Constructor - initiate totalWords, wordLength, and numOfGuess
	// Add all words with the user's length into a TreeSet
	// Throw error if length is less than 1 and max guess is less than 0
	// Generate pattern for the guessed letter
	public HangmanManager (List<String> dictionary, int length, int max){
		if(length < 1 || max < 0){
			throw new IllegalArgumentException();
		}
		numOfGuess = max;
		wordLength = length;
		guessedLetters = new TreeSet<Character>();
		totalWords = new TreeSet<String>();
		
		for (String word : dictionary){
			if(word.length() == length){
				totalWords.add(word);
			}
		}
		
		pattern = "";
		for (int i = 0; i < length; i++){
			pattern += "-";
		}
	}
	
	// Return a set of the remaining words in play in a form of a Set
	public Set<String> words(){
		return totalWords;
	}
	
	// Return the remaining number of guesses
	public int guessesLeft(){
		return numOfGuess;
	}
	
	// Return a sorted set of characters previously guessed
	public SortedSet<Character> guesses(){
		return guessedLetters;
	}
	
	//Return the pattern of the guessed words including the guessed letter
	public String pattern(){
		if(totalWords.isEmpty()){
			throw new IllegalStateException();
		}
		return pattern;
	}
	
	// Param: Takes in the letter guessed by the user
	// Count the number of occurances that the guessed letter
	// appeared in the remaining word.
	private int countOccurance(char guess){
		int count = 0;
		for (int i = 0; i < pattern.length(); i++){
			if (pattern.charAt(i) == guess){
				count++;
			}
		}
		
		// Decrease the number of guess if user guessed wrong
		if(count == 0){
			numOfGuess--;
		}
		return count;
	}
	
	// Param: Takes in the letter guessed by the user
	// Add the guess letter to the Sorted set and create a new map to separate the
	// different values into their appropriate pattern.
	// For example, if there are three words left (ally, cool, good). The user guessed
	// the letter 'o'. The two families will be (ally, shows as ---- pattern) and
	// (cool and good, shows as -oo-).
	public int record(char guess){
		if (numOfGuess < 1 || totalWords.isEmpty()){
			throw new IllegalStateException();
		}
		
		if (!totalWords.isEmpty() && guessedLetters.contains(guess)){
			throw new IllegalArgumentException();
		}
		
		guessedLetters.add(guess);
		Map<String, TreeSet<String>> family = new TreeMap<String,TreeSet<String>>();
		for(String word : totalWords){
			String tempPattern = pattern;
			for (int i = 0; i < wordLength; i++){
				if(word.charAt(i) == guess){
					// Java Strings are immutable, cannot be changed directly
					tempPattern = tempPattern.substring(0, i) + guess + tempPattern.substring(i+1);
				} 
			}
			if(!family.containsKey(tempPattern)){
				family.put(tempPattern, new TreeSet<String>());
			} 
			family.get(tempPattern).add(word);
			
		}
		pickFamily(family);
		return countOccurance(guess);
	}
	
	// Helper function to pick the key and value with the most values
	// Param: Takes in a map with pattern as keys, holding all matching values
	// Pre: Record() called this method to pick the map with the most values
	// Post: Update totalWords to reflect new remaining words, update
	//		 the pattern to reflect guess character. 
	private void pickFamily(Map<String, TreeSet<String>> family){
		int largestFamily = 0;
		for(String key : family.keySet()){ // Iterate through each key to get value
			TreeSet<String> temp = family.get(key); // put value in temp TreeSet
			if(temp.size() > largestFamily){
				largestFamily = temp.size();
				totalWords = temp;
				pattern = key;
			}
		}
	}
}
