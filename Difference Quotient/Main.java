// Difference Quotient Calculator
// Made by Jack Prewitt on 5/27/2020
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Takes in the function
        Scanner s = new Scanner(System.in);
        System.out.print("f(x): ");
        String fx = s.nextLine();
        fx.replace(" ", ""); // gets rid of spaces
        fx.toLowerCase(); // lowercases variables
        System.out.print("x for f'(x): ");
        int xValue = s.nextInt();
        s.close(); // closes scanner
        // stores string array returns of each method
        String[] termArgs = maker(fx);
        String[] fxandhminushArgs = maker2(fx, termArgs);
        String[] diffQuotient = maker3(fxandhminushArgs);
        String finalDiffQuotient = prettify(diffQuotient);
        String basicDerivation = basicDerivative(diffQuotient);
        String derivative = plugin(diffQuotient, xValue);
        //prints the return
        System.out.println("f(x+h) = " + Arrays.toString(termArgs));
        System.out.println("f(x+h)-f(x) = " + Arrays.toString(fxandhminushArgs));
        System.out.println("(f(x+h)-f(x)) / h = " + Arrays.toString(diffQuotient));
        System.out.println("Difference Quotient: " + finalDiffQuotient);
        System.out.println("d/dx: " + basicDerivation);     
        System.out.println("f'(x): " + derivative);       
    }
    public static String[] maker(String function) {
        // seperates into terms
        function = function.replace("-","+-");
        String[] terms = function.split("\\+");
        int lengthTerms = terms.length;
        //f(x+h)
        for (int i=0; i<lengthTerms; i++) {
            terms[i] = terms[i].replaceAll(" ", ""); // Removes spaces within the terms
            if (terms[i].endsWith("x")) { //Allows input of x instead of x^1
                terms[i] = terms[i].replace("x", "x^1");
            }
            terms[i] = terms[i].replace("x", "(x+h)"); // replaces x with x+h
            //Used to distribute the power
            if (terms[i].contains("^")) {
                //determines power by making a substring from everything after the ^ and parsing it to an integer
                int power = Integer.parseInt(terms[i].substring(terms[i].indexOf("^")+1,terms[i].length()));
                ArrayList<Integer> row = pascalTriangle.nthRow(power); //Calls pascalTriangle to get row of pascal triangke that corrsponds to the power
                int coef = 1; // defaylst coeffecient to 1 (x = 1x)
                if (!(terms[i].substring(0,terms[i].indexOf("(")+1).equals("("))) {
                    coef = Integer.parseInt(terms[i].substring(0, terms[i].indexOf("("))); //determines coefficient by making a substring from everything before ( and parsing it to an integer
                }
                terms[i] = (row.get(0) * coef) + "x^" + power; // first term of expansion. Done outside for loop as h power is not needed
                for (int j = 1; j<power; j++) {
                    terms[i] = terms[i] + "+" + (row.get(j)*coef) + "x^" + (power-j) + "h^" + j;  // Determines each term by mu
                }
                terms[i] = terms[i] + "+" + (row.get(power)*coef) + "h^" + power; // last term of expansion. Done outside for loop as x power is not needed
            }
        }
        return terms;
    }
    public static String[] maker2 (String function, String[] diffTerms) {
        // f(x) creation
        function = function.replace("-","+-"); // replaces all - with a + - to make it easier for code to be run
        String[] terms = function.split("\\+"); //spits function into terms again
        int lengthTerms = terms.length; // stores length  into variable
        for (int i=0; i<lengthTerms; i++) {
            terms[i] = terms[i].replaceAll(" ", ""); // removes spaces
            if (terms[i].endsWith("x")) {
                terms[i] = terms[i].replace("x", "x^1"); //Allows input of x instead of x^1
            }
            if (terms[i].startsWith("x")) {
                terms[i] = 1 + terms[i]; //fixes coefficient 
            }
        }
        // f(x and h) creation and formatting
        String diffTermsString = Arrays.toString(diffTerms);
        diffTermsString = diffTermsString.replace("[", ""); //formats x+h
        diffTermsString = diffTermsString.replace("]", ""); //formats x+h
        String[] fxandh = diffTermsString.split("\\+|\\, "); //formats x+h
        List<Object> fxandhminushList = new ArrayList<Object>(Arrays.asList(fxandh)); //creates arraylist to allow for removal of duplicate terms
        for (String i: terms) {
            for (String j: fxandh) {
                if (i.equals(j)) {
                    fxandhminushList.remove(j); //REMOVES duplicate terms
                }
            }
        }       
        String[] fxandhminush = new String[fxandhminushList.size()];  //creates string array to store result in
        fxandhminush = fxandhminushList.toArray(fxandhminush);  //converts arraylist to array
        return fxandhminush;
    }
    public static String[] maker3(String[] numeratorinp) {
        String [] differenceQuotient = numeratorinp.clone(); // duplicates the input string array
         for (int i = 0; i<differenceQuotient.length; i++) {
            if (differenceQuotient[i].contains("h^")) {
                //determines power of h by taking everything after the h and exponent within the term
                int hpow = Integer.parseInt(differenceQuotient[i].substring(differenceQuotient[i].indexOf("h^")+2,differenceQuotient[i].length()));
                int newPow = hpow-1; //makes the new power one less
                if (newPow == 0) {
                    //if the new power of h is 0 the h is removed because h^0 is equal to 1
                    differenceQuotient[i] = differenceQuotient[i].substring(0, differenceQuotient[i].indexOf("h^"));
                } else if(newPow == 1) {
                    //removes the exponent if the new power is going to be 1 because h^1 is equal to h
                    differenceQuotient[i] = differenceQuotient[i].substring(0, differenceQuotient[i].indexOf("h^")+1);
                }  else {
                    //changes the power of h to the new, updated power
                    differenceQuotient[i] = differenceQuotient[i].substring(0,differenceQuotient[i].indexOf("h^")+2) + newPow;
                }
            }
         }
         return differenceQuotient; //returns the final difference quotient
    }
    public static String prettify(String[] pretty) {
        String finalString = Arrays.toString(pretty); //Makes string from an arary
        //formats the string to look better and have proper formatting
        finalString = finalString.replace("[", "");
        finalString = finalString.replace("]", "");
        finalString = finalString.replace(",", " +");
        finalString = finalString.replaceAll("\\+ -", "\\- ");
        finalString = finalString.replace("x^1", "x");
        return finalString;
    }
    public static String basicDerivative(String[] diffQuotient) {
        String diffQuotString = Arrays.toString(diffQuotient);
        diffQuotString = diffQuotString.replace("[", "");
        diffQuotString = diffQuotString.replace("]", ""); 
        String[] diffQuotientArray = diffQuotString.split("\\+|\\, "); 
        ArrayList<String> pluginList = new ArrayList<String>(Arrays.asList(diffQuotientArray));
        String s;
        for (int i = 0; i < pluginList.size(); i++) {
            s = pluginList.get(i);
            if ((s.indexOf("h") != -1)) {
                pluginList.remove(s);
                i-=1;
            }
        }
        String[] basicDerivativeArray = new String[pluginList.size()];
        basicDerivativeArray = pluginList.toArray(basicDerivativeArray);
        String basicDerivative = prettify(basicDerivativeArray);
        return basicDerivative;
    }
    public static String plugin(String[] diffQuotient, int xVal) {
        String diffQuotString = Arrays.toString(diffQuotient);
        diffQuotString = diffQuotString.replace("[", "");
        diffQuotString = diffQuotString.replace("]", ""); 
        String[] diffQuotientArray = diffQuotString.split("\\+|\\, "); 
        ArrayList<String> pluginList = new ArrayList<String>(Arrays.asList(diffQuotientArray));
        int termSum = 0;
        String s;
        int term;
        for (int i = 0; i < pluginList.size(); i++) {
            s = pluginList.get(i);
            if (s.contains("h")) { //removes h as it approaches 0
                pluginList.remove(s);
                i -= 1;
            } else if (s.contains("^")) { //x values
                int indexPow = s.indexOf("^");
                int indexX = s.indexOf("x");
                int coef = Integer.parseInt(s.substring(0, indexX));
                int power = Integer.parseInt(s.substring(indexPow + 1, s.length()));
                int exponentiation = (int) Math.pow(xVal, power);
                term = coef * exponentiation;
                termSum += term;
            } else { //constant
                term = Integer.parseInt(s);
                termSum += term;
            }
        }
        String pluginString = Integer.toString(termSum);
        return pluginString;
    }
}