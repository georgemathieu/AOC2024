package day11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day11 {

    private static Map<Parameters, Long> cachedResults = new HashMap<>();

    private static final int NB_BLINKS = 75;

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        final String line = "4022724 951333 0 21633 5857 97 702 6";
        List<Long> numbers =
                new ArrayList<>(Arrays.stream(line.split(" ")).map(Long::parseLong).toList());

        int depth = 0;
        long count = 0;
        for (Long number : numbers) {
            count += goDown(number, depth);
        }
        // System.out.println(count); // dephts 25 211306
        long end = System.currentTimeMillis();
        System.out.println("time : " + (end - start) + " ms"); // 250783680217283 _ time : 89 ms

    }

    private static long goDown(long value, int depth) {
        Parameters parameters = new Parameters(value, depth);
        if (cachedResults.containsKey(parameters)) {
            return cachedResults.get(parameters);
        }

        if (depth >= NB_BLINKS) {
            return 1;
        }

        long count = 0;
        String asStr = String.valueOf(value);
        if (value == 0L) {
            count = goDown(1L, depth + 1);
        } else if (asStr.length() % 2 == 0) {
            count += goDown(Long.parseLong(asStr.substring(0, asStr.length() / 2)), depth + 1);
            count += goDown(Long.parseLong(asStr.substring(asStr.length() / 2)), depth + 1);
        } else {
            count = goDown(value * 2024, depth + 1);
        }
        cachedResults.put(parameters, count);
        return count;
    }

    record Parameters(long value, int depth) {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Parameters param)) return false;
            return depth == param.depth && value == param.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, depth);
        }
    }
}
