package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by coleksii on 4/1/19.
 */
public class Reader {

    public static List<String> read(String [] args){

        List<String> fileRaw = new ArrayList<>();

        if (args.length < 1)
            throw new IllegalArgumentException("null file");
        BufferedReader br = null;
        FileReader fr = null;

        try {

            fr = new FileReader(args[0]);
            br = new BufferedReader(fr);

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                fileRaw.add(sCurrentLine);
            }
        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        return fileRaw;
    }
}
