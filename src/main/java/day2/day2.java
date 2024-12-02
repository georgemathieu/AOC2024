package day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class day2 {

    private static final int MAX_ERROR = 1;

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day2/input.txt");
        List<String> lines = Files.readAllLines(inputPath);

        // Part 1
        int count = 0;
        for (String line : lines) {
            List<Integer> numbers = Arrays.stream(line.split(" "))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            final boolean increasing = numbers.get(1) - numbers.get(0) > 0;
            if (!increasing) {
                Collections.reverse(numbers); // All valid lists should increase now
            }

            boolean isValid = true;
            for (int i = 1; i < numbers.size(); i++) {
                int gap = numbers.get(i) - numbers.get(i - 1);
                isValid &= gap >= 1 && gap <= 3;
            }
            if (isValid) {
                count++;
            }
        }
        System.out.println(count);

        // Part 2
        count = 0;

        int errorCount;
        ErrorFound errorId = null;
        for (String line : lines) {
            List<Integer> numbers = Arrays.stream(line.split(" "))
                    .map(Integer::parseInt).toList();

            // Check for increasing or decreasing sequences
            if (checkSequence(numbers, 1, 3) || checkSequence(numbers, -3, -1)) {
                count++;
            }
        }
        System.out.println(count);
    }

    private static boolean checkSequence(List<Integer> numbers, int minGap, int maxGap) {
        boolean isValid = true;
        int errorCount = 0;
        ErrorFound errorId = null;

        // Validate sequence with allowed gaps
        for (int i = 1; i < numbers.size(); i++) {
            int gap = numbers.get(i) - numbers.get(i - 1);
            isValid &= gap >= minGap && gap <= maxGap;

            if (!isValid && errorCount < MAX_ERROR) {
                errorCount++;
                isValid = true;
                errorId = new ErrorFound(i, i - 1);
            }
        }

        // If valid without any errors
        if (isValid && errorId == null) {
            return true;
        }

        // If an error exists, check by removing the problematic element
        if (errorId != null) {
            if (isValidAfterRemoving(numbers, errorId.a(), minGap, maxGap) ||
                    isValidAfterRemoving(numbers, errorId.b(), minGap, maxGap)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValidAfterRemoving(List<Integer> numbers, int indexToRemove, int minGap, int maxGap) {
        List<Integer> modifiedList = new ArrayList<>(numbers);
        modifiedList.remove(indexToRemove);

        for (int i = 1; i < modifiedList.size(); i++) {
            int gap = modifiedList.get(i) - modifiedList.get(i - 1);
            if (gap < minGap || gap > maxGap) {
                return false;
            }
        }
        return true;
    }

    record ErrorFound(int a, int b) {}
}
