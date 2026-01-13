package be.unamur.chess.model;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * King piece class.
 */
public class King extends Piece {

    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public Set<Point> getValidMoves(Piece[][] boardState, int row, int col) {
        Set<Point> moves = new HashSet<>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    int newRow = row + i;
                    int newCol = col + j;
                    if (newRow >= 0 && newRow <= 8 && newCol >= 0 && newCol <= 8) {
                        Piece target = boardState[newRow][newCol];
                        if (target == null || target.isWhite() != isWhite) {
                            moves.add(new Point(newRow, newCol));
                        }
                    }
                }
            }
        }

        return moves;
    }

    @Override
    public String toString() {
        return isWhite() ? "WKing" : "BKing";
    }
}