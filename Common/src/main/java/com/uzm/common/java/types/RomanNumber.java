package com.uzm.common.java.types;

import java.util.TreeMap;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

public class RomanNumber {

    private final static TreeMap<Integer, String> DIGITS = new TreeMap<>();

    static {
        DIGITS.put(1000, "M");
        DIGITS.put(900, "CM");
        DIGITS.put(500, "D");
        DIGITS.put(400, "CD");
        DIGITS.put(100, "C");
        DIGITS.put(90, "XC");
        DIGITS.put(50, "L");
        DIGITS.put(40, "XL");
        DIGITS.put(10, "X");
        DIGITS.put(9, "IX");
        DIGITS.put(5, "V");
        DIGITS.put(4, "IV");
        DIGITS.put(1, "I");
    }

    /**
     * Use recursive programming and TreeMap to convert
     * a integer in roman number.
     *
     * @param number Number to convert to roman.
     * @return Number in roman format Ex: IV, XX
     */

    public static String toRoman(int number) {
        int l = DIGITS.floorKey(number);
        if (number == l) {
            return DIGITS.get(number);
        }
        return DIGITS.get(l) + toRoman(number - l);
    }

}