package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;

import java.util.ArrayList;

public class Rook extends Piece {
    private int[][] lookupTable;

    public Rook(Board board, ChessColor color, int tile) {
        super(board, color, PieceType.ROOK, tile);
        generateMovesLookupTable();
    }

    @Override
    public void generateThreats(boolean[] captures) {
        if (!isActive())
            return;

        generateCaptures(0, captures);
        generateCaptures(2, captures);
        generateCaptures(4, captures);
        generateCaptures(6, captures);
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
        generateMovesAndCaptures(2, moves, captures);
        generateMovesAndCaptures(4, moves, captures);
        generateMovesAndCaptures(6, moves, captures);
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
