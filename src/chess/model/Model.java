package chess.model;

import chess.controller.ChessColor;
import chess.controller.MoveHistory;
import chess.controller.PieceType;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * The Model.
 */
public class Model {
    private int[] column;
    private int[] row;
    private int[][] advancement;
    private String[] tileNames;
    private HashMap<String, Integer> indexNameHash;
    private HashMap<Integer, String> nameIndexHash;
    private Board board;
    private MoveGenerator moveGenerator;
    private Evaluator evaluator;
    private MoveHistory moveHistory;
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
        generateTileNames();
        generateNameTiles();

        moveHistory = new MoveHistory(this);
        board = new Board(this, moveHistory);
        moveGenerator = new MoveGenerator(board, moveHistory);
        evaluator = new Evaluator(board);
        depth = 4;
        engine = new Engine(board, moveGenerator, evaluator, depth);

        setupBoard();
        nextHalfStep();
    }

    /**
     * Generates next half-step.
     * If it is the humans turn, it creates the move and capture list so those can be highlighted when a piece is selected
     * and moves can be validated.
     * If it is the turn of the human, the moves and captures of this move will get created so they can be used to validate moves.
     * Otherwise the Engine gets called to generate the next move as a CompletableFuture.
     */
    public void nextHalfStep() {
        if (board.getWhosTurn() == ChessColor.BLACK) {
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(engine::move);

            CompletableFuture<Void> future = completableFuture
                    .thenRun(() -> nextHalfStep());

            try {
                future.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        } else {
            moveGenerator.findMovesAndCaptures();
            moveGenerator.removeInvalidMoves();
        }

        System.out.println(moveHistory.getMoveHistoryNotation());
    }

    /**
     * If a piece at the given coordinates has access to the destination coordinates, returns true, otherwise false.
     *
     * @param startIndex       x-coordinate of the start
     * @param destinationIndex x-coordinate of the destination
     * @return                 yes if start can move to destination, false if not
     */
    public boolean hasPieceAccessToTile(int startIndex, int destinationIndex) {
        return findAndGetMove(startIndex % 8, startIndex / 8, destinationIndex % 8, destinationIndex / 8) != null;
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
    private void generateTileNames() {
        String coloumnNames = "ABCDEFGH";
        tileNames = new String[64];
        indexNameHash = new HashMap<>();


        for (int y = 0; y < 8; y++)
            for (int x = 0; x < 8; x++)
                tileNames[x + y * 8] = coloumnNames.charAt(x) + "" + (8 - y);

        for (int i = 0; i < 64; i++)
                indexNameHash.put(tileNames[i], i);
    }

    /**
     * Generates the HashMap to convert tile indexes to tile names.
     * For example, 0 returns "A8".
     */
    private void generateNameTiles() {
        String coloumnNames = "ABCDEFGH";
        nameIndexHash = new HashMap<>();

        for (int y = 0; y < 8; y++)
            for (int x = 0; x < 8; x++)
                nameIndexHash.put(x + y * 8, coloumnNames.charAt(x) + "" + (8 - y));
    }

    /**
     * Returns the index of a tile name.
     *
     * @param tileName the name of the tile
     * @return         the index of the tile
     */
    public int tileNameToIndex(String tileName) {
        return indexNameHash.get(tileName);
    }

    /**
     * Returns toe tile name of the index.
     *
     * @param index the index of the tile
     * @return      the name of the tile
     */
    public String indexToTileName(int index) {
        return nameIndexHash.get(index);
    }

    /**
     * Sets up the starting position of a board.
     */
    private void setupBoard() {
        for (int i = 0; i < 64; i++)
            board.clear();

        for (int i = 0; i < 8; i++)
            board.addPiece(PieceType.PAWN, ChessColor.WHITE, indexNameHash.get("A2") + i);

        board.addPiece(PieceType.ROOK, ChessColor.WHITE, indexNameHash.get("A1"));
        board.addPiece(PieceType.KNIGHT, ChessColor.WHITE, indexNameHash.get("B1"));
        board.addPiece(PieceType.BISHOP, ChessColor.WHITE, indexNameHash.get("C1"));
        board.addPiece(PieceType.QUEEN, ChessColor.WHITE, indexNameHash.get("D1"));
        board.addPiece(PieceType.KING, ChessColor.WHITE, indexNameHash.get("E1"));
        board.addPiece(PieceType.BISHOP, ChessColor.WHITE, indexNameHash.get("F1"));
        board.addPiece(PieceType.KNIGHT, ChessColor.WHITE, indexNameHash.get("G1"));
        board.addPiece(PieceType.ROOK, ChessColor.WHITE, indexNameHash.get("H1"));

        for (int i = 0; i < 8; i++)
            board.addPiece(PieceType.PAWN, ChessColor.BLACK, indexNameHash.get("A7") + i);

        board.addPiece(PieceType.ROOK, ChessColor.BLACK, indexNameHash.get("A8"));
        board.addPiece(PieceType.KNIGHT, ChessColor.BLACK, indexNameHash.get("B8"));
        board.addPiece(PieceType.BISHOP, ChessColor.BLACK, indexNameHash.get("C8"));
        board.addPiece(PieceType.QUEEN, ChessColor.BLACK, indexNameHash.get("D8"));
        board.addPiece(PieceType.KING, ChessColor.BLACK, indexNameHash.get("E8"));
        board.addPiece(PieceType.BISHOP, ChessColor.BLACK, indexNameHash.get("F8"));
        board.addPiece(PieceType.KNIGHT, ChessColor.BLACK, indexNameHash.get("G8"));
        board.addPiece(PieceType.ROOK, ChessColor.BLACK, indexNameHash.get("H8"));
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