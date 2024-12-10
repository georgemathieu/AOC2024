package map;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MapComponent<T> {

    private final int nbColumns;
    private final int nbRows;
    private T[][] innerMap;

    public MapComponent(int nbColumns, int nbRows) {
        this.nbColumns = nbColumns;
        this.nbRows = nbRows;
    }

    public void processInput(List<String> lines) {

    }

    /*private Function<String, T> getMappingFunction(Class<T> clazz) {
        switch (clazz) {
            case Integer.class:
                return Integer::parseInt;
            case Character.class:
                return Character::getNumericValue;

        }

    }*/


    /**
     * Returns true if a coordinate is within bounds.
     */
    public boolean isValid(Coordinate coordinate) {
        return coordinate.x >= 0 && coordinate.x < nbColumns && coordinate.y >= 0 && coordinate.y < nbRows;
    }

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

    /**
     * Record to use coordinates in a 2d-array with x and y coordinates.
     * @param x the x coordinate (start at 0), horizontal axis
     * @param y the y coordinate (starts at 0), vertical axis
     */
    public record Coordinate(int x, int y) {

        Coordinate apply(Direction direction) {
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

    public int getNbColumns() {
        return nbColumns;
    }

    public int getNbRows() {
        return nbRows;
    }
}
