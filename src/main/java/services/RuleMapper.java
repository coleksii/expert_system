package services;

import model.AllData;
import model.Conditional;
import model.Rule;
import model.Sign;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coleksii on 4/2/19.
 */
public class RuleMapper {

    private static boolean isIfAndOnlyIf = false;
    private static int counterParentheses = 0;
    private AllData allData = new AllData();

    public AllData createRules(List<String> raw) {
        for (String s : raw) {
            createAndAddRule(s);
        }
        createParams(raw.get(raw.size() - 2));
        createRequest(raw.get(raw.size() - 1));
        List<Rule> rules = allData.getRules();
        for (Rule rule: rules){
            rule.mapToConditional();
        }
        return allData;
    }

    private void createRequest(String s) {
        List<Character> requests = new ArrayList<>();
        char[] chars = s.toCharArray();
        if (chars[0] != '?')
            throw new IllegalArgumentException();
        for (int i = 1; i < chars.length; i++) {
            requests.add(chars[i]);
        }
        if (requests.isEmpty())
            throw new IllegalArgumentException("Syntax Error: Have no parameters");
        allData.setRequest(requests);

    }

    private void createParams(String s) {
        List<Character> params = new ArrayList<>();
        char[] chars = s.toCharArray();
        if (chars[0] != '=')
            throw new IllegalArgumentException();
        for (int i = 1; i < chars.length; i++) {
            params.add(chars[i]);
        }
        allData.setParams(params);
    }


    /**
     * If we see => than we switch conditionals List on result List and save result into AllData
     */
    private void createAndAddRule(String s) {
        Rule rule = new Rule();

        List<Conditional> conditionals = new ArrayList<>();
        List<Conditional> results = new ArrayList<>();
        char[] chars = s.toCharArray();

        if (!s.matches("[A-Z<=>|^+!()? ]+"))
            throw new IllegalArgumentException("Syntax Error");
        Conditional conditional = null;
        List<Conditional> list = conditionals;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '?')
                break;
            if (chars[i] == '=' && chars.length > i + 1 && chars[i + 1] != '>')
                break;
            if (chars[i] == '=')
                continue;
            if (chars[i] == '>') {
                list = results;
                conditional = null;
                if (i > 1 && chars[i - 1] == '=' && chars[i - 2] == '<')
                    isIfAndOnlyIf = true;
                continue;
            }
            if (chars[i] == ')')
                throw new IllegalArgumentException("Syntax Error");

            if (chars[i] == ' ')
                continue;
            if (conditional == null) {
                conditional = new Conditional();
                if (chars[i] == '!') {
                    // If write twice !!, for example "!!B"
                    if (!conditional.isActive()) {
                        throw new IllegalArgumentException();
                    }
                    conditional.setActive(false);
                    continue;
                } else {
                    conditional.setActive(true);
                }
            }
            if (chars[i] == '('){
                    conditional = new Conditional();
                if (i > 0 && chars[i - 1] == '!')
                    conditional.setActive(false);
                    counterParentheses++;
                conditional.setOnlyCharacter(false);
                List<Conditional> embededConditionals = createConditionalWithParentheses(chars, i + 1);
                conditional.setConditionals(embededConditionals);
                i = skipParentheses(chars, i);
                list.add(conditional);
            }
            if (Character.isUpperCase(chars[i])) {
                conditional.setCharacter(chars[i]);
                conditional.setOnlyCharacter(true);
                list.add(conditional);
                continue;
            }
            if (chars[i] == Sign.AND.getValue()) {
                conditional.setSign(Sign.AND);
                conditional = null;
            } else if (chars[i] == Sign.XOR.getValue()) {
                conditional.setSign(Sign.XOR);
                conditional = null;
            } else if (chars[i] == Sign.OR.getValue()) {
                conditional.setSign(Sign.OR);
                conditional = null;
            }
        }
        if (!conditionals.isEmpty()) {
            rule.setConditionals(conditionals);
            allData.getRules().add(rule);
            rule.setResults(results);
            if (isIfAndOnlyIf) {
                Rule converse = new Rule();
                converse.setConditionals(results);
                converse.setResults(conditionals);
                allData.getRules().add(converse);
            }
        }
    }

    private List<Conditional> createConditionalWithParentheses(char[] chars, int i) {
        List<Conditional> conditionals = new ArrayList<>();
        Conditional conditional = null;
        for (; i < chars.length; i++){
            if (chars[i] == ')'){
                if (counterParentheses > 0){
                    counterParentheses--;
                    return conditionals;
                }
                else throw new IllegalArgumentException("Syntax Error");

            }
            if (chars[i] == ' ')
                continue;
            if (conditional == null) {
                conditional = new Conditional();
                if (chars[i] == '!') {
                    // If write twice !!, for example "!!B"
                    if (!conditional.isActive()) {
                        throw new IllegalArgumentException();
                    }
                    conditional.setActive(false);
                    continue;
                } else {
                    conditional.setActive(true);
                }
            }
            if (chars[i] == '('){
                conditional = new Conditional();
                if (i > 0 && chars[i - 1] == '!')
                    conditional.setActive(false);
                counterParentheses++;
                conditional.setOnlyCharacter(false);
                List<Conditional> embededConditionals = createConditionalWithParentheses(chars, i + 1);
                conditional.setConditionals(embededConditionals);
                i = skipParentheses(chars, i);
                conditionals.add(conditional);
            }
            if (Character.isUpperCase(chars[i])) {
                conditional.setCharacter(chars[i]);
                conditional.setOnlyCharacter(true);
                conditionals.add(conditional);
                continue;
            }
            if (chars[i] == Sign.AND.getValue()) {
                conditional.setSign(Sign.AND);
                conditional = null;
            } else if (chars[i] == Sign.XOR.getValue()) {
                conditional.setSign(Sign.XOR);
                conditional = null;
            } else if (chars[i] == Sign.OR.getValue()) {
                conditional.setSign(Sign.OR);
                conditional = null;
            }
        }
        throw new IllegalArgumentException("Syntax Error");
    }

    private int skipParentheses(char[] chars, int i){
        int counter = 0;
        while (i < chars.length){
            if (chars[i] == ('('))
                counter++;
            if (chars[i] == ')' && counter == 1){
                break;
            }
            else if (chars[i] == (')'))
                counter--;
            if (counter < 0)
                throw new IllegalArgumentException("Syntax Error");
            i++;
        }
        return i;
    }
}
