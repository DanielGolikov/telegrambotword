package org.WordGame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;

import static com.google.common.collect.Sets.combinations;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;
import static org.WordGame.GFG.GFGgetResult;

 class Permutation {
   static void getAllPossibleValues(String inputString, int targetLength){
        int length = inputString.length();

        Set<Integer> indexes = range(0, length).boxed().collect(toSet());
        Set<String> collect = range(1, length + 1)
                .boxed()
                .flatMap(numberOfCharInComb -> combinations(indexes, numberOfCharInComb).stream())
                .map(indicesSet ->
                        indicesSet.stream()
                                .map(inputString::charAt)
                                .map(String::valueOf)
                                .collect(joining()))
                .flatMap(str -> GFGgetResult(str).stream())
                .collect(toSet());
        ArrayList<String> allValues = new ArrayList<>(collect);


       new File("row_values.csv");
       try (PrintWriter csvWriter = new PrintWriter(new FileWriter("row_values.csv"))) {
           for (String item:allValues){
               if (item.length()>=targetLength){
                   csvWriter.println(item);
               }
           }

       } catch (IOException e) {
           e.printStackTrace();
       }
    }
}
