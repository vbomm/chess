package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;

import java.util.ArrayList;

public class Knight extends Piece {
    private int[][] lookupTable;

    /**
     * Initializes a new Knight and gets the corresponding lookup table for moves.
     *
     * @param board the Board object
     * @param color the color of this Object
     * @param tile the tile of this Object
     */
    public Knight(Board board, ChessColor color, int tile) {
        super(board, color, PieceType.KNIGHT, tile);
        lookupTable = getBoard().getLookupTables().getKnight();
    }

    @Override
    public void generateThreats(boolean[] captures) {
        if (!isActive())
            return;

        int m = 0;
        int tile = lookupTable[getTile()][m++];

        while (tile > -1) {
            if (!getBoard().isSameColor(getTile(), tile))
                captures[tile] = true;

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
}
