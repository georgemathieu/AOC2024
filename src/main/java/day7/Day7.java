package day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day7 {

    private static List<Equation> equations = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day7/input.txt");
        List<String> lines = Files.readAllLines(inputPath);

        for (String line : lines) {
            String[] content = line.split(":");
            long result = Long.parseLong(content[0]);

            List<Long> numbers = Arrays.stream(content[1].trim().split(" "))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            equations.add(new Equation(result, numbers));
        }

        // Part 1
        long count = 0;
        for (Equation equation : equations) {
            if (getNbSolutions(equation.numbers, equation.result, 0) > 0) {
                count += equation.result;
            }
        }
        // 3312271365652 Part 1
        // 509463489296712 Part 2
        System.out.println(count);
    }

    private static int getNbSolutions(List<Long> numbers, long expectedResult, int count) {
        if (numbers.size() == 1) {
            if (numbers.get(0).equals(expectedResult)) {
                return count + 1;
            } else {
                return count;
            }
        }
        for (Operators op : Operators.values()) {
            Long calc = op.compute(numbers.get(0), numbers.get(1));
            List<Long> originalList = new ArrayList<>(numbers);
            numbers.set(0, calc);
            numbers.remove(1);
            count = getNbSolutions(numbers, expectedResult, count);
            numbers.clear();
            numbers.addAll(originalList);
        }
        return count;
    }

    record Equation(long result, List<Long> numbers) {
    }

    enum Operators {
        PLUS {
            Long compute(Long a, Long b) { return a + b; }
        },
        MULTIPLY {
            Long compute(Long a, Long b) { return a * b; }
        },
        CONCAT {
            Long compute(Long a, Long b) { return Long.valueOf("" + a + b);}
        };

        abstract Long compute(Long a, Long b);
    }

}
