package chess.model;

import chess.controller.ChessColor;
import chess.controller.PieceType;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {
    private Model model;
    private LookupTables lookupTables;
    private Piece[] tile;
    private ArrayList<ArrayList<Piece>> pieces;
    private boolean whiteCanLongCastle;
    private boolean whiteCanShortCastle;
    private boolean blackCanLongCastle;
    private boolean blackCanShortCastle;
    private ChessColor whosTurn;
    private int noPawnMoveOrCaptureCounter;
    private ArrayList<Move> moveHistory;
    private HashMap<ChessColor, Integer> colorHash;
    private Piece[] kings;

    public Board(Model model) {
        this.model = model;
        lookupTables = new LookupTables(this);

        tile = new Piece[64];
        kings = new King[2];

        noPawnMoveOrCaptureCounter = 0;

        colorHash = new HashMap<>();
        colorHash.put(ChessColor.WHITE, 0);
        colorHash.put(ChessColor.BLACK, 1);

        initBoard();
    }

    private void initBoard() {
        moveHistory = new ArrayList<>();
        whosTurn = ChessColor.WHITE;
        whiteCanLongCastle = true;
        whiteCanShortCastle = true;
        blackCanLongCastle = true;
        blackCanShortCastle = true;

        initPieceLists();
    }

    public void clear() {
        initBoard();
        for (int i = 0; i < 64; i++)
            tile[i] = null;
    }

    private void initPieceLists() {
        pieces = new ArrayList<>();
        ArrayList<Piece> whitePieces = new ArrayList<>();
        ArrayList<Piece> blackPieces = new ArrayList<>();
        pieces.add(whitePieces);
        pieces.add(blackPieces);
    }

    public void addPiece(PieceType type, ChessColor color, int index) {
        //if (tile[index] != null)
        //    return;

        switch (type) {
            case PAWN: tile[index] = new Pawn(this, color, index); break;
            case KNIGHT: tile[index] = new Knight(this, color, index); break;
            case BISHOP: tile[index] = new Bishop(this, color, index); break;
            case ROOK: tile[index] = new Rook(this, color, index); break;
            case QUEEN: tile[index] = new Queen(this, color, index); break;
            case KING: tile[index] = new King(this, color, index); kings[colorHash.get(color)] = tile[index]; break;
        }

        pieces.get(colorHash.get(color)).add(tile[index]);
    }

    public ArrayList<ArrayList<Piece>> getPieceList() {
        return pieces;
    }

    public int getColumn(int index) {
        return model.getColumn(index);
    }

    public int getRow(int index) {
        return model.getRow(index);
    }

    public int getAdvancement(ChessColor color, int index) {
        return model.getAdvancement(color, index);
    }

    //public void setTile(int index, int value) {
    //    tile[index] = value;
    //}

    public boolean isTileEmpty(int index) {
        return tile[index] == null;
    }

    //public void setTile(int index, int tile, int color) {
    //    this.tile[index] = tile;
    //    this.color[index] = color;
    //}

    public Piece getTile(int index) {
        return tile[index];
    }

    //public void setColor(int index, int value) {
    //    color[index] = value;
    //}

    public ChessColor getColor(int index) {
        if (tile[index] == null)
            return ChessColor.EMPTY;

        return tile[index].getColor();
    }

    public boolean isSameColor(int index1, int index2) {
        if (tile[index1] == null || tile[index2] == null)
            return false;

        return tile[index1].getColor() == tile[index2].getColor();
    }

    public void setWhosTurn(ChessColor value) {
        whosTurn = value;
    }

    public ChessColor getWhosTurn() {
        return whosTurn;
    }


    private void setPieceTile(int from, int to) {
        tile[from].setTile(to);
        tile[to] = tile[from];
    }

    public void executeMove(Move move) {
        // 50 and 75 move rule
        if (move.getPiece().getType() == PieceType.PAWN || move.getTarget() != null)
            noPawnMoveOrCaptureCounter = 0;
        else
            noPawnMoveOrCaptureCounter = move.getNoPawnMoveOrCaptureCounter() + 1;

        if (tile[move.getStart()].getType() == PieceType.KING) {
            kings[colorHash.get(move.getPiece().getColor())] = move.getPiece();

            if (move.getStart() - move.getDestination() == 2)
                // long castling
                if (move.getStart() == 4) {
                    // black
                    setPieceTile(0, 3);
                    tile[0] = null;
                } else {
                    // white
                    setPieceTile(56, 59);
                    tile[56] = null;
                }
            else if (move.getStart() - move.getDestination() == -2)
                // short castling
                if (move.getStart() == 4) {
                    // black
                    setPieceTile(7, 5);
                    tile[7] = null;
                } else {
                    // white
                    setPieceTile(63, 61);
                    tile[63] = null;
                }
        }

        // deactivate target
        if (move.getTarget() != null)
            move.getTarget().deactivate();

        // move piece
        setPieceTile(move.getStart(), move.getDestination());
        tile[move.getStart()] = null;

        // pawn special moves
        if (tile[move.getDestination()].getType() == PieceType.PAWN) {
            // promotion
            if (model.getAdvancement(whosTurn, move.getDestination()) == 7) {
                move.getPiece().deactivate();
                addPiece(PieceType.QUEEN, whosTurn, move.getDestination());
            }

            // en passant
            if (move.getTarget() != null && move.getDestination() != move.getTarget().getTile())
                tile[move.getTarget().getTile()] = null;
        }

        move.getPiece().increaseMoveCounter();
        moveHistory.add(move);

        changeWhosTurn();
    }

    public void reverseMove(Move move) {
        noPawnMoveOrCaptureCounter = move.getNoPawnMoveOrCaptureCounter() - 1;

        if (tile[move.getDestination()].getType() == PieceType.KING) {
            kings[colorHash.get(move.getPiece().getColor())] = move.getPiece();

            if (move.getStart() - move.getDestination() == 2)
                // long castling
                if (move.getStart() == 4) {
                    // black
                    setPieceTile(3, 0);
                    tile[3] = null;
                } else {
                    // white
                    setPieceTile(59, 56);
                    tile[59] = null;
                }
            else if (move.getStart() - move.getDestination() == -2)
                // short castling
                if (move.getStart() == 4) {
                    // black
                    setPieceTile(5, 7);
                    tile[5] = null;
                } else {
                    // white
                    setPieceTile(61, 63);
                    tile[61] = null;
                }
        }

        // undo promotion, remove queen from piece list and activate pawn
        if (tile[move.getDestination()].getType() == PieceType.QUEEN && move.getPiece().getType() == PieceType.PAWN) {
            pieces.get(colorHash.get(move.getPiece().getColor())).remove(pieces.get(colorHash.get(move.getPiece().getColor())).size() - 1);
            tile[move.getDestination()].deactivate();
            tile[move.getDestination()] = null;
            move.getPiece().activate();
        }
        // undo move, also works to undo promotion as long pawn gets activated and queen deactivated
        tile[move.getStart()] = move.getPiece();
        move.getPiece().setTile(move.getStart());
        tile[move.getStart()].setTile(move.getStart());

        // undo en passant
        if (move.getTarget() != null && move.getDestination() != move.getTarget().getTile()) {
            tile[move.getTarget().getTile()] = move.getTarget();
            tile[move.getDestination()] = null;
        } else
            tile[move.getDestination()] = move.getTarget();

        if (move.getTarget() != null)
            move.getTarget().activate();

        move.getPiece().decreaseMoveCounter();
        moveHistory.remove(move);

        changeWhosTurn();
    }

    public void printBoard() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                char symbol = '-';
                int index = x + y * 8;

                if (tile[index] != null) {
                    switch (tile[index].getType()) {
                        case PAWN: symbol = 'P'; break;
                        case KNIGHT: symbol = 'N'; break;
                        case BISHOP: symbol = 'B'; break;
                        case ROOK: symbol = 'R'; break;
                        case QUEEN: symbol = 'Q'; break;
                        case KING: symbol = 'K'; break;
                    }

                    if (tile[index].getColor() == ChessColor.BLACK)
                        symbol = Character.toLowerCase(symbol);
                }
                System.out.print(symbol);
            }
            System.out.println();
        }
        System.out.println();
    }

    public int tileNameToIndex(String tileName) {
        return model.tileNameToIndex(tileName);
    }

    public void changeWhosTurn() {
        whosTurn = whosTurn == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE;
    }

    public ArrayList<Move> getMoveHistory() {
        return moveHistory;
    }

    public int getNoPawnMoveOrCaptureCounter() {
        return noPawnMoveOrCaptureCounter;
    }

    public Piece getKing(ChessColor color) {
        return kings[colorHash.get(color)];
    }

    public LookupTables getLookupTables() {
        return lookupTables;
    }

    //public void addMove(int start, int destination) {}
    //public void addCapture(int start, int destination) {}
}