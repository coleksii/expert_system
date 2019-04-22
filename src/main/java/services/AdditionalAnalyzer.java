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
    public void start(AllData allData){
        rules = allData.getRules();

        int i = 0;
        while (i < rules.size()){
            Rule rule = rules.get(i);
            int k = 0;
            while (k < rules.size()){
                if (k == i) {
                    k++;
                    continue;
                }
                Rule comparedRule = rules.get(k);
                if (comparedRule.isChecked()) {
                    k++;
                    continue;
                }
                analyze(rule, comparedRule);
                k++;
            }
            i++;
        }
        if (!additionalRules.isEmpty()) {
            rules.addAll(additionalRules);
            additionalRules.clear();
            start(allData);
        }
    }

    private void analyze(Rule rule, Rule comparedRule) {
        boolean containTarget = false;

        Conditional conditional = rule.getConditional();
        Conditional comparedConditional = comparedRule.getConditional();

        if (conditional.isOnlyCharacter()){
            Character character = conditional.getCharacter();
            containTarget = findCharacterInConditionalRecursive(character, comparedConditional);
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
            containTarget = findComplexConditionalMatch(conditional, comparedConditional);
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
                conditional.setConditionals(target.getConditionals());
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
