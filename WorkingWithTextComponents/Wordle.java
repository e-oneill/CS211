package WorkingWithTextComponents;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
public class Wordle {
	public static JFrame windowFrame = null;
	public static JPanel attempt1 = new JPanel();
	public static int attempts = 6;
	public static int currAttempt = 0;
	public static String target = "";
	public static int[] targetChars = new int[26];
	public static JDialog gameOver = new JDialog(windowFrame);
	static String targetsFile = "C://CS210//wordleTargets.txt";
	static Dictionary targets = new Dictionary(targetsFile);
	static String validWords = "C://CS210//wordleValids.txt";
	static Dictionary valids = new Dictionary(validWords);
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
		
		
		targetChars = new int[26];
        target = targets.getWord(rand.nextInt(targets.getSize())).toUpperCase();
        System.out.println(target);
        HashMap<Integer, Character> targetHash = new HashMap<Integer, Character>(); //hashmap to store the charAt index and character
        for (int i = 0; i < target.length() - 1; i++)
        {
        	targetChars[(int) (target.toLowerCase().charAt(i)) - 97]++;
        	
        	targetHash.put(i,(Character) target.charAt(i));
        }
        for (int i = 0; i < 26; i++)
        {
        	System.out.print(targetChars[i] + ", ");
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
		
		windowFrame.pack();
		windowFrame.setVisible(true);
	}

}

class WordleAttempt extends JPanel  {
	//each attempt at the wordle is contained in an object of this class
	protected static final String TextFieldString = "JTextField";
	protected LinkedJTextField[] charFields; //array for holding the text field for each character
	protected Character[] attempt; //array for holding the characters entered into each field
	protected ArrayList<Character> attemptList = new ArrayList<Character>();
	public WordleAttempt next = null; //the worldAttempts are a linkedlist, we use this data structure for traversal
	
	public WordleAttempt(int chars, HashMap<Integer, Character> targetHash) {
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		JPanel attemptPanel = new JPanel();
		attemptPanel.setLayout(new GridLayout(1, chars));
		charFields = new LinkedJTextField[chars];
		attempt = new Character[chars];
		int[] attemptChars = new int[26];
		
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
						JTextField textField = (JTextField) e.getSource();
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
							int charCode = (int) (Character.toLowerCase(attempt[i])) - 97;
								if (targetHash.get(i) == attempt[i])
								{
									attemptChars[charCode]++;
									matches++;
									charFields[i].setBackground(Color.GREEN);
								}
						}
						for (int i = 0; i < attempt.length; i++)
						{
							int charCode = (int) (Character.toLowerCase(attempt[i])) - 97;
							if (targetHash.containsValue(attempt[i]) && attemptChars[charCode] < Wordle.targetChars[charCode] && !(targetHash.get(i) == attempt[i]))
							{
									attemptChars[charCode]++;
									charFields[i].setBackground(Color.ORANGE);
							}
							else if (!(targetHash.get(i) == attempt[i]))
							{
								charFields[i].setBackground(Color.DARK_GRAY);
								charFields[i].setForeground(Color.WHITE);
							}
						}
						Wordle.currAttempt++;
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
						
						if (matches == attempt.length)
						{
							//correct word found
							System.out.println("Correct Word Found");
							dialogText = new JLabel("You guessed the right word!", SwingConstants.CENTER); 
							dialog.add(dialogText);
							dialog.setVisible(true);
						}
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
