package be.unamur.chess.ai;

import be.unamur.chess.model.Piece;

import java.awt.*;

/**
 * A basic strategy class to implement a simple AI for chess.
 */
public class SimpleStrategy implements Strategy {

    @Override
    public Point[] getNextMove(Piece[][] boardState, boolean isWhite) {
        Point[] bestMove = null;

        // Iterate through all pieces on the board
        for (int row = 0; row < boardState.length; row++) {
            for (int col = 0; col < boardState[row].length; col++) {
                Piece currentPiece = boardState[row][col];
                if (currentPiece != null && currentPiece.isWhite() == isWhite) {

                    // Get valid moves for the currentPiece
                    for (Point move : currentPiece.getValidMoves(boardState, row, col)) {
                        // Example basic strategy: take the first valid move found
                        if (bestMove == null) {
                            bestMove = new Point[]{new Point(row, col), move};
                        }

                        // Example advanced strategy: prefer capturing moves
                        if (boardState[move.x][move.y] != null && boardState[move.x][move.y].isWhite() != isWhite) {
                            return new Point[]{new Point(row, col), move};
                        }
                    }
                }
            }
        }

        return bestMove;
    }

}
