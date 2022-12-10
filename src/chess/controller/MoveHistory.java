package chess.controller;

import chess.model.Model;
import chess.model.Move;

import java.util.ArrayList;

public class MoveHistory {
    private Model model;
    private ArrayList<Move> list;

    public MoveHistory(Model model) {
        this.model = model;
        list = new ArrayList<>();
    }

    /**
     * Returns the move history.
     *
     * @return the move history
     */
    public ArrayList<Move> getMoveHistory() {
        return list;
    }

    public ArrayList<String> getMoveHistoryNotation() {
        ArrayList<String> historyNotation = new ArrayList<>();

        for (var move : list)
            historyNotation.add(model.indexToTileName(move.getStart()) + "->" + model.indexToTileName(move.getDestination()));

        return historyNotation;
    }

    /**
     * Adds move to history.
     *
     * @param move move to be added to history
     */
    public void addMoveToHistory(Move move) {
        list.add(move);
    }

    /**
     * Removes move from history.
     *
     * @param move move to be removed from history
     */
    public void removeMoveFromHistory(Move move) {
        list.remove(move);
    }
}
