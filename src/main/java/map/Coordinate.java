package map;

import java.util.Objects;

/**
 * Record to use coordinates in a 2d-array with x and y coordinates.
 * @param x the x coordinate (start at 0), horizontal axis
 * @param y the y coordinate (starts at 0), vertical axis
 */
public record Coordinate(int x, int y) {

    public Coordinate apply(Direction direction) {
        return new Coordinate(this.x + direction.vector.x, this.y + direction.vector.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate coordinate = (Coordinate) o;
        return x == coordinate.x && y == coordinate.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
