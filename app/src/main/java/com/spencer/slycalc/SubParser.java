package com.spencer.slycalc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Pete on 8/18/2014.
 */
public class SubParser {

    private static int findLeftBound(String workingSegment, int currentOperatorIndex) {
        for(int i = currentOperatorIndex - 1; i >= 0; i--) {
            if (!".0123456789".contains(String.valueOf(workingSegment.charAt(i)))) {
                return i + 1;
            }
        }
        return 0;
    }

    private static int findRightBound(String workingSegment, int currentOperatorIndex) {
        for(int i = currentOperatorIndex + 1; i < workingSegment.length(); i++) {
            if (!".0123456789".contains(String.valueOf(workingSegment.charAt(i)))) {
                return i - 1;
            }
        }
        return workingSegment.length()-1;
    }

    private static int findCurrentOperatorIndex(String workingSegment) {
        for(char c : Operators.operators()) {
            int currentOperatorIndex = workingSegment.indexOf(c);
            if(currentOperatorIndex >= 0) {
                return currentOperatorIndex;
            }
        }
        return -1;
    }

    public static String eval(String mathExpression) {
        String exp = mathExpression;
        while(containsOperators(exp)) {
            exp = evaluate(exp);
        }
        return new BigDecimal(exp).stripTrailingZeros().toPlainString();
    }

    private static String evaluate(String mathExperession) {
        String workingSegment;
        int rightmostOpenPerentheses = mathExperession.lastIndexOf("(");
        int matchingClosingPerentheses = mathExperession.indexOf(")",rightmostOpenPerentheses);
        if(rightmostOpenPerentheses >= 0) {
            workingSegment = mathExperession.substring(rightmostOpenPerentheses, matchingClosingPerentheses + 1);
        }
        else {
            workingSegment = mathExperession;
        }



        int currentOperatorIndex = findCurrentOperatorIndex(workingSegment);
        int leftBound = findLeftBound(workingSegment, currentOperatorIndex);
        int rightBound = findRightBound(workingSegment, currentOperatorIndex);




        BigDecimal leftO = new BigDecimal(workingSegment.substring(leftBound, currentOperatorIndex));
        BigDecimal rightO = new BigDecimal(workingSegment.substring(currentOperatorIndex + 1, rightBound + 1));

        BigDecimal calculated = calculate(leftO, workingSegment.charAt(currentOperatorIndex), rightO);


        int subBegin = Math.max(0,rightmostOpenPerentheses) + leftBound;
        int subEnd = Math.max(0, rightmostOpenPerentheses) + rightBound;

        if(rightmostOpenPerentheses >= 0 && rightmostOpenPerentheses + 1 == subBegin
                && matchingClosingPerentheses >= 0 && matchingClosingPerentheses -1 == subEnd) {
            //used up everything in perens - get rid of the perens
            subBegin = rightmostOpenPerentheses;
            subEnd = matchingClosingPerentheses;
        }
        String leftSide = mathExperession.substring(0, subBegin);
        String rightSide = mathExperession.substring(subEnd + 1, mathExperession.length());



        return leftSide + calculated + rightSide;






    }

    private static BigDecimal calculate(BigDecimal leftO, char operator, BigDecimal rightO) {
        if(Operators.ADD == operator) {
            return leftO.add(rightO);
        }
        else if (Operators.SUBTRACT == operator) {
            return leftO.subtract(rightO);
        }
        else if (Operators.MULTIPLY == operator || Operators.MULTIPLY2 == operator) {
            return leftO.multiply(rightO);
        }
        else if (Operators.DIVIDE == operator || Operators.DIVIDE2 == operator) {
            if(BigDecimal.ZERO.equals(rightO)) {
                throw new IllegalArgumentException("Error: Cannot divide by zero.");
            }

            return leftO.divide(rightO, 100, RoundingMode.HALF_UP);
        }
        throw new IllegalArgumentException("Illegal math operator in calculate(): " + operator);
    }

    private static boolean containsOperators(String exp) {
        for(char c : Operators.operators()) {
            if(exp.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }

    public static List<String> validate(String screen) {
        List<String> errors = new ArrayList<String>();
        if(perenthesesMismatch(screen)) {
            errors.add("Check your perentheses - they don't match up!");
        }
        if(perenthesesAdjacentToDigit(screen)) {
            errors.add("You  have a number touching the outside of a perentheses... shouldn't there be a math operator in between?");
        }
        return errors;
    }

    private static boolean perenthesesAdjacentToDigit(String screen) {
        if(screen.matches(".*\\)\\d.*") || screen.matches(".*\\d\\(.*")) {
            return true;
        }
        return false;
    }

    private static boolean perenthesesMismatch(String screen) {
        int depth = 0;
        for (char c : screen.toCharArray()) {
            if('(' == c) {
                depth++;
            }
            else if (')' == c) {
                depth--;
                if (depth < 0) {
                    return true;
                }
            }
        }
        if(depth != 0) {
            return true;
        }
        else return false;
    }
}
