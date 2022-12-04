package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;

import java.util.HashMap;

/**
 * The Model.
 */
public class Model {
    private int[] column;
    private int[] row;
    private int[][] advancement;
    private String[] tileNames;
    private HashMap<String, Integer> tileNameHash;
    private Board board;
    private MoveGenerator moveGenerator;
    private Evaluator evaluator;
    private int depth;
    private Engine engine;

private HashMap<ChessColor, Integer> colorHash;

    /**
     * Calls method to put pieces on their starting points and calls for the first half step to be generated or received.
     */
    public Model() {
        colorHash = new HashMap<>();
        colorHash.put(ChessColor.WHITE, 0);
        colorHash.put(ChessColor.BLACK, 1);

        initColumn();
        initRow();
        initAdvancement();
        initCellNames();

        board = new Board(this);
        moveGenerator = new MoveGenerator(board);
        evaluator = new Evaluator(board);
        depth = 4;
        engine = new Engine(board, moveGenerator, evaluator);

        setupBoard();
        nextHalfStep();
    }

    /**
     * Generates next half-step.
     * If it is the humans turn, it creates the move and capture list so those can be highlighted when a piece is selected
     * and moves can be validated.
     * If it is the CPUs turn, the Engine gets called to generate the next move.
     */
    public void nextHalfStep() {
        if (board.getWhosTurn() == ChessColor.BLACK) {
            engine.move(depth);
            nextHalfStep();
        } else {
            moveGenerator.findMovesAndCaptures();
            moveGenerator.removeInvalidMoves();
        }
    }

    /**
     * If a piece at the given coordinates has access to the destination coordinates, returns true, otherwise false.
     *
     * @param startX       x-coordinate of the start
     * @param startY       y-coordinate of the start
     * @param destinationX x-coordinate of the destination
     * @param destinationY y-coordinate of the destination
     * @return             yes if start can move to destination, false if not
     */
    public boolean hasPieceAccessToTile(int startX, int startY, int destinationX, int destinationY) {
        return findAndGetMove(startX, startY, destinationX, destinationY) != null;
    }

    /**
     * If a piece at the given coordinates has access to the destination coordinates, returns true, otherwise false.
     *
     * @param startX       start index of the start
     * @param startY       y-coordinate of the start
     * @param destinationX x-coordinate of the destination
     * @param destinationY y-coordinate of the destination
     * @return             yes if start can move to destination, false if not
     */
    public Move findAndGetMove(int startX, int startY, int destinationX, int destinationY) {
        int start = coordinatesToIndex(startX, startY);
        int destination = coordinatesToIndex(destinationX, destinationY);

        for (var m : moveGenerator.getLastGeneratedMoves())
            if (start == m.getStart() && destination == m.getDestination())
                return m;

        for (var m : moveGenerator.getLastGeneratedCaptures())
            if (start == m.getStart() && destination == m.getDestination())
                return m;

        return null;
    }

    /**
     * Returns the column of the given index.
     *
     * @param i the index of the tile
     * @return  the column of the index
     */
    public int indexToColumn(int i) {
        return column[i];
    }

    /**
     * Returns the row of the given index.
     *
     * @param i the index of the tile
     * @return  the row of the index
     */
    public int indexToRow(int i) {
        return row[i];
    }

    /**
     * Generates the value of the column for each tile index.
     */
    private void initColumn() {
        column = new int[64];

        for (int i = 0; i < 64; i++)
            column[i] = i % 8;
    }

    /**
     * Generates the value of the row for each tile index.
     */
    private void initRow() {
        row = new int[64];

        for (int i = 0; i < 64; i++)
            row[i] = i / 8;
    }

    /**
     * Creates the advancement lookup table for white and black.
     * Value ranges from 0-7. Black gets highest score at row 1, white at row 8.
     */
    private void initAdvancement() {
        advancement = new int[2][64];

        for (int i = 0; i < 64; i++) {
            advancement[0][i] = (63 - i) / 8;
            advancement[1][i] = i / 8;
        }
    }

    /**
     * Returns the column of the given index.
     *
     * @param i the index
     * @return  the column
     */
    public int getColumn(int i) {
        return column[i];
    }

    /**
     * Returns the row of the given index.
     *
     * @param i the index
     * @return  the row
     */
    public int getRow(int i) {
        return row[i];
    }

    /**
     * Returns the advancement value.
     *
     * @param color the color of the piece
     * @param index the index of the tile
     * @return      the advancement value
     */
    public int getAdvancement(ChessColor color, int index) {
        return advancement[colorHash.get(color)][index];
    }

    /**
     * Generates the HashMap to convert tile names to tile indexes.
     * For example, "A8" returns 0.
     */
    private void initCellNames() {
        String coloumnNames = "ABCDEFGH";
        tileNames = new String[64];
        tileNameHash = new HashMap<>();


        for (int y = 0; y < 8; y++)
            for (int x = 0; x < 8; x++)
                tileNames[x + y * 8] = coloumnNames.charAt(x) + "" + (8 - y);

        for (int i = 0; i < 64; i++)
                tileNameHash.put(tileNames[i], i);
    }

    /**
     * Returns the index of a tile name.
     *
     * @param tileName the tile name
     * @return         the index
     */
    public int tileNameToIndex(String tileName) {
        return tileNameHash.get(tileName);
    }

    /**
     * Sets up the starting position of a board.
     */
    private void setupBoard() {
        for (int i = 0; i < 64; i++)
            board.clear();

        for (int i = 0; i < 8; i++)
            board.addPiece(PieceType.PAWN, ChessColor.WHITE, tileNameHash.get("A2") + i);

        board.addPiece(PieceType.ROOK, ChessColor.WHITE, tileNameHash.get("A1"));
        board.addPiece(PieceType.KNIGHT, ChessColor.WHITE, tileNameHash.get("B1"));
        board.addPiece(PieceType.BISHOP, ChessColor.WHITE, tileNameHash.get("C1"));
        board.addPiece(PieceType.QUEEN, ChessColor.WHITE, tileNameHash.get("D1"));
        board.addPiece(PieceType.KING, ChessColor.WHITE, tileNameHash.get("E1"));
        board.addPiece(PieceType.BISHOP, ChessColor.WHITE, tileNameHash.get("F1"));
        board.addPiece(PieceType.KNIGHT, ChessColor.WHITE, tileNameHash.get("G1"));
        board.addPiece(PieceType.ROOK, ChessColor.WHITE, tileNameHash.get("H1"));

        for (int i = 0; i < 8; i++)
            board.addPiece(PieceType.PAWN, ChessColor.BLACK, tileNameHash.get("A7") + i);

        board.addPiece(PieceType.ROOK, ChessColor.BLACK, tileNameHash.get("A8"));
        board.addPiece(PieceType.KNIGHT, ChessColor.BLACK, tileNameHash.get("B8"));
        board.addPiece(PieceType.BISHOP, ChessColor.BLACK, tileNameHash.get("C8"));
        board.addPiece(PieceType.QUEEN, ChessColor.BLACK, tileNameHash.get("D8"));
        board.addPiece(PieceType.KING, ChessColor.BLACK, tileNameHash.get("E8"));
        board.addPiece(PieceType.BISHOP, ChessColor.BLACK, tileNameHash.get("F8"));
        board.addPiece(PieceType.KNIGHT, ChessColor.BLACK, tileNameHash.get("G8"));
        board.addPiece(PieceType.ROOK, ChessColor.BLACK, tileNameHash.get("H8"));
    }

    /**
     * Converts coordinates to an index of a tile.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return  the index
     */
    private int coordinatesToIndex(int x, int y) {
        return x + y * 8;
    }

    /**
     * Returns true if a tile is empty, false otherwise.
     *
     * @param  x the x-coordinate
     * @param  y the y-coordinate
     * @return true if empty, false if not
     */
    public boolean isTileEmpty(int x, int y) {
        return board.isTileEmpty(coordinatesToIndex(x, y));
    }

    /**
     * Returns the tile on the given coordinates. If empty, returns null, otherwise the Piece.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return  null if tile is empty, the Piece on the tile otherwise
     */
    public Piece getTile(int x, int y) {
        return board.getTile(coordinatesToIndex(x, y));
    }

    /**
     * Returns the color of the piece on a tile. EMPTY if empty.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return  the color of the piece, if tile empty returns EMPTY
     */
    public ChessColor getColor(int x, int y) {
        return board.getColor(coordinatesToIndex(x, y));
    }

    /**
     * Returns the color of the players whose turn it is.
     *
     * @return the color of the players whose turn it is
     */
    public ChessColor getWhosTurn() {
        return board.getWhosTurn();
    }

    /**
     * If start can't move to destination, returns falls, otherwise moves the piece.
     *
     * @param startX       the x-coordinate of the start tile
     * @param startY       the y-coordinate of the start tile
     * @param destinationX the x-coordinate of the destination tile
     * @param destinationY the y-coordinate of the destination tile
     * @return             true if move is possible, false if not
     */
    public boolean movePiece(int startX, int startY, int destinationX, int destinationY) {
        Move m = findAndGetMove(startX, startY, destinationX, destinationY);
        if (m == null)
            return false;

        board.executeMove(m);

        return true;
    }
}