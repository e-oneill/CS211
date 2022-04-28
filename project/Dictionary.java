package project;

import java.io.*;
import java.util.HashMap;

public class Dictionary{
     
    public double input[][];
    public String newInput[];
    public HashMap<Integer, String> inputHash;
    private String filepath = "project/120schools.txt";
    public Dictionary(){
        input = load(filepath); 
        inputHash = new HashMap<Integer, String>();
    }
    
    public Dictionary(String filepath) {
    	
    	this.filepath = filepath;
    	input = load(filepath);
    	inputHash = new HashMap<Integer, String>();
    }
    
    public int getSize(){
        return input.length;
    }
    
    
    
    public double[] getCoords(int n){
        return input[n];
    }
    
    private double[][] load(String file) {
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
        double[][] twoDArray = new double[array.length][2];
        int i = 0;
        for(String s: array){
        	String[] coords = s.split(",");
        	twoDArray[i][0] = Double.parseDouble(coords[1]);
        	twoDArray[i][1] = Double.parseDouble(coords[2].substring(0, coords[2].length()-1));
        	i++;
            s.trim();
        }
        return twoDArray;
    }
}