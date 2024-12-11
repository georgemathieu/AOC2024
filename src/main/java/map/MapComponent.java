package map;

import java.util.List;

public record MapComponent(int nbColumns, int nbRows) {

    /**
     * Transforms a list of strings to a 2d-array of integers.
     * @param lines a given list of strings
     */
    public int[][] asInts(List<String> lines) {
        int[][] map = new int[nbColumns][nbRows];
        for (int j = 0; j < nbRows; j++) {
            for (int i = 0; i < nbColumns; i++) {
                map[j][i] = Character.getNumericValue(lines.get(j).charAt(i));
            }
        }
        return map;
    }

    /**
     * Transforms a list of strings to a 2d-array of chars.
     * @param lines a given list of strings
     */
    public char[][] asChars(List<String> lines) {
        char[][] map = new char[nbColumns][nbRows];
        for (int j = 0; j < nbRows; j++) {
            for (int i = 0; i < nbColumns; i++) {
                map[j][i] = lines.get(j).charAt(i);
            }
        }
        return map;
    }

    /**
     * Returns true if a coordinate is within bounds.
     */
    public boolean isValid(Coordinate coordinate) {
        return coordinate.x() >= 0 && coordinate.x() < nbColumns && coordinate.y() >= 0 && coordinate.y() < nbRows;
    }
}
