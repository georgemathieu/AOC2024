package day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day2Test {

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day2/input.txt");
        List<String> lines = Files.readAllLines(inputPath);

        int count = 0;
        for (String line : lines) {
            List<Integer> numbers = Arrays.stream(line.split(" "))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            if (isSorted(numbers) || canBeSortedByRemovingOne(numbers)) {
                count++;
            }
        }
        System.out.println(count);
    }

    // Check if a list is sorted (ascending or descending)
    private static boolean isSorted(List<Integer> list) {
        boolean ascending = true;
        boolean descending = true;
        for (int i = 1; i < list.size(); i++) {
            int gap = list.get(i) - list.get(i - 1);
            if (gap < 1 || gap > 3) ascending = false;
            if (gap < -3 || gap > - 1) descending = false;
        }
        return ascending || descending;
    }

    // Check if a list can be sorted by removing a single element
    private static boolean canBeSortedByRemovingOne(List<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            List<Integer> subList = new ArrayList<>(list);
            subList.remove(i);
            if (isSorted(subList)) {
                return true;
            }
        }
        return false;
    }
}
