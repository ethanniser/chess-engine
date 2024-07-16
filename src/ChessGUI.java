import javax.swing.*;
import java.awt.*;

public class ChessGUI {

    //creates baseframe and gets the board template
    static JFrame chessFrame = new JFrame();
    static String[][] mainBoard = Board.getBoardTemplate();
    public static JLabel[] JLabelCollection = new JLabel[64];
    static int[] pieceSelectedAndCoordinate = new int[2];

    //creates chessboard gui
    private static void initializeChessFrame()
    {
        //makes the frame an 8x8 grid layout
        chessFrame.setLayout(new GridLayout(8, 8));
        /*iterates through each cell and sets it to either black or white,
        also chooses the proper icon based on the board template and places it on the board
         */
        for(int i = 0; i < 64; i++)
        {
            JLabel b = new JLabel();
            b.setOpaque(true);
            String imgSrc = "src\\Assets\\";

            String currentPiece = mainBoard[2+(i/8)][1+(i%8)];

            //sets block colors
            if(((i/8)%2) == 0) {
                if((i%2) == 0){
                    b.setBackground(new Color(225, 215, 183));
                }
                else {
                    b.setBackground(new Color(57, 40, 20));
                }
            }
            else if((i%2) == 1) {
                b.setBackground(new Color(225, 215, 183));
            }
            else {
                b.setBackground(new Color(57, 40, 28));
            }

            //chooses correct icon for each phase of the iteration
            switch (currentPiece) {
                case " " -> chessFrame.add(b);
                case "r" -> imgSrc += "black-rook.png";
                case "n" -> imgSrc += "black-knight.png";
                case "b" -> imgSrc += "black-bishop.png";
                case "q" -> imgSrc += "black-queen.png";
                case "k" -> imgSrc += "black-king.png";
                case "p" -> imgSrc += "black-pawn.png";
                case "R" -> imgSrc += "white-rook.png";
                case "N" -> imgSrc += "white-knight.png";
                case "B" -> imgSrc += "white-bishop.png";
                case "Q" -> imgSrc += "white-queen.png";
                case "K" -> imgSrc += "white-king.png";
                case "P" -> imgSrc += "white-pawn.png";
            }

            //resizes image
            ImageIcon currentPieceImageIcon = new ImageIcon(imgSrc);
            Image currentPieceImage = currentPieceImageIcon.getImage();
            Image tempImg = currentPieceImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            currentPieceImageIcon.setImage(tempImg);

            b.setIcon((Icon) currentPieceImageIcon);
            b.setSize(5, 5);
            JLabelCollection[i] = b;
        }

        placeBoardsAgain();

        //establishes board
        chessFrame.setSize(1200, 1200);
        chessFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chessFrame.setName("Chess");
        chessFrame.setResizable(false);
        chessFrame.setVisible(true);

        GameplayController mouseListenerController = new GameplayController();
        chessFrame.addMouseListener(mouseListenerController);

    }

    public static void placeBoardsAgain()
    {
        for(JLabel j : JLabelCollection)
        {
            chessFrame.add(j);
        }
    }

    public static void startChessGUI()
    {
        initializeChessFrame();
    }

    public static int isPieceSelected() {
        return pieceSelectedAndCoordinate[0];
    }

    public static int getPointSelected()
    {
        if(pieceSelectedAndCoordinate[0] == 0) { return -1; }
        return pieceSelectedAndCoordinate[1];
    }

    public static void setPieceSelectedTrue(int row, int col) {
        pieceSelectedAndCoordinate[0] = 1;
        pieceSelectedAndCoordinate[1] = ((row*8) + col);
    }
    public static void setPieceSelectedFalse() {
        pieceSelectedAndCoordinate[0] = 0;
        pieceSelectedAndCoordinate[1] = -1;
    }
}
