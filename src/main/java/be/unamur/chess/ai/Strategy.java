package be.unamur.chess.ai;

import be.unamur.chess.model.Piece;

import java.awt.*;

public interface Strategy {

    /**
     * Selects the next move for the player.
     *
     * @param boardState The current state of the chessboard.
     * @param isWhite    True if the is playing as white, false if black.
     * @return An array of Points where the first element is the source position
     *         and the second element is the destination position.
     */
    Point[] getNextMove(Piece[][] boardState, boolean isWhite);
}
