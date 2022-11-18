package chess.controller;

import chess.model.*;
import chess.view.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Controller {
    private Model model;
    private View view;

    private Piece selectedPiece;
    private int selectedPieceX;
    private int selectedPieceY;
    private int selectedPieceDragX;
    private int selectedPieceDragY;

    final private Color whiteTile = new Color(226, 229, 240);
    final private Color whiteTileHighlighted = new Color(226, 229, 140);
    final private Color blackTile = new Color(127, 134, 147);
    final private Color blackTileHighlighted = new Color(127, 134, 247);
    final private Color lastMoved = new Color(200, 150, 29);

    private BufferedImage whitePawn;
    private ImageIcon whitePawnRescaled;
    private BufferedImage whiteRook;
    private ImageIcon whiteRookRescaled;
    private BufferedImage whiteKnight;
    private ImageIcon whiteKnightRescaled;
    private BufferedImage whiteBishop;
    private ImageIcon whiteBishopRescaled;
    private BufferedImage whiteQueen;
    private ImageIcon whiteQueenRescaled;
    private BufferedImage whiteKing;
    private ImageIcon whiteKingRescaled;

    private BufferedImage blackPawn;
    private ImageIcon blackPawnRescaled;
    private BufferedImage blackRook;
    private ImageIcon blackRookRescaled;
    private BufferedImage blackKnight;
    private ImageIcon blackKnightRescaled;
    private BufferedImage blackBishop;
    private ImageIcon blackBishopRescaled;
    private BufferedImage blackQueen;
    private ImageIcon blackQueenRescaled;
    private BufferedImage blackKing;
    private ImageIcon blackKingRescaled;

    public Controller() {
        loadPieceImages();
        selectedPiece = null;

        model = new Model();
        view = new View(this);
        setAllBackgrounds();
        setAllIcons();

        SwingUtilities.invokeLater(() -> {
            view.show();
        });
    }

    public void setSelectedPiece(int x, int y) {
        if (x != -1 && y != -1 && !model.isTileEmpty(x, y) && model.getColor(x, y) == model.getWhosTurn()) {
            selectedPiece = model.getTile(x, y);
            selectedPieceX = x;
            selectedPieceY = y;
            view.setIcon(y * 8 + x, null);
            for (int i = 0; i < 64; i++)
                if (model.hasPieceAccessToTile(selectedPieceX, selectedPieceY, i % 8, i / 8))
                    view.setBackground(i, getHighlightedTileColor(i));
        } else
            selectedPiece = null;
    }

    public void setSelectedPieceCoordinates(int x, int y) {
        selectedPieceDragX = x;
        selectedPieceDragY = y;
    }

    public void mouseReleased(int x, int y) {
        if (selectedPiece != null) {
            for (int i = 0; i < 64; i++)
                if (model.hasPieceAccessToTile(selectedPieceX, selectedPieceY, i % 8, i / 8))
                    view.setBackground(i, getSpaceColor(i));

            view.setIcon(selectedPieceY * 8 + selectedPieceX, getPieceIcon(model.getColor(selectedPieceX, selectedPieceY), model.getTile(selectedPieceX, selectedPieceY).getType()));

            if (model.movePiece(selectedPieceX, selectedPieceY, x, y)) {
                model.nextHalfStep();
                setAllIcons();
            }
        }

        selectedPiece = null;
    }

    private Color getSpaceColor(int i) {
        return (i / 8 + i) % 2 == 0 ? whiteTile : blackTile;
    }

    private Color getHighlightedTileColor(int i) {
        return (i / 8 + i) % 2 == 0 ? whiteTileHighlighted : blackTileHighlighted;
    }

    private void loadPieceImages() {
        try {
            whitePawn = ImageIO.read(new File("gfx/white_pawn.png"));
            whiteRook = ImageIO.read(new File("gfx/white_rook.png"));
            whiteKnight = ImageIO.read(new File("gfx/white_knight.png"));
            whiteBishop = ImageIO.read(new File("gfx/white_bishop.png"));
            whiteQueen = ImageIO.read(new File("gfx/white_queen.png"));
            whiteKing = ImageIO.read(new File("gfx/white_king.png"));

            blackPawn = ImageIO.read(new File("gfx/black_pawn.png"));
            blackRook = ImageIO.read(new File("gfx/black_rook.png"));
            blackKnight = ImageIO.read(new File("gfx/black_knight.png"));
            blackBishop = ImageIO.read(new File("gfx/black_bishop.png"));
            blackQueen = ImageIO.read(new File("gfx/black_queen.png"));
            blackKing = ImageIO.read(new File("gfx/black_king.png"));
        } catch (IOException e) {
            System.err.println("IO error");
        }
    }

    private ImageIcon getPieceIcon(ChessColor color, PieceType type) {
        HashMap<Integer, ImageIcon> pieceIcons = new HashMap<>();
        pieceIcons.put(hashColorType(ChessColor.WHITE, PieceType.PAWN), whitePawnRescaled);
        pieceIcons.put(hashColorType(ChessColor.WHITE, PieceType.KNIGHT), whiteKnightRescaled);
        pieceIcons.put(hashColorType(ChessColor.WHITE, PieceType.BISHOP), whiteBishopRescaled);
        pieceIcons.put(hashColorType(ChessColor.WHITE, PieceType.ROOK), whiteRookRescaled);
        pieceIcons.put(hashColorType(ChessColor.WHITE, PieceType.QUEEN), whiteQueenRescaled);
        pieceIcons.put(hashColorType(ChessColor.WHITE, PieceType.KING), whiteKingRescaled);

        pieceIcons.put(hashColorType(ChessColor.BLACK, PieceType.PAWN), blackPawnRescaled);
        pieceIcons.put(hashColorType(ChessColor.BLACK, PieceType.KNIGHT), blackKnightRescaled);
        pieceIcons.put(hashColorType(ChessColor.BLACK, PieceType.BISHOP), blackBishopRescaled);
        pieceIcons.put(hashColorType(ChessColor.BLACK, PieceType.ROOK), blackRookRescaled);
        pieceIcons.put(hashColorType(ChessColor.BLACK, PieceType.QUEEN), blackQueenRescaled);
        pieceIcons.put(hashColorType(ChessColor.BLACK, PieceType.KING), blackKingRescaled);

        return pieceIcons.get(hashColorType(color, type));
    }

    private int hashColorType(ChessColor color, PieceType type) {
        int hash = -1;

        switch (type) {
            case PAWN: hash = 0; break;
            case KNIGHT: hash = 1; break;
            case BISHOP: hash = 2; break;
            case ROOK: hash = 3; break;
            case QUEEN: hash = 4; break;
            case KING: hash = 5; break;
        }

        if (color == ChessColor.BLACK)
            hash += 6;

        return hash;
    }


    public void rescaleIcons(int width, int height) {
        whitePawnRescaled = new ImageIcon(whitePawn.getScaledInstance(width, height, Image.SCALE_DEFAULT));
        whiteRookRescaled = new ImageIcon(whiteRook.getScaledInstance(width, height, Image.SCALE_DEFAULT));
        whiteKnightRescaled = new ImageIcon(whiteKnight.getScaledInstance(width, height, Image.SCALE_DEFAULT));
        whiteBishopRescaled = new ImageIcon(whiteBishop.getScaledInstance(width, height, Image.SCALE_DEFAULT));
        whiteQueenRescaled = new ImageIcon(whiteQueen.getScaledInstance(width, height, Image.SCALE_DEFAULT));
        whiteKingRescaled = new ImageIcon(whiteKing.getScaledInstance(width, height, Image.SCALE_DEFAULT));

        blackPawnRescaled = new ImageIcon(blackPawn.getScaledInstance(width, height, Image.SCALE_DEFAULT));
        blackRookRescaled = new ImageIcon(blackRook.getScaledInstance(width, height, Image.SCALE_DEFAULT));
        blackKnightRescaled = new ImageIcon(blackKnight.getScaledInstance(width, height, Image.SCALE_DEFAULT));
        blackBishopRescaled = new ImageIcon(blackBishop.getScaledInstance(width, height, Image.SCALE_DEFAULT));
        blackQueenRescaled = new ImageIcon(blackQueen.getScaledInstance(width, height, Image.SCALE_DEFAULT));
        blackKingRescaled = new ImageIcon(blackKing.getScaledInstance(width, height, Image.SCALE_DEFAULT));

        // use rescaled icons
        setAllIcons();
    }

    private void setAllIcons() {
        for (int i = 0; i < 64; i++) {
            int x = i % 8;
            int y = i / 8;
            Piece currentPiece = model.getTile(x, y);
            if (currentPiece == null)
                view.setIcon(i, null);
            else
                view.setIcon(i, getPieceIcon(model.getColor(x, y), model.getTile(x, y).getType()));
        }
    }

    private void setAllBackgrounds() {
        for (int i = 0; i < 64; i++)
            view.setBackground(i, getSpaceColor(i));
    }

    public boolean isAPieceSelected() {
        return selectedPiece != null;
    }

    public Image getSelectedPieceImage() {
        return getPieceIcon(model.getColor(selectedPieceX, selectedPieceY), model.getTile(selectedPieceX, selectedPieceY).getType()).getImage();
    }

    public int getSelectedPieceDragX() {
        return selectedPieceDragX;
    }

    public int getSelectedPieceDragY() {
        return selectedPieceDragY;
    }

    /*private int coordsToSpaceX(int x) {
        return x / 8;
    }

    private int coordsToSpaceY(int y) {
        return y / 8;
    }*/
}
