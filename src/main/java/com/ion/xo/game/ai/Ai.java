package com.ion.xo.game.ai;

import com.ion.xo.game.Cell;
import com.ion.xo.game.Commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.ion.xo.game.Commons.BOTTOM;
import static com.ion.xo.game.Commons.LEFT;
import static com.ion.xo.game.Commons.TOP;
import static com.ion.xo.game.Commons.RIGHT;

public class Ai {

    String keyPrefix = "b";
    public Ai() {
    }

    public void callAi(Cell[][] boardCells, int user) {
        boolean run = true;
        boolean force = false;
        while(run) {
            int result = generateMove(boardCells, user, force);
            if (result == 1 ) {
                // clicked and closed a box
                run = true;
            } else if (result == 0) {
                // just clicked a border
                run = false;
            } else if (result == 2 && !force) {
                // something not roght. Force the operation on next round
                force = true;
            }  else {
                run = false;
            }
        }
    }

    public int generateMove(Cell[][] boardCells, int user, boolean force) {
        Map<String, List<Cell>> cellsBordersAvailableMap = new HashMap<String, List<Cell>>();
        for (int i = 0; i < boardCells.length; i++) {
            for (int j = 0; j < boardCells[0].length; j++) {
                int availableBorders = boardCells[i][j].getUnclickedBordersCount();
                String mapKey = keyPrefix + availableBorders;
                if (!cellsBordersAvailableMap.containsKey(mapKey)) {
                    cellsBordersAvailableMap.put(mapKey, new ArrayList<Cell>());
                }
                cellsBordersAvailableMap.get(mapKey).add(boardCells[i][j]);
            }
        }

        Random rand = new Random();
        if (cellsBordersAvailableMap.containsKey(keyPrefix + 1)) {

            List<Cell> list = cellsBordersAvailableMap.get(keyPrefix + 1);
            Cell selectedCell = list.get(rand.nextInt(list.size()));
            List<String> cellUnclickedBorders = selectedCell.getUnclickedBorders();
            if (cellUnclickedBorders.size() == 1) {
                return click_column(boardCells, selectedCell.getRow(), selectedCell.getColumn(), user, cellUnclickedBorders.get(0));
            }
        }
        if (cellsBordersAvailableMap.containsKey(keyPrefix + 4)) {
            int result = reviewBorders(cellsBordersAvailableMap, rand, boardCells, user, 4, force);
            if (result == 1 || result == 0) {
                return result;
            }
        }
        if (cellsBordersAvailableMap.containsKey(keyPrefix + 3)) {
            int result = reviewBorders(cellsBordersAvailableMap, rand, boardCells, user, 3, force);
            if (result == 1 || result == 0) {
                return result;
            }
        }
        if (cellsBordersAvailableMap.containsKey(keyPrefix + 2)) {
            return reviewBorders(cellsBordersAvailableMap, rand, boardCells, user, 2, force);
        }
        return 2;
    }

    private int reviewBorders (Map<String, List<Cell>> cellsBordersAvailableMap,
                                   Random rand,
                                   Cell[][] boardCells,
                                   int user,
                                   int key,
                                   boolean force) {
            List<Cell> reviedCells = new ArrayList<>();
            List<Cell> list = cellsBordersAvailableMap.get(keyPrefix + key);
            boolean flag1 = true;
            if (list.size() > 0) {
                while (flag1) {
                    Cell selectedCell = list.get(rand.nextInt(list.size()));
                    if (!reviedCells.contains(selectedCell)) {
                        reviedCells.add(selectedCell);
                        List<String> cellUnclickedBorders = selectedCell.getUnclickedBorders();
                        List<String> reviewedBorders = new ArrayList<>();
                        boolean flag2 = true;
                        if (cellUnclickedBorders.size() > 0) {
                            while (flag2) {
                                String cellRandomBorder = cellUnclickedBorders.get(rand.nextInt(cellUnclickedBorders.size()));
                                if (!reviewedBorders.contains(cellRandomBorder)) {
                                    reviewedBorders.add(cellRandomBorder);

                                    if (isNeighbourCellClear(boardCells, selectedCell.getRow(),
                                            selectedCell.getColumn(), cellRandomBorder) || force) {
                                        return click_column(boardCells,
                                                selectedCell.getRow(),
                                                selectedCell.getColumn(),
                                                user,
                                                cellRandomBorder);
                                    } else if (key <= 2) {
                                        return click_column(boardCells,
                                                selectedCell.getRow(),
                                                selectedCell.getColumn(),
                                                user,
                                                cellRandomBorder);
                                    }
                                }
                                if (cellUnclickedBorders.size() == reviewedBorders.size()) {
                                    flag2 = false;
                                }
                            }
                        }
                    }
                    if (reviedCells.size() == list.size()) {
                        flag1 = false;
                    }
                }
            }
            return 2;
    }

    private boolean isNeighbourCellClear(Cell[][] boardCells, int row, int column, String border) {
        if (border.equals(TOP) && (row - 1) >= 0) {
            return boardCells[row - 1][column].getUnclickedBordersCount() > 2;
        } else if (border.equals(BOTTOM) && (row + 1) < boardCells.length) {
            return boardCells[row + 1][column].getUnclickedBordersCount() > 2;
        } else if (border.equals(LEFT) && (column - 1) >= 0) {
            return boardCells[row][column - 1].getUnclickedBordersCount() > 2;
        } else if (border.equals(RIGHT) && (column + 1) < boardCells[0].length) {
            return boardCells[row][column + 1].getUnclickedBordersCount() > 2;
        }
        return true;
    }

    private int click_column(Cell[][] boardCells, int row, int column, int currentUser, String border) {
        int check1 = boardCells[row][column].setBorderValueAndCheck(border, currentUser, currentUser);
        int check2 = Commons.setNeighbourCellsBorderValueIfRequired(boardCells, border, row, column, currentUser);
        if (check1 == 1 || check2 == 1) {
            return 1;
        } else if (check1 == 0 || check2 == 0) {
            return 0;
        } else {
            return 2;
        }
    }
}
