package WordGame;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static WordGame.Permutation.*;


public class WordGameConnector {
    private static Logger log = LoggerFactory.getLogger(WordGameConnector.class);

    static public String playWordGame(String word, int length) {

        log.info("word: " + word + ";length: " + length);
        getAllPossibleValues(word, length);
        String result = WordGame.PostgresSQLWordChecker.findAllWordsFromArray();
        return result;
    }
}

