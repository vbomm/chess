package chess.model;

/**
 * Represents a move.
 */
public class Move {
    private int start;
    private int destination;
    private Piece piece;
    private int noPawnMoveOrCaptureCounter;
    private Piece target;

    /**
     * Default constructor.
     *
     * @param start                      the start tile of the move
     * @param destination                the destination of the move
     * @param piece                      the piece that moved
     * @param target                     the captured target, null if there is none
     * @param noPawnMoveOrCaptureCounter counter for 50-move and 75-move rule
     */
    public Move(int start, int destination, Piece piece, Piece target, int noPawnMoveOrCaptureCounter) {
        this.start = start;
        this.destination = destination;
        this.piece = piece;
        this.noPawnMoveOrCaptureCounter = noPawnMoveOrCaptureCounter;
        this.target = target;
    }

    /**
     * Returns the counter for the 50-move and 75-move rule.
     *
     * @return the counter for the 50-move and 75-move rule
     */
    public int getNoPawnMoveOrCaptureCounter() {
        return noPawnMoveOrCaptureCounter;
    }

    /**
     * Returns the start tile of the move.
     *
     * @return the start tile of the move
     */
    public int getStart() {
        return start;
    }

    /**
     * Returns the destination of the move.
     *
     * @return the destination of the move
     */
    public int getDestination() {
        return destination;
    }

    /**
     * Returns the captured piece, null if there is none.
     *
     * @return the captured piece, null if there is none
     */
    public Piece getTarget() {
        return target;
    }

    /**
     * Returns the piece that moved.
     *
     * @return the piece that moved
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Returns the start position and the destination as a String.
     *
     * @return the start position and the destination as a String
     */
    @Override
    public String toString() {
        return start + " to " + destination;
    }
}