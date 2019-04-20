package model;

/**
 * Created by coleksii on 4/1/19.
 */
public enum Sign {
    AND('+'), OR('|'), XOR('^');

    private char value;

    Sign(char s) {
        value = s;
    }
    public char getValue(){
        return value;
    }
}
