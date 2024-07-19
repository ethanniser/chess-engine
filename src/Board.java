public class Board {

    // declares white pieces
    static int[] whiteRooks, whiteKnights, whiteBishops, whiteQueen, whiteKing, whitePawns = new int[120];

    // black pieces
    static int[] blackRooks, blackKnights, blackBishops, blackQueen, blackKing, blackPawns = new int[120];

    static int[][] pieceBoards = { blackRooks, blackKnights, blackBishops, blackQueen, blackKing, blackPawns,
            whiteRooks, whiteKnights, whiteBishops, whiteQueen, whiteKing, whitePawns };

    // determines side to move
    static boolean whiteTurn = true;
    static boolean[] kingsChecked = { false, false };
    static int[] kingLocations = { -1, -1 };

    // creates a default board template used to generate the bitboards
    private static final String[][] boardTemplate = {
            { "2", "2", "2", "2", "2", "2", "2", "2", "2", "2" },
            { "2", "2", "2", "2", "2", "2", "2", "2", "2", "2" },
            { "2", "r", "n", "b", "q", "k", "b", "n", "r", "2" },
            { "2", "p", "p", "p", "p", "p", "p", "p", "p", "2" },
            { "2", " ", " ", " ", " ", " ", " ", " ", " ", "2" },
            { "2", " ", " ", " ", " ", " ", " ", " ", " ", "2" },
            { "2", " ", " ", " ", " ", " ", " ", " ", " ", "2" },
            { "2", " ", " ", " ", " ", " ", " ", " ", " ", "2" },
            { "2", "P", "P", "P", "P", "P", "P", "P", "P", "2" },
            { "2", "R", "N", "B", "Q", "K", "B", "N", "R", "2" },
            { "2", "2", "2", "2", "2", "2", "2", "2", "2", "2" },
            { "2", "2", "2", "2", "2", "2", "2", "2", "2", "2" }
    };

    // initializes the bitboards
    private static void createBitboards(int[][] bitboards) {
        // runs the creator for each individual board
        for (int i = 0; i < 12; i++) {
            // logs the current board
            int[] currentBoard = new int[120];
            // base board without any logged pieces
            StringBuilder baseLong = new StringBuilder(
                    "222222222222222222222000000002200000000220000000022000000002200000000220000000022000000002200000000222222222222222222222");
            for (int j = 0; j < 120; j++) {
                String currentPiece = boardTemplate[j / 10][j % 10];

                if (i == 0) {
                    if (currentPiece.equals("r")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 1) {
                    if (currentPiece.equals("n")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 2) {
                    if (currentPiece.equals("b")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 3) {
                    if (currentPiece.equals("q")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 4) {
                    if (currentPiece.equals("k")) {
                        baseLong.replace(j, j + 1, "1");
                        kingLocations[0] = j;
                    }
                }
                if (i == 5) {
                    if (currentPiece.equals("p")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 6) {
                    if (currentPiece.equals("R")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 7) {
                    if (currentPiece.equals("N")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 8) {
                    if (currentPiece.equals("B")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 9) {
                    if (currentPiece.equals("Q")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }
                if (i == 10) {
                    if (currentPiece.equals("K")) {
                        baseLong.replace(j, j + 1, "1");
                        kingLocations[1] = j;
                    }
                }
                if (i == 11) {
                    if (currentPiece.equals("P")) {
                        baseLong.replace(j, j + 1, "1");
                    }
                }

            }
            for (int k = 0; k < 120; k++) {
                currentBoard[k] = Integer.parseInt(baseLong.substring(k, k + 1));
            }
            bitboards[i] = currentBoard;
        }
    }

    // sets the boardlist to the newly generated boards and prints the board to the
    // console
    public static void generateBoards() {
        createBitboards(pieceBoards);
        for (int i = 0; i < 120; i++) {
            if (i % 10 == 0) {
                System.out.println();
            }
            System.out.print(pieceBoards[5][i]);
        }
    }

    // gui usage
    public static boolean isThereAPieceThere(int rowpass, int colpass) {
        int startingIndex = 0;
        if (Boolean.TRUE.equals(whiteTurn)) {
            startingIndex = 6;
        }
        int pieceIndex = (((rowpass * 8) + 20) + (colpass + 1)) + (rowpass * 2);
        for (int i = startingIndex; i < startingIndex + 6; i++) {
            if (pieceBoards[i][pieceIndex] == 1) {
                return true;
            }
        }
        return false;
    }

    // evaluates which side is winning and by how much
    public static double evaluateBoard(int[][] bitboards) {
        double totalScore = 0;
        for (int i = 0; i < 12; i++) {
            double baseValue = 0;
            switch (i) {
                case 0:
                    baseValue = -5;
                    break;
                case 1, 2:
                    baseValue = -3;
                    break;
                case 3:
                    baseValue = -9;
                    break;
                case 4, 10:
                    baseValue = 0;
                    break;
                case 5:
                    baseValue = -1;
                    break;
                case 6:
                    baseValue = 5;
                    break;
                case 7, 8:
                    baseValue = 3;
                    break;
                case 9:
                    baseValue = 9;
                    break;
                case 11:
                    baseValue = 1;
                    break;
            }
            // runs through each bitboard, checks if there is a piece there
            for (int j = 0; j < bitboards[i].length; j++) {
                if (bitboards[i][j] == 1) {
                    // if there is a piece there, find the value and find its distance from the
                    // center and multiply that by the base value
                    double centerDistance = Math.abs(Conv.to64From120(j) % 8 - 3.5);
                    double heightDistance = Math.abs(Conv.to64From120(j) / 8 - 3.5);
                    totalScore += (baseValue / (centerDistance * heightDistance)) / 4;
                }
            }
        }
        return (double) Math.round(totalScore * 100) / 100;
    }

    // gets the boards to use for gui and eval functions
    public static int[][] getBitboards() {
        return pieceBoards;
    }

    public static String[][] getBoardTemplate() {
        return boardTemplate;
    }

}