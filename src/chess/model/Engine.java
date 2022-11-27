package chess.model;

import chess.controller.ChessColor;

import java.util.ArrayList;

/**
 * The engine to generate moves of the CPU player.
 */
public class Engine {
    private Board board;
    private MoveGenerator moveGenerator;
    private Evaluator evaluator;

    /**
     * The default constructor.
     *
     * @param board         the Board object
     * @param moveGenerator the moveGenerator object
     * @param evaluator     the Evaluator object
     */
    public Engine(Board board, MoveGenerator moveGenerator, Evaluator evaluator) {
        this.board = board;
        this.moveGenerator = moveGenerator;
        this.evaluator = evaluator;
    }

    /**
     * Starts to generate and find the best move.
     *
     * @param depth the depth of the search
     */
    public void move(int depth) {
        moveGenerator.findMovesAndCaptures();
        ArrayList<Move> captures = moveGenerator.getLastGeneratedCaptures();
        ArrayList<Move> moves = moveGenerator.getLastGeneratedMoves();
        int bestWhiteScore = Integer.MIN_VALUE;
        int bestBlackScore = Integer.MAX_VALUE;
        int bestScore = board.getWhosTurn() == ChessColor.WHITE ? bestWhiteScore : bestBlackScore;
        int currentScore;
        Move bestMove = null;

        if (!captures.isEmpty())
            for (var c : captures) {
                board.executeMove(c);
                currentScore = search(depth, bestWhiteScore, bestBlackScore);
                board.reverseMove(c);
                if (board.getWhosTurn() == ChessColor.WHITE) {
                    if (currentScore > bestScore || currentScore == bestScore && Math.random() < 0.5) {
                        bestMove = c;
                        bestScore = currentScore;
                        bestWhiteScore = Math.max(bestWhiteScore, currentScore);
                        //if (bestBlackScore <= bestWhiteScore)
                        //    break;
                    }
                } else {
                    if (currentScore < bestScore || currentScore == bestScore && Math.random() < 0.5) {
                        bestMove = c;
                        bestScore = currentScore;
                        bestBlackScore = Math.min(bestBlackScore, currentScore);
                        //if (bestBlackScore <= bestWhiteScore)
                        //    break;
                    }
                }
            }

        if (!moves.isEmpty())
            for (var m : moves) {
                board.executeMove(m);
                currentScore = search(depth, bestWhiteScore, bestBlackScore);
                board.reverseMove(m);
                if (board.getWhosTurn() == ChessColor.WHITE) {
                    if (currentScore > bestScore || currentScore == bestScore && Math.random() < 0.5) {
                        bestMove = m;
                        bestScore = currentScore;
                        bestWhiteScore = Math.max(bestWhiteScore, currentScore);
                        //if (bestBlackScore <= bestWhiteScore)
                        //    break;
                    }
                } else {
                    if (currentScore < bestScore || currentScore == bestScore && Math.random() < 0.5) {
                        bestMove = m;
                        bestScore = currentScore;
                        bestBlackScore = Math.min(bestBlackScore, currentScore);
                        //if (bestBlackScore <= bestWhiteScore)
                        //    break;
                    }
                }
            }
        else
            System.err.println("No moves found");

        if (bestMove != null)
            board.executeMove(bestMove);
        else {
            System.out.println(captures.size() + " " + moves.size());
            System.err.println("Was unable to move");
            board.changeWhosTurn();
        }
    }

    /**
     * Method to generate moves recursively
     *
     * @param depth          the depth of the search
     * @param bestWhiteScore the best white score
     * @param bestBlackScore the best black score
     * @return               the value of the move
     */
    public int search(int depth, int bestWhiteScore, int bestBlackScore) {
        if (--depth == 0)
            return evaluator.getScore();

        moveGenerator.findMovesAndCaptures();
        ArrayList<Move> captures = moveGenerator.getLastGeneratedCaptures();
        ArrayList<Move> moves = moveGenerator.getLastGeneratedMoves();
        int bestScore = board.getWhosTurn() == ChessColor.WHITE ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentscore;

        if (!captures.isEmpty())
            for (var c : captures) {
                board.executeMove(c);
                currentscore = search(depth, bestWhiteScore, bestBlackScore);
                board.reverseMove(c);
                if (board.getWhosTurn() == ChessColor.WHITE) {
                    if (currentscore > bestScore || currentscore == bestScore && Math.random() < 0.5) {
                        bestScore = currentscore;
                        bestWhiteScore = Math.max(bestWhiteScore, currentscore);
                        //if (bestBlackScore <= bestWhiteScore)
                        //    break;
                    }
                } else {
                    if (currentscore < bestScore || currentscore == bestScore && Math.random() < 0.5) {
                        bestScore = currentscore;
                        bestBlackScore = Math.min(bestBlackScore, currentscore);
                        //if (bestBlackScore <= bestWhiteScore)
                        //    break;
                    }
                }
            }
        if (!moves.isEmpty())
            for (var m : moves) {
                board.executeMove(m);
                currentscore = search(depth, bestWhiteScore, bestBlackScore);
                board.reverseMove(m);
                if (board.getWhosTurn() == ChessColor.WHITE) {
                    if (currentscore > bestScore || currentscore == bestScore && Math.random() < 0.5) {
                        bestScore = currentscore;
                        bestWhiteScore = Math.max(bestWhiteScore, currentscore);
                        //if (bestBlackScore <= bestWhiteScore)
                        //    break;
                    }
                } else {
                    if (currentscore < bestScore || currentscore == bestScore && Math.random() < 0.5) {
                        bestScore = currentscore;
                        bestBlackScore = Math.min(bestBlackScore, currentscore);
                        //if (bestBlackScore <= bestWhiteScore)
                        //    break;
                    }
                }
            }
        else {
            System.err.println("No moves found");
            board.changeWhosTurn();
            bestScore = evaluator.getScore();
        }

        return bestScore;
    }
}
