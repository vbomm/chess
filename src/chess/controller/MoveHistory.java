package chess.controller;

import chess.model.Model;
import chess.model.Move;

import java.util.ArrayList;

public class MoveHistory {
    private Model model;
    private ArrayList<Move> moveHistory;

    /**
     * Sets model and initializes moveHistory.
     *
     * @param model the Model object
     */
    public MoveHistory(Model model) {
        this.model = model;
        moveHistory = new ArrayList<>();
    }

    /**
     * Returns the move history.
     *
     * @return the move history
     */
    public ArrayList<Move> getMoveHistory() {
        return moveHistory;
    }

    /**
     * Generates and returns a move history in the form of notations.
     *
     * @return notation move history
     */
    public ArrayList<String> getMoveHistoryNotation() {
        ArrayList<String> historyNotation = new ArrayList<>();

        for (var move : moveHistory)
            if (move.getTarget() == null)
                historyNotation.add(model.indexToTileName(move.getStart()) + "->" + model.indexToTileName(move.getDestination()));
            else
                historyNotation.add(model.indexToTileName(move.getStart()) + "x" + model.indexToTileName(move.getDestination()));

        return historyNotation;
    }

    /**
     * Adds move to history.
     *
     * @param move move to be added to history
     */
    public void addMoveToHistory(Move move) {
        moveHistory.add(move);
    }

    /**
     * Removes move from history.
     *
     * @param move move to be removed from history
     */
    public void removeMoveFromHistory(Move move) {
        moveHistory.remove(move);
    }
}
