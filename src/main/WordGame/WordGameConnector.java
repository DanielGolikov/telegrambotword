package WordGame;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static WordGame.Permutation.*;


public class WordGameConnector {
    private static Logger log = LoggerFactory.getLogger(WordGameConnector.class);

    static public String playWordGame(String word, int length) {

        log.info("word: " + word + ";length: " + length);
        getAllPossibleValues(word, length);
        long start = System.currentTimeMillis();
        String result = WordGame.PostgresSQLWordChecker.findAllWordsFromArray();
        long finish = System.currentTimeMillis();
        double timeConsumedMillis = finish - start;
        log.info("Time consumed: " + timeConsumedMillis / 1000 + " ms");
        return result;
    }
}

