package Lab8;

import java.util.Scanner;
//I could have just use Integer.parseInt(num, baseNumberForm) but I wanted to do something more challenging
public class LabSolution {
  static boolean debug = false;
  public static void main(String args[]){
      /* Enter your code here. Read input from STDIN. Print output to STDOUT */
      /* Feel free to add additional static methods */
      takeInput();
  }
  
  public static void takeInput()
{
   Scanner sc = new Scanner(System.in);
   int origin = Integer.parseInt(sc.nextLine());
   int dest = Integer.parseInt(sc.nextLine());
   String num = sc.nextLine();
   System.out.println(convertNumberBase(origin, dest, num));
}

public static String convertNumberBase(int origin, int dest, String num)
{
   if (debug)
       System.out.println("Commencing conversion of: " + num + " from base " + origin + " to base " + dest);
   
   int pow = num.length() - 1;
   int decValue = 0;
   
   //region | Convert number from current base to base 10;
   if (origin != 10) //if the number is already in base ten, skip this step;
   {
           for (int i = 0; i < num.length(); i++)
           {
               int x = 0;
               if (i < num.length() -1)
               {
                   if (origin > 10)
                   {
                       x = Integer.parseInt(num.substring(i, i+1), 16);
                   }
                   else
                   {
                       x = Integer.parseInt(num.substring(i, i+1)); 
                   }
                   
               }
                   
               else
               {
                   if (origin > 10)
                   {
                       x = Integer.parseInt(num.substring(i), 16);
                   }
                   else
                   x = Integer.parseInt(num.substring(i));
               }
                   
               
               x *= Math.pow(origin, pow--);
               decValue += x;
           }
           
       
   }
   else
   {
       decValue = Integer.parseInt(num);
   }
   //endregion;
   if (debug)
       System.out.println("Number in decimal format: " + decValue);
   
   
   //if the destination is 10, return now;
   if (dest == 10)
   {
       String num2 = "";
       return num2 += decValue;
   }

   //convert number base by euclidean division
   return euclideanDivision(dest, decValue);
}

public static String euclideanDivision(int base, int num)
{
   //using string builder so I can use the handy reverse function
   StringBuilder result = new StringBuilder("");
   //divide the number by the target number base, retaining the remainder each time
   //append the steps together in a string, and then reverse before returning
   while (num > 0)
   {
       int remainder = num % base;
       num /= base;
       if (base > 10 && remainder > 9)
       {
           switch (remainder)
           {
               case 10:
                   result.append("A");
                   break;
               case 11:
                   result.append("B");
                   break;
               case 12:
                   result.append("C");
                   break;
               case 13:
                   result.append("D");
                   break;
               case 14:
                   result.append("E");
                   break;
               case 15:
                   result.append("F");
                   break;
           }
       }
       else
       {
           result.append(remainder);
       }
       
   }
   
   return result.reverse().toString();
}

}
  
