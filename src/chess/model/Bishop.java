package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;

import java.util.ArrayList;

public class Bishop extends Piece {
    private int[][] lookupTable;

    public Bishop(Board board, ChessColor color, int tile) {
        super(board, color, PieceType.BISHOP, tile);
        lookupTable = getBoard().getLookupTables().getBishop();
    }

    @Override
    public void generateThreats(boolean[] captures) {
        if (!isActive())
            return;

        generateCaptures(1, captures);
        generateCaptures(3, captures);
        generateCaptures(5, captures);
        generateCaptures(7, captures);
    }

    private void generateCaptures(int direction, boolean[] captures) {
        int tile = lookupTable[getTile()][direction];

        while(tile > -1) {
            if(!getBoard().isTileEmpty(tile)) {
                if(!getBoard().isSameColor(getTile(), tile)) {
                    captures[tile] = true;
                }
                break;
            }
            tile = lookupTable[tile][direction];
        }
    }

    @Override
    public void generateMovesAndCaptures(ArrayList<Move> moves, ArrayList<Move> captures) {
        if (!isActive())
            return;

        generateMovesAndCaptures(1, moves, captures);
        generateMovesAndCaptures(3, moves, captures);
        generateMovesAndCaptures(5, moves, captures);
        generateMovesAndCaptures(7, moves, captures);
    }

    private void generateMovesAndCaptures(int direction, ArrayList<Move> moves, ArrayList<Move> captures) {
        int tile = lookupTable[getTile()][direction];

        while(tile > -1) {
            if(!getBoard().isTileEmpty(tile)) {
                if(!getBoard().isSameColor(getTile(), tile)) {
                    captures.add(new Move(getTile(), tile, this, getBoard().getTile(tile), getBoard().getNoPawnMoveOrCaptureCounter()));
                }
                break;
            }
            moves.add(new Move(getTile(), tile, this, getBoard().getTile(tile), getBoard().getNoPawnMoveOrCaptureCounter()));
            tile = lookupTable[tile][direction];
        }
    }
}
