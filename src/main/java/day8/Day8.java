package day8;

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

public class Day8 {

    private static int nbColumns = 0;
    private static int nbRows = 0;

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day8/input.txt");
        List<String> lines = Files.readAllLines(inputPath);

        nbColumns = lines.get(0).length();
        nbRows = lines.size();

        // Init map
        Map<Character, Set<Coord>> antennas = new HashMap<>();
        for (int j = 0; j < nbRows; j++) {
            for (int i = 0; i < nbColumns; i++) {
                char c = lines.get(j).charAt(i);
                if (c == '.') continue;
                antennas.computeIfAbsent(c, k -> new HashSet<>()).add(new Coord(i, j));
            }
        }

        int count = getNbNodes_Part1(antennas);
        // 220 too high
        // 214
        System.out.println(count);

        // Part 2
        count = getNbNodes_Part2(antennas);
        // 809
        System.out.println(count);
    }

    private static int getNbNodes_Part2(Map<Character, Set<Coord>> antennas) {
        Set<Coord> nodes = new HashSet<>();
        for (Map.Entry<Character, Set<Coord>> antenna : antennas.entrySet()) {
            Set<Coord> antennaGroup = antenna.getValue();
            for (Coord coordA : antennaGroup) {
                for (Coord coordB : antennaGroup) {
                    if (coordA.equals(coordB)) continue;
                    if (antennaGroup.size() > 1) {
                        nodes.add(coordA);
                        nodes.add(coordB);
                    }
                    final int gapX = coordB.x - coordA.x;
                    final int gapY = coordB.y - coordA.y;
                    addAllPossibleNodes(new Coord(coordA.x - gapX, coordA.y - gapY), nodes, -gapX, -gapY);
                    // addAllPossibleNodes(new Coord(coordB.x + gapX, coordB.y + gapY), nodes, gapX, gapY);
                }
            }
        }
        return nodes.size();
    }

    private static void addAllPossibleNodes(Coord next, Set<Coord> nodes, int gapX, int gapY) {
        while (coordIsValid(next)) {
            nodes.add(next);
            next = new Coord(next.x + gapX, next.y + gapY);
        }
    }

    private static int getNbNodes_Part1(Map<Character, Set<Coord>> antennas) {
        Set<Coord> nodes = new HashSet<>();
        for (Map.Entry<Character, Set<Coord>> antenna : antennas.entrySet()) {
            Set<Coord> antennaGroup = antenna.getValue();
            for (Coord coordA : antennaGroup) {
                for (Coord coordB : antennaGroup) {
                    if (coordA.equals(coordB)) continue;
                    final int gapX = coordB.x - coordA.x;
                    final int gapY = coordB.y - coordA.y;
                    addIfValid(nodes, new Coord(coordA.x - gapX, coordA.y - gapY));
                    addIfValid(nodes, new Coord(coordB.x + gapX, coordB.y + gapY));
                }
            }
        }
        return nodes.size();
    }

    private static boolean coordIsValid(Coord antenna) {
        return antenna.x >= 0 && antenna.x < nbColumns && antenna.y >= 0 && antenna.y < nbRows;
    }

    private static void addIfValid(Set<Coord> nodes, Coord antenna) {
        if (antenna.x < 0 || antenna.x >= nbColumns || antenna.y < 0 || antenna.y >= nbRows) {
            return; // invalid
        }
        nodes.add(antenna);
    }

    record Coord(int x, int y) {

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
