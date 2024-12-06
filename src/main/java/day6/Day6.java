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

        // Travel
        Direction direction = Direction.UP;
        try {
            while (true) {
                Coord next = position.applyDirection(direction);
                if (map[next.y][next.x] == '#') {
                    direction = shiftDirection(direction);
                } else {
                    visitedPositions.add(next);
                    map[next.y][next.x] = 'X';
                    position = next;
                }
            }
        } catch (ArrayIndexOutOfBoundsException oups) {
            /*for (int j = 0; j < nbRows; j++) {
                for (int i = 0; i < lineLength; i++) {
                    System.out.print(map[j][i]);
                }
                System.out.println();
            }*/
            System.out.println(visitedPositions.size());
        }

        // Fail 4647
    }

    enum Direction {
        UP(0, -1), RIGHT(1, 0), LEFT(-1, 0), DOWN(0, 1);

        Coord jump;

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

    private Coord applyDirection(Coord coord, Direction direction) {
        return new Coord(coord.x + direction.jump.x, coord.y + direction.jump.y);
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
