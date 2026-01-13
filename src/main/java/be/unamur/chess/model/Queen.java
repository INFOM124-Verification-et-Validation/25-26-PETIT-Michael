package be.unamur.chess.model;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Queen piece class.
 */
public class Queen extends Piece {
    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public Set<Point> getValidMoves(Piece[][] boardState, int row, int col) {
        Set<Point> moves = new HashSet<>();

        moves.addAll(new Rook(isWhite).getValidMoves(boardState, row, col));
        moves.addAll(new Bishop(isWhite).getValidMoves(boardState, row, col));

        return moves;
    }

    @Override
    public String toString() {
        return isWhite() ? "WQueen" : "BQueen";
    }
}
