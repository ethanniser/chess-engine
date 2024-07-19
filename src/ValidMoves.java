import java.util.*;

public class ValidMoves {

    private static final int[] KING_OPERATIONS = { -11, -10, -9, -1, 1, 9, 10, 11 };
    private static final int[][] ROOK_OPERATIONS = {
            { -1, -2, -3, -4, -5, -6, -7 },
            { 1, 2, 3, 4, 5, 6, 7 },
            { -10, -20, -30, -40, -50, -60, -70 },
            { 10, 20, 30, 40, 50, 60, 70 }
    };
    private static final int[] KNIGHT_OPERATIONS = { -19, -21, -8, 12, 21, 19, 8, -12 };
    private static final int[][] BISHOP_OPERATIONS = {
            { -11, -22, -33, -44, -55, -66, -77 },
            { -9, -18, -27, -36, -45, -54, -63 },
            { 11, 22, 33, 44, 55, 66, 77 },
            { 9, 18, 27, 36, 45, 54, 63 }
    };

    private static final int[] BLACK_PAWN_OPERATIONS = { 10, 20, 9, 11 };
    private static final int[] WHITE_PAWN_OPERATIONS = { -10, -20, -9, -11 };

    public HashSet<Integer> possibleMoveFinderAllPieces(int location, int[][] bitboards) {
        HashSet<Integer> possibleMoves = new HashSet<>();
        int pieceType = -1;
        for (int i = 0; i < 12; i++) {
            if (bitboards[i][location] == 1) {
                pieceType = i;
                // sets the first value of the HashSet to the piecetype
            }
        }
        // checks what piecetype to determine what moves to find
        possibleMoves = switch (pieceType) {
            case 0 -> possibleRookMoves(location, 0, bitboards);
            case 1 -> possibleKnightMoves(location, 0, bitboards);
            case 2 -> possibleBishopMoves(location, 0, bitboards);
            case 3 -> possibleQueenMoves(location, 0, bitboards);
            case 4 -> possibleKingMoves(location, 0, bitboards);
            case 5 -> possibleBlackPawnMoves(location, bitboards);
            case 6 -> possibleRookMoves(location, 1, bitboards);
            case 7 -> possibleKnightMoves(location, 1, bitboards);
            case 8 -> possibleBishopMoves(location, 1, bitboards);
            case 9 -> possibleQueenMoves(location, 1, bitboards);
            case 10 -> possibleKingMoves(location, 1, bitboards);
            case 11 -> possibleWhitePawnMoves(location, bitboards);
            default -> possibleMoves;
        };

        if (!Arrays.deepEquals(bitboards, Board.pieceBoards)) {
            return possibleMoves;
        }

        return possibleMoves;
    }

    // returns list of possible moves for a king
    private HashSet<Integer> possibleKingMoves(int location, int side, int[][] bitboards) {
        HashSet<Integer> totalPossibleMoves = new HashSet<>();
        // runs through each possible operation and checks for validity
        for (int op : KING_OPERATIONS) {
            boolean cancelOperation = false;
            for (int i = 0; i < 12; i++) {
                if (bitboards[i][(location + op)] != 0) {
                    if (bitboards[i][(location + op)] == 2) {
                        // out of bounds
                        cancelOperation = true;
                        break;
                    }
                    // if the side is the same
                    if (side == (i / 6)) {
                        // same team
                        // makes it not a possible move
                        cancelOperation = true;
                        break;
                    } else {
                        // adds the possible move as a capture (+100)
                        totalPossibleMoves.add(location + op + 100);
                        cancelOperation = true;
                        break;
                    }
                }
            }
            if (Boolean.FALSE.equals(cancelOperation)) {
                totalPossibleMoves.add(location + op);
            }
        }
        return totalPossibleMoves;
    }

    // returns a list of possible knight moves
    private HashSet<Integer> possibleKnightMoves(int location, int side, int[][] bitboards) {
        HashSet<Integer> totalPossibleMoves = new HashSet<>();
        // runs through each possible operation and checks for validity
        for (int op : KNIGHT_OPERATIONS) {
            boolean cancelOperation = false;
            for (int i = 0; i < 12; i++) {
                if (bitboards[i][(location + op)] != 0) {
                    if (bitboards[i][(location + op)] == 2) {
                        // out of bounds
                        cancelOperation = true;
                        break;
                    }
                    // if the side is the same
                    if (side == (i / 6)) {
                        // same team
                        // makes it not a possible move
                        cancelOperation = true;
                        break;
                    } else {
                        // adds the possible move as a capture (+100)
                        totalPossibleMoves.add(location + op + 100);
                        cancelOperation = true;
                        break;
                    }
                }
            }
            if (Boolean.FALSE.equals(cancelOperation)) {
                totalPossibleMoves.add(location + op);
            }
        }

        int otherSide = switch (side) {
            case 0 -> 1;
            case 1 -> 0;
            default -> -1;
        };

        return totalPossibleMoves;
    }

    // returns a list of possible moves for a rook
    private HashSet<Integer> possibleRookMoves(int location, int side, int[][] bitboards) {
        HashSet<Integer> totalPossibleMoves = new HashSet<>();
        // iterates through the set of sets of rook ops
        for (int j = 0; j < 4; j++) {
            // iterates through each set of rook ops
            for (int i = 0; i < ROOK_OPERATIONS[1].length; i++) {
                // if out of bounds quit
                boolean cancelOperation = false;
                if (bitboards[0][location + ROOK_OPERATIONS[j][i]] == 2) {
                    break;
                } else {
                    for (int k = 0; k < 12; k++) {
                        // if there is a piece there then check if it is the enemy, if enemy allow it if
                        // friendly do not
                        // break the rest of the loop both times
                        if (bitboards[k][location + ROOK_OPERATIONS[j][i]] == 1) {
                            cancelOperation = true;
                            if (k / 6 == side) {
                                break;
                            } else {
                                totalPossibleMoves.add(location + ROOK_OPERATIONS[j][i] + 100);
                                break;
                            }
                        }
                    }
                }
                if (!cancelOperation) {
                    totalPossibleMoves.add(location + ROOK_OPERATIONS[j][i]);
                } else {
                    break;
                }
            }
        }

        int otherSide = switch (side) {
            case 0 -> 1;
            case 1 -> 0;
            default -> -1;
        };

        return totalPossibleMoves;
    }

    // returns a list of possible moves for a bishop
    private HashSet<Integer> possibleBishopMoves(int location, int side, int[][] bitboards) {
        HashSet<Integer> totalPossibleMoves = new HashSet<>();
        // iterates through the set of sets of bishop ops
        for (int j = 0; j < 4; j++) {
            // iterates through each set of bishop ops
            for (int i = 0; i < BISHOP_OPERATIONS[0].length; i++) {
                // if out of bounds quit
                boolean cancelOperation = false;
                if (bitboards[0][location + BISHOP_OPERATIONS[j][i]] == 2) {
                    break;
                } else {
                    for (int k = 0; k < 12; k++) {
                        // if there is a piece there then check if it is the enemy, if enemy allow it if
                        // friendly do not
                        // break the rest of the loop both times
                        if (bitboards[k][location + BISHOP_OPERATIONS[j][i]] == 1) {
                            cancelOperation = true;
                            if (k / 6 == side) {
                                break;
                            } else {
                                totalPossibleMoves.add(location + BISHOP_OPERATIONS[j][i] + 100);
                                break;
                            }
                        }
                    }
                }
                if (!cancelOperation) {
                    totalPossibleMoves.add(location + BISHOP_OPERATIONS[j][i]);
                } else {
                    break;
                }
            }
        }

        int otherSide = switch (side) {
            case 0 -> 1;
            case 1 -> 0;
            default -> -1;
        };

        return totalPossibleMoves;
    }

    // returns a list of possible queen moves
    private HashSet<Integer> possibleQueenMoves(int location, int side, int[][] bitboards) {
        HashSet<Integer> totalPossibleMoves = new HashSet<>(possibleRookMoves(location, side, bitboards));
        totalPossibleMoves.addAll(possibleBishopMoves(location, side, bitboards));
        return totalPossibleMoves;
    }

    // returns all possible moves for a black pawn
    private HashSet<Integer> possibleBlackPawnMoves(int location, int[][] bitboards) {
        HashSet<Integer> totalPossibleMoves = new HashSet<Integer>();
        // adds all not out of bounds moves to possible moves with the rest of the
        // method taking out invalid ones
        for (int op : BLACK_PAWN_OPERATIONS) {
            // sets a variable to -100 if the operation is a capture one
            if (!(bitboards[0][location + op] == 2)) {
                totalPossibleMoves.add(op + location);
            }
        }

        // removes moving forward once and twice if the forward move is unavailable
        for (int i = 0; i < 12; i++) {
            if (bitboards[i][location + BLACK_PAWN_OPERATIONS[0]] == 1) {
                totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[0]);
                totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[1]);
                break;
            }
        }

        if (location < 39 && location > 30) {
            for (int j = 0; j < 12; j++) {
                if (bitboards[j][location + BLACK_PAWN_OPERATIONS[1]] == 1) {
                    totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[1]);
                    break;
                }
            }
        } else {
            totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[1]);
        }

        for (int k = 0; k < 12; k++) {
            // checks for an enemy piece
            if (bitboards[k][location + BLACK_PAWN_OPERATIONS[2]] == 1) {
                if ((k / 6) == 0) {
                    totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[2]);
                } else {
                    totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[2]);
                    totalPossibleMoves.add(location + BLACK_PAWN_OPERATIONS[2] + 100);
                }
                break;
            }
            if (k == 11) {
                totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[2]);
            }
        }

        for (int l = 0; l < 12; l++) {
            if (bitboards[l][location + BLACK_PAWN_OPERATIONS[3]] == 1) {
                if ((l / 6) == 0) {
                    totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[3]);
                } else {
                    totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[3]);
                    totalPossibleMoves.add(location + BLACK_PAWN_OPERATIONS[3] + 100);
                }
                break;
            }
            if (l == 11) {
                totalPossibleMoves.remove(location + BLACK_PAWN_OPERATIONS[3]);
            }
        }
        return new HashSet<Integer>(totalPossibleMoves);
    }

    // returns all possible moves for a white pawn
    private HashSet<Integer> possibleWhitePawnMoves(int location, int[][] bitboards) {
        HashSet<Integer> totalPossibleMoves = new HashSet<Integer>();
        // adds all not out of bounds moves to possible moves with the rest of the
        // method taking out invalid ones
        for (int op : WHITE_PAWN_OPERATIONS) {
            // sets a variable to -100 if the operation is a capture one
            if (!(bitboards[0][location + op] == 2)) {
                totalPossibleMoves.add(op + location);
            }
        }

        // removes moving forward once and twice if the forward move is unavailable
        for (int i = 0; i < 12; i++) {
            if (bitboards[i][location + WHITE_PAWN_OPERATIONS[0]] == 1) {
                totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[0]);
                totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[1]);
                break;
            }
        }

        if (location < 89 && location > 80) {
            for (int j = 0; j < 12; j++) {
                if (bitboards[j][location + WHITE_PAWN_OPERATIONS[1]] == 1) {
                    totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[1]);
                    break;
                }
            }
        } else {
            totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[1]);
        }

        for (int k = 0; k < 12; k++) {
            // checks for an enemy piece
            if (bitboards[k][location + WHITE_PAWN_OPERATIONS[2]] == 1) {
                if ((k / 6) == 1) {
                    totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[2]);
                } else {
                    totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[2]);
                    totalPossibleMoves.add(location + WHITE_PAWN_OPERATIONS[2] + 100);
                }
                break;
            }
            if (k == 11) {
                totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[2]);
            }
        }

        for (int l = 0; l < 12; l++) {
            if (bitboards[l][location + WHITE_PAWN_OPERATIONS[3]] == 1) {
                if ((l / 6) == 1) {
                    totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[3]);
                } else {
                    totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[3]);
                    totalPossibleMoves.add(location + WHITE_PAWN_OPERATIONS[3] + 100);
                }
                break;
            }
            if (l == 11) {
                totalPossibleMoves.remove(location + WHITE_PAWN_OPERATIONS[3]);
            }
        }

        return new HashSet<Integer>(totalPossibleMoves);
    }

    public boolean isKingChecked(int[][] bitboards, int side, int[] kingLocations) {
        int otherSide = side == 1 ? 0 : 1;
        HashSet<Integer> totalPossibleMoves = new HashSet<>();
        // looks through enemy pieces and finds their possible moves
        for (int i = otherSide * 6; i < otherSide * 6 + 6; i++) {
            for (int j = 0; j < bitboards[0].length; j++) {
                if (bitboards[i][j] == 1) {
                    totalPossibleMoves.addAll(possibleMoveFinderAllPieces(j, bitboards));
                }
            }
        }
        return totalPossibleMoves.contains(kingLocations[side] + 100);
    }

    // returns if the move will put the king in check
    // CLBNLB = currentLocation&Bitboard, nextLocation&Bitboard
    public boolean willThisMovePutOurKingInCheck(int currentLocation, int currentBitboard, int nextLocation,
            int nextBitboard) {

        GameplayController tempGC = new GameplayController();
        // creates temporary bitboards to run isKingChecked on;
        int[][] tempBitboards = new int[12][120];
        int[] tempKingLocations = Arrays.copyOf(Board.kingLocations, 2);

        for (int i = 0; i < 12; i++) {
            System.arraycopy(Board.pieceBoards[i], 0, tempBitboards[i], 0, 120);
        }

        // simulates move change
        tempBitboards[currentBitboard][currentLocation] = 0;
        tempBitboards[nextBitboard][nextLocation] = 0;
        tempBitboards[currentBitboard][nextLocation] = 1;

        // simulates king location change
        if (currentBitboard == 4 || currentBitboard == 10) {
            tempKingLocations[currentBitboard / 6] = nextLocation;
            System.out.println("The new king location is " + nextLocation);
        }

        return isKingChecked(tempBitboards, currentBitboard / 6, tempKingLocations);
    }

    // finds all possible moves for a side
    public HashSet<Integer> allAvailableMoves(int[][] bitboards, int side) {
        HashSet<Integer> possibleMovesForSide = new HashSet<Integer>();
        for (int i = side * 6; i < (side * 6) + 6; i++) {
            for (int j = 0; j < bitboards[0].length; j++) {
                if (bitboards[i][j] != 1) {
                    continue;
                }
                // checks each piece to see if it removes the check
                HashSet<Integer> possibleMovesForPiece = possibleMoveFinderAllPieces(j, bitboards);
                possibleMovesForSide.addAll(possibleMovesForPiece);
                for (int possibleMove : possibleMovesForPiece) {
                    int nextBitboard = i;
                    if (possibleMove > 100) {
                        for (int k = 0; k < 12; k++) {
                            if (bitboards[k][possibleMove % 100] == 1) {
                                nextBitboard = k;
                                System.out.println(nextBitboard);
                            }
                        }
                    }
                    // if check is maintained then remove the move. it is impossible to make
                    if (willThisMovePutOurKingInCheck(j, i, possibleMove % 100, nextBitboard)) {
                        possibleMovesForSide.remove(possibleMove);
                    }
                }

            }
        }
        return possibleMovesForSide;
    }

}
