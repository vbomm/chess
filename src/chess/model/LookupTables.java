package chess.model;

public class LookupTables {
    private Board board;
    private int[][] knight;
    private int[][] bishop;
    private int[][] rook;
    private int[][] queen;

    public LookupTables(Board board) {
        this.board = board;

        initKnight();
        initBishop();
        initRook();
        initQueen();
    }

    private void initKnight() {
        knight = new int[64][9];

        for (int i = 0; i < 64; i++) {
            int m = 0;
            int column = board.getColumn(i);
            int row = board.getRow(i);

            if (row < 6 && column < 7) knight[i][m++] = i + 17;
            if (row < 7 && column < 6) knight[i][m++] = i + 10;
            if (row < 6 && column > 0) knight[i][m++] = i + 15;
            if (row < 7 && column > 1) knight[i][m++] = i + 6;
            if (row > 1 && column < 7) knight[i][m++] = i - 15;
            if (row > 0 && column < 6) knight[i][m++] = i - 6;
            if (row > 1 && column > 0) knight[i][m++] = i - 17;
            if (row > 0 && column > 1) knight[i][m++] = i - 10;

            knight[i][m] = -1;
        }
    }

    private void initBishop() {
        bishop = new int[64][5];

        for (int i = 0; i < 64; i++) {
            for (int m = 0; m < 5; m++)
                bishop[i][m] = -1;

            int column = board.getColumn(i);
            int row = board.getRow(i);

            if (column < 7 && row < 7) bishop[i][0] = i + 9;
            if (column < 7 && row > 0) bishop[i][1] = i - 7;
            if (column > 0 && row > 0) bishop[i][2] = i - 9;
            if (column > 0 && row < 7) bishop[i][3] = i + 7;
        }
    }

    private void initRook() {
        rook = new int[64][5];

        for (int i = 0; i < 64; i++) {
            for (int m = 0; m < 5; m++)
                rook[i][m] = -1;

            int column = board.getColumn(i);
            int row = board.getRow(i);

            if (row < 7) rook[i][0] = i + 8;
            if (column < 7) rook[i][1] = i + 1;
            if (row > 0) rook[i][2] = i - 8;
            if (column > 0) rook[i][3] = i - 1;
        }
    }

    private void initQueen() {
        queen = new int[64][9];

        for (int i = 0; i < 64; i++) {
            for (int m = 0; m < 9; m++)
                queen[i][m] = -1;

            int column = board.getColumn(i);
            int row = board.getRow(i);

            if (row < 7) queen[i][0] = i + 8;
            if (column < 7 && row < 7) queen[i][1] = i + 9;
            if (column < 7) queen[i][2] = i + 1;
            if (column < 7 && row > 0) queen[i][3] = i - 7;
            if (row > 0) queen[i][4] = i - 8;
            if (column > 0 && row > 0) queen[i][5] = i - 9;
            if (column > 0) queen[i][6] = i - 1;
            if (column > 0 && row < 7) queen[i][7] = i + 7;
        }
    }

    public int[][] getKnight() {
        return knight;
    }

    public int[][] getBishop() {
        return bishop;
    }

    public int[][] getRook() {
        return rook;
    }

    public int[][] getQueen() {
        return queen;
    }
}
