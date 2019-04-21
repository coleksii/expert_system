package services;

import model.AllData;
import model.Conditional;
import model.Rule;

import java.util.List;

/**
 * Created by coleksii on 4/21/19.
 */
public class AdditionalAnalyzer {
    public void start(AllData allData){
        prepareRule(allData);
    }

    private void prepareRule(AllData allData) {
        for (Rule rule : allData.getRules()) {
            List<Conditional> conditionals = rule.getConditionals();
            Conditional conditional = new Conditional();
            if (conditionals.size() == 1) {
                conditional.setOnlyCharacter(true);
                conditional.setCharacter(conditionals.get(0).getCharacter());
                conditional.setActive(conditionals.get(0).isActive());
                conditional.setSign(conditionals.get(0).getSign());
            } else if (conditionals.size() > 1) {
                conditional.setOnlyCharacter(false);
                conditional.setConditionals(conditionals);
                conditional.setActive(true);
            }
            rule.setConditional(conditional);
        }
    }
}
