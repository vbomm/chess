package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;

import java.util.ArrayList;

public class Rook extends Piece {
    private int[][] lookupTable;

    public Rook(Board board, ChessColor color, int tile) {
        super(board, color, PieceType.ROOK, tile);
        lookupTable = getBoard().getLookupTables().getRook();
    }

    @Override
    public void generateThreats(boolean[] captures) {
        if (!isActive())
            return;

        generateCaptures(0, captures);
        generateCaptures(1, captures);
        generateCaptures(2, captures);
        generateCaptures(3, captures);
    }

    private void generateCaptures(int dir, boolean[] captures) {
        int tile = lookupTable[getTile()][dir];

        while(tile > -1) {
            if(!getBoard().isTileEmpty(tile))
                if(!getBoard().isSameColor(getTile(), tile)) {
                    captures[tile] = true;

                break;
            }
            tile = lookupTable[tile][dir];
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
