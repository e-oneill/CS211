package DESEncryption;

import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

//
//9859 3399 4300
//6727 5767
//-103, -41, -88, -49, -71, 125, -83, 25, -41, -10, -113, -115, -43, 96, -66, 101, -8, -65, 60, -81, 73, 107, 83, -38

//4027 2423 2270
//3053 118
//43, 70, -30, -26, -103, -113, -79, -99, 36, -80, 20, -75, -94, 22, -63, -13

//150001 7 66436
//90429 57422

//29 2 3
//16 27


public class Solution {
	static String[] input1 = {
			"9859 3399 4300", 
			"6727 5767", 
			"-103, -41, -88, -49, -71, 125, -83, 25, -41, -10, -113, -115, -43, 96, -66, 101, -8, -65, 60, -81, 73, 107, 83, -38"
			};
	
	static String[] input2 = {
			"4027 2423 2270",
			"3053 118",
			"43, 70, -30, -26, -103, -113, -79, -99, 36, -80, 20, -75, -94, 22, -63, -13"
	};
	
	static String[] input3 = {
		"150001 7 66436",
		"90429 57422",
		"-103, -41, -88, -49, -71, 125, -83, 25, -41, -10, -113, -115, -43, 96, -66, 101, -8, -65, 60, -81, 73, 107, 83, -38"
	};
	
	static String[] input4 = {
			"29 2 3",
			"16 27",
			"-103, -41, -88, -49, -71, 125, -83, 25, -41, -10, -113, -115, -43, 96, -66, 101, -8, -65, 60, -81, 73, 107, 83, -38"
	};
	
    public static void main (String args[])
    {
    	String[] input = input4;
    	
    	long[] publicKeyArr = decomposeString(input[0], "\\s");
    	long[] receiverArr = decomposeString(input[1], "\\s");
    	String payload = input1[2];
    	
    	//get x from g^x
    	long p = publicKeyArr[0];
    	long g = publicKeyArr[1];
    	long h = publicKeyArr[2];
    	long c1 = receiverArr[0];
    	long c2 = receiverArr[1];
    	
    	//get m
    	System.out.println("Getting shared key, p = " + p + ", g = " + g + ", h = " + h);
    	long m = getSharedKey(p, g, h, c1, c2);
    	
//    	System.out.println(m);
    	String decrypted = decrypt(m, payload);
    	System.out.println(decrypted);
    }
    
    public static long getSharedKey(long p, long g, long h, long c1, long c2)
    {
    	long x = babyStepGiantStep(g, p, h);
    	System.out.println("X = " + x); 	
    	long y = babyStepGiantStep(g, p, c1);
    	System.out.println("Y = " + y);
    	System.out.println("C1 = " + c1 + ", C2 = "+ c2);
    	//calculate c2/c1^x
    	// ((1 / c1^x) * c2) % p
    	//Step 1: 1 / c1 ^x
    	long pow = p - 1 - x;
    	long c1Over1 = modPow(c1, pow, p);
//    	System.out.println(c1Over1);
    	long m = (c1Over1 * c2) % p;
    	System.out.println("m: " + m);
    	return m;
    }
    
    public static long babyStepGiantStep(long g, long p, long h)
    {
    	long n = (long) Math.ceil(Math.sqrt((double)(p-1)));
//    	System.out.println("N = " + n);
    	ArrayList<Long> babyTable = new ArrayList<Long>();
    	for (int i = 0; i < n; i++)
    	{
    		babyTable.add(modPow(g, (long) i, p));
    		System.out.print(i + ":" + babyTable.get(i) + ", ");
    	}
    	System.out.println();
    	
    	//Giant-Step
    	//compute g^-n
    	long power = n * (p - 2);
    	long x = -1;
    	long c = modPow(g, power, p);
    	for (double j = 0; j < n; j++)
    	{
    		long y = (long) (h * Math.pow(c, j) % p);
    		if (babyTable.contains(y))
    		{
    			x = (long) (j * n + babyTable.indexOf(y));
    			return x;
    		}
    	}
    	return x;
    }
    
    public static long[] decomposeString(String str, String delimiter)
    {
    	String[] split = str.split(delimiter);
    	long[] arr = new long[split.length];
    	for (int i = 0; i < arr.length; i++)
    	{
    		arr[i] = Long.parseLong(split[i]);
//    		System.out.println(arr[i]);
    	}
    	return arr;
    }
    








    public static String decrypt(long sharedkey, String bytelist){
//send this method the shared key and bytelist read from the third input line and it will decrypt using DES and return the decrypted String
        try{
            byte[] keyBytes= new byte[8];
            byte[] ivBytes= new byte[8];
            for (int i = 7; i >= 0; i--) {
                keyBytes[i] = (byte)(sharedkey & 0xFF);
                ivBytes[i] = (byte)(sharedkey & 0xFF);
                sharedkey >>= 8;
            }
            // wrap key data in Key/IV specs to pass to cipher
            SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            // create the cipher with the algorithm you choose
            // see javadoc for Cipher class for more info, e.g.
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            String[] process=bytelist.split(", ");
            int enc_len=process.length;
            byte[] encrypted= new byte[cipher.getOutputSize(enc_len)];
            for(int i=0;i<process.length;i++){
                encrypted[i]=(byte)(Integer.parseInt(process[i]));
            }
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            byte[] decrypted = new byte[cipher.getOutputSize(enc_len)];
            int dec_len = cipher.update(encrypted, 0, enc_len, decrypted, 0);
            cipher.doFinal(decrypted, dec_len);
            return (new String(decrypted, "UTF-8").trim());  
        }catch(Exception e){return "Error: "+e;}
    }
    
    
    public static long modPow(long number, long power, long modulus)
    {
//raises a number to a power with the given modulus
//when raising a number to a power, the number quickly becomes too large to handle
//you need to multiply numbers in such a way that the result is consistently moduloed to keep it in the range
//however you want the algorithm to work quickly - having a multiplication loop would result in an O(n) algorithm!
//the trick is to use recursion - keep breaking the problem down into smaller pieces and use the modMult method to join them back together
        if(power==0)
        {
            return 1;
        }
        else if (power%2==0)
        {
            long halfpower=modPow(number, power/2, modulus);
            return modMult(halfpower,halfpower,modulus);
        }
        else
        {
            long halfpower=modPow(number, power/2, modulus);
            long firstbit = modMult(halfpower,halfpower,modulus);
            return modMult(firstbit,number,modulus);
        }
    }
    
    public static long modMult(long first, long second, long modulus)
    {
//multiplies the first number by the second number with the given modulus
//a long can have a maximum of 19 digits. Therefore, if you're multiplying two ten digits numbers the usual way, things will go wrong
//you need to multiply numbers in such a way that the result is consistently moduloed to keep it in the range
//however you want the algorithm to work quickly - having an addition loop would result in an O(n) algorithm!
//the trick is to use recursion - keep breaking down the multiplication into smaller pieces and mod each of the pieces individually
        if(second==0)
        {
            return 0;
        }
        else if (second%2==0)
        {
            long half=modMult(first, second/2, modulus);
            return (half+half)%modulus;
        }
        else
        {
            long half=modMult(first, second/2, modulus);
            return (half+half+first)%modulus;
        }
    }
}
