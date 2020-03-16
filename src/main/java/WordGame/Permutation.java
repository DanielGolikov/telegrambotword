package WordGame;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import static WordGame.GFG.getGfgResult;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;

class Permutation {
    private static Logger log = LoggerFactory.getLogger(Permutation.class);

    static void getAllPossibleValues(String inputString, int targetLength) {
        int length = inputString.length();

        Set<Integer> indexes = range(0, length).boxed().collect(toSet());
        List<String> allValues = range(1, length + 1)
                .boxed()
                .flatMap(numberOfCharInComb -> Sets.combinations(indexes, numberOfCharInComb).stream())
                .map(indicesSet ->
                        indicesSet.stream()
                                .map(inputString::charAt)
                                .map(String::valueOf)
                                .collect(joining()))
                .flatMap(str -> getGfgResult(str).stream())
                .filter(item -> item.length() >= targetLength)
                .distinct()
                .collect(toList());


        File file = new File("row_values.csv");
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (String item : allValues) {
                printWriter.println(item);
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
