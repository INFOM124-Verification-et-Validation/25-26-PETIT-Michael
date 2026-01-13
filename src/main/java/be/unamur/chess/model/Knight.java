package be.unamur.chess.model;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Knight piece class.
 */
public class Knight extends Piece {
    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public Set<Point> getValidMoves(Piece[][] boardState, int row, int col) {
        Set<Point> moves = new HashSet<>();
        int[][] offsets = {{-2, -1}, {-2, 1}, {2, -1}, {2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}};

        for (int[] offset : offsets) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];

            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                Piece target = boardState[newRow][newCol];
                if (target == null || target.isWhite() != isWhite) {
                    moves.add(new Point(newRow, newCol));
                }
            }
        }

        return moves;
    }

    @Override
    public String toString() {
        return isWhite() ? "WKnight" : "BKnight";
    }
}
