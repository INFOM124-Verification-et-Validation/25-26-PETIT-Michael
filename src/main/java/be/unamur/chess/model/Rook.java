package be.unamur.chess.model;

/**
 * Rook piece class.
 */
public class Rook extends SlidingPiece {

    private static final int[][] ORTHOGONALS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    protected int[][] directions() {
        return ORTHOGONALS;
    }

    @Override
    public String toString() {
        return isWhite() ? "WRook" : "BRook";
    }
}