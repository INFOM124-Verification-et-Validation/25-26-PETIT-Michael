package be.unamur.chess.ai;

import be.unamur.chess.model.*;

import java.awt.*;

public class SmarterStrategy implements Strategy{

    @Override
    public Point[] getNextMove(Piece[][] boardState, boolean isWhite) {
        Point[] bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        // Iterate through all pieces on the board
        for (int row = 0; row < boardState.length; row++) {
            for (int col = 0; col < boardState[row].length; col++) {
                Piece piece = boardState[row][col];
                if (piece != null && piece.isWhite() == isWhite) {

                    // Get valid moves for the current piece
                    for (Point move : piece.getValidMoves(boardState, row, col)) {
                        int moveScore = evaluateMove(boardState, piece, new Point(row, col), move, isWhite);

                        if (moveScore > bestScore) {
                            bestScore = moveScore;
                            bestMove = new Point[]{new Point(row, col), move};
                        }
                    }
                }
            }
        }

        return bestMove;
    }

    /**
     * Evaluates the score of a move based on a heuristic.
     *
     * @param boardState The current state of the chessboard.
     * @param piece      The piece being moved.
     * @param from       The source position of the piece.
     * @param to         The destination position of the piece.
     * @param isWhite    True if the AI is playing as white, false if black.
     * @return A score representing the desirability of the move.
     */
    private int evaluateMove(Piece[][] boardState, Piece piece, Point from, Point to, boolean isWhite) {
        int score = 0;

        // Reward capturing opponent pieces
        Piece target = boardState[to.x][to.y];
        if (target != null && target.isWhite() != isWhite) {
            score += getPieceValue(target);
        }

        // Penalize leaving the king in check (basic protection)
        Piece[][] simulatedBoard = simulateMove(boardState, from, to);
        if (isInCheck(simulatedBoard, isWhite)) {
            score -= 1000;
        }

        // Reward control of the center (positions near [3,3], [3,4], [4,3], [4,4])
        if ((to.x >= 3 && to.x <= 4) && (to.y >= 3 && to.y <= 4)) {
            score += 5;
        }

        return score;
    }

    /**
     * Simulates a move on the board and returns the resulting board state.
     *
     * @param boardState The current state of the chessboard.
     * @param from       The source position of the piece.
     * @param to         The destination position of the piece.
     * @return The new board state after the move.
     */
    private Piece[][] simulateMove(Piece[][] boardState, Point from, Point to) {
        Piece[][] newBoard = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newBoard[i][j] = boardState[i][j];
            }
        }
        newBoard[to.x][to.y] = newBoard[from.x][from.y];
        newBoard[from.x][from.y] = null;
        return newBoard;
    }

    /**
     * Determines if the given player is in check.
     *
     * @param boardState The current state of the chessboard.
     * @param isWhite    True if checking for white, false for black.
     * @return True if the player is in check, false otherwise.
     */
    private boolean isInCheck(Piece[][] boardState, boolean isWhite) {
        Point kingPosition = findKing(boardState, isWhite);
        if (kingPosition == null) {
            return false;
        }
        for (int row = 0; row < boardState.length; row++) {
            for (int col = 0; col < boardState[row].length; col++) {
                Piece piece = boardState[row][col];
                if (piece != null && piece.isWhite() != isWhite) {
                    for (Point move : piece.getValidMoves(boardState, row, col)) {
                        if (move.equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Finds the position of the king for the given player.
     *
     * @param boardState The current state of the chessboard.
     * @param isWhite    True if searching for the white king, false for black.
     * @return The position of the king, or null if not found.
     */
    private Point findKing(Piece[][] boardState, boolean isWhite) {
        for (int row = 0; row < boardState.length; row++) {
            for (int col = 0; col < boardState[row].length; col++) {
                Piece piece = boardState[row][col];
                if (piece instanceof King && piece.isWhite() == isWhite) {
                    return new Point(row, col);
                }
            }
        }
        return null;
    }

    /**
     * Gets the value of a piece based on its type.
     *
     * @param piece The piece whose value is to be determined.
     * @return The value of the piece.
     */
    private int getPieceValue(Piece piece) {
        if (piece instanceof Pawn) return 1;
        if (piece instanceof Knight || piece instanceof Bishop) return 3;
        if (piece instanceof Rook) return 5;
        if (piece instanceof Queen) return 9;
        if (piece instanceof King) return 1000;
        return 0;
    }

}
