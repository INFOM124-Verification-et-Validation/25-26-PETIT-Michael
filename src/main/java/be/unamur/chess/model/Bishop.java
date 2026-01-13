package be.unamur.chess.model;

/**
 * Bishop piece class.
 */
public class Bishop extends SlidingPiece {

    private static final int[][] DIAGONALS = {
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };

    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    protected int[][] directions() {
        return DIAGONALS;
    }

    @Override
    public String toString() {
        return isWhite() ? "WBishop" : "BBishop";
    }
}