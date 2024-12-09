package day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.IntPredicate;

public class Day9 {

    private static final IntPredicate isEmpty = i -> i % 2 == 1;
    private static final int EMPTY = -1;

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day9/input.txt");
        List<String> lines = Files.readAllLines(inputPath);
        final String line = lines.get(0);

        TreeMap<Integer, Integer> positions = new TreeMap<>();

        // Part 1
        fillMap(line, positions);
        TreeMap<Integer, Integer> map = reorderPositions(new TreeMap<>(positions));

        long result = computeResult(map);
        System.out.println(result); // 6154342787400

        // Part 2
        map = reorderPositionsFullBlocks(new TreeMap<>(positions));
        result = computeResult(map);
        System.out.println(result); // 6183633059094 too high (lent)
    }

    private static long computeResult(TreeMap<Integer, Integer> positions) {
        long result = 0;
        for (Map.Entry<Integer, Integer> position : positions.entrySet()) {
            if (position.getValue() == EMPTY) continue;
            result += (long) position.getKey() * position.getValue();
        }
        return result;
    }

    private static TreeMap<Integer, Integer> reorderPositionsFullBlocks(TreeMap<Integer, Integer> positions) {
        TreeMap<Integer, Range> ranges = new TreeMap<>();
        for (Map.Entry<Integer, Integer> position : positions.entrySet()) {
            int id = position.getValue();
            int idx = position.getKey();
            if (id == EMPTY) continue;
            Range range = ranges.get(id);
            if (range == null) {
                range = new Range();
                range.start = idx;
                range.end = idx;
                ranges.put(id, range);
                continue;
            }
            if (range.start > idx) range.start = idx;
            if (range.end < idx) range.end = idx;
        }

        for (Map.Entry<Integer, Range> rangePosition : ranges.descendingMap().entrySet()) {
            Range range = rangePosition.getValue();
            int fromStart = positions.firstKey();
            int fromEnd = range.start;

            int emptyBlockSize = Integer.MAX_VALUE;
            int emptyStart = 0;
            boolean foundEmpty = false;
            for (int i = fromStart; i < fromEnd; i++) {
                if (positions.get(i) == EMPTY && !foundEmpty) {
                    foundEmpty = true;
                    emptyBlockSize = 1;
                    emptyStart = i;
                    continue;
                }
                if (positions.get(i) == EMPTY && foundEmpty) {
                    emptyBlockSize++;
                    continue;
                }
                if (positions.get(i) != EMPTY && foundEmpty) {
                    if (emptyBlockSize >= range.length()) {

                        for (int j = emptyStart; j < emptyStart + range.length(); j++) {
                            positions.put(j, rangePosition.getKey());
                        }

                        for (int j = range.start; j <= range.end; j++) {
                            positions.put(j, EMPTY);
                        }
                        break;
                    } else {
                        emptyBlockSize = 0;
                        emptyStart = 0;
                        foundEmpty = false;
                    }

                }
            }



        }
        return positions;
    }

    private static TreeMap<Integer, Integer> reorderPositions(TreeMap<Integer, Integer> positions) {
        int fromStart = positions.firstKey();
        int fromEnd = positions.lastKey();

        while (fromStart < fromEnd) {
            while (positions.get(fromStart) != EMPTY) { // Get free space
                fromStart++;
            }

            while (positions.get(fromEnd) == EMPTY) { // Get next id
                fromEnd--;
            }

            if (fromStart >= fromEnd) return positions;

            int id = positions.get(fromEnd);
            positions.put(fromStart, id);
            positions.put(fromEnd, EMPTY);
        }
        return positions;
    }

    private static void fillMap(String line, TreeMap<Integer, Integer> positions) {
        int cpt = 0;
        int idCount = 0;
        for (int i = 0; i < line.length(); i++) {
            int val = Character.getNumericValue(line.charAt(i));

            for (int j = 0; j < val; j++) {
                if (isEmpty.test(i)) {
                    positions.put(cpt, EMPTY);
                } else {
                    positions.put(cpt, idCount);
                }
                cpt++;
            }
            if (!isEmpty.test(i)) {
                idCount++;
            }
        }
    }

    private static class Range {
        int start = Integer.MAX_VALUE;
        int end = 0;

        int length() {
            return end - start + 1;
        }

        @Override
        public String toString() {
            return "Range{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }
}
