package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;

import java.util.ArrayList;

public class Knight extends Piece {
    private int[][] lookupTable;

    public Knight(Board board, ChessColor color, int tile) {
        super(board, color, PieceType.KNIGHT, tile);
        generateMovesLookupTable();
    }

    @Override
    public void generateThreats(boolean[] captures) {
        if (!isActive())
            return;

        //int x = getTile();
        int m = 0;
        int tile = lookupTable[getTile()][m++];

        while (tile > -1) {
            if (!getBoard().isSameColor(getTile(), tile))
                captures[tile] = true;

            //if (k == 9) break;
            tile = lookupTable[getTile()][m++];
        }
    }

    @Override
    public void generateMovesAndCaptures(ArrayList<Move> moves, ArrayList<Move> captures) {
        if (!isActive())
            return;

        //int sq = getTile();
        int m = 0;
        int tile = lookupTable[getTile()][m++];

        while (tile > - 1) {
            if (getBoard().isTileEmpty(tile))
                moves.add(new Move(getTile(), tile, this, getBoard().getTile(tile), getBoard().getNoPawnMoveOrCaptureCounter()));
            else if (!getBoard().isSameColor(getTile(), tile))
                captures.add(new Move(getTile(), tile, this, getBoard().getTile(tile), getBoard().getNoPawnMoveOrCaptureCounter()));

            tile = lookupTable[getTile()][m++];
        }
    }

    public void generateMovesLookupTable() {
        lookupTable = new int[64][9];

        for (int i = 0; i < 64; i++) {
            int m = 0;
            int column = getBoard().getColumn(i);
            int row = getBoard().getRow(i);

            if (row < 6 && column < 7) lookupTable[i][m++] = i + 17;
            if (row < 7 && column < 6) lookupTable[i][m++] = i + 10;
            if (row < 6 && column > 0) lookupTable[i][m++] = i + 15;
            if (row < 7 && column > 1) lookupTable[i][m++] = i + 6;
            if (row > 1 && column < 7) lookupTable[i][m++] = i - 15;
            if (row > 0 && column < 6) lookupTable[i][m++] = i - 6;
            if (row > 1 && column > 0) lookupTable[i][m++] = i - 17;
            if (row > 0 && column > 1) lookupTable[i][m++] = i - 10;

            lookupTable[i][m] = -1;
        }
    }
}
