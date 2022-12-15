package chess.model;

import chess.controller.ChessColor;
import chess.controller.MoveHistory;
import chess.controller.PieceType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents the chess board.
 */
public class Board {
    private Model model;
    private MoveExecutor moveExecutor;
    private LookupTables lookupTables;
    private Piece[] tile;
    private ArrayList<ArrayList<Piece>> pieces;
    private boolean whiteCanLongCastle;
    private boolean whiteCanShortCastle;
    private boolean blackCanLongCastle;
    private boolean blackCanShortCastle;
    private ChessColor whosTurn;
    private int noPawnMoveOrCaptureCounter;

    private HashMap<ChessColor, Integer> colorHash;
    private Piece[] kings;

    /**
     * Default constructor.
     *
     * @param model
     */
    public Board(Model model, MoveHistory moveHistory) {
        this.model = model;
        moveExecutor = new MoveExecutor(this, moveHistory);
        lookupTables = new LookupTables(this);

        tile = new Piece[64];
        kings = new King[2];

        noPawnMoveOrCaptureCounter = 0;

        colorHash = new HashMap<>();
        colorHash.put(ChessColor.WHITE, 0);
        colorHash.put(ChessColor.BLACK, 1);

        initBoard();
    }

    /**
     * Sets the variables of the board.
     */
    private void initBoard() {
        whosTurn = ChessColor.WHITE;
        whiteCanLongCastle = true;
        whiteCanShortCastle = true;
        blackCanLongCastle = true;
        blackCanShortCastle = true;

        initPieceLists();
    }

    /**
     * Sets all tiles of the board array to null.
     */
    public void clear() {
        initBoard();
        for (int i = 0; i < 64; i++)
            tile[i] = null;
    }

    /**
     * Sets the piece lists.
     */
    private void initPieceLists() {
        pieces = new ArrayList<>();
        ArrayList<Piece> whitePieces = new ArrayList<>();
        ArrayList<Piece> blackPieces = new ArrayList<>();
        pieces.add(whitePieces);
        pieces.add(blackPieces);
    }

    /**
     * Adds a piece to the board.
     *
     * @param type  the type of the piece
     * @param color the color of the piece
     * @param index the index of the piece
     */
    public void addPiece(PieceType type, ChessColor color, int index) {
        switch (type) {
            case PAWN: tile[index] = new Pawn(this, color, index); break;
            case KNIGHT: tile[index] = new Knight(this, color, index); break;
            case BISHOP: tile[index] = new Bishop(this, color, index); break;
            case ROOK: tile[index] = new Rook(this, color, index); break;
            case QUEEN: tile[index] = new Queen(this, color, index); break;
            case KING: tile[index] = new King(this, color, index); kings[colorHash.get(color)] = tile[index]; break;
        }

        pieces.get(colorHash.get(color)).add(tile[index]);
    }

    /**
     * Returns the list of the pieces.
     *
     * @return the list of the pieces
     */
    public ArrayList<ArrayList<Piece>> getPieceList() {
        return pieces;
    }

    /**
     * Returns the column of an index of the board.
     *
     * @param index the index to look up
     * @return      the column of the index
     */
    public int getColumn(int index) {
        return model.getColumn(index);
    }

    /**
     * Returns the row of an index of the board.
     *
     * @param index the index to loop up
     * @return      the row of the index
     */
    public int getRow(int index) {
        return model.getRow(index);
    }

    /**
     * Returns how far away a piece is from its colors start row.
     * For example, row 1 gives a white piece a score of 0, black piece a score of 7.
     *
     * @param  color the color of the piece
     * @param  index the index where the piece is located
     * @return the advancement score of the piece
     */
    public int getAdvancement(ChessColor color, int index) {
        return model.getAdvancement(color, index);
    }

    /**
     * When the tile is empty, returns true, otherwise false.
     *
     * @param  index the index of the tile
     * @return true of tile is empty, otherwise false
     */
    public boolean isTileEmpty(int index) {
        return tile[index] == null;
    }

    /**
     * Returns the piece on a tile, or null if empty.
     *
     * @param  index the index ot the tile
     * @return the piece on the tile, or null if there is no piece
     */
    public Piece getTile(int index) {
        return tile[index];
    }

    /**
     * Returns the color of the piece on the index, if empty it returns EMPTY.
     *
     * @param index the index of the tile
     * @return      the color of the piece at the index, if empty it returns EMPTY
     */
    public ChessColor getColor(int index) {
        if (tile[index] == null)
            return ChessColor.EMPTY;

        return tile[index].getColor();
    }

    /**
     * If both pieces at the provides indexes have the same color, returns true.
     * If at least one of the tiles is null, it returns false.
     *
     * @param index1 the index of a tile
     * @param index2 the index of a tile
     * @return       true if pieces have the same color, false if not, also false if at at least one tile returns null
     */
    public boolean isSameColor(int index1, int index2) {
        if (tile[index1] == null || tile[index2] == null)
            return false;

        return tile[index1].getColor() == tile[index2].getColor();
    }

    /**
     * Returns whose turn it is.
     *
     * @return the color whose turn it is
     */
    public ChessColor getWhosTurn() {
        return whosTurn;
    }

    /**
     * Moves a piece to one tile to another both on the board array and the piece itself.
     * Doesn't clear the tile in the tile array where the piece is coming from.
     *
     * @param from
     * @param to
     */
    public void setPieceTile(int from, int to) {
        tile[from].setTile(to);
        tile[to] = tile[from];
    }

    /**
     * Sets a tile to a piece.
     *
     * @param index the index of the tile
     * @param piece the piece
     */
    public void setTile(int index, Piece piece) {
        tile[index] = piece;
    }

    /**
     * Calls the moveExecutor to execute a move.
     *
     * @param move the move to be executed
     */
    public void executeMove(Move move) {
        moveExecutor.executeMove(move);
    }

    /**
     * Calls the moveExecutor to reverse a move.
     *
     * @param move the move to be reversed
     */
    public void reverseMove(Move move) {
        moveExecutor.reverseMove(move);
    }

    /**
     * Prints the board on the console.
     */
    public void printBoard() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                char symbol = '-';
                int index = x + y * 8;

                if (tile[index] != null) {
                    switch (tile[index].getType()) {
                        case PAWN: symbol = 'P'; break;
                        case KNIGHT: symbol = 'N'; break;
                        case BISHOP: symbol = 'B'; break;
                        case ROOK: symbol = 'R'; break;
                        case QUEEN: symbol = 'Q'; break;
                        case KING: symbol = 'K'; break;
                    }

                    if (tile[index].getColor() == ChessColor.BLACK)
                        symbol = Character.toLowerCase(symbol);
                }
                System.out.print(symbol);
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Returns the tile index of a tile name. "A8" would return 0 for example.
     *
     * @param tileName the name of the tile
     * @return         the index of the tile
     */
    public int tileNameToIndex(String tileName) {
        return model.tileNameToIndex(tileName);
    }

    /**
     * Switches to the other players color.
     */
    public void changeWhosTurn() {
        whosTurn = whosTurn == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE;
    }

    /**
     * Sets the variable used for the 50-move/75-move rule.
     *
     * @param value the new value of the counter
     */
    public void setNoPawnMoveOrCaptureCounter(int value) {
        noPawnMoveOrCaptureCounter = value;
    }

    /**
     * Returns the variable used for the 50-move/75-move rule
     *
     * @return the variable used for the 50-move/75-move rule
     */
    public int getNoPawnMoveOrCaptureCounter() {
        return noPawnMoveOrCaptureCounter;
    }

    /**
     * Returns the king of the given color.
     *
     * @param color the color of the wanted king
     * @return      the king of the given color
     */
    public Piece getKing(ChessColor color) {
        return kings[colorHash.get(color)];
    }

    /**
     * Returns the lookupTables object
     *
     * @return the lookupTables object
     */
    public LookupTables getLookupTables() {
        return lookupTables;
    }

    /**
     * Removes that last piece on a piece list.
     * At the moment used to remove pieces after undoing pawn promotion.
     *
     * @param color the color of the piece to remove
     */
    public void removeLastPiece(ChessColor color) {
        pieces.get(colorHash.get(color)).remove(pieces.get(colorHash.get(color)).size() - 1);
    }
}