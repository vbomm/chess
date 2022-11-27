package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;

import java.util.ArrayList;

public class Pawn extends Piece {
    private int forward;
    private int doubleForward;
    private int captureLeft;
    private int captureRight;


    /**
     * Initializes a new Pawn and sets the value for moves depending on the color/direction.
     *
     * @param board the Board object
     * @param color the color of this Object
     * @param tile the tile of this Object
     */
    public Pawn(Board board, ChessColor color, int tile) {
        super(board, color, PieceType.PAWN, tile);

        if (color == ChessColor.WHITE) {
            forward = -8;
            doubleForward = -16;
            captureLeft = -9;
            captureRight = -7;
        } else {
            forward = 8;
            doubleForward = 16;
            captureLeft = 7;
            captureRight = 9;
        }
    }


    @Override
    public void generateThreats(boolean[] captures) {
        if (!isActive())
            return;

        if(getBoard().getColumn(getTile()) > 0 && !getBoard().isSameColor(getTile(), getTile() + captureLeft)) {
            captures[getTile() + captureLeft] = true;
        }
        if(getBoard().getColumn(getTile()) < 7 && !getBoard().isSameColor(getTile(), getTile() + captureRight)) {
            captures[getTile() + captureRight] = true;
        }
    }

    @Override
    public void generateMovesAndCaptures(ArrayList<Move> moves, ArrayList<Move> captures) {
        if (!isActive())
            return;

        if (getBoard().isTileEmpty(getTile() + forward)) {
            moves.add(new Move(getTile(),getTile() + forward, this, getBoard().getTile(getTile() + forward), getBoard().getNoPawnMoveOrCaptureCounter()));
            if(getBoard().getAdvancement(getColor(), getTile()) == 1 && getBoard().isTileEmpty(getTile() + doubleForward))
                moves.add(new Move(getTile(), getTile() + doubleForward, this, getBoard().getTile(getTile() + doubleForward), getBoard().getNoPawnMoveOrCaptureCounter()));
        }

        if(getBoard().getColumn(getTile()) > 0 && !getBoard().isTileEmpty(getTile() + captureLeft) && !getBoard().isSameColor(getTile(), getTile() + captureLeft))
            captures.add(new Move(getTile(), getTile() + captureLeft, this, getBoard().getTile(getTile() + captureLeft), getBoard().getNoPawnMoveOrCaptureCounter()));

        if(getBoard().getColumn(getTile()) < 7 && !getBoard().isTileEmpty(getTile() + captureRight) && !getBoard().isSameColor(getTile(), getTile() + captureRight))
            captures.add(new Move(getTile(),getTile() + captureRight, this, getBoard().getTile(getTile() + captureRight), getBoard().getNoPawnMoveOrCaptureCounter()));
    }
}
