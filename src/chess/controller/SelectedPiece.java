package chess.controller;

import chess.model.Piece;

public class SelectedPiece {
    private Piece selectedPiece;
    private int selectedPieceX;
    private int selectedPieceY;
    private int selectedPieceDragX;
    private int selectedPieceDragY;

    public SelectedPiece() {
        selectedPiece = null;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;

        if (selectedPiece == null)
            return;

        selectedPieceX = selectedPiece.getTile() % 8;
        selectedPieceY = selectedPiece.getTile() / 8;
    }

    public int getSelectedPieceX() {
        return selectedPieceX;
    }

    public void setSelectedPieceX(int selectedPieceX) {
        this.selectedPieceX = selectedPieceX;
    }

    public int getSelectedPieceY() {
        return selectedPieceY;
    }

    public void setSelectedPieceY(int selectedPieceY) {
        this.selectedPieceY = selectedPieceY;
    }

    public int getSelectedPieceDragX() {
        return selectedPieceDragX;
    }

    public void setSelectedPieceDragX(int selectedPieceDragX) {
        this.selectedPieceDragX = selectedPieceDragX;
    }

    public int getSelectedPieceDragY() {
        return selectedPieceDragY;
    }

    public void setSelectedPieceDragY(int selectedPieceDragY) {
        this.selectedPieceDragY = selectedPieceDragY;
    }

    public void setSelectedPieceDragXY(int selectedPieceDragX, int selectedPieceDragY) {
        this.selectedPieceDragX = selectedPieceDragX;
        this.selectedPieceDragY = selectedPieceDragY;
    }
}
