package Lab11;

import java.io.*;
import java.util.HashMap;

public class Dictionary{
     
    public String input[];
    public String newInput[];
    public HashMap<Integer, String> inputHash;
    private String filepath = "C://CS210//words.txt";
    public Dictionary(){
        input = load(filepath); 
        inputHash = new HashMap<Integer, String>();
    	newInput = new String[input.length];
    	for (int i = 0; i < input.length; i++)
    	{
    		newInput[i] = input[i].substring(0, 5);
    		inputHash.put(i, input[i].substring(0, 5));
    	}
    	input = newInput;
    }
    
    public Dictionary(String filepath) {
    	this.filepath = filepath;
    	input = load(filepath);
    	inputHash = new HashMap<Integer, String>();
    	newInput = new String[input.length];
    	for (int i = 0; i < input.length; i++)
    	{
    		newInput[i] = input[i].substring(0, 5);
    		inputHash.put(i, input[i].substring(0, 5));
    	}
    	input = newInput;
    }
    
    public int getSize(){
        return input.length;
    }
    
    
    
    public String getWord(int n){
        return input[n];
    }
    
    private String[] load(String file) {
        File aFile = new File(file);     
        StringBuffer contents = new StringBuffer();
        BufferedReader input = null;
        try {
            input = new BufferedReader( new FileReader(aFile) );
            String line = null; 
            int i = 0;
            while (( line = input.readLine()) != null){
                contents.append(line);
                i++;
                contents.append(System.getProperty("line.separator"));
            }
        }catch (FileNotFoundException ex){
            System.out.println("Can't find the file - are you sure the file is in this location: "+file);
            ex.printStackTrace();
        }catch (IOException ex){
            System.out.println("Input output exception while processing file");
            ex.printStackTrace();
        }finally{
            try {
                if (input!= null) {
                    input.close();
                }
            }catch (IOException ex){
                System.out.println("Input output exception while processing file");
                ex.printStackTrace();
            }
        }
        String[] array = contents.toString().split("\n");
        for(String s: array){
        	s = s.substring(0, 5);
            s.trim();
        }
        return array;
    }
}