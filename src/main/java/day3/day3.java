package day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class day3 {

    private static final String regMul = "mul\\([0-9]{1,3},[0-9]{1,3}\\)";
    private static final String regDo = "do\\(\\)";
    private static final String regDont = "don\\'t\\(\\)";
    private static final String fullRegex = regMul + "|" + regDo + "|" + regDont;

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day3/input.txt");
        List<String> lines = Files.readAllLines(inputPath);

        // Part 1
        long sum = 0;
        for (String line : lines) {
            Matcher m = Pattern.compile(regMul).matcher(line);
            while (m.find()) {
                sum = getSum(sum, m.group());
            }
        }
        System.out.println(sum);

        // Part 2
        sum = 0;
        boolean isDisabled = false;
        for (String line : lines) {
            Matcher m = Pattern.compile(fullRegex).matcher(line);
            while (m.find()) {
                String match = m.group();

                if (match.startsWith("don't")) {
                    isDisabled = true;
                    continue;
                }

                if (match.startsWith("do")) {
                    isDisabled = false;
                    continue;
                }

                if (!isDisabled) {
                    sum = getSum(sum, match);
                }
            }
        }
        System.out.println(sum);
    }

    /**
     * Parses the remaining string and computes the sum.
      */
    private static long getSum(long sum, String match) {
        String[] content = match.split(",");
        int first = Integer.parseInt(content[0].replace("mul(", ""));
        int second = Integer.parseInt(content[1].replace(")", ""));
        sum += first * second;
        return sum;
    }
}
