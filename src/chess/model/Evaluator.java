package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;

import java.util.HashMap;

public class Evaluator {
    private Board board;
    private HashMap<PieceType, Integer> pieceHash;
    private HashMap<PieceType, Integer> pieceValues;
    private int[][][] position_score;

    public Evaluator(Board board) {
        this.board = board;

        pieceHash = new HashMap<>();
        pieceHash.put(PieceType.PAWN, 0);
        pieceHash.put(PieceType.KNIGHT, 1);
        pieceHash.put(PieceType.BISHOP, 2);
        pieceHash.put(PieceType.ROOK, 3);
        pieceHash.put(PieceType.QUEEN, 4);
        pieceHash.put(PieceType.KING, 5);

        initPieceValues();
        initPositionScores();
    }

    public int getScore() {
        int whiteScore = 0;
        int blackScore = 0;

        for (var list : board.getPieceList())
            for (var piece : list)
                if (piece.isActive())
                    if (piece.getColor() == ChessColor.WHITE)
                        whiteScore += pieceValues.get(piece.getType()) + position_score[0][pieceHash.get(piece.getType())][piece.getTile()];
                    else
                        blackScore += pieceValues.get(piece.getType()) + position_score[0][pieceHash.get(piece.getType())][piece.getTile()];

        /*for (int i = 0; i < 64; i++)
            if (!board.isTileEmpty(i))
                if (board.getColor(i) == ChessColor.WHITE)
                    whiteScore += pieceValues.get(board.getTile(i).getType());
                else
                    blackScore += pieceValues.get(board.getTile(i).getType());*/

        return whiteScore - blackScore;
    }

    private void initPieceValues() {
        // https://chess.stackexchange.com/questions/5941/what-relative-point-values-of-pieces-do-engines-use
        pieceValues = new HashMap<>();
        pieceValues.put(PieceType.PAWN, 126);
        pieceValues.put(PieceType.KNIGHT, 781);
        pieceValues.put(PieceType.BISHOP, 825);
        pieceValues.put(PieceType.ROOK, 1276);
        pieceValues.put(PieceType.QUEEN, 2538);
        pieceValues.put(PieceType.KING, 15000);
    }

    private void initPositionScores() {
        position_score = new int[2][6][64];

        position_score[0][pieceHash.get(PieceType.PAWN)] = new int[]{
                0, 0, 0, 0, 0, 0, 0, 0,
                100, 100, 100, 100, 100, 100, 100, 100,
                4, 8, 10, 16, 16, 10, 8, 4,
                0, 2, 4, 8, 8, 4, 2, 0,
                0, 2, 4, 8, 8, 4, 2, 0,
                0, 2, 4, 4, 4, 4, 2, 0,
                0, 2, 4, -12, -12, 4, 2, 0,
                0, 0, 0, 0, 0, 0, 0, 0 };

        position_score[0][pieceHash.get(PieceType.KNIGHT)] = new int[]{
                -150, -20, -10, -5, -5, -10, -20, -150,
                -20, -10, -2, 0, 0, -2, -10, -20,
                -10, -2, 4, 6, 6, 4, -2, -10,
                -5, 0, 6, 8, 8, 6, 0, -5,
                -5, 0, 6, 8, 8, 6, 0, -5,
                -8, -2, 4, 6, 6, 4, -2, -8,
                -16, -6, -2, 0, 0, -2, -6, -16,
                -30, -20, -10, -8, -8, -10, -20, -30 };

        position_score[0][pieceHash.get(PieceType.BISHOP)] = new int[]{
                -10, -10, -10, -10, -10, -10, -10, -10,
                -10, 4, 4, 4, 4, 4, 4, -10,
                2, 4, 6, 6, 6, 6, 4, 2,
                2, 4, 6, 8, 8, 6, 4, 2,
                2, 4, 6, 8, 8, 6, 4, 2,
                2, 4, 6, 6, 6, 6, 4, 2,
                0, 4, 4, 4, 4, 4, 4, 0,
                -10, -10, -12, -10, -10, -12, -10, -10 };

        position_score[0][pieceHash.get(PieceType.ROOK)] = new int[]{
                10, 10, 10, 10, 10, 10, 10, 10,
                20, 20, 20, 20, 20, 20, 20, 20,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                4, 4, 4, 6, 6, 4, 4, 4 };

        position_score[0][pieceHash.get(PieceType.QUEEN)] = new int[]{
                -10, -10, 2, 2, 2, 2, -10, -10,
                -10, -10, 2, 2, 2, 2, -10, -10,
                2, 2, 2, 3, 3, 2, 2, 2,
                2, 2, 3, 4, 4, 3, 2, 2,
                2, 2, 3, 4, 4, 3, 2, 2,
                2, 2, 2, 3, 3, 2, 2, 2,
                -10, 2, 2, 2, 2, 2, 2, -10,
                -10, -10, -6, -4, -4, -6, -10, -10 };

        position_score[0][pieceHash.get(PieceType.KING)] = new int[]{
                -48, -48, -48, -48, -48, -48, -48, -48,
                -48, -48, -48, -48, -48, -48, -48, -48,
                -48, -48, -48, -48, -48, -48, -48, -48,
                -48, -48, -48, -48, -48, -48, -48, -48,
                -48, -48, -48, -48, -48, -48, -48, -48,
                -48, -48, -48, -48, -48, -48, -48, -48,
                15, 20, -25, -30, -30, -25, 20, 15,
                20, 20, 20, -40, 10, -60, 20, 20 };

        for (int i = 0; i < 64; i++)
            for (int j = 0; j < 6; j++)
                position_score[1][j][i] = position_score[0][j][63 - i];
    }


}
