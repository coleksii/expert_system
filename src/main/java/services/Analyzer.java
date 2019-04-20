package services;

import model.AllData;
import model.Conditional;
import model.Rule;
import model.Sign;

import java.util.ArrayList;
import java.util.List;

public class Analyzer {

    private List<Rule> trueRule = new ArrayList<>();
    private boolean isResultMode = false;
    private List<Character> rightResultsPerTurn = new ArrayList<>();
    private List<Character> rightResultsTotal = new ArrayList<>();
    private List<Character> wrongResultsPerTurn = new ArrayList<>();
    private List<Character> wrongResultsTotal = new ArrayList<>();

    public void start(AllData allData){
        List<Rule> rules = allData.getRules();
        List<Character> params = allData.getParams();
        List<Character> request = allData.getRequest();

        for (Rule rule : rules){
            analyseRule(rule, params);
        }
        isResultMode = true;
        for (Rule rule : trueRule){
            analyseResult(rule, request);
        }
        allData.setRightResults(rightResultsTotal);
        allData.setWrongResults(wrongResultsTotal);
    }

    private void analyseResult(Rule rule, List<Character> request) {
        if (checkConditionsRecursive(rule.getResults(), request)){
            rightResultsTotal.addAll(rightResultsPerTurn);
        }else
            wrongResultsTotal.addAll(wrongResultsPerTurn);
        rightResultsPerTurn.clear();
        wrongResultsPerTurn.clear();
    }

    private void analyseRule(Rule rule, List<Character> params) {
        if (checkConditionsRecursive(rule.getConditionals(), params))
            trueRule.add(rule);
    }

    private boolean checkConditionsRecursive(List<Conditional> conditionals, List<Character> params) {
        boolean isTrueRule = true;

        boolean isXor = false;
        boolean isTrueIfXor = false;
        for (Conditional conditional: conditionals){
            Sign sign = conditional.getSign();
            if(checkConditional(conditional, params)){
                if (isXor){
                    isXor = false;
                    if (isTrueIfXor){
                        isTrueRule = false;
                    }
                }
                if (sign == Sign.AND)
                    continue;
                if (sign == Sign.OR){
                    break;
                }
                if (sign == Sign.XOR){
                    isTrueIfXor = true;
                    isXor = true;
                }
            }else {
                if (sign == null && !isXor){
                    isTrueRule = false;
                    break;
                }
                if (isXor){
                    isXor=false;
                    if (!isTrueIfXor){
                        isTrueRule = false;
                    }
                }
                if (sign == Sign.AND) {
                    isTrueRule = false;
                    break;
                }
                if (sign == Sign.OR){
                    continue;
                }
                if (sign == Sign.XOR){
                    isTrueIfXor = false;
                    isXor = true;
                }
            }
        }
        return isTrueRule;
    }

    private boolean checkConditional(Conditional conditional, List<Character> params) {

        if (conditional.isOnlyCharacter()) {
            Character character = conditional.getCharacter();
            if (conditional.isActive()) {
                boolean result = params.contains(character);
                if (isResultMode){
                    rightResultsPerTurn.add(character);
                }
                return result;
            }
            else {
                boolean result = !params.contains(character);
                if (isResultMode){
                    wrongResultsPerTurn.add(character);
                }
                return result;
            }
        }
        else {
            boolean result = checkConditionsRecursive(conditional.getConditionals(), params);
            if (conditional.isActive())
                return result;
            else
                return !result;
        }
    }
}
