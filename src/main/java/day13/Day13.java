package day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day13 {

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day13/input.txt");
        List<String> lines = Files.readAllLines(inputPath);

        List<Game> games = new ArrayList<>();
        for (int i = 0; i < lines.size(); i += 4) {
            String[] content = lines.get(i).split(":")[1].split(",");
            Machine machineA = new Machine(Integer.parseInt(content[0].split("\\+")[1]),
                    Integer.parseInt(content[1].split("\\+")[1]),
                    3);

            content = lines.get(i + 1).split(":")[1].split(",");
            Machine machineB = new Machine(Integer.parseInt(content[0].split("\\+")[1]),
                    Integer.parseInt(content[1].split("\\+")[1]),
                    1);

            content = lines.get(i + 2).split(":")[1].split(",");
            Prize prize = new Prize(Integer.parseInt(content[0].split("=")[1]) + 10000000000000L,
                    Integer.parseInt(content[1].split("=")[1]) + 10000000000000L);

            games.add(new Game(machineA, machineB, prize));
        }

        long sum = 0;
        for (Game game : games) {
            sum += solve(game);
        }
        System.out.println(sum); // 37680
        // part 2 87550094242995
    }

    private static long solve(Game game) {
        Machine ma = game.machineA;
        Machine mb = game.machineB;
        Prize prize = game.prize;

        /**
         * Button A: X+94, Y+34
         * Button B: X+22, Y+67
         * Prize: X=8400, Y=5400
         *
         * 94 + 22b = 8400
         * 34 + 67b = 5400
         */

        double[][] coeff = {
                {ma.x, mb.x},
                {ma.y, mb.y}
        };

        double[] results = {prize.x, prize.y};

        double determinant = determinant(coeff);
        if (determinant == 0) {
            return 0; // no solutions
        }

        double deterA = determinant(new double[][] {
                {results[0], coeff[0][1]},
                {results[1], coeff[1][1]}
        });

        double deterB = determinant(new double[][] {
                {coeff[0][0], results[0]},
                {coeff[1][0], results[1]}
        });

        double a = deterA / determinant;
        double b = deterB / determinant;

        if (a % 1 == 0 && b % 1 == 0 /*&& a < 100 && b < 100*/) { // only full values
            return (long) (3 * a + b);
        }
        return 0L;
    }

    /**
     * a * d - b * c, cramer's rule.
     */
    private static double determinant(double[][] matrix) {
        return (matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]);
    }

    record Machine(int x, int y, int cost) {}

    record Prize(long x, long y) {}

    record Game(Machine machineA, Machine machineB, Prize prize) {}
}
