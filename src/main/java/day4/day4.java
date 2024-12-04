package day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class day4 {

    private static final String usedLetters = "XMAS";
    private static final Pattern pattern = Pattern.compile(usedLetters);

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day4/input.txt");
        List<String> lines = Files.readAllLines(inputPath);


        char[][] letters = new char[lines.size()][lines.get(0).length()];

        int count = 0;
        // Horizontal
        count += getCount(lines);

        for (int i = 0; i < lines.size(); i++) { // rows
            for (int j = 0; j < lines.get(i).length(); j++) { // columns
                char c = lines.get(i).charAt(j);
                letters[i][j] = c;
                // System.out.print(c);
            }
            // System.out.println();
        }
        List<String> verticalLines = new ArrayList<>();
        for (int i = 0; i < lines.get(0).length(); i++) { // columns
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < lines.size(); j++) { // rows
                sb.append(letters[j][i]);
            }
            verticalLines.add(sb.toString());
        }

        count += getCount(verticalLines);


        // Test diagonal
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line);
        }
        String singleLine = sb.toString();

        final int initialSize = lines.get(0).length();
        count += getCountDiagonals(initialSize, singleLine);

        count += getCountDiagonals(initialSize, sb.reverse().toString());


        // 2455 too high
        // 2447
        System.out.println(count);



    }

    private static int getCountDiagonals(int initialSize, String singleLine) {
        int count = 0;
        final int diagonalFwdJump = initialSize;
        final String regDiagonalForward = "(?=(X\\w{" + diagonalFwdJump +
                "}M\\w{" + diagonalFwdJump + "}A\\w{" + diagonalFwdJump + "}S)).";
        final Pattern patternDiagonalFwd = Pattern.compile(regDiagonalForward);

        Matcher matcherFwd = patternDiagonalFwd.matcher(singleLine);
        while (matcherFwd.find()) {
            boolean shouldIgnore = false;
            int idx = matcherFwd.start() % initialSize;
            for (int i = matcherFwd.start() + diagonalFwdJump + 1; i < matcherFwd.start() + 3 * diagonalFwdJump + 4; i += diagonalFwdJump + 1) {
                int currentIdx = i % initialSize;
                if (currentIdx != idx + 1) {
                    shouldIgnore = true; // skip line jump
                }
                idx = i % initialSize;
            }
            if (!shouldIgnore) {
                count++;
            }
        }

        final int diagonalBwdJump = diagonalFwdJump - 2;
        final String regDiagonalBwd = "(?=(X\\w{" + diagonalBwdJump
                + "}M\\w{" + diagonalBwdJump + "}A\\w{" + diagonalBwdJump + "}S)).";
        final Pattern patternDiagonalBwd = Pattern.compile(regDiagonalBwd);

        Matcher matcherBwd = patternDiagonalBwd.matcher(singleLine);
        while (matcherBwd.find()) {
            boolean shouldIgnore = false;
            int idx = matcherBwd.start() % initialSize;
            for (int i = matcherBwd.start() + diagonalBwdJump + 1; i < matcherBwd.start() + 3 * diagonalBwdJump + 4; i += diagonalBwdJump + 1) {
                int currentIdx = i % initialSize;
                if (currentIdx != idx - 1) {
                    shouldIgnore = true; // skip line jump
                }
                idx = i % initialSize;
            }
            if (!shouldIgnore) {
                count++;
            }
        }
        return count;
    }

    private static int getCount(List<String> lines) {
        int count = 0;
        for (String line : lines) {
            Matcher forward = pattern.matcher(line);
            while (forward.find()) {
                count++;
            }

            StringBuilder sb = new StringBuilder(line);
            sb.reverse();
            Matcher backward = pattern.matcher(sb.toString());
            while (backward.find()) {
                count++;
            }
        }
        return count;
    }
}
