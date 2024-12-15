package day15;

import map.Coordinate;
import map.Direction;
import map.MapComponent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day15 {

    private static final char WALL = '#';
    private static final char BOX = 'O';
    private static final char ROBOT = '@';
    private static final char EMPTY = '.';

    private static int nbColumns = 0;
    private static int nbRows = 0;
    private static Coordinate robot = null;

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day15/input.txt");
        List<String> lines = Files.readAllLines(inputPath);

        nbColumns = lines.get(0).length();
        List<Instruction> instructions = new ArrayList<>();
        // Init instructions
        for (String line : lines) {
            if (line.startsWith("" + WALL)) {
                nbRows++;
            } else {
                for (int i = 0; i < line.length(); i++) {
                    Instruction instruction = Instruction.toEnum(line.charAt(i));
                    if (instruction != null) instructions.add(instruction);
                }
            }
        }

        MapComponent mc = new MapComponent(nbColumns, nbRows);
        char[][] map = mc.asChars(lines);

        for (int j = 0; j < nbRows; j++) {
            for (int i = 0; i < nbColumns; i++) {
                if (lines.get(j).charAt(i) == ROBOT) robot = new Coordinate(i, j);
            }
        }

        // Process instructions
        for (Instruction instruction : instructions) {
            int movableItems = getNbMovable(robot, instruction.vector, map, 0);
            moveItems(robot, instruction.vector, map, movableItems);
        }

        // Get boxes positions
        Set<Coordinate> boxes = new HashSet<>();
        for (int j = 0; j < nbRows; j++) {
            for (int i = 0; i < nbColumns; i++) {
                if (map[j][i] == BOX) {
                    boxes.add(new Coordinate(i, j));
                }
            }
        }

        long result = 0L;
        for (Coordinate box : boxes) {
            result += box.x() + 100L * box.y();
        }
        System.out.println(result); // Part 1 1360570
    }

    private static void moveItems(Coordinate coordinate, Direction vector, char[][] map, int movableItems) {
        Coordinate next = coordinate.apply(vector);
        for (int i = 0; i <= movableItems; i++) {
            if (i == 0) {
                map[next.y()][next.x()] = ROBOT;
                map[coordinate.y()][coordinate.x()] = EMPTY;
                robot = next;
            } else {
                map[next.y()][next.x()] = BOX;
            }
            next = next.apply(vector);
        }
    }

    private static int getNbMovable(Coordinate coordinate, Direction vector, char[][] map, int movableItems) {
        if (map[coordinate.y()][coordinate.x()] == EMPTY) {
            return movableItems;
        } else if (map[coordinate.y()][coordinate.x()] == WALL) {
            return -1;
        } else if (map[coordinate.y()][coordinate.x()] == BOX) {
            movableItems++;
        }
        Coordinate next = coordinate.apply(vector);
        return getNbMovable(next, vector, map, movableItems);
    }

    private enum Instruction {
        UP(Direction.UP),
        LEFT(Direction.LEFT),
        RIGHT(Direction.RIGHT),
        DOWN(Direction.DOWN);

        private final Direction vector;

        Instruction(Direction direction) {
            this.vector = direction;
        }

        public static Instruction toEnum(char c) {
            return switch (c) {
                case '^' -> UP;
                case '<' -> LEFT;
                case '>' -> RIGHT;
                case 'v' -> DOWN;
                default -> null;
            };
        }
    }
}
