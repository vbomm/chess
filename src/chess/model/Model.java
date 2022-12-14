package chess.model;

import chess.controller.ChessColor;
import chess.controller.MoveHistory;
import chess.controller.PieceType;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * The Model class. Takes commands from/gives updates to Controller.
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
     * Generates moves and captures, then removes invalid moves. This is used for move highlighting in the UI and end game detection.
     * If it is not the turn of the human, Engine gets called to generate the next move as a CompletableFuture.
     */
    public void nextHalfStep() {
        moveGenerator.findMovesAndCaptures();
        moveGenerator.removeInvalidMoves();

        System.out.println(moveGenerator.getLastGeneratedMoves().size() + " " + moveGenerator.getLastGeneratedCaptures().size());
        if (moveGenerator.getLastGeneratedMoves().size() + moveGenerator.getLastGeneratedCaptures().size() == 0)
            System.out.println("no moves possible");

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
        }
    }

    /**
     * If a piece at the given index has access to the destination index, returns true, otherwise false.
     *
     * @param startIndex       index of the start
     * @param destinationIndex index of the destination
     * @return                 yes if start can move to destination, false if not
     */
    public boolean hasPieceAccessToTile(int startIndex, int destinationIndex) {
        return findAndGetMove(startIndex, destinationIndex) != null;
    }

    /**
     * If a piece at the given index has access to the destination index, returns true, otherwise false.
     *
     * @param startIndex       index of the start
     * @param destinationIndex index of the destination
     * @return                 yes if start can move to destination, false if not
     */
    public Move findAndGetMove(int startIndex, int destinationIndex) {
        for (var m : moveGenerator.getLastGeneratedMoves())
            if (startIndex == m.getStart() && destinationIndex == m.getDestination())
                return m;

        for (var m : moveGenerator.getLastGeneratedCaptures())
            if (startIndex == m.getStart() && destinationIndex == m.getDestination())
                return m;

        return null;
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
     * Returns true if a tile is empty, false otherwise.
     *
     * @param  index the index of the tile
     * @return       true if empty, false if not
     */
    public boolean isTileEmpty(int index) {
        return board.isTileEmpty(index);
    }

    /**
     * Returns the tile on the given index. If empty, returns null, otherwise the Piece.
     *
     * @param   index the index of the tile
     * @return  null if tile is empty, the Piece on the tile otherwise
     */
    public Piece getTile(int index) {
        return board.getTile(index);
    }

    /**
     * Returns the color of the piece at index. EMPTY if empty.
     *
     * @param index the index of the tile
     * @return      the color of the piece, if tile empty returns EMPTY
     */
    public ChessColor getColor(int index) {
        return board.getColor(index);
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
     * @param startIndex       the index of the start tile
     * @param destinationIndex the index of the destination tile
     * @return                 true if move is possible, false if not
     */
    public boolean movePiece(int startIndex, int destinationIndex) {
        Move m = findAndGetMove(startIndex, destinationIndex);
        if (m == null)
            return false;

        board.executeMove(m);

        return true;
    }
}