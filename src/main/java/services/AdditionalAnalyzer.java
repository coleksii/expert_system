package services;

import model.AllData;
import model.Conditional;
import model.Rule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coleksii on 4/21/19.
 */
public class AdditionalAnalyzer {

    private boolean isMatched = false;
    private List<Rule> rules;
    private List<Rule>additionalRules = new ArrayList<>();
    private static int counter = 5;
    public void start(AllData allData){
        rules = allData.getRules();

        int i = 0;
        while (i < rules.size() && i < 100){
            Rule rule = rules.get(i);
            int k = 0;
            while (k < rules.size() && k < 100){
                if (k == i) {
                    k++;
                    continue;
                }
                Rule comparedRule = rules.get(k);
//                if (comparedRule.isChecked()) {
//                    k++;
//                    continue;
//                }
                if (!rule.getAlreadyChecked().contains(comparedRule)) {
                    analyze(rule, comparedRule);
                    simplify(rules);
                    rule.getAlreadyChecked().add(comparedRule);
                }
                k++;
            }
            i++;
        }
        if (!additionalRules.isEmpty() && counter > 0) {
            counter--;
            rules.addAll(additionalRules);
            additionalRules.clear();
            start(allData);
        }
        System.out.println();
    }

    private void simplify(List<Rule> rules) {
        for (Rule rule : rules){
            Conditional conditional = rule.getConditional();
            if (!conditional.isOnlyCharacter()){
                List<Conditional> includeConditionals = conditional.getConditionals();
                if (chek(includeConditionals)) {
                    conditional.setOnlyCharacter(true);
                    Conditional replace = includeConditionals.get(includeConditionals.size() - 1);
                    conditional.setCharacter(replace.getCharacter());
                    conditional.setConditionals(null);
                }
            }
        }
    }

    private boolean chek(List<Conditional> includeConditionals) {
        boolean flag = true;
        char character = includeConditionals.get(0).getCharacter();
        for (Conditional conditional : includeConditionals){
            if (character != conditional.getCharacter()){
                flag = false;
                break;
            }
        }
        if (flag){
            Conditional replace = includeConditionals.get(includeConditionals.size() - 1);
            replace.setOnlyCharacter(true);
            includeConditionals.clear();
            includeConditionals.add(replace);
        }
        return flag;
    }

    private void analyze(Rule rule, Rule comparedRule) {
        boolean containTarget = false;

        Conditional conditional = rule.getConditional();
        Conditional comparedConditional = comparedRule.getConditional();

        if (conditional.isOnlyCharacter()){
            Character character = conditional.getCharacter();
            containTarget = findCharacterInConditionalRecursive(character, comparedConditional)
                    || findCharacterInConditionalRecursive(character, comparedRule.getResult());
            if (containTarget){
                Conditional replace = rule.getResult();
                Rule copy = new Rule();
                copy.setConditional(copyRecursiveListWithReplace(replace, conditional, comparedConditional));
                copy.setResult(copyRecursiveListWithReplace(replace, conditional, comparedRule.getResult()));
                copy.setChecked(false);
                Conditional conditionalCopy = copy.getConditional();
                Conditional result = copy.getResult();
                copy.getResults().add(result);
                copy.getConditionals().add(conditionalCopy);
                additionalRules.add(copy);
                isMatched = true;
            } else {
                comparedRule.setChecked(true);
            }
        } else {
            containTarget = findComplexConditionalMatch(conditional, comparedConditional)
                    || findComplexConditionalMatch(conditional, comparedRule.getResult());
            if (containTarget) {
                Conditional replace = rule.getResult();
                Rule copy = new Rule();
                copy.setConditional(copyRecursiveListWithReplace(replace, conditional, comparedConditional));
                copy.setResult(copyRecursiveListWithReplace(replace, conditional, comparedRule.getResult()));
                copy.setChecked(false);
                Conditional conditionalCopy = copy.getConditional();
                Conditional result = copy.getResult();
                copy.getResults().add(result);
                copy.getConditionals().add(conditionalCopy);
                additionalRules.add(copy);
                isMatched = true;
            }
        }
        comparedRule.setChecked(true);
    }

    private boolean findComplexConditionalMatch(Conditional conditional, Conditional comparedConditional) {
        if (comparedConditional.isOnlyCharacter())
            return false;
        for (Conditional cond : conditional.getConditionals()){
            char character = cond.getCharacter();
            boolean isContain = false;
            for (Conditional compCond : comparedConditional.getConditionals()){
                if (compCond.getCharacter() == character)
                    isContain = true;
            }
            if (!isContain)
                return false;
        }
        return true;
    }

    private Conditional copyRecursiveListWithReplace(Conditional replace, Conditional match, Conditional target) {
        if (target.isOnlyCharacter()){
            if (match.isOnlyCharacter()){
                Conditional conditional = new Conditional();
                conditional.setSign(target.getSign());
                conditional.setActive(target.isActive());
                conditional.setOnlyCharacter(target.isOnlyCharacter());
                if (match.getCharacter() == target.getCharacter()) {
                    if (replace.isOnlyCharacter()) {
                        conditional.setOnlyCharacter(true);
                        conditional.setCharacter(replace.getCharacter());
                    }else {
                        conditional.setOnlyCharacter(false);
                        conditional.setConditionals(replace.getConditionals());
                    }
                }
                else
                    conditional.setCharacter(target.getCharacter());
                return conditional;
            }
            else {
                Conditional conditional = new Conditional();
                conditional.setSign(target.getSign());
                conditional.setActive(target.isActive());
                conditional.setOnlyCharacter(target.isOnlyCharacter());
                conditional.setConditionals(target.getConditionals());
                conditional.setCharacter(target.getCharacter());
                return conditional;
            }
        } else {
            if (match.isOnlyCharacter()) {
                Conditional conditional = new Conditional();
                conditional.setSign(target.getSign());
                conditional.setActive(target.isActive());
                conditional.setOnlyCharacter(target.isOnlyCharacter());
                List<Conditional> list = new ArrayList<>();
                conditional.setConditionals(list);
                for (Conditional cond : target.getConditionals()){
                    if (cond.getCharacter() == match.getCharacter()){
                        Conditional newCond = new Conditional();
                        newCond.setOnlyCharacter(replace.isOnlyCharacter());
                        newCond.setCharacter(replace.getCharacter());
                        newCond.setConditionals(replace.getConditionals());
                        newCond.setSign(cond.getSign());
                        newCond.setActive(cond.isActive());
                        list.add(newCond);
                    }else {
                        list.add(cond);
                    }
                }
                conditional.setCharacter(target.getCharacter());
                return conditional;
            }
            else {
                Conditional conditional = new Conditional();
                conditional.setSign(target.getSign());
                conditional.setActive(target.isActive());
                conditional.setOnlyCharacter(target.isOnlyCharacter());
                if (findComplexConditionalMatch(match, target)){
                    if (replace.isOnlyCharacter()) {
                        conditional.setOnlyCharacter(true);
                        conditional.setCharacter(replace.getCharacter());
                    }else {
                        conditional.setOnlyCharacter(false);
                        conditional.setConditionals(replace.getConditionals());
                    }
                } else {
                    conditional.setConditionals(target.getConditionals());
                    conditional.setCharacter(target.getCharacter());
                    conditional.setOnlyCharacter(target.isOnlyCharacter());
                }
                return conditional;
            }
        }
    }


    private boolean findCharacterInConditionalRecursive(Character character, Conditional comparedConditional) {
        if (comparedConditional.isOnlyCharacter()){
            return comparedConditional.getCharacter() == character;
        } else {
            for (Conditional itarate : comparedConditional.getConditionals()){
                if (findCharacterInConditionalRecursive(character, itarate))
                    return true;
            }
        }
        return false;
    }
}
