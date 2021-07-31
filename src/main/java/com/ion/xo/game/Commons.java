package com.ion.xo.game;

public class Commons {
    public static final String TOP = "top";
    public static final String BOTTOM = "bottom";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final int CELL_DIMENTIONS = 8; // in mm
    private static final int EXCLUDED_ID = 4;

    public Commons() {

    }

    public static int setNeighbourCellsBorderValueIfRequired(Cell[][] boardCells, String border, int row, int column, int currentUser) {
        if (row - 1 >= 0 && border.equals(TOP)) {
            return boardCells[row - 1][column].setBorderValueAndCheck(BOTTOM, EXCLUDED_ID, currentUser);
        } else if (row + 1 < boardCells.length && border.equals(BOTTOM)) {
            return boardCells[row + 1][column].setBorderValueAndCheck(TOP, EXCLUDED_ID, currentUser);
        } else if (column - 1 >= 0 && border.equals(LEFT)) {
            return boardCells[row][column - 1].setBorderValueAndCheck(RIGHT, EXCLUDED_ID, currentUser);
        } else if (column + 1 < boardCells[0].length && border.equals(RIGHT)) {
            return boardCells[row][column + 1].setBorderValueAndCheck(LEFT, EXCLUDED_ID, currentUser);
        }
        return 2;
    }
}
