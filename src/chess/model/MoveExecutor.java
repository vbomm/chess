package chess.model;

import chess.controller.PieceType;

/**
 * Executes and reverses moves.
 */
public class MoveExecutor {
    private Board board;

    /**
     * Default constructor.
     *
     * @param board the board moves are executed on
     */
    public MoveExecutor(Board board) {
        this.board = board;
    }

    /**
     * Executes a move from a Move object.
     * Also checks for special moves.
     *
     * @param move the move to execute
     */
    public void executeMove(Move move) {
        // 50 and 75 move rule
        if (move.getPiece().getType() == PieceType.PAWN || move.getTarget() != null)
            board.setNoPawnMoveOrCaptureCounter(0);
        else
            board.setNoPawnMoveOrCaptureCounter(move.getNoPawnMoveOrCaptureCounter() + 1);

        if (board.getTile(move.getStart()).getType() == PieceType.KING) {
            board.setKing(move.getPiece().getColor(), move.getPiece());

            if (move.getStart() - move.getDestination() == 2)
                // long castling
                if (move.getStart() == 4) {
                    // black
                    board.setPieceTile(0, 3);
                    board.setTile(0, null);
                } else {
                    // white
                    board.setPieceTile(56, 59);
                    board.setTile(56, null);
                }
            else if (move.getStart() - move.getDestination() == -2)
                // short castling
                if (move.getStart() == 4) {
                    // black
                    board.setPieceTile(7, 5);
                    board.setTile(7, null);
                } else {
                    // white
                    board.setPieceTile(63, 61);
                    board.setTile(63, null);
                }
        }

        // deactivate target
        if (move.getTarget() != null)
            move.getTarget().deactivate();

        // move piece
        board.setPieceTile(move.getStart(), move.getDestination());
        board.setTile(move.getStart(), null);

        // pawn special moves
        if (board.getTile(move.getDestination()).getType() == PieceType.PAWN) {
            // promotion
            if (board.getAdvancement(board.getWhosTurn(), move.getDestination()) == 7) {
                move.getPiece().deactivate();
                board.addPiece(PieceType.QUEEN, board.getWhosTurn(), move.getDestination());
            }

            // en passant
            if (move.getTarget() != null && move.getDestination() != move.getTarget().getTile())
                board.setTile(move.getTarget().getTile(), null);
        }

        move.getPiece().increaseMoveCounter();
        board.addMoveToHistory(move);

        board.changeWhosTurn();
    }

    /**
     * Reverses a move from a Move object.
     * Also checks for special moves.
     *
     * @param move the move to reverse
     */
    public void reverseMove(Move move) {
        board.setNoPawnMoveOrCaptureCounter(move.getNoPawnMoveOrCaptureCounter() - 1);

        if (board.getTile(move.getDestination()).getType() == PieceType.KING) {
            board.setKing(move.getPiece().getColor(), move.getPiece());

            if (move.getStart() - move.getDestination() == 2)
                // long castling
                if (move.getStart() == 4) {
                    // black
                    board.setPieceTile(3, 0);
                    board.setTile(3, null);
                } else {
                    // white
                    board.setPieceTile(59, 56);
                    board.setTile(59, null);
                }
            else if (move.getStart() - move.getDestination() == -2)
                // short castling
                if (move.getStart() == 4) {
                    // black
                    board.setPieceTile(5, 7);
                    board.setTile(5, null);
                } else {
                    // white
                    board.setPieceTile(61, 63);
                    board.setTile(61, null);
                }
        }

        // undo promotion, remove queen from piece list and activate pawn
        if (board.getTile(move.getDestination()).getType() == PieceType.QUEEN && move.getPiece().getType() == PieceType.PAWN) {
            board.removeLastPiece(move.getPiece().getColor());
            //pieces.get(colorHash.get(move.getPiece().getColor())).remove(pieces.get(colorHash.get(move.getPiece().getColor())).size() - 1);
            board.getTile(move.getDestination()).deactivate();
            board.setTile(move.getDestination(), null);
            move.getPiece().activate();
        }
        // undo move, also works to undo promotion as long pawn gets activated and queen deactivated
        board.setTile(move.getStart(), move.getPiece());
        move.getPiece().setTile(move.getStart());
        board.getTile(move.getStart()).setTile(move.getStart());

        // undo en passant
        if (move.getTarget() != null && move.getDestination() != move.getTarget().getTile()) {
            board.setTile(move.getTarget().getTile(), move.getTarget());
            board.setTile(move.getDestination(), null);
        } else
            board.setTile(move.getDestination(), move.getTarget());

        if (move.getTarget() != null)
            move.getTarget().activate();

        move.getPiece().decreaseMoveCounter();
        board.removeMoveFromHistory(move);

        board.changeWhosTurn();
    }


}
