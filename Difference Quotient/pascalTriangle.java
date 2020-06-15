import java.util.*;

class pascalTriangle{
   // This method will return ArrayList containing nth row 
   public static ArrayList<Integer> nthRow(int N)
   {
       ArrayList<Integer> res = new ArrayList<Integer>();
       // Adding 1 as first element of every row is 1
       res.add(1);
       for (int i = 1; i <= N; i++) {
           res.add(i, (res.get(i - 1) * (N - i + 1)) / (i));
       }
       return res;
   }
}