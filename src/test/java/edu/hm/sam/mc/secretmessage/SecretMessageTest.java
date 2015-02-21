package edu.hm.sam.mc.secretmessage;

import edu.hm.cs.sam.mc.secretmessage.SecretMessageController;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class SecretMessageTest {

    private final SecretMessageController secretMessageController = SecretMessageController
            .getInstance();

    @Test
    public void testSearchWords() {

        final List<String> wordList = secretMessageController.searchWords("ifre");
        assertTrue("Search Words for Letters 'ifre' should find 2 Words", wordList.size() == 2);

        assertTrue("Search words for letter 'ifre' should find rife", wordList.contains("rife"));
        assertTrue("Search words for letter 'ifre' should find  fire", wordList.contains("fire"));
    }

}
