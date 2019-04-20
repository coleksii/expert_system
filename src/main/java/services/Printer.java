package services;

import model.AllData;

import java.util.List;

public class Printer {
    public void printResult(AllData allData) {

        List<Character> rightResult = allData.getRightResults();
        List<Character> forbiddenResult = allData.getWrongResults();
        for (Character character : allData.getRequest()){
            if (rightResult.contains(character) && forbiddenResult.contains(character))
                System.out.println(character + " has a \033[0;35mCONFLICT\033[0m");
            else if (rightResult.contains(character)){
                System.out.println(character + " is \033[0;32mTRUE\033[0m");
            }
            else
                System.out.println(character + " is \033[0;31mFALSE\033[0m");
        }
    }
}
