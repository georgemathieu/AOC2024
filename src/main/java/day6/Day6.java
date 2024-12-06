package day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day6 {

    private static Set<Coord> visitedPositions = new HashSet<>();

    private static final int LOOP_LIMIT = 20_000;
    private static final char OBSTACLE = '#';
    private static final char CRATE = 'O';
    private static final char EMPTY = '.';

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day6/input.txt");
        List<String> lines = Files.readAllLines(inputPath);

        Coord position = new Coord(-1, -1);
        final int lineLength = lines.get(0).length();
        final int nbRows = lines.size();

        // Init map
        char[][] map = new char[lineLength][nbRows];
        for (int j = 0; j < nbRows; j++) {
            for (int i = 0; i < lineLength; i++) {
                char c = lines.get(j).charAt(i);
                map[j][i] = lines.get(j).charAt(i);
                if (c == '^') {
                    position = new Coord(i, j);
                    visitedPositions.add(position);
                }
            }
        }

        // Part 1
        Direction direction = Direction.UP;
        try {
            travelMap(position, direction, map);
        } catch (ArrayIndexOutOfBoundsException oups) {
            // debug(nbRows, lineLength, map);
            // 4647
            System.out.println(visitedPositions.size());
            // System.out.println(nbSteps);
        }

        // Part 2
        int count = 0;
        for (int j = 0; j < nbRows; j++) {
            for (int i = 0; i < lineLength; i++) {
                if (map[j][i] != EMPTY) continue;
                map[j][i] = CRATE; // Put obstacle

                try {
                    travelMap(position, direction, map);

                    // No exception after 20k steps = no solution => loop
                    count ++;
                } catch (ArrayIndexOutOfBoundsException oups) {
                }
                map[j][i] = EMPTY; // Remove obstacle
            }
        }
        // 1723
        System.out.println(count);
    }

    private static void travelMap(Coord position, Direction direction, char[][] map) {
        long nbSteps = 0;
        while (true) {
            Coord next = position.applyDirection(direction);
            if (map[next.y][next.x] == OBSTACLE || map[next.y][next.x] == CRATE) {
                direction = shiftDirection(direction);
            } else {
                visitedPositions.add(next);
                // map[next.y][next.x] = 'X';
                position = next;
                nbSteps++;
            }

            if (nbSteps > LOOP_LIMIT) {
                return;
            }
        }
    }

    private static void debug(int nbRows, int lineLength, char[][] map) {
        for (int j = 0; j < nbRows; j++) {
            for (int i = 0; i < lineLength; i++) {
                System.out.print(map[j][i]);
            }
            System.out.println();
        }
    }

    enum Direction {
        UP(0, -1), RIGHT(1, 0), LEFT(-1, 0), DOWN(0, 1);

        final Coord jump;

        Direction(int x, int y) {
            this.jump = new Coord(x, y);
        }
    }

    private static Direction shiftDirection(Direction direction) {
        return switch (direction) {
            case UP -> Direction.RIGHT;
            case RIGHT -> Direction.DOWN;
            case DOWN -> Direction.LEFT;
            case LEFT -> Direction.UP;
        };
    }

    record Coord(int x, int y) {

        Coord applyDirection(Direction direction) {
            return new Coord(this.x + direction.jump.x, this.y + direction.jump.y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coord coord = (Coord) o;
            return x == coord.x && y == coord.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
