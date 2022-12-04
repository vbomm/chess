package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;

import java.util.ArrayList;

public class Queen extends Piece {
    private int[][] lookupTable;

    /**
     * Initializes a new Queen and gets the corresponding lookup table for moves.
     *
     * @param board the Board object
     * @param color the color of this Object
     * @param tile  the tile of this Object
     */
    public Queen(Board board, ChessColor color, int tile) {
        super(board, color, PieceType.QUEEN, tile);
        lookupTable = getBoard().getLookupTables().getQueen();
    }

    @Override
    public void generateThreats(boolean[] captures) {
        if (!isActive())
            return;

        generateCaptures(0, captures);
        generateCaptures(1, captures);
        generateCaptures( 2, captures);
        generateCaptures( 3, captures);
        generateCaptures( 4, captures);
        generateCaptures( 5, captures);
        generateCaptures( 6, captures);
        generateCaptures( 7, captures);
    }

    private void generateCaptures(int direction, boolean[] captures) {
        int tile = lookupTable[getTile()][direction];

        while(tile > -1) {
            if(!getBoard().isTileEmpty(tile)) {
                if(!getBoard().isSameColor(getTile(), tile))
                    captures[tile] = true;

                break;
            }
            tile = lookupTable[tile][direction];
        }
    }

    @Override
    public void generateMovesAndCaptures(ArrayList<Move> moves, ArrayList<Move> captures) {
        if (!isActive())
            return;

        generateMovesAndCaptures(0, moves, captures);
        generateMovesAndCaptures(1, moves, captures);
        generateMovesAndCaptures(2, moves, captures);
        generateMovesAndCaptures(3, moves, captures);
        generateMovesAndCaptures(4, moves, captures);
        generateMovesAndCaptures(5, moves, captures);
        generateMovesAndCaptures(6, moves, captures);
        generateMovesAndCaptures(7, moves, captures);
    }

    private void generateMovesAndCaptures(int direction, ArrayList<Move> moves, ArrayList<Move> captures) {
        int tile = lookupTable[getTile()][direction];

        while(tile > -1) {
            if(!getBoard().isTileEmpty(tile)) {
                if(!getBoard().isSameColor(getTile(), tile))
                    captures.add(new Move(getTile(), tile, this, getBoard().getTile(tile), getBoard().getNoPawnMoveOrCaptureCounter()));

                break;
            }
            moves.add(new Move(getTile(), tile, this, getBoard().getTile(tile), getBoard().getNoPawnMoveOrCaptureCounter()));
            tile = lookupTable[tile][direction];
        }
    }
}
