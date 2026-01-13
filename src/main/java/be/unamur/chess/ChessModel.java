package be.unamur.chess;

import be.unamur.chess.model.*;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The model class that represents the game logic and state of the chess game.
 */
class ChessModel {
    private final int ROWS = 8;
    private final int COLS = 8;
    private Piece[][] boardState;

    /**
     * Constructs a ChessModel and initializes the board state.
     */
    public ChessModel() {
        boardState = new Piece[ROWS][COLS];
        initializeBoard();
    }

    /**
     * Initializes the chess board with the starting positions of pieces.
     */
    private void initializeBoard() {
        for (int col = 0; col < COLS; col++) {
            boardState[1][col] = new Pawn(false); // Black Pawn
            boardState[6][col] = new Pawn(true); // White Pawn
        }
        boardState[0][0] = new Rook(false);
        boardState[0][7] = new Rook(false);
        boardState[7][0] = new Rook(true);
        boardState[7][7] = new Rook(true);

        boardState[0][1] = new Knight(false);
        boardState[0][6] = new Knight(false);
        boardState[7][1] = new Knight(true);
        boardState[7][6] = new Knight(true);

        boardState[0][2] = new Bishop(false);
        boardState[0][5] = new Bishop(false);
        boardState[7][2] = new Bishop(true);
        boardState[7][5] = new Bishop(true);

        boardState[0][3] = new Queen(false);
        boardState[7][3] = new Queen(true);

        boardState[0][4] = new King(false);
        boardState[7][4] = new King(true);
    }

    /**
     * Gets the board state.
     *
     * @return the current state of the board.
     */
    public Piece[][] getBoardState() {
        return boardState;
    }

    /**
     * Moves a piece from one position to another.
     *
     * @param start the starting position.
     * @param end   the ending position.
     * @return true if the move was successful, false otherwise.
     */
    public boolean movePiece(Point start, Point end) {
        int startRow = start.x;
        int startCol = start.y;
        int endRow = end.x;
        int endCol = end.y;

        Piece piece = boardState[startRow][startCol];
        if (piece == null) {
            return false;
        }

        Set<Point> validMoves = piece.getValidMoves(boardState, startRow, startCol);
        if (validMoves.contains(end)) {
            boardState[endRow][endCol] = piece;
            boardState[startRow][startCol] = null;
            return true;
        }
        return false;
    }

    /**
     * Gets a set of valid moves for the piece at the specified position.
     *
     * @param row the row of the piece
     * @param col the column of the piece
     * @return a set of valid moves as points
     */
    public Set<Point> getValidMoves(int row, int col) {
        Piece piece = boardState[row][col];
        if (piece != null) {
            return piece.getValidMoves(boardState, row, col);
        }
        return new HashSet<>();
    }
}
