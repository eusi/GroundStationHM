package edu.hm.cs.sam.mc.secretmessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.apache.log4j.Logger;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;

public class SecretMessageController extends Observable {

	private static SecretMessageController instance = null;
	
	   protected SecretMessageController() {
		   
	   }
	   public static SecretMessageController getInstance() {
	      if(instance == null) {
	         instance = new SecretMessageController();
	         instance.setupInstance();
	      }
	      return instance;
	   }
	   
	public void setupInstance() {
		langTool.disableRule("UPPERCASE_SENTENCE_START");
	}
	
    private static Set<String> generatePerm(final String input) {
        final Set<String> set = new HashSet<String>();
        if (input == "") {
            return set;
        }

        final Character a = input.charAt(0);

        if (input.length() > 1) {
            final String myInput = input.substring(1);

            final Set<String> permSet = generatePerm(myInput);

            for (final String x : permSet) {
                for (int i = 0; i <= x.length(); i++) {
                    set.add(x.substring(0, i) + a + x.substring(i));
                }
            }
        } else {
            set.add(a + "");
        }
        return set;
    }

    private final JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());

    private static final Logger LOG = Logger.getLogger(SecretMessageController.class.getName());

    /*
     * Checks word if it inside the dictionary
     * @param word the String to check
     * @return boolean if it is inside the dictionary
     */

    private boolean checkWord(final String word) throws IOException {

        try {
            langTool.activateDefaultPatternRules();
        } catch (final IOException e) {
            LOG.error("Could not read", e);
        }
        final List<RuleMatch> matches = langTool.check(word);

        return matches.isEmpty();
    }

    /*
     * Generates all permutations of a String
     * @param input the string to permute
     * @return Set<String> all permutations of the given String
     */

    /*
     * Checks the String and all its permutations if it is inside the english
     * word dictionary
     * @param letters the String to permute and check
     * @return returns possible Words
     */
    public ArrayList<String> searchWords(final String letters) {
        final Set<String> permutations = generatePerm(letters);

        final ArrayList<String> possibleWords = new ArrayList();

        for (final String permutation : permutations) {
            try {
                if (checkWord(permutation)) {
                    possibleWords.add(permutation);
                }
            } catch (final IOException e) {
                LOG.error("Could not read", e);
            }
        }
        return possibleWords;
    }
    
    /*
     * Set New Characters / Letters to the SecretMessage which will be shown in the view.
     * @param letters the new letters to set
     */
    public void setNewLetters(String letters) {
    	setChanged(); 
        notifyObservers(letters); 
    }

}
