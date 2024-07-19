public class NewBoard {
    public enum BoardItem {
        BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_PAWN, WHITE_ROOK, WHITE_KNIGHT,
        WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_PAWN, EMPTY
    }

    public BoardItem[][] board;

    public NewBoard() {
        this.board = new BoardItem[][] {
                { BoardItem.BLACK_ROOK, BoardItem.BLACK_KNIGHT, BoardItem.BLACK_BISHOP, BoardItem.BLACK_QUEEN,
                        BoardItem.BLACK_KING, BoardItem.BLACK_BISHOP, BoardItem.BLACK_KNIGHT, BoardItem.BLACK_ROOK },
                { BoardItem.BLACK_PAWN, BoardItem.BLACK_PAWN, BoardItem.BLACK_PAWN, BoardItem.BLACK_PAWN,
                        BoardItem.BLACK_PAWN, BoardItem.BLACK_PAWN, BoardItem.BLACK_PAWN, BoardItem.BLACK_PAWN },
                { BoardItem.EMPTY, BoardItem.EMPTY, BoardItem.EMPTY, BoardItem.EMPTY, BoardItem.EMPTY, BoardItem.EMPTY,
                        BoardItem.EMPTY, BoardItem.EMPTY },
                { BoardItem.EMPTY, BoardItem.EMPTY, BoardItem.EMPTY, BoardItem.EMPTY, BoardItem.EMPTY, BoardItem.EMPTY,
                        BoardItem.EMPTY, BoardItem.EMPTY },
                { BoardItem.EMPTY, BoardItem.EMPTY, BoardItem.EMPTY, BoardItem.EMPTY, BoardItem.EMPTY, BoardItem.EMPTY,
                        BoardItem.EMPTY, BoardItem.EMPTY },
                { BoardItem.EMPTY, BoardItem.EMPTY, BoardItem.EMPTY, BoardItem.EMPTY, BoardItem.EMPTY, BoardItem.EMPTY,
                        BoardItem.EMPTY, BoardItem.EMPTY },
                { BoardItem.WHITE_PAWN, BoardItem.WHITE_PAWN, BoardItem.WHITE_PAWN, BoardItem.WHITE_PAWN,
                        BoardItem.WHITE_PAWN, BoardItem.WHITE_PAWN, BoardItem.WHITE_PAWN, BoardItem.WHITE_PAWN },
                { BoardItem.WHITE_ROOK, BoardItem.WHITE_KNIGHT, BoardItem.WHITE_BISHOP, BoardItem.WHITE_QUEEN,
                        BoardItem.WHITE_KING, BoardItem.WHITE_BISHOP, BoardItem.WHITE_KNIGHT, BoardItem.WHITE_ROOK }
        };
    }

}

// the actual game state ([][])
// "Board" a light wrapper around the raw 2d array
// `getItem(row, col)` returns the item at that location
// `setItem(row, col, item)` sets the item at that location
// `getPiece(row, col)` returns the piece at that location
// `setPiece(row, col, piece)` sets the piece at that location
// `movePiece(row, col, row2, col2)` moves the piece at row, col to row2, col2

// the "chess" logic (is this move legal?)
// "Game" the juice of the game
// `isInCheckMate`
// `isInCheck`
// `isMoveValid`

// the "ai"
// `Move getNextMove(Board board, whiteOrBlack)`

// the "gui"
// is purely a view of the game state
// and sends events to the game state