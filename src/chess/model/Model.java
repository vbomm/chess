package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;

import java.util.HashMap;

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

    public void nextHalfStep() {
        if (board.getWhosTurn() == ChessColor.BLACK) {
            engine.move(depth);
            nextHalfStep();
        } else {
            moveGenerator.findMovesAndCaptures();
            moveGenerator.removeInvalidMoves();
        }
    }

    public boolean hasPieceAccessToTile(int startX, int startY, int destinationX, int destinationY) {
        return findAndGetMove(startX, startY, destinationX, destinationY) != null;
    }

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

    public int indexToColumn(int i) {
        return column[i];
    }

    public int indexToRow(int i) {
        return row[i];
    }

    private void initColumn() {
        column = new int[64];

        for (int i = 0; i < 64; i++)
            column[i] = i % 8;
    }

    private void initRow() {
        row = new int[64];

        for (int i = 0; i < 64; i++)
            row[i] = i / 8;
    }

    private void initAdvancement() {
        advancement = new int[2][64];

        for (int i = 0; i < 64; i++) {
            advancement[0][i] = (63 - i) / 8;
            advancement[1][i] = i / 8;
        }
    }

    public int getColumn(int i) {
        return column[i];
    }

    public int getRow(int i) {
        return row[i];
    }

    public int getAdvancement(ChessColor color, int index) {
        return advancement[colorHash.get(color)][index];
    }

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

    public int tileNameToIndex(String tileName) {
        return tileNameHash.get(tileName);
    }

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

    private int coordinatesToIndex(int x, int y) {
        return x + y * 8;
    }

    public boolean isTileEmpty(int x, int y) {
        return board.isTileEmpty(coordinatesToIndex(x, y));
    }

    public Piece getTile(int x, int y) {
        return board.getTile(coordinatesToIndex(x, y));
    }

    public ChessColor getColor(int x, int y) {
        return board.getColor(coordinatesToIndex(x, y));
    }

    public ChessColor getWhosTurn() {
        return board.getWhosTurn();
    }

    public boolean movePiece(int startX, int startY, int destinationX, int destinationY) {
        Move m = findAndGetMove(startX, startY, destinationX, destinationY);
        if (m == null)
            return false;

        board.executeMove(m);

        return true;
    }
}