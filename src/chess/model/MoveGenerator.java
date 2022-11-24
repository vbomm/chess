package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;
import chess.controller.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MoveGenerator {
    private ArrayList<Move> moves;
    private ArrayList<Move> captures;
    private Board board;
    private boolean[] threats;
    private int[] castleKingStart;
    private int[] longCastleRookStart;
    private int[] longCastleRookDestination;
    private int[] shortCastleRookStart;
    private int[] shortCasteRookDestination;
    private int[][] longCastleEmptyTiles;
    private int[][] shortCastleEmptyTiles;
    private int[] longCastleKingDestination;
    private int[] shortCastleKingDestination;
    private int[][] whoCanEnPassant;
    private int[] enPassantDestinationTile;
    private HashMap<ChessColor, Integer> colorHash;

    public MoveGenerator(Board board) {
        this.board = board;
        colorHash = new HashMap<>();
        colorHash.put(ChessColor.WHITE, 0);
        colorHash.put(ChessColor.BLACK, 1);

        initCastling();
        initEnPassant();
    }

    private void initCastling() {
        castleKingStart = new int[2];
        longCastleRookStart = new int[2];
        longCastleRookDestination = new int[2];
        shortCastleRookStart = new int[2];
        shortCasteRookDestination = new int[2];
        longCastleEmptyTiles = new int[2][3];
        shortCastleEmptyTiles = new int[2][2];
        longCastleKingDestination = new int[2];
        shortCastleKingDestination = new int[2];

        castleKingStart[Type.white()] = board.tileNameToIndex("E1");

        longCastleRookStart[Type.white()] = board.tileNameToIndex("A1");
        longCastleRookDestination[Type.white()] = board.tileNameToIndex("D1");
        longCastleEmptyTiles[Type.white()][0] = board.tileNameToIndex("B1");
        longCastleEmptyTiles[Type.white()][1] = board.tileNameToIndex("C1");
        longCastleEmptyTiles[Type.white()][2] = board.tileNameToIndex("D1");
        longCastleKingDestination[Type.white()] = board.tileNameToIndex("C1");

        shortCastleKingDestination[Type.white()] = board.tileNameToIndex("G1");
        shortCastleEmptyTiles[Type.white()][0] = board.tileNameToIndex("F1");
        shortCastleEmptyTiles[Type.white()][1] = board.tileNameToIndex("G1");
        shortCastleRookStart[Type.white()] = board.tileNameToIndex("H1");
        shortCasteRookDestination[Type.white()] = board.tileNameToIndex("F1");

        longCastleRookStart[Type.black()] = board.tileNameToIndex("A8");
        longCastleRookDestination[Type.black()] = board.tileNameToIndex("D8");
        longCastleEmptyTiles[Type.black()][0] = board.tileNameToIndex("B8");
        longCastleEmptyTiles[Type.black()][1] = board.tileNameToIndex("C8");
        longCastleEmptyTiles[Type.black()][2] = board.tileNameToIndex("D8");
        castleKingStart[Type.black()] = board.tileNameToIndex("E8");
        longCastleKingDestination[Type.black()] = board.tileNameToIndex("C8");

        shortCastleKingDestination[Type.black()] = board.tileNameToIndex("G8");
        shortCastleEmptyTiles[Type.black()][0] = board.tileNameToIndex("F8");
        shortCastleEmptyTiles[Type.black()][1] = board.tileNameToIndex("G8");
        shortCastleRookStart[Type.black()] = board.tileNameToIndex("H8");
        shortCasteRookDestination[Type.black()] = board.tileNameToIndex("F8");
    }

    private void initEnPassant() {
        whoCanEnPassant = new int[64][2];
        enPassantDestinationTile = new int[64];

        for (int i = 0; i < 24; i++) {
            whoCanEnPassant[i][0] = -1;
            whoCanEnPassant[i][1] = -1;
            enPassantDestinationTile[i] = -1;
        }
        for (int i = 24; i < 32; i++) {
            // black pawn can get captured
            if (board.getColumn(i) == 0) {
                whoCanEnPassant[i][0] = i + 1;
                whoCanEnPassant[i][1] = -1;
            } else if (board.getColumn(i) == 7) {
                whoCanEnPassant[i][0] = i - 1;
                whoCanEnPassant[i][1] = -1;
            } else {
                whoCanEnPassant[i][0] = i - 1;
                whoCanEnPassant[i][1] = 1 + 1;
            }
            enPassantDestinationTile[i] = i - 8;
        }
        for (int i = 32; i < 39; i++) {
            // white pawn can get captured
            if (board.getColumn(i) == 0) {
                whoCanEnPassant[i][0] = i + 1;
                whoCanEnPassant[i][1] = -1;
            } else if (board.getColumn(i) == 7) {
                whoCanEnPassant[i][0] = i - 1;
                whoCanEnPassant[i][1] = -1;
            } else {
                whoCanEnPassant[i][0] = i - 1;
                whoCanEnPassant[i][1] = 1 + 1;
            }
            enPassantDestinationTile[i] = i + 8;
        }
        for (int i = 40; i < 64; i++) {
            whoCanEnPassant[i][0] = -1;
            whoCanEnPassant[i][1] = -1;
            enPassantDestinationTile[i] = -1;
        }
    }

    public void findMovesAndCaptures() {
        findThreats();

        moves = new ArrayList<>();
        captures = new ArrayList<>();

        findEnPassant();

        for (int i = 0; i < board.getPieceList().get(colorHash.get(board.getWhosTurn())).size(); i++)
            board.getPieceList().get(colorHash.get(board.getWhosTurn())).get(i).generateMovesAndCaptures(moves, captures);

        findCastling(moves);
    }

    public ArrayList<Move> getLastGeneratedMoves() {
        return moves;
    }

    public ArrayList<Move> getLastGeneratedCaptures() {
        return captures;
    }

    private void findThreats() {
        threats = new boolean[64];
        int color = colorHash.get(board.getWhosTurn() == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE);

        for (int i = 0; i < board.getPieceList().get(color).size(); i++)
            board.getPieceList().get(color).get(i).generateThreats(threats);
    }

    public boolean[] getLastGeneratedThreats() {
        return threats;
    }

    private void findCastling(ArrayList<Move> moves) {
        int color = colorHash.get(board.getWhosTurn());

        // general conditions
        if (!castleKingStart(color))
            return;

        // long castling
        if (longCastleRookStart(color) && longCastleEmptyTiles(color)) {
            moves.add(new Move(castleKingStart[color], longCastleKingDestination[color], board.getTile(castleKingStart[color]), null, board.getNoPawnMoveOrCaptureCounter()));
            return;
        }

        //short castling
        if (shortCastleRookStart(color) && shortCastleEmptyTiles(color))
            moves.add(new Move(castleKingStart[color], shortCastleKingDestination[color], board.getTile(castleKingStart[color]), null, board.getNoPawnMoveOrCaptureCounter()));
    }

    private boolean castleKingStart(int color) {
        return board.getTile(castleKingStart[color]) != null && board.getTile(castleKingStart[color]).neverMoved() && !threats[castleKingStart[color]]
                && board.getTile(castleKingStart[color]).getType() == PieceType.KING && board.getColor(castleKingStart[color]) == board.getWhosTurn();
    }

    private boolean longCastleRookStart(int color) {
        return (board.getTile(longCastleRookStart[color]) != null && board.getTile(longCastleRookStart[color]).neverMoved()
                && board.getTile(longCastleRookStart[color]).getType() == PieceType.ROOK && board.getColor(longCastleRookStart[color]) == board.getWhosTurn());
    }

    private boolean longCastleEmptyTiles(int color) {
        return !threats[longCastleEmptyTiles[color][0]] && !threats[longCastleEmptyTiles[color][1]] && !threats[longCastleEmptyTiles[color][2]]
                && board.isTileEmpty(longCastleEmptyTiles[color][0]) && board.isTileEmpty(longCastleEmptyTiles[color][1]) && board.isTileEmpty(longCastleEmptyTiles[color][2]);
    }

    private boolean shortCastleRookStart(int color) {
        return (board.getTile(shortCastleRookStart[color]) != null && board.getTile(shortCastleRookStart[color]).neverMoved()
                && board.getTile(shortCastleRookStart[color]).getType() == PieceType.ROOK && board.getColor(shortCastleRookStart[color]) == board.getWhosTurn());
    }

    private boolean shortCastleEmptyTiles(int color) {
        return !threats[shortCastleEmptyTiles[color][0]] && !threats[shortCastleEmptyTiles[color][1]]
                && board.isTileEmpty(shortCastleEmptyTiles[color][0]) && board.isTileEmpty(shortCastleEmptyTiles[color][1]);
    }

    private void findEnPassant() {
        if (board.getMoveHistory() == null || board.getMoveHistory().size() == 0)
            return;

        Move move = board.getMoveHistory().get(board.getMoveHistory().size() - 1);

        if (move.getPiece().getType() == PieceType.PAWN && Math.abs(move.getStart() - move.getDestination()) == 16)
            if (whoCanEnPassant[move.getDestination()][0] != -1
                    && !board.isTileEmpty(whoCanEnPassant[move.getDestination()][0])
                    && board.getTile(whoCanEnPassant[move.getDestination()][0]).getType() == PieceType.PAWN
                    && board.isTileEmpty(enPassantDestinationTile[move.getDestination()])
                    && board.getTile(whoCanEnPassant[move.getDestination()][0]).getColor() != move.getPiece().getColor())
                moves.add(new Move(whoCanEnPassant[move.getDestination()][0],
                        enPassantDestinationTile[move.getDestination()],
                        board.getTile(whoCanEnPassant[move.getDestination()][0]), board.getTile(move.getDestination()), board.getNoPawnMoveOrCaptureCounter()
                ));
            if (whoCanEnPassant[move.getDestination()][1] != -1
                    && !board.isTileEmpty(whoCanEnPassant[move.getDestination()][1])
                    && board.getTile(whoCanEnPassant[move.getDestination()][1]).getType() == PieceType.PAWN
                    && board.isTileEmpty(enPassantDestinationTile[move.getDestination()])
                    && board.getTile(whoCanEnPassant[move.getDestination()][1]).getColor() != move.getPiece().getColor())
                moves.add(new Move(whoCanEnPassant[move.getDestination()][1],
                        enPassantDestinationTile[move.getDestination()],
                        board.getTile(whoCanEnPassant[move.getDestination()][1]), board.getTile(move.getDestination()), board.getNoPawnMoveOrCaptureCounter()
                ));
    }

    public void removeInvalidMoves() {
        // board.getNoPawnMoveOrCaptureCounter();

        LinkedList<Move> capturesToRemove = new LinkedList<>();
        LinkedList<Move> movesToRemove = new LinkedList<>();

        for (int cm = 0; cm < 2; cm++) {
            ArrayList<Move>  moveList;
            if (cm == 0) moveList = moves;
            else moveList = captures;

            for (var m : moveList) {
                board.executeMove(m);

                boolean[] threats = new boolean[64];
                for (int i = 0; i < board.getPieceList().get(1).size(); i++)
                    board.getPieceList().get(1).get(i).generateThreats(threats);

                if (threats[board.getKing(ChessColor.WHITE).getTile()]) {
                    movesToRemove.add(m);

                    if (cm == 0) movesToRemove.add(m);
                    else capturesToRemove.add(m);
                }

                board.reverseMove(m);
            }
        }

        while (!capturesToRemove.isEmpty())
            captures.remove(capturesToRemove.pop());

        while (!movesToRemove.isEmpty())
            moves.remove(movesToRemove.pop());
    }
}
