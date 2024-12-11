package day10;

import map.Coordinate;
import map.Direction;
import map.MapComponent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day10 {

    private static MapComponent mc;

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day10/input.txt");
        List<String> lines = Files.readAllLines(inputPath);

        mc = new MapComponent(lines.get(0).length(), lines.size());
        int[][] map = mc.asInts(lines);

        // Part 1
        Map<Coordinate, Set<Coordinate>> headsMap = new HashMap<>();
        for (int j = 0; j < mc.nbRows(); j++) {
            for (int i = 0; i < mc.nbColumns(); i++) {
                final int nb = map[j][i];
                if (nb == 0) {
                    Coordinate start = new Coordinate(i, j);
                    headsMap.put(start, getHeads(map, start, nb, new HashSet<>()));
                }
            }
        }

        int sum = headsMap.values().stream().mapToInt(Set::size).sum();
        System.out.println(sum); // 698

        // Part 2
        Map<Coordinate, Integer> pathsMap = new HashMap<>();
        for (int j = 0; j < mc.nbRows(); j++) {
            for (int i = 0; i < mc.nbColumns(); i++) {
                final int nb = map[j][i];
                if (nb == 0) {
                    Coordinate start = new Coordinate(i, j);
                    pathsMap.put(start, getPaths(map, start, nb, 0));
                }
            }
        }
        sum = pathsMap.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println(sum); // 1436

    }

    private static int getPaths(int[][] map, Coordinate start, int currentValue, int sum) {
        if (currentValue == 9) {
            return sum + 1;
        }

        for (Direction vector : Direction.values()) {
            Coordinate next = start.apply(vector);
            if (mc.isValid(next) && map[next.y()][next.x()] == currentValue + 1) {
                sum = getPaths(map, next, currentValue + 1, sum);
            }

        }
        return sum;
    }

    private static Set<Coordinate> getHeads(int[][] map, Coordinate start, int currentValue, Set<Coordinate> set) {
        if (currentValue == 9) {
            set.add(start);
            return set;
        }

        for (Direction vector : Direction.values()) {
            Coordinate next = start.apply(vector);
            if (mc.isValid(next) && map[next.y()][next.x()] == currentValue + 1) {
                set = getHeads(map, next, currentValue + 1, set);
            }

        }
        return set;
    }
}
