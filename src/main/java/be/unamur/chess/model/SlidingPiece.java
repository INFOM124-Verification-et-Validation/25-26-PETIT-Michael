package be.unamur.chess.model;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public abstract class SlidingPiece extends Piece {

    protected SlidingPiece(boolean isWhite) {
        super(isWhite);
    }

    protected abstract int[][] directions();

    @Override
    public Set<Point> getValidMoves(Piece[][] boardState, int row, int col) {
        return generateSlidingMoves(boardState, row, col, directions());
    }

    protected Set<Point> generateSlidingMoves(Piece[][] boardState, int row, int col, int[][] dirs) {
        Set<Point> moves = new HashSet<>();
        int rows = boardState.length;
        int cols = boardState[0].length;

        for (int[] dir : dirs) {
            int r = row + dir[0];
            int c = col + dir[1];

            while (r >= 0 && r < rows && c >= 0 && c < cols) {
                Piece target = boardState[r][c];
                if (target == null) {
                    moves.add(new Point(r, c));
                } else {
                    if (target.isWhite() == this.isWhite()) {
                        moves.add(new Point(r, c)); // capture
                    }
                    break; // Stop when finding a Piece
                }
                r += dir[0];
                c += dir[1];
            }
        }
        return moves;
    }
}
