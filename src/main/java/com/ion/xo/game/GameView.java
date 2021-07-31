package com.ion.xo.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.MotionEvent;

import androidx.core.content.res.ResourcesCompat;

import com.ion.xo.FullScreenAdMob;
import com.ion.xo.R;
import com.ion.xo.game.ai.Ai;

import java.util.Random;

import static com.ion.xo.game.Commons.*;
import static com.ion.xo.game.Session.*;

public class GameView extends View {
    private static final int WALL_SIZE = 19;

    private static final int SYSTEM_ID = 0;
    private static final int USER_1_ID = 1;
    private static final int USER_2_ID = 2;
    private static final int UNDEFINED_ID = 3;

    private boolean screenLocked = false;

    private static final int THIN_LINE_STROKE_SIZE = 1;
    private static final int LINE_STROKE_SIZE = 7;
    private static final int TEXT_STROKE_SIZE = 7;

    private static final int BORDER_ZONE = 20;

    private int numColumns, numRows;
    private int cellWidth, cellHeight;

    private Paint[] paints = new Paint[3];
    private Paint[] linePaints = new Paint[4];
    private String[] xo = {"", "X", "O"};
    private int currentUser = USER_1_ID;
    private int xoOffestX = 10;
    private int xoOffestY = 10;
    private int xoSize = 50; // percentage 1 to 100
    private int xoTextSize = 10;

    private Ai ai = null;
    Random rand = new Random();

    // Header
    private int headerHeight, footerHeight, headerWidth;
    private int headerSizeRatio = 6; // percentage 1 to 100
    // private int footerSizeRatio = 10; // percentage 1 to 100
    private Paint[] headerPaints = new Paint[4];
    private int headerTextSize = 10;
    private int HEADER_TEXT_STROKE_SIZE;
    private int headerDotRadius;
    private int headerTextSizeRation = 50; // percentage 1 to 100 of the total header height
    private int headerOffestY = 20;
    private int headerOffestX = 10;
    private int headerMenuWidthSize = 40;
    private int headerMenuWidthSizeRatio = 3; // percentage 1 to 100 of the total header width size

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(getBackgroundImage());
        if (aiEnabled) {
            ai = new Ai();
        }
        initApp();
    }

    private int getBackgroundImage() {
        if (background_id >= 0) {
            return background_id;
        }
        String name = "wall" + rand.nextInt(WALL_SIZE);
        background_id = this.getResources().getIdentifier(name, "drawable", this.getContext().getPackageName());
        return background_id;
    }

    private void initApp() {
        calculateDimensions();

        if (!initialized && numRows > 0 && numColumns > 0) {
            boardCells = initGrid(new Cell[numRows][numColumns]);
            USER_1_SCORE = 0;
            USER_2_SCORE = 0;
            initialized = true;
        }

        initPaints();
    }

    private void initPaints() {
        initGamePaints();
        initLinePaints();
        initHeaderPaints();
    }

    private void initHeaderPaints() {
        Typeface typeface =  ResourcesCompat.getFont(this.getContext(), R.raw.allerta_stencil_regular);

        Paint blackPaint = new Paint();
        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        blackPaint.setStrokeWidth(HEADER_TEXT_STROKE_SIZE);
        blackPaint.setTypeface(typeface);

        Paint redPaint = new Paint();
        //redPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        redPaint.setColor(Color.RED);
        redPaint.setStrokeWidth(HEADER_TEXT_STROKE_SIZE);
        redPaint.setTextSize(headerTextSize);
        redPaint.setTypeface(typeface);

        Paint bluePaint = new Paint();
        bluePaint.setTypeface(typeface);
         // bluePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bluePaint.setStrokeWidth(HEADER_TEXT_STROKE_SIZE);
        bluePaint.setColor(Color.BLUE);
        bluePaint.setTextSize(headerTextSize);

        Paint menuPaint = new Paint();
        menuPaint.setStyle(Paint.Style.FILL);
        menuPaint.setColor(Color.BLACK);
        menuPaint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        menuPaint.setTextSize(headerTextSize);

        headerPaints[SYSTEM_ID] = blackPaint;
        headerPaints[USER_1_ID] = redPaint;
        headerPaints[USER_2_ID] = bluePaint;
        headerPaints[UNDEFINED_ID] = menuPaint;
    }

    private void initGamePaints() {
        Paint redPaint = new Paint();
        redPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        redPaint.setColor(Color.RED);
        redPaint.setStrokeWidth(TEXT_STROKE_SIZE);
        redPaint.setTextSize(xoTextSize);

        Paint bluePaint = new Paint();
        bluePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bluePaint.setColor(Color.BLUE);
        bluePaint.setStrokeWidth(TEXT_STROKE_SIZE);
        bluePaint.setTextSize(xoTextSize);

        paints[USER_1_ID] = redPaint;
        paints[USER_2_ID] = bluePaint;
    }

    private void initLinePaints() {
        Paint blackPaint = new Paint();
        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStrokeWidth(THIN_LINE_STROKE_SIZE);

        Paint lineBlackPaint = new Paint();
        lineBlackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        lineBlackPaint.setStrokeWidth(LINE_STROKE_SIZE);

        Paint lineRedPaint = new Paint();
        lineRedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        lineRedPaint.setColor(Color.RED);
        lineRedPaint.setStrokeWidth(LINE_STROKE_SIZE);

        Paint lineBluePaint = new Paint();
        lineBluePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        lineBluePaint.setColor(Color.BLUE);
        lineBluePaint.setStrokeWidth(LINE_STROKE_SIZE);

        linePaints[SYSTEM_ID] = lineBlackPaint;
        linePaints[USER_1_ID] = lineRedPaint;
        linePaints[USER_2_ID] = lineBluePaint;
        linePaints[UNDEFINED_ID] = blackPaint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initApp();
    }

    private void calculateDimensions() {
        calculateHeaderFooterDimensions();
        calculateRowsAndColumns();
        calculateTableDimensions();

        invalidate();
    }

    private void calculateHeaderFooterDimensions() {
        headerHeight = getHeight() * headerSizeRatio / 100;
        // footerHeight = getHeight() * footerSizeRatio / 100;
        footerHeight = 0;
        headerWidth = getWidth();

        HEADER_TEXT_STROKE_SIZE = headerHeight / 20;
        headerDotRadius = headerHeight / 10;

        headerTextSize = headerHeight * headerTextSizeRation / 100;

        headerOffestY = (headerHeight - headerTextSize)/2 + LINE_STROKE_SIZE;

        headerMenuWidthSize = getWidth() * headerMenuWidthSizeRatio / 100;

    }

    private void calculateRowsAndColumns() {
        numColumns = (int)((getWidth() / dpi) * 25.4) / CELL_DIMENTIONS;
        numRows = (int) (((getHeight() - (headerHeight + footerHeight)) / dpi) * 25.4) / CELL_DIMENTIONS;
    }

    private void calculateTableDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return;
        }

        cellWidth = getWidth() / numColumns;
        cellHeight = (getHeight() - (headerHeight + footerHeight)) / numRows;

        int textWidth = cellWidth * xoSize / 100;
        int textHeight = cellHeight * xoSize / 100;

        xoTextSize = textHeight;

        xoOffestX = (cellWidth - textWidth)/2 + LINE_STROKE_SIZE/2;
        xoOffestY = (cellHeight - textHeight)/2 + xoTextSize - LINE_STROKE_SIZE/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getHeight() > 0) {
            canvas.drawColor(Color.TRANSPARENT);

            if (boardCells.length == 0 || boardCells[0].length == 0) {
                return;
            }

            drawHeader(canvas);
            drawGrid(canvas);
            drawContour(canvas);
        }
    }

    private void drawHeader(Canvas canvas) {
        drawHighlights(canvas);
        drawScores(canvas);
        drawMenu(canvas);
    }

    private void drawHighlights(Canvas canvas) {
        if (!aiEnabled) {
            Paint paint = new Paint();
            paint.setColor(Color.argb(100, 153, 238, 153));
            paint.setTextSize(18.0f);
            if (currentUser == USER_1_ID) {
                canvas.drawRect(0, 0,
                        headerWidth / 2, headerHeight, paint);
            } else {
                canvas.drawRect(headerWidth / 2, 0,
                        headerWidth, headerHeight, paint);
            }
        }
    }

    private void drawScores(Canvas canvas) {
        canvas.drawText(USER_1 + " : " + USER_1_SCORE,
                headerOffestX,
                headerHeight - headerOffestY,
                headerPaints[USER_1_ID]);

        canvas.drawText(USER_2 + " : " + USER_2_SCORE,
                headerWidth / 2 + headerOffestX,
                headerHeight - headerOffestY,
                headerPaints[USER_2_ID]);
    }

    private void drawMenu(Canvas canvas) {
        canvas.drawCircle(headerWidth - headerMenuWidthSize,
                          headerHeight/8*2,
                headerDotRadius, headerPaints[UNDEFINED_ID]);

        canvas.drawCircle(headerWidth - headerMenuWidthSize,
                headerHeight/8*4,
                headerDotRadius, headerPaints[UNDEFINED_ID]);

        canvas.drawCircle(headerWidth - headerMenuWidthSize,
                headerHeight/8*6,
                headerDotRadius, headerPaints[UNDEFINED_ID]);
    }

    private void drawGrid(Canvas canvas) {
        canvas.drawLine(0, headerHeight, headerWidth, headerHeight, linePaints[SYSTEM_ID]);
        canvas.drawLine(0, getHeight() - footerHeight, headerWidth, getHeight() - footerHeight, linePaints[SYSTEM_ID]);

        for (int i = 0; i < boardCells.length; i++) {
            for (int j = 0; j < boardCells[0].length; j++) {
                if (boardCells[i][j].isChecked()) {

                    canvas.drawText (xo[boardCells[i][j].getChecked()],
                            j * cellWidth + xoOffestX,
                            i * cellHeight + xoOffestY + headerHeight,
                            paints[boardCells[i][j].getChecked()]);
                }
                if (boardCells[i][j].getLeftBorder() <= 3) {
                    canvas.drawLine(j * cellWidth,
                            i * cellHeight + headerHeight,
                            j * cellWidth,
                            (i + 1) * cellHeight + headerHeight,
                            linePaints[boardCells[i][j].getLeftBorder()]);
                }
                if (boardCells[i][j].getRightBorder() <= 3) {
                    canvas.drawLine((j + 1) * cellWidth,
                            i * cellHeight + headerHeight,
                            (j + 1) * cellWidth,
                            (i + 1) * cellHeight + headerHeight,
                            linePaints[boardCells[i][j].getRightBorder()]);
                }
                if (boardCells[i][j].getTopBorder() <= 3) {
                    canvas.drawLine(j * cellWidth,
                            i * cellHeight + headerHeight,
                            (j + 1) * cellWidth,
                            i * cellHeight + headerHeight,
                            linePaints[boardCells[i][j].getTopBorder()]);
                }
                if (boardCells[i][j].getBottomBorder() <= 3) {
                    canvas.drawLine(j * cellWidth,
                            (i + 1) * cellHeight + headerHeight,
                            (j + 1) * cellWidth,
                            (i + 1) * cellHeight + headerHeight,
                            linePaints[boardCells[i][j].getBottomBorder()]);
                }
            }
        }
    }

    private void drawContour(Canvas canvas) {
        // vertical left border
        canvas.drawLine(0, headerHeight,
                0,
                getHeight() - footerHeight,
                linePaints[SYSTEM_ID]);

        // vertical right border
        canvas.drawLine(headerWidth, headerHeight,
                headerWidth,
                getHeight() - footerHeight,
                linePaints[SYSTEM_ID]);

        // horizontal top border
        canvas.drawLine(0, headerHeight,
                headerWidth,
                headerHeight,
                linePaints[SYSTEM_ID]);

        // horizontal bottom border
        canvas.drawLine(0, getHeight() - footerHeight,
                headerWidth,
                getHeight() - footerHeight,
                linePaints[SYSTEM_ID]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (menuWasClicked(event)) {
            onMenuClicked();
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN && !screenLocked) {
            int column = (int)(event.getX() / cellWidth);
            int row = (int)((event.getY() - headerHeight) / cellHeight);

            if ((column >= boardCells[0].length) || (row >= boardCells.length)) {
                return true;
            }

            this.playSoundEffect(SoundEffectConstants.CLICK);

            String border = getClickedBorder(event.getX(), event.getY(), cellWidth, cellHeight, column, row);

            if (boardCells[row][column].canCheck()) {
                boardCells[row][column].setChecked(currentUser);
            } else if ( border != null && boardCells[row][column].getBorderValue(border) == 3) {
                int check1 = boardCells[row][column].setBorderValueAndCheck(border, currentUser, currentUser);
                int check2 = Commons.setNeighbourCellsBorderValueIfRequired(boardCells, border, row, column, currentUser);
                if (!(check1 == 1 || check2 == 1)) {
                    toggleCurrentUser();
                }
            } else {
                // add a warning
            }
            updateStatus();
            invalidate();
        }

        return true;
    }

    private boolean menuWasClicked(MotionEvent event) {
        if (event.getY() <= headerHeight && event.getX() > headerWidth - headerMenuWidthSize - 20) {
            return true;
        }
        return false;
    }

    private String getClickedBorder(float X, float Y, int cellWidth, int cellHeight, int column, int row) {
        if (column * cellWidth + BORDER_ZONE >= X) {
            return LEFT;
        } else if ((column + 1) * cellWidth - BORDER_ZONE <= X) {
            return RIGHT;
        } else if ((row + 1) * cellHeight + headerHeight - BORDER_ZONE <= Y) {
            return BOTTOM;
        }  else if (row * cellHeight + headerHeight + BORDER_ZONE >= Y) {
            return TOP;
        }
        return null;
    }

    private void toggleCurrentUser() {
        if (currentUser == USER_1_ID) {
            currentUser = USER_2_ID;
            if (aiEnabled) {
                screenLocked = true;
                ai.callAi(boardCells, currentUser);
                screenLocked = false;
                currentUser = USER_1_ID;
            }
        } else {
            currentUser = USER_1_ID;
        }
    }

    private Cell[][] initGrid(Cell[][] cells) {
        for (int i=0; i < cells[0].length; i++) {
            for (int j = 0; j < cells.length; j++) {
                Cell cell = new Cell(j, i);
                if (i == 0) {
                    cell.setLeftBorder(SYSTEM_ID);
                }
                if (i == cells[0].length - 1) {
                    cell.setRightBorder(SYSTEM_ID);
                }
                if (j == 0) {
                    cell.setTopBorder(SYSTEM_ID);
                }
                if (j == cells.length - 1) {
                    cell.setBottomBorder(SYSTEM_ID);
                }
                cells[j][i] = cell;
            }
        }
        return cells;
    }

    private void updateStatus() {
        boolean hasUncheckedCells = false;
        int p1 = 0;
        int p2 = 0;
        for (int i = 0; i < boardCells.length; i++) {
            for (int j = 0; j < boardCells[0].length; j++) {
                if (boardCells[i][j].isChecked()) {
                    if (boardCells[i][j].getChecked() == USER_1_ID) {
                        p1++;
                    } else {
                        p2++;
                    }
                } else {
                    hasUncheckedCells = true;
                }
            }
        }
        USER_1_SCORE = p1;
        USER_2_SCORE = p2;
        if (!hasUncheckedCells) {
            onGameOver();
        }
    }

    private void onMenuClicked() {
        level = Level.PAUSE;
        Intent i = new Intent(this.getContext(), FullScreenAdMob.class);
        this.getContext().startActivity(i);
    }

    private void onGameOver() {
        level = Level.GAME_OVER;
        Intent i = new Intent(this.getContext(), FullScreenAdMob.class);
        this.getContext().startActivity(i);
    }
}
