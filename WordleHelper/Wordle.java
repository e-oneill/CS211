//region Result of 50 test runs of this helper using "Slate":
//	test	success	guesses
//	1	1	3
//	2	1	4
//	3	1	4
//	4	1	2
//	5	1	6
//	6	1	5
//	7	1	4
//	8	1	5
//	9	1	4
//	10	1	4
//	11	1	5
//	12	1	3
//	13	1	5
//	14	1	4
//	15	1	4
//	16	1	2
//	17	1	4
//	18	1	4
//	19	1	4
//	20	1	4
//	21	1	5
//	22	1	6
//	23	1	5
//	24	1	4
//	25	1	3
//	26	1	5
//	27	1	5
//	28	1	6
//	29	1	6
//	30	1	3
//	31	1	5
//	32	1	4
//	33	0	6
//	34	1	4
//	35	1	5
//	36	0	6
//	37	1	4
//	38	1	4
//	39	1	5
//	40	1	4
//	41	1	4
//	42	1	4
//	43	1	5
//	44	1	2
//	45	1	5
//	46	1	4
//	47	1	4
//	48	0	6
//	49	1	3
//	50	1	4
//
//Wins: 94%, Average Guesses per win: 4.2
//endregion;

//region Result of 50 test runs using "Crane"
//test	success	guesses
//1	1	4
//2	1	3
//3	1	6
//4	1	3
//5	1	3
//6	1	4
//7	1	3
//8	1	4
//9	1	5
//10	1	5
//11	1	4
//12	1	3
//13	1	5
//14	1	5
//15	1	4
//16	1	4
//17	1	4
//18	1	5
//19	1	3
//20	1	2
//21	1	3
//22	1	3
//23	1	3
//24	1	4
//25	1	3
//26	1	3
//27	1	5
//28	1	5
//29	1	3
//30	1	5
//31	1	4
//32	1	5
//33	1	4
//34	1	5
//35	1	4
//36	1	5
//37	1	5
//38	1	3
//39	1	5
//40	1	4
//41	1	5
//42	1	5
//43	1	6
//44	1	6
//45	1	4
//46	1	3
//47	1	4
//48	1	5
//49	1	5
//50	0	6
//98% accuracy 4.18 guesses
//endregion;

package WordleHelper;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.Robot;
public class Wordle {
	public static JFrame windowFrame = null;
	public static JPanel attempt1 = new JPanel();
	public static int attempts = 6;
	public static int currAttempt = 0;
	public static String target = "";
	public static boolean gameover = false;
	public static JDialog gameOver = new JDialog(windowFrame);
	static String targetsFile = "C://CS210//wordleTargets.txt";
	public static Dictionary targets = new Dictionary(targetsFile);
	static String validWords = "C://CS210//wordleValids.txt";
	static Dictionary valids = new Dictionary(validWords);
	public static WordleHelper helper;
	public static void main(String[] args) {
		startGame();
	}
	//Method initiatilises a wordle window
	public static void startGame() {
		if (windowFrame != null) // if wordle already was open, dispose of the window
		{
			windowFrame.dispose();
		}
		Random rand = new Random();
		currAttempt = 1;
		gameover = false;
		
        target = targets.getWord(rand.nextInt(targets.getSize())).toUpperCase();
        HashMap<Integer, Character> targetHash = new HashMap<Integer, Character>(); //hashmap to store the charAt index and character
        for (int i = 0; i < target.length(); i++)
        {
        	targetHash.put(i,(Character) target.charAt(i));
        }
        //the window is run here for thread safety
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createWindowFrame(targetHash);
			}
		});
	}
	
	public static void createWindowFrame(HashMap<Integer, Character> targetHash) {
		
		//Element Size variables;
		Dimension windowResolution = new Dimension(1024, 768);
		//Create the window
		windowFrame = new JFrame();
		windowFrame.setTitle("Wordle");
		windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windowFrame.setSize(windowResolution);
		windowFrame.setLayout(new GridLayout(8,1));
		//Create the menu
		JLabel header;
		final String headerString = "<html><div>Wordle</div></html>";
        header = new JLabel(headerString, SwingConstants.CENTER);
//        header.setBounds(85, 10, 853, 48);
        header.setFont(new Font("Roboto", Font.BOLD, 72));
        header.setFocusable(false);
        header.setForeground(Color.BLACK);
		
		//Add components to window
//		windowFrame.setJMenuBar(menu);
		windowFrame.add(header);
		
		WordleAttempt[] attemptsArr = new WordleAttempt[attempts];
		for (int i = 0; i < attempts; i++)
		{
			attemptsArr[i] = new WordleAttempt(5, targetHash);
			if (i > 0)
			{
				attemptsArr[i-1].next = attemptsArr[i];
				attemptsArr[i].disableTextFields();
			}
			windowFrame.add(attemptsArr[i]);
		}
		
		WordleHelper helper = new WordleHelper(attemptsArr, targets.input);
		Wordle.helper = helper;
		windowFrame.pack();
		windowFrame.setVisible(true);
		
		helper.makeAttempt();
//		
//		helper.makeAttempt();
	}

}

class WordleHelper {
	Random rand = new Random();
	WordleAttempt[] attempts;
	char[] knowns = new char[5];
	char[] unknowns = new char[5];
	ArrayList<Character> unknownsList = new ArrayList<Character>();
	ArrayList<Character> allKnownList;
	String[] unchangedPossibilities;
	boolean attemptHasChanged = false;
	ArrayList<Character> notInWord = new ArrayList<Character>();
	int currAttempt;
	ArrayList<String> possibilities;
	String opener = "Local";
	String attempt;
	
	public WordleHelper(WordleAttempt[] attempts, String[] possibilities)
	{
		unchangedPossibilities = possibilities;
		this.attempts = attempts;
		this.currAttempt = 1;
		this.possibilities = new ArrayList<String>(Arrays.asList(possibilities));
	}
	
	
	public void makeAttempt()
	{
		
		if (currAttempt == 1)
		{
			attempt = "crane";
		}
		else if (attempt == "" || attempt == "crane" ||!attemptHasChanged)
		{
			attempt = possibilities.get(rand.nextInt(possibilities.size()));
		}
		
		for (int i = 0; i < 5; i++)
		{
			String newText = "";
			newText += attempt.charAt(i);
			attempts[currAttempt-1].charFields[i].setText(newText);
		}
		
		try {

			Robot robot = new Robot();
			//simulate enter press

			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	public void getResult() {
		allKnownList = new ArrayList<Character>();
		attemptHasChanged = false;
		int[] result = attempts[currAttempt-1].attemptResult;
		for (int i = 0; i < result.length; i++)
		{
			System.out.print(result[i] + " ");
			if (result[i] == 2)
			{
				knowns[i] = attempt.charAt(i);
				if (!allKnownList.contains(knowns[i]))
				allKnownList.add(knowns[i]);
			}
			if (result[i] == 1)
			{
				unknowns[i] = attempt.charAt(i);
				unknownsList.add(unknowns[i]);
				if (!allKnownList.contains(unknowns[i]))
				allKnownList.add(unknowns[i]);
			}
			if (result[i] == 0)
			{
				notInWord.add(attempt.charAt(i));
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
				if (notInWord.contains(word.charAt(j)))
				{
//					possibilities.remove(s);
//					System.out.println("Word excluded");
					iter.remove();
					removed = true;
					break;
				}
				if (word.charAt(j) == unknowns[j])
				{
					iter.remove();
					removed = true;
					break;
				}
				
				if (unknownsList.size() > 0)
				{
					if (unknownsList.contains(word.charAt(j)))
						containsUnknown = true;
				}
				if (allKnownList.contains(word.charAt(j)))
				{
					matches++;
					thisWord++;
				}
				

				
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
				attempt = word;
				attemptHasChanged = true;
			}
		}
		
		System.out.println();
		currAttempt++;
		
		if (currAttempt <= 6)
		{
			makeAttempt();
		}
		if (currAttempt > 6)
		{
			for (int i = 0; i < result.length; i++)
			{
				System.out.print(knowns[i] + " ");
			}
		}
	}
}

class WordleAttempt extends JPanel  {
	//each attempt at the wordle is contained in an object of this class
	protected static final String TextFieldString = "JTextField";
	protected LinkedJTextField[] charFields; //array for holding the text field for each character
	protected Character[] attempt; //array for holding the characters entered into each field
	public WordleAttempt next = null; //the worldAttempts are a linkedlist, we use this data structure for traversal
	public int[] attemptResult = new int[5];
	public WordleAttempt(int chars, HashMap<Integer, Character> targetHash) {
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		JPanel attemptPanel = new JPanel();
		attemptPanel.setLayout(new GridLayout(1, chars));
		charFields = new LinkedJTextField[chars];
		attempt = new Character[chars];
		AbstractDocument abs;
		for (int i = 0; i < chars; i++)	
		{
			charFields[i] = new LinkedJTextField();
			charFields[i].setPreferredSize(new Dimension(64, 64));
			charFields[i].setActionCommand(TextFieldString);
			//properties put into the document so we can access them in document listeners
			charFields[i].getDocument().putProperty("Source", charFields[i]);
			charFields[i].getDocument().putProperty("Index", i);
			charFields[i].setHorizontalAlignment(JTextField.CENTER);
			charFields[i].setAlignmentY(CENTER_ALIGNMENT);
			charFields[i].setFont(new Font("Roboto", Font.BOLD, 24));
			if (i > 0) {charFields[i-1].next = charFields[i];}
			abs = (AbstractDocument) charFields[i].getDocument();
			abs.setDocumentFilter(new DocumentSizeFilter(1));
			//adding listener to the textfield document, to handle when the user makes changes
			charFields[i].getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
				 System.out.println("Document changed");	
				}
				public void removeUpdate(DocumentEvent e) {
					//user has deleted the character
					modifyArray(e);
				}
				public void insertUpdate(DocumentEvent e) {
					//user has added a character
					modifyArray(e);
					//moving the focus to the next item
					LinkedJTextField source = (LinkedJTextField) e.getDocument().getProperty("Source");
					if (source.next != null)
					{
						source.next.requestFocus();
					}
				}
				
				public void modifyArray(DocumentEvent e) {
					LinkedJTextField source = (LinkedJTextField) e.getDocument().getProperty("Source");
					int index = (int) e.getDocument().getProperty("Index");
					String entry = source.getText();
					if (entry.length() > 0)
					{
						attempt[index] = source.getText().charAt(0);
					}
					else
					{
						attempt[index] = null;
					}
					
				}
				
			});
			//listener for the enter key to be pressed
			charFields[i].addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == 10) {
//						System.out.println("Enter pressed");
						int matches = 0;
						//concatenating the attempt characters together
						String attemptWord = "";
						for (int j = 0; j < attempt.length; j++)
						{
							attemptWord += attempt[j];
							if (attempt[j] == null)
							{
								System.out.println("Null character found, aborting");
								return;
							}
						}
						if (!Wordle.valids.inputHash.containsValue(attemptWord.toLowerCase()))
						{
							System.out.println(attemptWord);
							System.out.println("Invalid Word");
							return;
						}
						
						for (int i = 0; i < attempt.length; i++)
						{
							
							
							
							if (targetHash.containsValue(attempt[i]))
							{
								if (targetHash.get(i) == attempt[i])
								{
									matches++;
									charFields[i].setBackground(Color.GREEN);
									attemptResult[i] = 2;
								}
								else
								{
									charFields[i].setBackground(Color.ORANGE);
									attemptResult[i] = 1;
								}
							}
							else
							{
								charFields[i].setBackground(Color.DARK_GRAY);
								charFields[i].setForeground(Color.WHITE);
								attemptResult[i] = 0;
							}
							Wordle.currAttempt++;
						}
						if (next != null && matches != attempt.length)
						{
							next.enableTextFields();
							next.charFields[0].requestFocus();
						}
						JDialog dialog = new JDialog();
						dialog.setLayout(new GridLayout(3, 1));
						JLabel dialogText; 
						JButton exitButton = new JButton("Exit");
						
						exitButton.addActionListener(new ActionListener () {
							public void actionPerformed(ActionEvent e) {
								System.exit(0);
								dialog.dispose();
							}
						});
						
						JButton newGame = new JButton("Play Again");
						newGame.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								Wordle.startGame();
								dialog.dispose();
							}
						});
						
						
						dialog.add(newGame);
						dialog.add(exitButton);
						dialog.setSize(400, 200);
						
						
						if (next == null && matches < attempt.length)
						{
							String target = "";
							for (Character value : targetHash.values())
							{
								target += value;
							}
							dialogText =  new JLabel("You ran out of attempts! The word was: " + target, SwingConstants.CENTER);
							dialog.add(dialogText);
							dialog.setVisible(true);
							
						}
						disableTextFields();
						if (matches == attempt.length)
						{
							//correct word found
							System.out.println("Correct Word Found");
							dialogText = new JLabel("You guessed the right word!", SwingConstants.CENTER); 
							dialog.add(dialogText);
							dialog.setVisible(true);
							Wordle.gameover=true;
						}
						else
						Wordle.helper.getResult();
					}
					
					
//					
				}
			});
		}
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1.0;
		
		createRow(charFields, gridbag, attemptPanel);
		add(attemptPanel);
	}
	
	private void createRow(JTextField[] charFields, GridBagLayout gridbag, Container container) {
		GridBagConstraints c = new GridBagConstraints();
		for (int i = 0; i < charFields.length; i++)
		{
			c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
            c.fill = GridBagConstraints.NONE;      //reset to default
            c.weightx = 0.0;                       //reset to default
            container.add(charFields[i], c);
		}
	}
	
	
	
	public void disableTextFields() {
		for (int i = 0; i < charFields.length; i++)
		{
			charFields[i].setEditable(false);
		}
	}
	
	public void enableTextFields() {
		for (int i = 0; i < charFields.length; i++)
		{
			charFields[i].setEditable(true);
		}
	}
}

class DocumentSizeFilter extends DocumentFilter {
	int maxCharacters;
	
	public DocumentSizeFilter(int maxChars) {
		maxCharacters = maxChars;
	}
	
	@Override
	public void insertString(DocumentFilter.FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
		if ((fb.getDocument().getLength() + str.length()) <= maxCharacters)
		{
			str = str.toUpperCase();
			super.insertString(fb, offs, str, a);
		}
			
		else
			Toolkit.getDefaultToolkit().beep();
	}
	
	@Override
	public void replace(DocumentFilter.FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
		int documentLength = fb.getDocument().getLength();
		str = str.toUpperCase();
		if (documentLength - length + str.length() <= maxCharacters)
			super.replace(fb, offs, length, str, a);
		else
			Toolkit.getDefaultToolkit().beep();
	}
}

class LinkedJTextField extends JFormattedTextField {
	public JTextField next = null;
	
	public LinkedJTextField()
	{
		super();
	}
}

class VirtualKeyboard {
	
}
