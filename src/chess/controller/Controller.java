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

/**
 * Interacts between Model and View.
 */
public class Controller {
    private Model model;
    private View view;
    private SelectedPiece selectedPiece;

    final private Color whiteTile = new Color(226, 229, 240);
    final private Color whiteTileHighlighted = new Color(226, 229, 140);
    final private Color blackTile = new Color(127, 134, 147);
    final private Color blackTileHighlighted = new Color(127, 134, 247);
    final private Color lastMoved = new Color(200, 150, 29);

    private BufferedImage whitePawn;
    private HashMap<Integer, ImageIcon> pieceIcons;
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

    /**
     * Loads the images. Calls methods to set the backgrounds and the icons of View.
     * Runs View in AWT Event dispatcher thread and sets the frame to be shown.
     */
    public Controller() {
        selectedPiece = new SelectedPiece();

        model = new Model();
        view = new View(this);

        loadPieceImages();

        generateIconHashes();

        setAllBackgrounds();
        setAllIcons();

        SwingUtilities.invokeLater(() -> {
            view.show();
        });
    }

    /**
     * Creates a HashMap to look up icons depending on piece type and color.
     */
    private void generateIconHashes() {
        pieceIcons = new HashMap<>();

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
    }

    /**
     * Gets called to select a piece on the given coordinates. If there is none, selectedPiece gets set to null.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void setSelectedPiece(int x, int y) {
        if (x != -1 && y != -1 && !model.isTileEmpty(x, y) && model.getColor(x, y) == model.getWhosTurn()) {
            selectedPiece.setPiece(model.getTile(x, y));
            view.setIcon(y * 8 + x, null);

            for (int i = 0; i < 64; i++)
                if (model.hasPieceAccessToTile(selectedPiece.getX(), selectedPiece.getY(), i % 8, i / 8))
                    view.setBackground(i, getHighlightedTileColor(i));
        } else
            selectedPiece.setPiece(null);
    }

    /**
     * Sets the coordinates of the selected piece.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void setSelectedPieceCoordinates(int x, int y) {
        selectedPiece.setSelectedPieceDragXY(x, y);
    }

    /**
     * If selectedPiece is not null, set highlighted board tiles back to their normal color, set the icon for the tile of
     * the selected piece, call for the next half step and set selectedPiece to null.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void deselectPiece(int x, int y) {
        if (selectedPiece.getPiece() == null)
            return;

        for (int i = 0; i < 64; i++)
            if (model.hasPieceAccessToTile(selectedPiece.getX(), selectedPiece.getY(), i % 8, i / 8))
                view.setBackground(i, getTileColor(i));

        view.setIcon(selectedPiece.getY() * 8 + selectedPiece.getX(), getPieceIcon(model.getColor(selectedPiece.getX(), selectedPiece.getY()), model.getTile(selectedPiece.getX(), selectedPiece.getY()).getType()));

        if (model.movePiece(selectedPiece.getX(), selectedPiece.getY(), x, y)) {
            model.nextHalfStep();
            setAllIcons();
        }

        selectedPiece.setPiece(null);
    }

    /**
     * Returns the color of the tile.
     *
     * @param i index the tile
     * @return  the color of the tile
     */
    private Color getTileColor(int i) {
        return (i / 8 + i) % 2 == 0 ? whiteTile : blackTile;
    }

    /**
     * Returns the highlighted color of the tile.
     *
     * @param i the index of the tile
     * @return  the highlighted color of the tile
     */
    private Color getHighlightedTileColor(int i) {
        return (i / 8 + i) % 2 == 0 ? whiteTileHighlighted : blackTileHighlighted;
    }

    /**
     * Loads the images for the pieces.
     */
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
            view.displayMessage("Error opening images!", "Error");
        }
    }

    /**
     * Returns the icon for the piece.
     *
     * @param color color of the piece
     * @param type  type of the piece
     * @return      icon of the piece
     */
    private ImageIcon getPieceIcon(ChessColor color, PieceType type) {


        return pieceIcons.get(hashColorType(color, type));
    }

    /**
     * Returns a hash value for piece type and piece color.
     *
     * @param color color of the piece
     * @param type  type of the piece
     * @return      hash value for the piece
     */
    private int hashColorType(ChessColor color, PieceType type) {
        int hash = -1;

        switch (type) {
            case PAWN: hash = 0; break;
            case KNIGHT: hash = 1; break;
            case BISHOP: hash = 2; break;
            case ROOK: hash = 3; break;
            case QUEEN: hash = 4; break;
            case KING: hash = 5; break;
            default: view.displayMessage("Unknown piece type detected!", "Error");
        }

        if (color == ChessColor.BLACK)
            hash += 6;

        return hash;
    }


    /**
     * Rescales the icons of the pieces.
     *
     * @param width  requested with of the icon
     * @param height requested height of the icon
     */
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


    /**
     * Sets all icons of the board.
     */
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

    /**
     * Sets all backgrounds of the board.
     */
    private void setAllBackgrounds() {
        for (int i = 0; i < 64; i++)
            view.setBackground(i, getTileColor(i));
    }

    /**
     * Returns true if a piece is selected, else false.
     *
     * @return true if a piece is selected, else false
     */
    public boolean isAPieceSelected() {
        return selectedPiece.getPiece() != null;
    }

    /**
     * Returns the icon for a piece.
     *
     * @return the icon for a piece
     */
    public Image getSelectedPieceIcon() {
        return getPieceIcon(model.getColor(selectedPiece.getX(), selectedPiece.getY()), model.getTile(selectedPiece.getX(), selectedPiece.getY()).getType()).getImage();
    }

    /**
     * Returns the dragged x-coordinate of the selected piece.
     *
     * @return the dragged x-coordinate of the selected piece
     */
    public int getSelectedPieceDragX() {
        return selectedPiece.getDragX();
    }

    /**
     * Returns the dragged y-coordinate of the selected piece.
     *
     * @return the dragged y-coordinate of the selected piece
     */
    public int getSelectedPieceDragY() {
        return selectedPiece.getDragY();
    }
}
