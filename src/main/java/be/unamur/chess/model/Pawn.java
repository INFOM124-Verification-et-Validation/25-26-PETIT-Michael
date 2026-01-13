package be.unamur.chess.model;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Pawn piece class.
 */
public class Pawn extends Piece {

    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public Set<Point> getValidMoves(Piece[][] boardState, int row, int col) {
        Set<Point> moves = new HashSet<>();
        int direction = isWhite ? -1 : 1;

        addForwardMoves(moves, boardState, row, col, direction);
        addCaptureMoves(moves, boardState, row, col, direction);

        return moves;
    }

    private void addForwardMoves(Set<Point> moves, Piece[][] boardState, int row, int col, int direction) {
        int nextRow = row + direction;
        if (!isWithinBounds(nextRow, col) || boardState[nextRow][col] != null) {
            return;
        }

        moves.add(new Point(nextRow, col));

        // Initial double move
        if (isAtStartingPosition(row)) {
            int doubleNextRow = row + 2 * direction;
            if (isWithinBounds(doubleNextRow, col) && boardState[doubleNextRow][col] == null) {
                moves.add(new Point(doubleNextRow, col));
            }
        }
    }

    private void addCaptureMoves(Set<Point> moves, Piece[][] boardState, int row, int col, int direction) {
        int nextRow = row + direction;
        for (int offset : new int[]{-1, 1}) {
            int nextCol = col + offset;
            if (isWithinBounds(nextRow, nextCol)) {
                Piece target = boardState[nextRow][nextCol];
                if (target != null && target.isWhite() != isWhite) {
                    moves.add(new Point(nextRow, nextCol));
                }
            }
        }
    }

    private boolean isAtStartingPosition(int row) {
        return (isWhite && row == 6) || (!isWhite && row == 1);
    }

    private boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    @Override
    public String toString() {
        return isWhite() ? "WPawn" : "BPawn";
    }
}