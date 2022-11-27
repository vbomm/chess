package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;

import java.util.ArrayList;

public class King extends Piece {
    private int[][] lookupTable;

    /**
     * Initializes a new King and gets the corresponding lookup table for moves.
     *
     * @param board the Board object
     * @param color the color of this Object
     * @param tile the tile of this Object
     */
    public King(Board board, ChessColor color, int tile) {
        super(board, color, PieceType.KING, tile);
        lookupTable = getBoard().getLookupTables().getKing();
    }

    @Override
    public void generateThreats(boolean[] captures) {
        if (!isActive())
            return;

        int m = 0;
        int tile = lookupTable[getTile()][m++];

        while(tile > -1) {
            if (!getBoard().isSameColor(getTile(), tile))
                captures[tile] = true;

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
}
