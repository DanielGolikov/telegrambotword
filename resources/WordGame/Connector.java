package org.WordGame;

import static org.WordGame.Permutation.*;


public class Connector {
    public static void playWordGame(String word,int length){
        getAllPossibleValues(word,length);
        long start = System.currentTimeMillis();
        org.WordGame.PostgresSQLWordChecker.findAllWordsFromArray();
        long finish = System.currentTimeMillis();
        double timeConsumedMillis = finish - start;
        System.out.println(timeConsumedMillis/1000);
        org.WordGame.Exporter.printResults();
    }
}
