package be.unamur.chess.model;

import java.awt.*;
import java.util.Set;

/**
 * Abstract class representing a chess piece.
 */
public abstract class Piece {

    protected boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public abstract Set<Point> getValidMoves(Piece[][] boardState, int row, int col);
}

