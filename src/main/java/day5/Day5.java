package day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day5 {

    private static List<Pair> dictionary = new ArrayList<>();

    private static final Comparator<Integer> OP_COMP = (Integer i1, Integer i2) -> {
        if (i1.equals(i2)) return 0;
        if (dictionary.contains(new Pair(i1, i2))) {
            return -1;
        } else {
            return 1;
        }
    };

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day5/input.txt");
        List<String> lines = Files.readAllLines(inputPath);

        List<String> invalidLines = new ArrayList<>();


        // Part 1
        int sum = 0;
        for (String line : lines) {
            if (line.contains("|")) {
                // Rule mode
                String[] content = line.split("\\|");
                dictionary.add(new Pair(Integer.parseInt(content[0]), Integer.parseInt(content[1])));
            } else {
                // Update mode
                if (!line.contains(",")) {
                    continue;
                }
                int[] content = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();

                boolean isValid = true;
                for (int i = 1; i < content.length; i++) {
                    if (!dictionary.contains(new Pair(content[i - 1], content[i]))) {
                        isValid = false;
                    }
                }

                if (isValid) {
                    sum += content[content.length / 2];
                } else {
                    invalidLines.add(line);
                }
            }
        }
        System.out.println(sum);

        // Part 2
        fixInvalidLines_WrongVersion(invalidLines);

        fixInvalidLines(invalidLines);
    }

    private static void fixInvalidLines(List<String> invalidLines) {
        int count = 0;
        TreeSet<Integer> set;
        for (String line : invalidLines) {
            set = new TreeSet<>(OP_COMP);
            int[] content = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
            for (int i = 0; i < content.length; i++) {
                set.add(content[i]);
            }
            count += set.toArray(new Integer[0])[content.length / 2];
        }
        System.out.println(count);
    }

    record Pair(int a, int b) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return a == pair.a && b == pair.b;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }

    /**
     * Doesn't work for lines with multiple errors.
     */
    private static void fixInvalidLines_WrongVersion(List<String> invalidLines) {
        int count = 0;

        for (String line : invalidLines) {
            boolean foundValid = false;
            int[] content = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();

            for (int i = 1; i < content.length; i++) {
                if (!dictionary.contains(new Pair(content[i - 1], content[i]))) {
                    int[] copy = new int[content.length];

                    for (int j = 0; j < content.length; j++) {
                        System.arraycopy(content, 0, copy, 0, content.length);
                        int temp = copy[i];
                        copy[i] = copy[j];
                        copy[j] = temp;

                        boolean isTestValid = true;
                        for (int k = 1; k < copy.length; k++) {
                            if (!dictionary.contains(new Pair(copy[k - 1], copy[k]))) {
                                isTestValid = false;
                            }
                        }

                        if (isTestValid) {
                            count += copy[copy.length / 2];
                            break;
                        }
                    }
                }
            }
        }
        System.out.println(count);
    }
}
