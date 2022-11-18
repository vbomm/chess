package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;

import java.util.ArrayList;

public class King extends Piece {
    private int[][] lookupTable;

    public King(Board board, ChessColor color, int tile) {
        super(board, color, PieceType.KING, tile);
        generateMovesLookupTable();
    }

    @Override
    public void generateThreats(boolean[] captures) {
        if (!isActive())
            return;

        int m = 0;
        int tile = lookupTable[getTile()][m++];

        while(tile > -1) {
            if (!getBoard().isSameColor(getTile(), tile)) {
                captures[tile] = true;
            }

            tile = lookupTable[getTile()][m++];
        }
    }

    @Override
    public void generateMovesAndCaptures(ArrayList<Move> moves, ArrayList<Move> captures) {
        if (!isActive())
            return;

        int m = 0;
        int tile = lookupTable[getTile()][m++];

        while (tile > -1) {
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

            if(column > 0) lookupTable[i][m++] = i - 1;
            if(column < 7) lookupTable[i][m++] = i + 1;
            if(row > 0) lookupTable[i][m++] = i - 8;
            if(row < 7) lookupTable[i][m++] = i + 8;
            if(column < 7 && row < 7) lookupTable[i][m++] = i + 9;
            if(column > 0 && row < 7) lookupTable[i][m++] = i + 7;
            if(column > 0 && row > 0) lookupTable[i][m++]= i - 9;
            if(column < 7 && row > 0) lookupTable[i][m++]= i - 7;

            lookupTable[i][m] = -1;
        }
    }
}
