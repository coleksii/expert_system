package services;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coleksii on 4/1/19.
 */
public class Redactor {
    public static List<String> redacture(List<String> rawList) {
        if (rawList.isEmpty())
            throw new IllegalArgumentException();
        List<String> refacotredRawList = new ArrayList<>();
        for (String raw : rawList){
            String redactoredRaw = raw.split("#")[0].trim();
            if (!redactoredRaw.isEmpty())
                refacotredRawList.add(redactoredRaw);
        }
        return refacotredRawList;

    }
}
