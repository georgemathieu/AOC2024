package day12;

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

public class Day12 {

    private static MapComponent mc;

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day12/input.txt");
        List<String> lines = Files.readAllLines(inputPath);
        mc = new MapComponent(lines.get(0).length(), lines.size());

        Map<Integer, Set<Coordinate>> map = new HashMap<>();
        char[][] chars = mc.asChars(lines);
        boolean[][] checked = new boolean[mc.nbColumns()][mc.nbRows()];

        int cpt = 0;
        for (int j = 0; j < mc.nbRows(); j++) {
            for (int i = 0; i < mc.nbColumns(); i++) {
                if (!checked[j][i]) {
                    cpt++;
                    char c = lines.get(j).charAt(i);
                    checked[j][i] = true;
                    Coordinate coordinate = new Coordinate(i, j);
                    Set<Coordinate> coordinates = new HashSet<>();
                    coordinates.add(coordinate);

                    goDown(coordinate, chars, c, checked, coordinates);

                    map.put(cpt, coordinates);
                }
            }
        }

        long area = 0;
        long perimeter = 0;
        long total = 0;
        for (Map.Entry<Integer, Set<Coordinate>> group : map.entrySet()) {
            Set<Coordinate> coordinates = group.getValue();

            area = computeArea(coordinates);
            perimeter = computePerimeter(group, chars);
            total += area * perimeter;
        }
        System.out.println(total); // 1573474
    }

    private static void goDown(Coordinate coordinate, char[][] chars, char c, boolean[][] checked, Set<Coordinate> coordinates) {
        for (Direction vector : Direction.values()) {
            Coordinate next = coordinate.apply(vector);
            if (mc.isValid(next) && chars[next.y()][next.x()] == c && !checked[next.y()][next.x()]) {
                coordinates.add(next);
                checked[next.y()][next.x()] = true;
                goDown(next, chars, c, checked, coordinates);
            }
        }
    }

    private static long computeArea(Set<Coordinate> coordinates) {
        return coordinates.size();
    }

    private static long computePerimeter(Map.Entry<Integer, Set<Coordinate>> group, char[][] charMap) {
        long perimeter = 0L;

        Set<Coordinate> coordinates = group.getValue();

        int groupCount = 0;
        for (Coordinate coordinate : coordinates) {
            boolean isSameGroup = coordinates.size() == 1;
            for (Direction vector : Direction.values()) {
                Coordinate next = coordinate.apply(vector);
                if (!mc.isValid(next) || charMap[next.y()][next.x()] != charMap[coordinate.y()][coordinate.x()]) {
                    perimeter++;
                } else {
                    isSameGroup = true;
                }
            }
            if (!isSameGroup) groupCount++;
        }
        return groupCount != 0 ? perimeter / groupCount : perimeter;
    }
}
