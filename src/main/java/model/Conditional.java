package model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * Created by coleksii on 4/1/19.
 *  "!A +" - COnditional
 *  "A" - character
 *  "+" - sign
 */
@Getter
@Setter
public class Conditional {
    /**If only character = than no should be Conditionals*/
    private boolean isOnlyCharacter;

    private char character;
    /**if have no sign "!" - it's active*/
    private boolean isActive = true;

    private List<Conditional> conditionals;

    private Sign sign;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (!isActive)
            result.append("!");
        if (isOnlyCharacter){
            result.append(character);
            if (sign != null)
                result.append(" ").append(sign.getValue()).append(" ");
        } else {
            for (Conditional conditional : conditionals)
                result.append(conditional.toString());
        }
        return result.toString();
    }
}
