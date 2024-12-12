package day12;

import map.Coordinate;
import map.Direction;
import map.MapComponent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day12 {

    private static MapComponent mc;

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day12/input.txt");
        List<String> lines = Files.readAllLines(inputPath);
        mc = new MapComponent(lines.get(0).length(), lines.size());

        Map<Character, Set<Coordinate>> map = mc.mapByValue(lines);
        char[][] chars = mc.asChars(lines);

        long area = 0;
        long perimeter = 0;
        long total = 0;
        for (Map.Entry<Character, Set<Coordinate>> group : map.entrySet()) {
            Set<Coordinate> coordinates = group.getValue();

            area = computeArea(coordinates);
            perimeter = computePerimeter(group, chars);
            total += area * perimeter;
        }
        System.out.println(total);
    }

    private static long computeArea(Set<Coordinate> coordinates) {
        return coordinates.size();
    }

    private static long computePerimeter(Map.Entry<Character, Set<Coordinate>> group, char[][] charMap) {
        long perimeter = 0L;

        Set<Coordinate> coordinates = group.getValue();
        Character key = group.getKey();

        int groupCount = 0;
        for (Coordinate coordinate : coordinates) {
            boolean isSameGroup = coordinates.size() == 1;
            for (Direction vector : Direction.values()) {
                Coordinate next = coordinate.apply(vector);
                if (!mc.isValid(next) || charMap[next.y()][next.x()] != key) {
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
