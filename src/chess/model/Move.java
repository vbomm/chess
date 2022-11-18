package chess.model;

public class Move {
    private int start;
    private int destination;
    private Piece piece;
    private int noPawnMoveOrCaptureCounter;
    private Piece target;

    public Move(int start, int destination, Piece piece, Piece target, int noPawnMoveOrCaptureCounter) {
        this.start = start;
        this.destination = destination;
        this.piece = piece;
        this.noPawnMoveOrCaptureCounter = noPawnMoveOrCaptureCounter;
        this.target = target;
    }

    public void setNoPawnMoveOrCaptureCounter(int value) {
        noPawnMoveOrCaptureCounter = value;
    }

    public int getNoPawnMoveOrCaptureCounter() {
        return noPawnMoveOrCaptureCounter;
    }
    public int getStart() {
        return start;
    }

    public int getDestination() {
        return destination;
    }

    public Piece getTarget() {
        return target;
    }

    public Piece getPiece() {
        return piece;
    }

    @Override
    public String toString() {
        return start + " to " + destination;
    }
}