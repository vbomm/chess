package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;

import java.util.ArrayList;

public class Bishop extends Piece {
    private int[][] lookupTable;

    public Bishop(Board board, ChessColor color, int tile) {
        super(board, color, PieceType.BISHOP, tile);
        generateMovesLookupTable();
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

    public void generateMovesLookupTable() {
        lookupTable = new int[64][9];

        for (int i = 0; i < 64; i++) {
            for (int m = 0; m < 8; m++) {
                lookupTable[i][m] = -1;
            }

            int column = getBoard().getColumn(i);
            int row = getBoard().getRow(i);

            if (row < 7) lookupTable[i][0] = i + 8;
            if (column < 7 && row < 7) lookupTable[i][1] = i + 9;
            if (column < 7) lookupTable[i][2] = i + 1;
            if (column < 7 && row > 0) lookupTable[i][3] = i - 7;
            if (row > 0) lookupTable[i][4] = i - 8;
            if (column > 0 && row > 0) lookupTable[i][5] = i - 9;
            if (column > 0) lookupTable[i][6] = i - 1;
            if (column > 0 && row < 7) lookupTable[i][7] = i + 7;
        }
    }
}
