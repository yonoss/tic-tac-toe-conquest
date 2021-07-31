package com.ion.xo.game;

public class Session {

    public static String USER_1;
    public static String USER_2;
    public static int USER_1_SCORE = 0;
    public static int USER_2_SCORE = 0;
    public static Level level = Level.UNDEFINED;
    public static Cell[][] boardCells = null;
    public static boolean initialized = false;
    public static boolean aiEnabled = false;

    public static int viewWidth = 0;
    public static int viewHeight = 0;
    public static float dpi = 0;
    public static int background_id = -1;
    public static int orientation = -1;


    public enum Level {
        UNDEFINED,
        START_1_PLAYER,
        START_2_PLAYERS,
        PAUSE,
        GAME,
        MENU,
        HIGH_SCORES,
        GAME_OVER
    }
}
