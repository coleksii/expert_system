package model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coleksii on 4/15/19.
 */

@Getter
@Setter
public class AllData {
    private List<Rule> rules = new ArrayList<>();
    private List<Character> params = new ArrayList<>();
    private List<Character> request = new ArrayList<>();
    private List<Character> rightResults;
    private List<Character> wrongResults;
}
