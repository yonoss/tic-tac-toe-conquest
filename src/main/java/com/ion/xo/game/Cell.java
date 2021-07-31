package com.ion.xo.game;

import java.util.ArrayList;
import java.util.List;

import static com.ion.xo.game.Commons.*;

public class Cell{
    private int row = -1;
    private int column = -1;
    private int topBorder = 3;
    private int bottomBorder = 3;
    private int leftBorder = 3;
    private int rightBorder = 3;
    private int checked = 0;

    public Cell(int row, int column){
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setTopBorder(int topBorder) {
        this.topBorder = topBorder;
    }

    public int getTopBorder() {
        return topBorder;
    }

    public void setBottomBorder(int bottomBorder) {
        this.bottomBorder = bottomBorder;
    }

    public int getBottomBorder() {
        return bottomBorder;
    }

    public void setLeftBorder(int leftBorder) {
        this.leftBorder = leftBorder;
    }

    public int getLeftBorder() {
        return leftBorder;
    }

    public void setRightBorder(int rightBorder) {
        this.rightBorder = rightBorder;
    }

    public int getRightBorder() {
        return rightBorder;
    }

    public void setChecked(int checked) {
        checked = checked;
    }

    public int getChecked() {
        return checked;
    }

    public boolean isChecked() {
        return checked > 0;
    }

    public boolean canCheck() {
        return topBorder != 3 && bottomBorder != 3 && leftBorder != 3 && rightBorder != 3 && checked == 0;
    }

    public int getBorderValue(String border) {
        if (border.equals(TOP)) {
            return topBorder;
        } else if (border.equals(BOTTOM)) {
            return bottomBorder;
        } else if (border.equals(LEFT)) {
            return leftBorder;
        } else {
            return rightBorder;
        }
    }

    public int setBorderValueAndCheck(String border, int borderUser, int checkedUser) {
        if (border.equals(TOP)) {
            topBorder = borderUser;
        } else if (border.equals(BOTTOM)) {
            bottomBorder = borderUser;
        } else if (border.equals(LEFT)) {
            leftBorder = borderUser;
        } else {
            rightBorder = borderUser;
        }
        if (canCheck()) {
            checked = checkedUser;
            return 1;
        }
        return 0;
    }

    public int getUnclickedBordersCount() {
        if (checked > 0) {
            return 0;
        }

        int cnt = 0;
        if (topBorder == 3) {
            cnt++;
        }
        if (bottomBorder == 3) {
            cnt++;
        }
        if (leftBorder == 3) {
            cnt++;
        }
        if (rightBorder == 3) {
            cnt++;
        }
        return cnt;
    }

    public List<String> getUnclickedBorders() {
        List<String> result = new ArrayList<>();
        if (topBorder == 3) {
            result.add(TOP);
        }
        if (bottomBorder == 3) {
            result.add(BOTTOM);
        }
        if (leftBorder == 3) {
            result.add(LEFT);
        }
        if (rightBorder == 3) {
            result.add(RIGHT);
        }
        return result;
    }
}