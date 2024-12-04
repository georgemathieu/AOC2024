package day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class day4_Part2 {

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day4/input.txt");
        List<String> lines = Files.readAllLines(inputPath);

        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line);
        }
        String singleLine = sb.toString();

        final int initialSize = lines.get(0).length();
        List<Integer> forwardAs = new ArrayList<>();
        List<Integer> backwardsAs = new ArrayList<>();

        fillAPositions(initialSize, singleLine, forwardAs, backwardsAs);

        int count = 0;
        for (Integer forwardA : forwardAs) {
            if (backwardsAs.contains(forwardA)) {
                count++;
            }
        }
        // 1868
        System.out.println(count);

    }

    private static void fillAPositions(int initialSize, String singleLine, List<Integer> forwardAs, List<Integer> backwardsAs) {
        final int diagonalFwdJump = initialSize;
        String regDiagonalForward = "(?=(M\\w{" + diagonalFwdJump + "}A\\w{" + diagonalFwdJump + "}S)).";
        Pattern patternDiagonalFwd = Pattern.compile(regDiagonalForward);
        Matcher matcherFwd = patternDiagonalFwd.matcher(singleLine);
        fillAPositions(initialSize, forwardAs, matcherFwd, diagonalFwdJump, 1);

        final int diagonalBwdJump = diagonalFwdJump - 2;
        String regDiagonalBwd = "(?=(M\\w{" + diagonalBwdJump + "}A\\w{" + diagonalBwdJump + "}S)).";
        Pattern patternDiagonalBwd = Pattern.compile(regDiagonalBwd);
        Matcher matcherBwd = patternDiagonalBwd.matcher(singleLine);
        fillAPositions(initialSize, backwardsAs, matcherBwd, diagonalBwdJump, -1);

        regDiagonalForward = "(?=(S\\w{" + diagonalFwdJump + "}A\\w{" + diagonalFwdJump + "}M)).";
        patternDiagonalFwd = Pattern.compile(regDiagonalForward);
        matcherFwd = patternDiagonalFwd.matcher(singleLine);
        fillAPositions(initialSize, forwardAs, matcherFwd, diagonalFwdJump, 1);

        regDiagonalBwd = "(?=(S\\w{" + diagonalBwdJump + "}A\\w{" + diagonalBwdJump + "}M)).";
        patternDiagonalBwd = Pattern.compile(regDiagonalBwd);
        matcherBwd = patternDiagonalBwd.matcher(singleLine);
        fillAPositions(initialSize, backwardsAs, matcherBwd, diagonalBwdJump, -1);
    }

    private static void fillAPositions(int initialSize, List<Integer> aPositions, Matcher matcher, int jump, int idxDelta) {
        while (matcher.find()) {
            boolean shouldIgnore = false;
            int idx = matcher.start() % initialSize;
            for (int i = matcher.start() + jump + 1; i < matcher.start() + 2 * jump + 3; i += jump + 1) {
                int currentIdx = i % initialSize;
                if (currentIdx != idx + idxDelta) {
                    shouldIgnore = true; // skip line jump
                }
                idx = i % initialSize;
            }
            if (!shouldIgnore) {
                aPositions.add(matcher.start() + jump + 1);
            }
        }
    }
}
