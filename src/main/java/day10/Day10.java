package day10;

import map.MapComponent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day10 {

    private static int nbColumns = 0;
    private static int nbRows = 0;

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day10/input.txt");
        List<String> lines = Files.readAllLines(inputPath);

        nbColumns = lines.get(0).length();
        nbRows = lines.size();

        // Init map
        int[][] map = new int[nbColumns][nbRows];
        for (int j = 0; j < nbRows; j++) {
            for (int i = 0; i < nbColumns; i++) {
                map[j][i] = Character.getNumericValue(lines.get(j).charAt(i));
            }
        }

        // Part 1
        Map<Coord, Set<Coord>> headsMap = new HashMap<>();
        for (int j = 0; j < nbRows; j++) {
            for (int i = 0; i < nbColumns; i++) {
                final int nb = map[j][i];
                if (nb == 0) {
                    Coord start = new Coord(i, j);
                    headsMap.put(start, getHeads(map, start, nb, new HashSet<>()));
                }
            }
        }

        int sum = headsMap.values().stream().mapToInt(Set::size).sum();
        System.out.println(sum); // 698

        // Part 2
        Map<Coord, Integer> pathsMap = new HashMap<>();
        for (int j = 0; j < nbRows; j++) {
            for (int i = 0; i < nbColumns; i++) {
                final int nb = map[j][i];
                if (nb == 0) {
                    Coord start = new Coord(i, j);
                    pathsMap.put(start, getPaths(map, start, nb, 0));
                }
            }
        }
        sum = pathsMap.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println(sum); // 1436

    }

    private static int getPaths(int[][] map, Coord start, int currentValue, int sum) {
        if (currentValue == 9) {
            return sum + 1;
        }

        for (Direction vector : Direction.values()) {
            Coord next = start.applyDirection(vector);
            if (isValid(next) && map[next.y][next.x] == currentValue + 1) {
                sum = getPaths(map, next, currentValue + 1, sum);
            }

        }
        return sum;
    }

    private static Set<Coord> getHeads(int[][] map, Coord start, int currentValue, Set<Coord> set) {
        if (currentValue == 9) {
            set.add(start);
            return set;
        }

        for (Direction vector : Direction.values()) {
            Coord next = start.applyDirection(vector);
            if (isValid(next) && map[next.y][next.x] == currentValue + 1) {
                set = getHeads(map, next, currentValue + 1, set);
            }

        }
        return set;
    }

    private static boolean isValid(Coord coord) {
        return coord.x >= 0 && coord.x < nbColumns && coord.y >= 0 && coord.y < nbRows;
    }

    enum Direction {
        UP(0, -1), RIGHT(1, 0), LEFT(-1, 0), DOWN(0, 1);

        final Coord jump;

        Direction(int x, int y) {
            this.jump = new Coord(x, y);
        }
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
