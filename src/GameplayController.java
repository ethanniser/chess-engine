import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;

public class GameplayController implements MouseListener {

    int selectedPiece = -1;
    HashSet<Integer> possibleMovesForSelectedPiece = null;
    int selectedPieceType = -1;
    ValidMoves vm = new ValidMoves();

    // makes the selected square gray and the possible moves red
    private void setPieceSelectedGUI(int rowpass, int colpass) {
        selectedPiece = (Conv.to120RC(rowpass, colpass));
        possibleMovesForSelectedPiece = vm.possibleMoveFinderAllPieces(selectedPiece, Board.pieceBoards);
        // sets the current piece type
        for (int i = 0; i < 12; i++) {
            if (Board.pieceBoards[i][Conv.to120RC(rowpass, colpass)] == 1) {
                selectedPieceType = i;
            }
        }
        int otherSide = selectedPieceType > 5 ? 0 : 1;

        // iterates through all possible moves and put moves that check into a hashset
        // to avoid concurrent modification exception
        HashSet<Integer> movesToRemove = new HashSet<>();
        int nextBitboard = selectedPieceType;
        for (int possibleMove : possibleMovesForSelectedPiece) {
            // locates the bitboard of the piece being captured if it is a capture
            if (possibleMove > 100) {
                for (int i = otherSide * 6; i < otherSide * 6 + 6; i++) {
                    System.out.println(i);
                    if (Board.pieceBoards[i][possibleMove - 100] == 1) {
                        nextBitboard = i;
                    }
                }
            }
            if (vm.willThisMovePutOurKingInCheck(selectedPiece, selectedPieceType, possibleMove % 100, nextBitboard)) {
                movesToRemove.add(possibleMove);
            }
        }
        possibleMovesForSelectedPiece.removeAll(movesToRemove);
        System.out.println("Possible moves are: " + possibleMovesForSelectedPiece);

        if (possibleMovesForSelectedPiece.contains(Board.kingLocations[otherSide] + 100)) {
            possibleMovesForSelectedPiece.remove(Board.kingLocations[otherSide] + 100);
            Board.kingsChecked[0] = true;
        }

        if ((ChessGUI.isPieceSelected() == 0)) {
            ChessGUI.JLabelCollection[(rowpass * 8) + colpass].setBackground(Color.DARK_GRAY);
            ChessGUI.setPieceSelectedTrue(rowpass, colpass);
            // handles showing the possible moves
            for (int possibleMove : possibleMovesForSelectedPiece) {
                // checks for capture
                if (possibleMove > 100) {
                    ChessGUI.JLabelCollection[Conv.to64From120(possibleMove - 100)].setBackground(Color.GREEN);
                    continue;
                }
                ChessGUI.JLabelCollection[Conv.to64From120(possibleMove)].setBackground(Color.RED);
            }
        }

        // deselects and sets colors back
        else if ((ChessGUI.getPointSelected() - colpass) - (rowpass * 8) == 0) {
            selectedPiece = -1;
            selectedPieceType = -1;
            setSquareToOriginalColor(rowpass, colpass);
            for (int possibleMoveToRemove : possibleMovesForSelectedPiece) {
                // checks for capture
                if (possibleMoveToRemove > 100) {
                    possibleMoveToRemove -= 100;
                }
                setSquareToOriginalColor(Conv.toRC120(possibleMoveToRemove)[0], Conv.toRC120(possibleMoveToRemove)[1]);
            }
            ChessGUI.setPieceSelectedFalse();
            possibleMovesForSelectedPiece = null;
        }

        ChessGUI.placeBoardsAgain();
    }

    // sets square back to its original color
    private void setSquareToOriginalColor(int rowpass, int colpass) {
        if (rowpass % 2 == 0) {
            if (colpass % 2 == 0) {
                ChessGUI.JLabelCollection[(rowpass * 8) + colpass].setBackground(new Color(225, 215, 183));
            } else {
                ChessGUI.JLabelCollection[(rowpass * 8) + colpass].setBackground(new Color(57, 40, 20));
            }
        } else if (colpass % 2 == 0) {
            ChessGUI.JLabelCollection[(rowpass * 8) + colpass].setBackground(new Color(57, 40, 20));
        } else {
            ChessGUI.JLabelCollection[(rowpass * 8) + colpass].setBackground(new Color(225, 215, 183));
        }
    }

    public int[][] movePiece(int row, int col, int[][] bitboards) {
        int currentSquare = Conv.to120RC(row, col);
        boolean onPossibleSquare = false;
        boolean capturingMove = false;
        if (possibleMovesForSelectedPiece == null) {
            return bitboards;
        }
        // searches through all possible moves for selected piece
        for (int currentPossibleMove : possibleMovesForSelectedPiece) {
            // checks for capture
            if (currentPossibleMove > 100) {
                currentPossibleMove -= 100;
                capturingMove = true;
            }
            // if the player clicks on a possible move, indicate that with the boolean
            if (currentPossibleMove == currentSquare) {
                onPossibleSquare = true;
                break;
            }
        }

        // if the player clicks on a not possible move return, no piece to move
        if (!onPossibleSquare) {
            return bitboards;
        }

        // sets the bitboard spot of the captured piece to 0 if the move is a capture
        if (capturingMove) {
            for (int i = 0; i < selectedPieceType; i++) {
                if (bitboards[i][currentSquare] == 1) {
                    bitboards[i][currentSquare] = 0;
                }
            }
            if (selectedPieceType != 11) {
                for (int j = selectedPieceType + 1; j < 12; j++) {
                    if (bitboards[j][currentSquare] == 1) {
                        bitboards[j][currentSquare] = 0;
                    }
                }
            }
        }

        // removes king from being a possible capture, 4 for black king 10 for white.
        if (selectedPieceType == 4) {
            Board.kingLocations[0] = currentSquare;
        } else if (selectedPieceType == 10) {
            Board.kingLocations[1] = currentSquare;
        }

        bitboards[selectedPieceType][currentSquare] = 1;
        bitboards[selectedPieceType][selectedPiece] = 0;

        setSquareToOriginalColor(Conv.toRC120(selectedPiece)[0], Conv.toRC120(selectedPiece)[1]);
        for (int possibleMoveToRemove : possibleMovesForSelectedPiece) {
            // checks for capture
            if (possibleMoveToRemove > 100) {
                possibleMoveToRemove -= 100;
            }
            // resets square of possible highlighted move
            setSquareToOriginalColor(Conv.toRC120(possibleMoveToRemove)[0], Conv.toRC120(possibleMoveToRemove)[1]);
        }

        ChessGUI.JLabelCollection[Conv.to64From120(currentSquare)]
                .setIcon(ChessGUI.JLabelCollection[Conv.to64From120(selectedPiece)].getIcon());
        ChessGUI.JLabelCollection[Conv.to64From120(selectedPiece)].setIcon(null);

        selectedPiece = -1;
        selectedPieceType = -1;
        possibleMovesForSelectedPiece = null;
        Board.whiteTurn = !Board.whiteTurn;

        ChessGUI.setPieceSelectedFalse();

        return bitboards;

    }

    // sets the selected square dark gray if there is a playable piece there
    @Override
    public void mousePressed(MouseEvent e) {
        int row = e.getY() / (ChessGUI.dimension / 16);
        int col = e.getX() / (ChessGUI.dimension / 16);
        System.out.println(e.getY() + ", " + e.getX());
        System.out.println(col + ", " + row);
        if (row == 4 && col == 4) {
            // prints if kings checked for testing
            System.out.println("King 0 is checked? " + Board.kingsChecked[0]);
            System.out.println("King 1 is checked? " + Board.kingsChecked[1]);
        }
        if (Board.isThereAPieceThere(row, col)) {
            setPieceSelectedGUI(row, col);
        } else {
            // controls checks
            Board.pieceBoards = movePiece(row, col, Board.pieceBoards);
            int sideToCheck = Board.whiteTurn ? 1 : 0;
            int otherSide = sideToCheck == 1 ? 0 : 1;
            if (Board.kingsChecked[otherSide]) {
                Board.kingsChecked[otherSide] = false;
            } else if (vm.isKingChecked(Board.pieceBoards, sideToCheck, Board.kingLocations)) {
                Board.kingsChecked[sideToCheck] = true;
                System.out.println("SIDE TO CHECK " + sideToCheck);
                // game over is printed outside the method as isCheckMated is used in simulation
                // as well
                if (vm.allAvailableMoves(Board.pieceBoards, sideToCheck).isEmpty()) {
                    System.out.println("GAME OVER");
                }
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
