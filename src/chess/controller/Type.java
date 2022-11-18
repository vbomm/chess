package chess.controller;

public class Type {
    private static int white = 0;
    private static int black = 1;

    private static int pawn = 0;
    private static int knight = 1;
    private static int bishop = 2;
    private static int rook = 3;
    private static int queen = 4;
    private static int king = 5;
    private static int empty = 6;

    public static int white() {
        return white;
    }

    public static int black() {
        return black;
    }

    public static int pawn() {
        return pawn;
    }

    public static int knight() {
        return knight;
    }

    public static int bishop() {
        return bishop;
    }

    public static int rook() {
        return rook;
    }

    public static int queen() {
        return queen;
    }

    public static int king() {
        return king;
    }

    public static int empty() {
        return empty;
    }
}
