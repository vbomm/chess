package chess.controller;

import chess.model.Piece;

public class SelectedPiece {
    private Piece piece;
    private int x;
    private int y;
    private int dragX;
    private int dragY;

    public SelectedPiece() {
        piece = null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;

        if (piece == null)
            return;

        x = piece.getTile() % 8;
        y = piece.getTile() / 8;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDragX() {
        return dragX;
    }

    public void setDragX(int dragX) {
        this.dragX = dragX;
    }

    public int getDragY() {
        return dragY;
    }

    public void setDragY(int dragY) {
        this.dragY = dragY;
    }

    public void setSelectedPieceDragXY(int selectedPieceDragX, int selectedPieceDragY) {
        this.dragX = selectedPieceDragX;
        this.dragY = selectedPieceDragY;
    }
}
