package edu.hm.cs.sam.mc.misc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andreas on 16.01.2015.
 */
public class Credits {

    String csvFile = "/credits/credits.csv";
    BufferedReader br = null;
    String line = "";
    List<String> people;
    int position = 0;

    /**
     *
     */
    public Credits() {
        people = new LinkedList<>();
        try {
            final InputStream in = getClass().getResourceAsStream(csvFile);
            br = new BufferedReader(new InputStreamReader(in));
            while ((line = br.readLine()) != null) {
                people.add(line);
            }
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Collections.shuffle(people);
    }

    /**
     * @return next person.
     */
    public String getNextPerson() {
        position++;
        if (position >= people.size()) {
            position = 0;
        }
        return people.get(position);
    }
}
