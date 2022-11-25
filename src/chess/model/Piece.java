package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;

import java.util.ArrayList;

/**
 * The parent of all chess pieces.
 */
public abstract class Piece {
    private Board board;
    private ChessColor color;
    private PieceType pieceType;
    private int tile;
    private boolean isActive;
    private int moveCounter;

    /**
     * Initializes a new Piece, sets it to active and the moveCounter to 0.
     *
     * @param board
     * @param color
     * @param pieceType
     * @param tile
     */
    public Piece(Board board, ChessColor color, PieceType pieceType, int tile) {
        this.board = board;
        this.color = color;
        this.pieceType = pieceType;
        this.tile = tile;
        isActive = true;
        moveCounter = 0;
    }

    /**
     * Generate captures and add true for possible captures to boolean array.
     *
     * @param c
     */
    public abstract void generateThreats(boolean[] c);
    public abstract void generateMovesAndCaptures(ArrayList<Move> moves, ArrayList<Move> captures);

    /**
     * Returns the Board.
     *
     * @return Board object
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the color of the Piece.
     *
     * @return color of the piece
     */
    public ChessColor getColor() {
        return color;
    }

    /**
     * Returns the type of the piece.
     *
     * @return type of the piece
     */
    public PieceType getType() {
        return pieceType;
    }

    /**
     * Sets the tile of the piece on the board.
     *
     * @param index tile of the piece on the board
     */
    public void setTile(int index) {
        tile = index;
    }


    /**
     * Returns the tile of the piece on the board.
     *
     * @return tile of the piece on the board.
     */
    public int getTile() {
        return tile;
    }


    /**
     * Returns true of piece is active, otherwise false.
     *
     * @return true if active, otherwise false
     */
    public boolean isActive() {
        return isActive;
    }


    /**
     * Sets the piece to active.
     */
    public void activate() {
        isActive = true;
    }

    /**
     * Deactivates the piece.
     */
    public void deactivate() {
        isActive = false;
    }

    /**
     * Returns some values of the piece.
     *
     * @return color, piece type, tile and if it active or not
     */
    public String toString() {
        return color + "" +  pieceType + tile + isActive;
    }

    /**
     * Increases the move counter by one.
     */
    public void increaseMoveCounter() {
        moveCounter++;
    }

    /**
     * Decreases the move counter by one.
     */
    public void decreaseMoveCounter() {
        moveCounter--;
    }

    /**
     * Returns yes of the move counter is zero, otherwise false.
     *
     * @return yes if move vounter is zer0, otherweise false
     */
    public boolean neverMoved() {
        return moveCounter == 0;
    }
}
