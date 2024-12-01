package day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class day1 {

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day1/input.txt");
        List<String> lines = Files.readAllLines(inputPath);

        // Part 1
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        for (String line : lines) {
            String[] content = line.split("\\s+");
            left.add(Integer.parseInt(content[1]));
            right.add(Integer.parseInt(content[0]));
        }
        left.sort(Integer::compareTo);
        right.sort(Integer::compareTo);

        double diff = 0d;
        for (int i = 0; i < left.size(); i++) {
            diff += Math.abs(left.get(i) - right.get(i));
        }
        System.out.println(diff);

        // Part 2
        Map<Integer, Integer> occurrencesRight = new HashMap<>();
        Map<Integer, Integer> occurrencesLeft = new HashMap<>();
        for (Integer l : left) {
            occurrencesRight.put(l, Collections.frequency(right, l));
            occurrencesLeft.put(l, Collections.frequency(left, l));
        }
        
        long similarity = 0;
        for (Map.Entry<Integer, Integer> rightEntry : occurrencesRight.entrySet()) {
            similarity += (long) rightEntry.getValue() * rightEntry.getKey() * occurrencesLeft.get(rightEntry.getKey());
        }
        System.out.println(similarity);
    }
}
