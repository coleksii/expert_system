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
}
