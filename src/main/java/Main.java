import model.AllData;
import services.*;

import java.util.List;


/**
 * Created by coleksii on 4/1/19.
 */
public class Main {
    public static void main(String[] args) {
        List<String> rawList = Reader.read(args);
        List<String> refactoredRawList = Redactor.redacture(rawList);

        RuleMapper ruleMapper = new RuleMapper();
        AllData allData = ruleMapper.createRules(refactoredRawList);

        Analyzer analyzer = new Analyzer();
        analyzer.start(allData);

        Printer printer = new Printer();
        printer.printResult(allData);
    }
}
