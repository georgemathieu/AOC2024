package map;

/**
 * Directions in a map with their given vector, no diagonals.
 */
public enum Direction {
    UP(0, -1), RIGHT(1, 0), LEFT(-1, 0), DOWN(0, 1);

    final Coordinate vector;

    Direction(int x, int y) {
        this.vector = new Coordinate(x, y);
    }
}