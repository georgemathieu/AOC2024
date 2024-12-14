package day14;

import map.Coordinate;
import map.Direction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day14 {

    private static final int nbColumns = 101;
    private static final int nbRows = 103;
    private static final int nbSeconds = 100;

    public static void main(String[] args) throws IOException {
        Path inputPath = Paths.get("src/main/java/day14/input.txt");
        List<String> lines = Files.readAllLines(inputPath);

        Set<Robot> robots = new HashSet<>();
        for (String line : lines) {
            String[] content = line.split(" ");

            String[] position = content[0].split("=")[1].split(",");
            Coordinate coordinate = new Coordinate(Integer.parseInt(position[0]), Integer.parseInt(position[1]));

            String[] velocity = content[1].split("=")[1].split(",");
            Vector vector = new Vector(Integer.parseInt(velocity[0]), Integer.parseInt(velocity[1]));

            robots.add(new Robot(coordinate, vector));
        }

        // Part 2
        char[][] map = new char[nbRows][nbColumns];
        for (int j = 0; j < nbRows; j++) {
            for (int i = 0; i < nbColumns; i++) {
                map[j][i] = '.';
            }
        }
        for (Robot robot : robots) {
            map[robot.position.y()][robot.position.x()] = 'X';
        }

        print(map);

        final int middleX = nbColumns / 2;
        final int middleY = nbRows / 2;

        File file = new File("src/main/java/day14/res.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < 10_000; i++) {
                for (Robot robot : robots) {
                    map[robot.position.y()][robot.position.x()] = '.';
                    robot.position = apply(robot.position, robot.velocity);
                    map[robot.position.y()][robot.position.x()] = 'X';
                }
                writer.newLine();
                writer.write("" + (i + 1));
                writer.newLine();
                for (char[] row : map) {
                    writer.write(row);
                    writer.newLine();
                }

                if (i == 99) {
                    long q1 = 0; // 0-50x, 0-50y
                    long q2 = 0; // 50-100x, 0-50y
                    long q3 = 0; // 0-50x, 50-100y
                    long q4 = 0; // 50-100x, 50-100y

                    for (Robot robot : robots) {
                        Coordinate position = robot.position;
                        if (position.x() < middleX && position.y() < middleY) {
                            q1++;
                        } else if (position.x() > middleX && position.y() < middleY) {
                            q2++;
                        } else if (position.x() < middleX && position.y() > middleY) {
                            q3++;
                        } else if (position.x() > middleX && position.y() > middleY) {
                            q4++;
                        }
                    }
                    System.out.println(q1 * q2 * q3 * q4); // 217328832 for 100s
                }
            }

        }
    }

    private static Coordinate apply(Coordinate coordinate, Vector direction) {
        int nextX = (coordinate.x() + direction.x) % nbColumns;
        if (nextX < 0) {
            nextX += nbColumns;
        }

        int nextY = (coordinate.y() + direction.y) % nbRows;
        if (nextY < 0) {
            nextY += nbRows;
        }

        return new Coordinate(nextX, nextY);
    }

    private static class Robot{
        Coordinate position;
        Vector velocity;

        public Robot(Coordinate coordinate, Vector vector) {
            this.position = coordinate;
            this.velocity = vector;
        }

        @Override
        public String toString() {
            return "Robot{" + "position=" + position + ", velocity=" + velocity + '}';
        }
    }

    private record Vector(int x, int y) {}

    private static void print(char[][] map) {
        for (int j = 0; j < nbRows; j++) {
            for (int i = 0; i < nbColumns; i++) {
                System.out.print(map[j][i]);
            }
            System.out.println();
        }
    }
}
