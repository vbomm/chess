package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;

import java.util.ArrayList;

public abstract class Piece {
    private Board board;
    private ChessColor color;
    private PieceType pieceType;
    private int tile;
    private boolean isActive;
    private int moveCounter;

    public Piece(Board board, ChessColor color, PieceType pieceType, int tile) {
        this.board = board;
        this.color = color;
        this.pieceType = pieceType;
        this.tile = tile;
        isActive = true;
        moveCounter = 0;
    }

    public abstract void generateThreats(boolean[] c);
    public abstract void generateMovesAndCaptures(ArrayList<Move> moves, ArrayList<Move> captures);

    public Board getBoard() {
        return board;
    }

    public ChessColor getColor() {
        return color;
    }

    public PieceType getType() {
        return pieceType;
    }

    public void setTile(int index) {
        tile = index;
    }

    public int getTile() {
        return tile;
    }

    public boolean isActive() {
        return isActive;
    }

    public void activate() {
        isActive = true;
    }

    public void deactivate() {
        isActive = false;
    }

    public String toString() {
        return color + "" +  pieceType + tile + isActive;
    }

    public void increaseMoveCounter() {
        moveCounter++;
    }

    public void decreaseMoveCounter() {
        moveCounter--;
    }

    public boolean neverMoved() {
        return moveCounter == 0;
    }
}
