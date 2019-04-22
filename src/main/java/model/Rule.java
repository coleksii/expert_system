package model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coleksii on 4/1/19.
 */
@Setter
@Getter
public class Rule {
    private List<Conditional> conditionals = new ArrayList<>();
    private List<Conditional> results = new ArrayList<>();
    private Conditional conditional = new Conditional();
    private Conditional result = new Conditional();
    private boolean isChecked = false;

    public void mapToConditional() {
        map(conditionals, conditional);
        map(results, result);
    }

    private void map(List<Conditional> conditionals, Conditional conditional) {
        if (conditionals.size() == 1) {
            conditional.setOnlyCharacter(true);
            conditional.setCharacter(conditionals.get(0).getCharacter());
            conditional.setActive(conditionals.get(0).isActive());
            conditional.setSign(conditionals.get(0).getSign());
        } else if (conditionals.size() > 1){
            conditional.setOnlyCharacter(false);
            conditional.setConditionals(conditionals);
            conditional.setActive(true);
        }
    }

}
