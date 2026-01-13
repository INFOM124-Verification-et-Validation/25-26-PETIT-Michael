package be.unamur.chess.model;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class QueenTest {

    @Test
    void testQueenValidMoves() {
        Piece[][] board = new Piece[8][8];
        Queen queen = new Queen(true);

        // Place queen in the center
        board[4][4] = queen;
        Set<Point> moves = queen.getValidMoves(board, 4, 4);

        // Queen should have all rook and bishop moves
        assertThat(moves).contains(new Point(4, 0));
        assertThat(moves).contains(new Point(4, 7));
        assertThat(moves).contains(new Point(0, 4));
        assertThat(moves).contains(new Point(7, 4));
        assertThat(moves).contains(new Point(7, 7));
        assertThat(moves).contains(new Point(1, 1));
    }

}