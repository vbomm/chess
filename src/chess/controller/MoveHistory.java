package chess.controller;

import chess.model.Move;

import java.util.ArrayList;

public class MoveHistory {
    private ArrayList<Move> list;

    public MoveHistory() {
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
