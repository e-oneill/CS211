// package Week4Lab;

// import java.util.*;


// public class HackerWordle{    
    
//     public static void main (String[] args){
//         Scanner myscanner = new Scanner(System.in);
//         String target = myscanner.nextLine();
//         ArrayList<String> original = new ArrayList<String>();
//         while(myscanner.hasNext()){
//             String word = myscanner.nextLine();
//             original.add(word);
//         }
//         int lives=6;
//         // Brain mybrain = new Brain(original);
//         // String feedback = "00000";
//         // while(lives>0){
//         //     String guess = mybrain.guessWord(feedback);
//         //     feedback=getFeedback(target, guess);
//         //     if(guess.equals(target)){
//         //         System.out.println(guess);
//         //         System.exit(0);
//         //     }
//         //     lives--;
//         // }
//         // System.out.println("You fail!");
//     }
       
//     public static String getFeedback(String chosen, String guess){
//         char[] chosencopy = new char[5];
//         for(int i=0;i<5;i++){
//             chosencopy[i]=chosen.charAt(i);
//         }
       
//         char[] guesscopy = new char[5];
//         for(int i=0;i<5;i++){
//             guesscopy[i]=guess.charAt(i);
//         }
//        char[] feedback = new char[5];
       
//         for(int i=0;i<5;i++){
//             feedback[i]='0';
//         }
//         for(int i=0;i<5;i++){
//             if(guesscopy[i]==chosencopy[i]){
//                 feedback[i]='2';
//                 chosencopy[i]='@';
//                 guesscopy[i]='*';
//             }
//         }
//         for(int i=0;i<5;i++){
//             for(int j=0;j<5;j++){
//                 if(guesscopy[i]==chosencopy[j]){
//                     feedback[i]='1';
//                     chosencopy[j]='@';
//                     guesscopy[i]='*';
//                     break;
//                 }
//             }
//         }
//         String answer="";
//         for(int i=0;i<5;i++){
//             answer=answer+feedback[i];
//         }
//         return answer;
//     }  
// }

// class Brain{
    
//     public ArrayList dictionary;
//     static ArrayList<Character> allKnownList = new ArrayList<Character>();
//     static String attempt = "";
    
//     public Brain(ArrayList wordlist){
//         dictionary = wordlist;
//     }
      
//     public String guessWord(String feedback){
//         String guess = "placeholder";
//         int[] result = new int[5];
//         char[] knowns = new char[5];
//         char[] unknowns = new char[5];
//         char[] knownNot = new char[5];
//         int[] targetChars = new int[26];
//         int[] knownQuantities = new int[26];
//         ArrayList<Character> unknownsList = new ArrayList<Character>();
//         ArrayList<Character> notInWord = new ArrayList<Character>();
//         //fill this in so as to choose a String that is contained in dictionary
//         //this method should return a String that is a good guess
//         //the lines below just return a random guess - not likely to do well!
//         //you receive your previous guess and the feedback as input
//         //0=grey; 1=yellow; 2=green
//         if (feedback == "00000") {
//             return "soare";
//         }
//         else {
//             for (int i = 0; i < 5; i++)
//             {
//                 String tempHolder = "";
//                 tempHolder += feedback.charAt(i);
//                 result[i] = Integer.parseInt(tempHolder);
// //                System.out.print(result[i] + ", ");
//             }
//         }
        
//         boolean attemptHasChanged = false; //boolean used to track that we've updated temp, a brute force solution to a situation where we keep getting all 00000 and it would guess the same word
//         //region consume the result array to produce our data structures we will use to check the list of words
//         for (int i = 0; i < result.length; i++)
//         {
//             char c = attempt.charAt(i);
//             int cIndex = (int) Character.toLowerCase(c) - 97;
//             System.out.print(result[i] + " ");
//             if (result[i] == 2)
//             {
//                 knowns[i] = c;
//                 targetChars[cIndex]++;
//                 if (!allKnownList.contains(knowns[i]))
//                 allKnownList.add(knowns[i]);
//             }
//             if (result[i] == 1)
//             {
//                 unknowns[i] = c;
//                 unknownsList.add(unknowns[i]);
//                 targetChars[cIndex]++;
//                 if (!allKnownList.contains(unknowns[i]))
//                 allKnownList.add(unknowns[i]);
//             }
//             if (result[i] == 0)
//             {
//                 knownNot[i] = c;
//                 if (!allKnownList.contains(c))
//                 notInWord.add(c);
//                 else
//                 {
//                     knownQuantities[cIndex] = Collections.frequency(allKnownList, c);
//                 }
//             }
//         }
// //        System.out.println();
//         //endregion;
        
//         Iterator<String> iter = dictionary.iterator();
//         int bestWord = 0; //int variable for tracking which word in the list has the highest score;
//         while (iter.hasNext())
//         {
//             ArrayList<Character> wordCharList = new ArrayList<Character>();
// //            float wordScore = 0;
//             String word = iter.next();
//             int thisWord = 0;
//             int matches = 0;
//             boolean containsUnknown = false;
//             boolean removed = false;
//             int[] wordChars = new int[26];
//             for (int j = 0; j < 5; j++)
//             {
//                 char c = word.charAt(j);
//                 wordCharList.add(c);
//                 if (Collections.frequency(wordCharList, c) > Collections.frequency(allKnownList, c))
//                 {
//                     thisWord--; //deducting score because of repetition of character beyond known repetition in target - used to avoid wasted tries
//                 }
//                 int cIndex = (int) Character.toLowerCase(c) - 97;
//                 wordChars[cIndex]++;
//                 if (notInWord.contains(c) || word.charAt(j) == unknowns[j] || knownNot[j] == c) //removing all words that contain characters we know are not in target, we know are in wrong place and we know are not in this play (e.g. got a 2 elsewhere but a 0 here)
//                 {
//                     iter.remove();
//                     removed = true;
//                     break;
//                 }
                
//                 if (unknownsList.size() > 0) //adding to our list of chars we know are in the word but are not sure where
//                 {
//                     if (unknownsList.contains(c))
//                         containsUnknown = true;
//                 }
//                 if (allKnownList.contains(c)) // adding a score to the word if it contains a character we know is in the word, to increase its likelihood of suggestion
//                 {
//                     matches++;
//                     thisWord++;
//                 }
                
//                 if (c == knowns[j])
//                     thisWord += 2;
//                 else if (knowns[j] != '\u0000' && c != knowns[j]  )
//                 {
//                     iter.remove();
//                     removed = true;
//                     break;
//                 }
//             }
//             for (int i = 0; i < 26; i++) //iterating through two alphabet-size arrays to remove when a character is present too many times, e.g. we know e is only in the word once but this word contains it twice
//             {
//                 if ((targetChars[i] > 0 && wordChars[i] < targetChars[i]) || (knownQuantities[i] > 0 && wordChars[i] > knownQuantities[i]))
//                 {
//                     if (!removed)
//                     {
//                         removed = true; 
//                         iter.remove(); //this removal is causing a bug right now
//                         /* Worked out why this was causing a bug:
//                          * in the case where we are removing a letter that appears too often, if a 0 letter is before the 2, it causes all words to be removed */
//                     }
                    
//                     break;
//                 }
//             }
//             if ((!containsUnknown && unknownsList.size() > 0) || matches <              allKnownList.size())
//             {
//                 if (!removed)
//                 iter.remove();
//                 continue;
//             }
//             if (thisWord > bestWord && !removed)
//             {
//                 bestWord = thisWord;
//                 guess = word.substring(0, 5);
//                 attemptHasChanged = true;
//             }
//         }
//         if (!attemptHasChanged)
//         {
//             Random rand = new Random();
//             guess = (String) dictionary.get(rand.nextInt(dictionary.size()));
//         }
//         attempt = guess;
//         return guess;
//     }        
// }