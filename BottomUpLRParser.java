
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class BottomUpLRParser {
    public static String stack = "0";
    public static String OUTSTR = "";
    public static String FileN = "output.txt";

    public static void main(String[] args) {
        String input = args[0];
        FileN=input;
        StringBuilder inputCpy = new StringBuilder();


        while (input.length() > 0) {
            switch (input.charAt(0)) {
                case 'i':
                    if (input.charAt(1) == 'd') {
                        input = input.substring(2);
                        inputCpy.append("id ");
                    } else
                        throw new IllegalArgumentException();
                    break;
                case '+':
                    input = input.substring(1);
                    inputCpy.append("+ ");
                    break;
                case '(':
                    input = input.substring(1);
                    inputCpy.append("( ");
                    break;
                case ')':
                    input = input.substring(1);
                    inputCpy.append(") ");
                    break;
                case '*':
                    input = input.substring(1);
                    inputCpy.append("* ");
                    break;
                case '$':
                    input = input.substring(1);
                    inputCpy.append("$ ");
                    break;
            }
        }
        OUTSTR+=String.format("%-25s", "Stack") + String.format("%-25s", "Input") + String.format("%-15s", "Action")+"\n";
        Scanner in = new Scanner(inputCpy.toString());

        while (in.hasNext()) {
            String next=in.next();
            decide(stack.charAt(stack.length()-1)-48, next, inputCpy);
        }
        Out(args[1]);
        System.out.println("The input has been parsed successfully.");
    }

    public static void decide(int actionNum, String Term, StringBuilder inputCpy) {

        int index=0 ;
        if(Term.equals("id")) index=0;
        else  if(Term.equals("+")) index=1;
        else  if(Term.equals("*")) index=2;
        else  if(Term.equals("(")) index=3;
        else  if(Term.equals(")")) index=4;
        else  if(Term.equals("$")) index=5;



        String[][] table = {
                { "S5", null, null, "S4", null, null},
                { null, "S6", null, null, null, "A1"},
                { null, "R2", "S7", null, "R2", "R2"},
                { null, "R4", "R4", null, "R4", "R4"},
                { "S5", null, null, "S4", null, null},
                { null, "R6", "R6", null, "R6", "R6"},
                { "S5", null, null, "S4", null, null},
                { "S5", null, null, "S4", null, null},
                { null, "S6", null, null, "S11",null},
                { null, "R1", "S7", null, "R1", "R1"},
                { null, "R3", "R3", null, "R3", "R3"},
                { null, "R5", "R5", null, "R5", "R5"}
        };
        if (table[actionNum][index] != null) {
            if (table[actionNum][index].length() > 2){
                reduceShift(table[actionNum][index].charAt(0),(table[actionNum][index].charAt(1)-48)*10 + table[actionNum][index].charAt(2)-48, inputCpy,Term);}
            else{reduceShift(table[actionNum][index].charAt(0),table[actionNum][index].charAt(1) - 48, inputCpy,Term);}
        }else Error(inputCpy);
    }

    public static void reduceShift(char type, int a, StringBuilder inputCpy,String term) {
        if (type == 'R') {
            OUTSTR+=String.format("%-25s", stack) + String.format("%-25s", inputCpy.toString()) + String.format("%-15s", "Reduce"+a)+"\n";
            switch (a) {
                case 1:
                    String newStack = stack.substring(stack.indexOf('E') + 1);
                    if (newStack.indexOf('+') == 1) {
                        newStack = stack.substring(stack.indexOf('+') + 1);
                        if (newStack.indexOf('T') == 1) {
                            int beginIndex = stack.indexOf('E');
                            stack = stack.substring(0, beginIndex) + "E" + GoTo(LastNum(stack.substring(0,beginIndex)), "E");

                        } else Error(inputCpy);
                    } else Error(inputCpy);
                    break;
                case 2 :
                    int beginIndex7 = stack.indexOf('T');
                    stack = stack.substring(0, beginIndex7) + "E" + GoTo(LastNum(stack.substring(0,beginIndex7)), "E");
                    break;
                case 3 :
                    String newStack5 = stack.substring(stack.indexOf('T') + 1);
                    if (newStack5.indexOf('*') == 1) {
                        newStack5 = stack.substring(stack.indexOf('*') + 1);
                        if (newStack5.indexOf('F') == 1) {
                            int beginIndex5 = stack.indexOf('T');
                            stack = stack.substring(0, beginIndex5) + "T" + GoTo(LastNum(stack.substring(0,beginIndex5)), "T");
                        } else Error(inputCpy);
                    } else Error(inputCpy);
                    break;
                case 4 :
                    int beginIndex2 = stack.indexOf('F');
                    stack = stack.substring(0, beginIndex2) + "T" + GoTo(LastNum(stack.substring(0,beginIndex2)), "T");
                    break;
                case 5 :
                    String newStack3 = stack.substring(stack.indexOf('(') + 1);
                    if (newStack3.indexOf('E') == 1) {
                        newStack3 = stack.substring(stack.indexOf('+') + 1);
                        if (newStack3.indexOf(')') == 1) {
                            int beginIndex3 = stack.indexOf('(');
                            stack = stack.substring(0, beginIndex3) + "F" + GoTo(LastNum(stack.substring(0, beginIndex3)), "F");
                        } else Error(inputCpy);
                    } else Error(inputCpy);
                    break;
                case 6 :
                    int beginIndex4 = stack.indexOf("id");
                    stack = stack.substring(0, beginIndex4) + "F" + GoTo(LastNum(stack.substring(0,beginIndex4)), "F");
                    break;
            }

            decide(LastNum(stack), term, inputCpy);
        }
        else if (type == 'S') {
            OUTSTR+=String.format("%-25s", stack) + String.format("%-25s", inputCpy.toString()) + String.format("%-15s", "Shift"+a)+"\n";
            Scanner in = new Scanner(inputCpy.toString());

            String word = in.next();
            stack += word;
            inputCpy.delete(0, word.length() + 1);
            in.close();
            if (a != 11)
                stack = stack + "" + a;
            else
                stack = stack.substring(0, stack.length() - 1) + "" + a;

        }
        else if (type == 'A') {
            OUTSTR+=String.format("%-25s", stack) + String.format("%-25s", inputCpy.toString()) + String.format("%-15s", "Accept")+"\n";
        }

    }
    public static int GoTo(int index,String s){
        int[][] table= {
                {1,2,3},
                {0,0,0},
                {0,0,0},
                {0,0,0},
                {8,2,3},
                {0,0,0},
                {0,9,3},
                {0,0,10},
                {0,0,0},
                {0,0,0},
                {0,0,0},
                {0,0,0},
        };
        int num=0;
        switch (s) {
            case "E" : num = 0; break;
            case "T" :num = 1;break;
            case "F": num = 2;break;
            default : System.out.println("HATAHATA");break;
        }
        if(table[index][num]==0) System.exit(1);
        return table[index][num];
    }
    public static int LastNum(String line){
        int i= line.length()-1;
        if (i<1) return Integer.parseInt(line);
        while(line.charAt(i)>47&&line.charAt(i)<58){
            i--;
        }
        return Integer.parseInt(line.substring(i+1));
    }
    public static void Error(StringBuilder inputCpy){
        OUTSTR+=String.format("%-25s", stack) + String.format("%-25s", inputCpy.toString()) + String.format("%-15s", "Error")+"\n";
        Out(FileN);
        System.out.println("Error occurred.");
        System.exit(1);
    }
    public static void Out(String Filename){
        try {
            FileWriter myWriter = new FileWriter(Filename);
            myWriter.write(OUTSTR);
            myWriter.close();

        } catch ( IOException e) {
            Out("output.txt");
        }
    }
}
