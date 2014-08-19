package com.spencer.slycalc;

/**
 * Created by Pete on 8/18/2014.
 */
public class Operators {
    public static char MULTIPLY = (char)215;
    public static char MULTIPLY2 = '*';
    public static char DIVIDE = (char)247;
    public static char DIVIDE2 = '/';
    public static char ADD = '+';
    public static char SUBTRACT = '-';

    public static char[] operators() {
        return new char[]{MULTIPLY, MULTIPLY2, DIVIDE, DIVIDE2, ADD, SUBTRACT};
    }
}
